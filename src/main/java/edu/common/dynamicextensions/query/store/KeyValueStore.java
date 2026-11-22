package edu.common.dynamicextensions.query.store;

import java.util.Iterator;

public interface KeyValueStore<K, V> extends Iterable<V>, AutoCloseable {
    V get(K key);

    void put(K key, V value);

    int size();

	@Override
    Iterator<V> iterator();

    void clear();

    @Override
    void close();
}
