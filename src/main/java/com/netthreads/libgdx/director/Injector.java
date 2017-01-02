package com.netthreads.libgdx.director;

/**
 * Created by k24 on 2017/01/02.
 */
public interface Injector {
    <T> Injector bind(Class<T> type, Creator<T> creator);

    <T> T getInstance(Class<T> type);
}
