package com.intexsoft.service;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CacheRx;
import com.intexsoft.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.Serializable;
import java.util.Objects;

public abstract class CommonService<E extends CommonModel<I, E>, I extends Serializable> {

    @Autowired
    private CommonRepository<E, I> repository;

    protected abstract Class<I> getIdClass();

    protected abstract Class<E> getModelClass();

    @Autowired
    protected CacheRx<E, I> cacheRx;

    protected abstract String searchCacheId();

    protected abstract String commonCacheId();

    protected abstract String withItemsCacheId();

    public Observable<E> getById(I id) {
        return Observable.just(id)
                .map(repository::getById)
                .compose(cacheRx.cachable(commonCacheId(), id))
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> store(E e) {
        return Observable.just(e)
                .map(repository::save)
                .compose(cacheRx.cachePut(commonCacheId()))
                .compose(cacheRx.cacheDeleteAll(searchCacheId()))
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> update(E e) {
        return Observable.just(e)
                .map(repository::update)
                .compose(cacheRx.cachePut(commonCacheId()))
                .compose(cacheRx.cacheDeleteAll(commonCacheId()))
                .compose(cacheRx.cacheDelete(commonCacheId()))
                .subscribeOn(Schedulers.io());

    }

    public Observable<I> delete(I id) {
        return Observable.just(id)
                .doOnNext(s -> repository.deleteById(s))
                .compose(cacheRx.cacheDelete(commonCacheId(), id))
                .compose(cacheRx.cacheDelete(withItemsCacheId(), id))
                .compose(cacheRx.cacheDeleteAll(searchCacheId()))
                .subscribeOn(Schedulers.io());
    }

}
