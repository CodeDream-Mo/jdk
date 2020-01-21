package com.liqiang.hashmap;

/**
 * 节点的基类接口
 * @param <K>
 * @param <V>
 */
public interface BaseEntry<K, V> {
    K getKey();//获取键

    V getValue();//获取值
}
