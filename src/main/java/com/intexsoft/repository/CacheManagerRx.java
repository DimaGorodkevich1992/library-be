package com.intexsoft.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CacheManagerRx<E> {

    private Cache getCache(String cacheId) {
        return cacheManager.getCache(cacheId);
    }

    @Autowired
    private CacheManager cacheManager;

    public void cachePut(String cacheId, Object key, Object value) {
        getCache(cacheId).put(key, value);
    }

    public E find(String cacheId, Object key, Class<E> clazz) {
        return getCache(cacheId).get(key, clazz);

    }

    public void deleteCache(String cacheId) {
        getCache(cacheId).clear();
    }

    public void deleteItemOfCache(String cachedId, Object key) {
        getCache(cachedId).evict(key);
    }

    public boolean isEmpty(String cacheId, Object key, Class<E> clazz) {
        return Objects.equals(getCache(cacheId).get(key, clazz), null);
    }
}
