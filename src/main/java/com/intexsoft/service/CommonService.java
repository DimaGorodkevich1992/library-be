package com.intexsoft.service;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import com.intexsoft.util.CacheRx;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.Serializable;
import java.util.Objects;

public abstract class CommonService<E extends CommonModel<I, E>, I extends Serializable> {

    @Autowired
    private CommonRepository<E, I> repository;

    @Autowired
    protected CacheRx cacheRx;

    protected abstract String searchCacheId();

    protected abstract String entityCacheId();

    protected abstract String entityWithItemsCacheId();

    public Observable<E> getById(I id) {
        return Observable.just(id)
                .map(repository::getById)
                .compose(cacheRx.cachable(entityCacheId(), id))
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> store(E e) {
        return Observable.just(e)
                .map(repository::save)
                .compose(cacheRx.cachePut(entityCacheId(), CommonModel::getId))
                .compose(cacheRx.cacheDeleteAll(searchCacheId()))
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> update(E e) {
        return Observable.just(e)
                .map(repository::update)
                .compose(cacheRx.cachePut(entityCacheId(), CommonModel::getId))
                .compose(cacheRx.cacheDeleteAll(searchCacheId()))
                .compose(cacheRx.cacheDelete(entityWithItemsCacheId(), CommonModel::getId))
                .subscribeOn(Schedulers.io());

    }

    public Observable<I> delete(I id) {
        return Observable.just(id)
                .doOnNext(s -> repository.deleteById(s))
                .compose(cacheRx.cacheDelete(entityCacheId(), s -> s))
                .compose(cacheRx.cacheDelete(entityWithItemsCacheId(), s -> s))
                .compose(cacheRx.cacheDeleteAll(searchCacheId()))
                .subscribeOn(Schedulers.io());
    }

}
