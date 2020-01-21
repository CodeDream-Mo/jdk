package com.liqiang.hashmap;

public class Entry<K, V> implements BaseEntry<K, V> {
    K k;
    V v;
    //HashMap是通过数组+链表，所以这边的节点是链表中的节点，存在next指向下一个节点。
    Entry<K, V> next;

    public Entry(K k, V v, Entry<K, V> next) {
        this.k = k;
        this.v = v;
        this.next = next;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }
}
