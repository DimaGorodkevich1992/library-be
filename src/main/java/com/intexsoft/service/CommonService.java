package com.intexsoft.service;

import com.intexsoft.model.CommonModel;
import com.intexsoft.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.Serializable;
import java.util.Objects;

public abstract class CommonService<E extends CommonModel<I, E>, I extends Serializable> {

    @Autowired
    private CommonRepository<E, I> repository;

    public Observable<E> getById(I id) {
        return Observable.just(id)
                .map(repository::getById)
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> store(E e) {
        return Observable.just(e)
                .doOnNext(repository::save)
                .subscribeOn(Schedulers.io());
    }

    public Observable<E> update(E e) {
        return Observable.just(e)
                .doOnNext(repository::update)
                .subscribeOn(Schedulers.io());

    }

    public Observable<I> delete(I id) {
        return Observable.just(id)
                .doOnNext(s -> repository.deleteById(s))
                .subscribeOn(Schedulers.io());
    }

}
