package com.intexsoft.repository;

import com.intexsoft.model.CommonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.functions.Action1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class CacheRx<E extends CommonModel<I, E>, I extends Serializable> {

    private Cache getCache(String cacheId) {
        return cacheManager.getCache(cacheId);
    }

    @Autowired
    private CacheManager cacheManager;

    private <S> List<S> find(String cacheId, Object itemKey) {
        List<S> list = getCache(cacheId).get(itemKey, ArrayList.class);
        return list;
    }

    private <L> void putCache(String cacheId, Object itemKey, List<L> list) {
        List<L> list2 = new ArrayList<>();
        list2.addAll(list);
        getCache(cacheId).put(itemKey, list2);
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

    public <L> Observable.Transformer<L, L> cachable(String cacheId, Object itemKey) {
        return source -> Observable.fromCallable(() -> isEmpty(cacheId, itemKey))
                .flatMap(empty -> empty
                        ? source
                        .toList()
                        .doOnNext(v -> putCache(cacheId, itemKey, v))
                        .flatMap(Observable::from)
                        : Observable.from(find(cacheId, itemKey)));
    }

    public Observable.Transformer<E, E> cachePut(String cacheId) {
        return commonTransformer(s -> putCache(cacheId, s.getId(), Arrays.asList(s)));
    }

    public Observable.Transformer<E, E> cacheDelete(String cacheId) {
        return commonTransformer(s -> delete(cacheId, s.getId()));
    }

    public <L> Observable.Transformer<L, L> cachePut(String cacheId, Object itemKey) {
        return commonTransformer(s -> putCache(cacheId, itemKey, Arrays.asList(s)));
    }

    public <L> Observable.Transformer<L, L> cacheDeleteAll(String cacheId) {
        return commonTransformer(s -> delete(cacheId));
    }

    public <L> Observable.Transformer<L, L> cacheDelete(String cacheId, Object itemKey) {
        return commonTransformer(s -> delete(cacheId, itemKey));
    }

    public <S> Observable.Transformer<S, S> commonTransformer(Action1<S> action1) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(objectObservable -> source
                        .doOnNext(action1));
    }

}
