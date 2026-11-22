package edu.common.dynamicextensions.query.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A disk-backed key-value store intended for use by a single caller. Instances
 * are not thread-safe; callers must provide synchronization when sharing a store.
 */
public class SpillableKeyValueStore<K, V> implements KeyValueStore<K, V> {
    private final int maxValuesInMemory;

    private final File tempDir;

    private final Map<K, DiskLocation> locations = new LinkedHashMap<>();

    private final LinkedHashMap<K, V> memoryValues = new LinkedHashMap<>(16, 0.75f, true);

    //
    // Keys whose current in-memory values are not represented on disk.
    // A caller that mutates a value returned by get() must call put() to mark it dirty.
    //
    private final Set<K> dirtyKeys = new LinkedHashSet<>();

    private File spillFile;

    private RandomAccessFile spill;

    private boolean closed;

    public SpillableKeyValueStore(int maxValuesInMemory) {
        this(maxValuesInMemory, new File(System.getProperty("java.io.tmpdir")));
    }

    public SpillableKeyValueStore(int maxValuesInMemory, File tempDir) {
        this.maxValuesInMemory = Math.max(1, maxValuesInMemory);
        this.tempDir = tempDir;
    }

    @Override
    public V get(K key) {
        ensureOpen();
        V value = memoryValues.get(key);
        if (value != null || memoryValues.containsKey(key)) {
            return value;
        }

        DiskLocation location = locations.get(key);
        if (location == null) {
            return null;
        }

        value = read(location);
        memoryValues.put(key, value);
        evictIfRequired();
        return value;
    }

    @Override
    public void put(K key, V value) {
        ensureOpen();
        locations.putIfAbsent(key, null);
        memoryValues.put(key, value);
        dirtyKeys.add(key);
        evictIfRequired();
    }

    @Override
    public int size() {
        ensureOpen();
        return locations.size();
    }

    @Override
    public Iterator<V> iterator() {
        ensureOpen();
        return new StoreIterator(new ArrayList<>(locations.keySet()).iterator());
    }

    @Override
    public void clear() {
        ensureOpen();
        reset();
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        try {
            reset();
        } finally {
            closed = true;
        }
    }

    private void evictIfRequired() {
        while (memoryValues.size() > maxValuesInMemory) {
            Iterator<Map.Entry<K, V>> iterator = memoryValues.entrySet().iterator();
            Map.Entry<K, V> eldest = iterator.next();
            if (dirtyKeys.remove(eldest.getKey())) {
                locations.put(eldest.getKey(), write(eldest.getValue()));
            }

            iterator.remove();
        }
    }

    private DiskLocation write(V value) {
        try {
            byte[] bytes = serialize(value);
            RandomAccessFile file = getSpill();
            long offset = file.length();
            file.seek(offset);
            file.write(bytes);
            return new DiskLocation(offset, bytes.length);
        } catch (IOException e) {
            throw new ResultStoreException("Error spilling keyed value to disk", e);
        }
    }

    private V read(DiskLocation location) {
        try {
            byte[] bytes = new byte[location.length()];
            RandomAccessFile file = getSpill();
            file.seek(location.offset());
            file.readFully(bytes);
            return deserialize(bytes);
        } catch (IOException | ClassNotFoundException e) {
            throw new ResultStoreException("Error reading spilled keyed value", e);
        }
    }

    private V valueForIteration(K key) {
        V value = memoryValues.get(key);
        if (value != null || memoryValues.containsKey(key)) {
            return value;
        }

        DiskLocation location = locations.get(key);
        return location != null ? read(location) : null;
    }

    private RandomAccessFile getSpill() throws IOException {
        if (spill != null) {
            return spill;
        }

        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new IOException("Unable to create key-value store directory: " + tempDir);
        }

        spillFile = File.createTempFile("de-query-key-values-", ".bin", tempDir);
        spill = new RandomAccessFile(spillFile, "rw");
        return spill;
    }

    private byte[] serialize(V value) throws IOException {
        try (
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(bytes)
        ) {
            output.writeObject(value);
            output.flush();
            return bytes.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    private V deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (V) input.readObject();
        }
    }

    private void reset() {
        locations.clear();
        memoryValues.clear();
        dirtyKeys.clear();
        try {
            closeSpill();
        } finally {
            deleteSpillFile();
        }
    }

    private void closeSpill() {
        if (spill == null) {
            return;
        }

        try {
            spill.close();
        } catch (IOException e) {
            throw new ResultStoreException("Error closing keyed result store", e);
        } finally {
            spill = null;
        }
    }

    private void deleteSpillFile() {
        if (spillFile != null && spillFile.exists() && !spillFile.delete()) {
            spillFile.deleteOnExit();
        }

        spillFile = null;
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("Key-value store has been closed");
        }
    }

    private record DiskLocation(long offset, int length) { }

    private class StoreIterator implements Iterator<V> {
        private final Iterator<K> keys;

        private StoreIterator(Iterator<K> keys) {
            this.keys = keys;
        }

        @Override
        public boolean hasNext() {
            return keys.hasNext();
        }

        @Override
        public V next() {
            if (!keys.hasNext()) {
                throw new NoSuchElementException();
            }

            ensureOpen();
            return valueForIteration(keys.next());
        }
    }
}
