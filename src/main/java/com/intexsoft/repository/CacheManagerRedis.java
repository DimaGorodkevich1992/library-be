/*package com.intexsoft.repository;

import com.intexsoft.model.CommonModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Component
public class CacheManagerRedis<E extends CommonModel<I, E>, I extends Serializable> implements InitializingBean {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private HashOperations<String, UUID, E> hashOperations;

    public E submitCache(String cacheId, String key, E value) {
        hashOperations
        return null;
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
}*/
