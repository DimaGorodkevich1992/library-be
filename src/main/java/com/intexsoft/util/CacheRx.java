package com.intexsoft.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.functions.Action1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class CacheRx {

    private Cache getCache(String cacheId) {
        return cacheManager.getCache(cacheId);
    }

    @Autowired
    private CacheManager cacheManager;

    private <S> List<S> find(String cacheId, Object itemKey) {
        return getCache(cacheId).get(itemKey, ArrayList::new);
    }

    private <L> void putCache(String cacheId, Object itemKey, List<L> listItems) {
        List<L> tmpListItems = new ArrayList<>(listItems);
        getCache(cacheId).put(itemKey, tmpListItems);
    }

    private boolean isEmpty(String cacheId, Object key) {
        return Objects.equals(getCache(cacheId).get(key), null);
    }

    private void delete(String cacheId) {
        getCache(cacheId).clear();
    }

    private void delete(String cachedId, Object itemKey) {
        getCache(cachedId).evict(itemKey);
    }

    public <L> Observable.Transformer<L, L> cachable(String cacheId, Object... itemKey) {
        return source -> Observable.fromCallable(() -> isEmpty(cacheId, itemKey))
                .flatMap(empty -> empty
                        ? source
                        .toList()
                        .doOnNext(v -> putCache(cacheId, itemKey, v))
                        .flatMap(Observable::from)
                        : Observable.from(find(cacheId, itemKey)));
    }

    public <L> Observable.Transformer<L, L> cachePut(String cacheId, Function<L, Object> itemKey) {
        return commonTransformer(s -> putCache(cacheId, itemKey.apply(s), Collections.singletonList(s)));
    }

    public <L> Observable.Transformer<L, L> cacheDeleteAll(String cacheId) {
        return commonTransformer(s -> delete(cacheId));
    }

    public <L> Observable.Transformer<L, L> cacheDelete(String cacheId, Function<L, Object> itemKey) {
        return commonTransformer(s -> delete(cacheId, itemKey.apply(s)));
    }

    public <L> Observable.Transformer<L, L> commonTransformer(Action1<L> typeOperation) {
        return source -> Observable.fromCallable(() -> source)
                .flatMap(objectObservable -> source
                        .doOnNext(typeOperation));
    }

}
