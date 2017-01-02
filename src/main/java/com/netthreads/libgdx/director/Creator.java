package com.netthreads.libgdx.director;

/**
 * Creator of instance
 */
public interface Creator<T> {
    T createInstance(Class<T> type);
}
