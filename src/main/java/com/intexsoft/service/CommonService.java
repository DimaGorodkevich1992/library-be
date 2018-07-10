package com.intexsoft.service;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CacheManagerRedis;
import com.intexsoft.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CommonService<E extends CommonModel<I, E>, I extends Serializable> {

    @PostConstruct
    private void initCache() {
        hashOperations = redisTemplate.opsForHash();
        hashOperationsWithItems = redisTemplate.opsForHash();
    }

    @Autowired
    private CacheManagerRedis<E, I> cacheManagerRedis;

    @Autowired
    private CommonRepository<E, I> repository;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    protected abstract String commonCacheId();

    protected List<String> getListKeyToDeleteItems(List<String> cacheIdForItems) {
        return cacheIdForItems;
    }

    protected HashOperations<String, Object, E> hashOperations;

    protected HashOperations<String, String, List<E>> hashOperationsWithItems;

    protected void isExistAndSubmitCache(String cacheId, E item) {
        if (!hashOperations.hasKey(cacheId, item.getId())) {
            hashOperations.put(cacheId, item.getId(), item);
        }
    }

    protected void isExistAndSubmitCacheWithItemsList(String cacheId, String hashKey, List<E> item) {
        if (!hashOperationsWithItems.hasKey(cacheId, hashKey)) {
            hashOperationsWithItems.put(cacheId, hashKey, item);
        }
    }

    protected void submitCacheAndDeleteItemsCache(String cacheId, E item) {
        hashOperations.put(cacheId, item.getId(), item);
        redisTemplate.delete(getListKeyToDeleteItems(new ArrayList<>()));
    }

    protected E isExistCacheAndGetResult(String cacheId, I hashKey, E item) {
        return hashOperations.hasKey(cacheId, hashKey)
                ? hashOperations.get(cacheId, hashKey)
                : item;
    }

    protected List<E> isExistCAcheAndGetResultWithItemsList(String cacheId, Object hashKey, List<E> items) {
        return hashOperationsWithItems.hasKey(cacheId, hashKey)
                ? hashOperationsWithItems.get(cacheId, hashKey)
                : items;
    }

    private boolean getCache(I hashKey) {
        return false;
    }

    public Observable<E> getById(I id) {
        return null; /*Observable.just(id)
                .map(repository::getById)
                .compose(observable ->
                        Observable.fromCallable(() -> cachable(id)
                                ? getCache(id)
                                : observable
                                .take(1)
                                .doOnNext(v -> cachePut(id, v))))
                .filter(Objects::nonNull)
                .doOnNext(s -> isExistAndSubmitCache(commonCacheId(), s))
                .subscribeOn(Schedulers.io());*/
    }

    public Observable<E> store(E e) {
        return Observable.just(e)
                .map(repository::save)
                .doOnNext(v -> cacheManagerRedis.cachePut("commonBook", v.getId(), v))
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> update(E e) {
        return Observable.just(e)
                .map(repository::update)
                .doOnNext(v -> submitCacheAndDeleteItemsCache(commonCacheId(), v))
                .subscribeOn(Schedulers.io());

    }

    public Observable<I> delete(I id) {
        return Observable.just(id)
                .doOnNext(s -> repository.deleteById(s))
                .doOnNext(v -> {
                    hashOperations.delete(commonCacheId(), id);
                    redisTemplate.delete(getListKeyToDeleteItems(new ArrayList<>()));
                })
                .subscribeOn(Schedulers.io());
    }

}
