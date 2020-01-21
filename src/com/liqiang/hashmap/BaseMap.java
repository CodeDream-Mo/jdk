package com.liqiang.hashmap;

/**
 * HashMap的基类接口
 */
public interface BaseMap<K, V> {
    //存
    V put(K k, V v);

    //取
    V get(K k);
}
