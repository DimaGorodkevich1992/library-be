package com.intexsoft.repository;

import com.intexsoft.model.CommonModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
public class CacheManagerRedis<E extends CommonModel<I, E>, I extends Serializable> implements InitializingBean {

    private Cache cache;
    @Autowired
    private CacheManager cacheManager;

    private void cacheInit() {
    }

    public void cachePut(String cacheId, I key, E value) {
        Collection<String> cacheIds = cacheManager.getCacheNames();
        cache = cacheManager.getCache(cacheId);
        cache.put(key, value);
    }

    public E find(String cacheId, String key) {
        return null;
    }

    public void deleteCache(String cachedId, E value) {
    }

    public E deleteItem(String cachedId, String key) {
        return null;
    }

    public boolean isEmpty(String cachedId, String key) {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("");
    }
}
