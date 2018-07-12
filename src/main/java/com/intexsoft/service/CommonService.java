package com.intexsoft.service;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CacheManagerRx;
import com.intexsoft.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.Serializable;
import java.util.Objects;

public abstract class CommonService<E extends CommonModel<I, E>, I extends Serializable> {

    @Autowired
    private CacheManagerRx<E> cacheManagerRx;

    @Autowired
    private CommonRepository<E, I> repository;

    protected abstract Class<E> getEntityClass();

    protected abstract String searchCacheId();

    protected abstract String commonCacheId();

    protected abstract String withItemsCacheId();

    public Observable<E> getById(I id) {
        return Observable.just(id)
                .map(repository::getById)
                .compose(observable ->
                        Observable.fromCallable(() -> cacheManagerRx.isEmpty(commonCacheId(), id, getEntityClass())
                                ? Observable.just(cacheManagerRx.find(commonCacheId(), id, getEntityClass()))
                                : observable.doOnNext(v -> cacheManagerRx.cachePut(commonCacheId(), v.getId(), v))))
                .compose(Observable::merge)
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> store(E e) {
        return Observable.just(e)
                .map(repository::save)
                .doOnNext(v -> {
                    cacheManagerRx.cachePut(commonCacheId(), v.getId(), v);
                    cacheManagerRx.deleteCache(searchCacheId());
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> update(E e) {
        return Observable.just(e)
                .map(repository::update)
                .doOnNext(v -> {
                    cacheManagerRx.cachePut(commonCacheId(), v.getId(), v);
                    cacheManagerRx.deleteCache(searchCacheId());
                    cacheManagerRx.deleteItemOfCache(withItemsCacheId(), v.getId());
                })
                .subscribeOn(Schedulers.io());

    }

    public Observable<I> delete(I id) {
        return Observable.just(id)
                .doOnNext(s -> repository.deleteById(s))
                .doOnNext(v -> {
                    cacheManagerRx.deleteItemOfCache(commonCacheId(), v);
                    cacheManagerRx.deleteCache(searchCacheId());
                    cacheManagerRx.deleteItemOfCache(withItemsCacheId(), v);
                })
                .subscribeOn(Schedulers.io());
    }

}
