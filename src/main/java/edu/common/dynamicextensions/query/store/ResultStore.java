package edu.common.dynamicextensions.query.store;

public interface ResultStore<T> extends Iterable<T>, AutoCloseable {
    void add(T value);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    void clear();

    @Override
    void close();
}
