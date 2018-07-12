package com.intexsoft.repository;

import com.intexsoft.model.CommonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.io.Serializable;
import java.util.Objects;

@Component
public class CacheRx<E extends CommonModel<I, E>, I extends Serializable> {

    private Cache getCache(String cacheId) {
        return cacheManager.getCache(cacheId);
    }

    @Autowired
    private CacheManager cacheManager;

    private E find(String cacheId, Object itemKey, Class<E> clazz) {
        return getCache(cacheId).get(itemKey, clazz);
    }

    private void putCache(String cacheId, Object itemKey, Object value) {
        getCache(cacheId).put(itemKey, value);
    }

    private boolean isEmpty(String cacheId, Object key) {
        return Objects.equals(getCache(cacheId).get(key), null);
    }

    private void delete(String cacheId) {
        getCache(cacheId).clear();
    }

    private void delete(String cachedId, Object itemHey) {
        getCache(cachedId).evict(itemHey);
    }

    public Observable.Transformer<E, E> cachable(String cacheId, Object itemKey, Class<E> clazz) {
        return source -> Observable.fromCallable(() -> isEmpty(cacheId, itemKey))
                .flatMap(empty -> empty
                        ? Observable.just(find(cacheId, itemKey, clazz))
                        : source.doOnNext(v -> putCache(cacheId, itemKey, v)));
    }

    public Observable.Transformer<E, E> cachePut(String cacheId, Object itemKey) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(observable -> source
                        .doOnNext(v -> putCache(cacheId, itemKey, v)));
    }

    public <L> Observable.Transformer<L, L> cachePut(String cacheId, Object itemKey, Class<L> clazz) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(observable -> source
                        .doOnNext(v -> putCache(cacheId, itemKey, v)));
    }

    public Observable.Transformer<E, E> cachePut(String cacheId) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(observable -> source
                        .doOnNext(v -> putCache(cacheId, v.getId(), v)));
    }

    public Observable.Transformer<E, E> cacheDelete(String cacheId) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(eObservable -> source
                        .doOnNext(v -> delete(cacheId, v.getId())));
    }

    public Observable.Transformer<E, E> cacheDeleteAll(String cacheId) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(observable -> source
                        .doOnNext(v -> delete(cacheId)));
    }

    public <L> Observable.Transformer<L, L> cacheDeleteAll(String cacheId, Class<L> clazz) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(observable -> source
                        .doOnNext(v -> delete(cacheId)));
    }

    public <L> Observable.Transformer<L, L> cacheDelete(String cacheId, Object itemKey, Class<L> clazz) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(eObservable -> source
                        .doOnNext(v -> delete(cacheId, itemKey)));
    }

}
