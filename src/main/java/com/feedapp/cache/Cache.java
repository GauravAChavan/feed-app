package com.feedapp.cache;

import java.time.Duration;
import java.util.function.Supplier;

public interface Cache {

    <T> void setCache(String key, T value, long expiryTime);

    /**
     * Get Cache without Fallback
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T getCache(String key);


    /**
     * Get Cache with Fallback using Supplier interface
     *
     * @param key
     * @param fallback
     * @param <T>
     * @return <T>
     */
    default <T> T getCache(String key, long expiryTime, Supplier<T> fallback) {
        T obj = null;
        try {
            obj = getCache(key);
        } catch (RuntimeException e) {
        }
        if (obj == null) {
            obj = fallback.get();
            if (obj != null) setCache(key, obj, expiryTime);
        }
        return obj;
    }
}


