package edu.common.dynamicextensions.query.store;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A disk-backed result store intended for use by a single caller. Instances are
 * not thread-safe; callers must provide synchronization when sharing a store.
 */
public class SpillableResultStore<T> implements ResultStore<T> {
    private final int maxValuesInMemory;

    private final File tempDir;

    private final List<T> memoryValues = new ArrayList<>();

    //
    // Tracks streams owned by active iterators so clear() and close() can also
    // close iterators that were abandoned before consuming all spilled values.
    //
    private final List<ObjectInputStream> inputs = new ArrayList<>();

    private File spillFile;

    private ObjectOutputStream output;

    private int size;

    private boolean sealed;

    private boolean closed;

    public SpillableResultStore(int maxValuesInMemory) {
        this(maxValuesInMemory, new File(System.getProperty("java.io.tmpdir")));
    }

    public SpillableResultStore(int maxValuesInMemory, File tempDir) {
        this.maxValuesInMemory = Math.max(0, maxValuesInMemory);
        this.tempDir = tempDir;
    }

    @Override
    public void add(T value) {
        ensureWritable();
        if (memoryValues.size() < maxValuesInMemory) {
            memoryValues.add(value);
        } else {
            writeToDisk(value);
        }

        ++size;
    }

    @Override
    public int size() {
        ensureOpen();
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        ensureOpen();
        seal();

        Iterator<T> memoryIterator = new ArrayList<>(memoryValues).iterator();
        int diskValues = size - memoryValues.size();
        ObjectInputStream input = diskValues > 0 ? openInput() : null;
        return new StoreIterator(memoryIterator, input, diskValues);
    }

    //
    // Releases the current contents while keeping this store reusable.
    //
    @Override
    public void clear() {
        ensureOpen();
        reset();
    }

    //
    // Permanently releases this store. Subsequent operations are rejected.
    //
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

    private void writeToDisk(T value) {
        try {
            getOutput().writeObject(value);
            getOutput().reset();
        } catch (IOException e) {
            throw new ResultStoreException("Error spilling query result to disk", e);
        }
    }

    private ObjectOutputStream getOutput() throws IOException {
        if (output != null) {
            return output;
        }

        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new IOException("Unable to create result store directory: " + tempDir);
        }

        spillFile = File.createTempFile("de-query-result-", ".bin", tempDir);
        output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(spillFile)));
        return output;
    }

    private ObjectInputStream openInput() {
        try {
            ObjectInputStream input = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(spillFile)));
            inputs.add(input);
            return input;
        } catch (IOException e) {
            throw new ResultStoreException("Error opening spilled query result", e);
        }
    }

    private void seal() {
        if (sealed) {
            return;
        }

        closeOutput();
        sealed = true;
    }

    //
    // Close every open reader and writer before deleting the spill file,
    // then restore the content state used by a new or empty store.
    //
    private void reset() {
        try {
            closeInputs();
        } finally {
            try {
                closeOutput();
            } finally {
                memoryValues.clear();
                deleteSpillFile();
                size = 0;
                sealed = false;
            }
        }
    }

    private void closeOutput() {
        if (output == null) {
            return;
        }

        try {
            output.close();
        } catch (IOException e) {
            throw new ResultStoreException("Error closing spilled query result", e);
        } finally {
            output = null;
        }
    }

    private void closeInputs() {
        ResultStoreException error = null;
        for (ObjectInputStream input : new ArrayList<>(inputs)) {
            try {
                input.close();
            } catch (IOException e) {
                if (error == null) {
                    error = new ResultStoreException("Error closing spilled query result", e);
                }
            }
        }

        inputs.clear();
        if (error != null) {
            throw error;
        }
    }

    private void closeInput(ObjectInputStream input) {
        if (input == null) {
            return;
        }

        inputs.remove(input);
        try {
            input.close();
        } catch (IOException e) {
            throw new ResultStoreException("Error closing spilled query result", e);
        }
    }

    private void deleteSpillFile() {
        if (spillFile != null && spillFile.exists() && !spillFile.delete()) {
            spillFile.deleteOnExit();
        }

        spillFile = null;
    }

    private void ensureWritable() {
        ensureOpen();
        if (sealed) {
            throw new IllegalStateException("Result store cannot be modified after iteration has started");
        }
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("Result store has been closed");
        }
    }

    private class StoreIterator implements Iterator<T> {
        private final Iterator<T> memoryIterator;

        private ObjectInputStream input;

        private int remainingDiskValues;

        private StoreIterator(Iterator<T> memoryIterator, ObjectInputStream input, int remainingDiskValues) {
            this.memoryIterator = memoryIterator;
            this.input = input;
            this.remainingDiskValues = remainingDiskValues;
        }

        @Override
        public boolean hasNext() {
            ensureOpen();
            return memoryIterator.hasNext() || remainingDiskValues > 0;
        }

        @Override
        public T next() {
            ensureOpen();
            if (memoryIterator.hasNext()) {
                return memoryIterator.next();
            }

            if (remainingDiskValues <= 0) {
                throw new NoSuchElementException();
            }

            try {
                @SuppressWarnings("unchecked")
                T value = (T) input.readObject();
                --remainingDiskValues;
                if (remainingDiskValues == 0) {
                    closeInput(input);
                    input = null;
                }

                return value;
            } catch (IOException | ClassNotFoundException e) {
                closeInput(input);
                input = null;
                remainingDiskValues = 0;
                throw new ResultStoreException("Error reading spilled query result", e);
            }
        }
    }
}
