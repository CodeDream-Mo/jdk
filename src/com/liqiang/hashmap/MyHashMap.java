package com.liqiang.hashmap;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements BaseMap<K, V> {
    private int defaultLength = 16;//默认长度
    private double defaultAddFactor = 0.75;//默认负载因子
    private double useSize;//使用数组位置的数量
    private Entry<K, V>[] table;//数组

    //定义了两个构造方法，一个默认使用基本长度和负载因子。一个可自定义长度和负载因子。
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int defaultLength, double defaultAddFactor) {
        if (defaultLength < 0) {
            throw new IllegalArgumentException("数组异常");
        }
        if (defaultAddFactor <= 0 || Double.isNaN(defaultAddFactor)) {
            throw new IllegalArgumentException("因子异常");
        }
        this.defaultLength = defaultLength;
        this.defaultAddFactor = defaultAddFactor;
        table = new Entry[defaultLength];
    }

    /**
     * 使用每个object的hashCode计算hashCode
     *
     * @param hashCode
     * @return hashCode
     */
    private int hash(int hashCode) {
        hashCode = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
        return hashCode ^ ((hashCode >>> 7) ^ hashCode >>> 4);
    }

    /**
     * 获取保存位置的数组下标
     * 根据hashcode取模计算下标
     *
     * @param k
     * @param length
     * @return
     */
    private int getIndex(K k, int length) {
        int m = length - 1;
        int index = hash(k.hashCode()) & m;//取模
        return index >= 0 ? index : -index;
    }


    /**
     * 存
     * 将得到的数据创建节点对象，计算出hashcode，得到对应下标，
     * 在对应数组的下标内，如果对应数组下标为null，直接将新的节点放入；
     * 反之，从链表头部开始遍历，如果存在相同key，说明是覆盖原来的值，
     * 反之将新节点插入链的尾部。
     *
     * @param k
     * @param v
     * @return
     */
    @Override
    public V put(K k, V v) {
        if (useSize > defaultAddFactor * defaultLength) {
            //扩容
            dilatation();
        }
        //计算出下标
        int index = getIndex(k, table.length);
        Entry<K, V> entry = table[index];
        Entry<K, V> newEntry = new Entry<>(k, v, null);
        if (entry == null) {
            table[index] = newEntry;
            useSize++;//table中有位置被占
        } else {
            Entry<K, V> t = entry;
            if (t.getKey() == k || (t.getKey() != null && t.getKey().equals(k))) {//相同key 对应修改当前value
                t.v = v;
            } else {
                while (t.next != null) {
                    if (t.next.getKey() == k || (t.next.getKey() != null && t.next.getKey().equals(k))) {//相同key 对应修改当前value
                        t.next.v = v;
                        break;
                    } else {
                        t = t.next;
                    }
                }
                if (t.next == null) {
                    t.next = newEntry;
                }
            }
        }
        return newEntry.getValue();
    }


    /**
     * 取值
     * 得到下标，剩下的就是链表找值的问题。
     *
     * @param k
     * @return
     */
    @Override
    public V get(K k) {
        int index = getIndex(k, table.length);
        Entry<K, V> entry = table[index];
        if (entry == null) {
            throw new NullPointerException();
        }
        while (entry != null) {
            if (k == entry.getKey() || k.equals(entry.getKey())) {
                return entry.v;
            } else {
                entry = entry.next;
            }
        }
        return null;
    }

    /**
     * 扩容
     * 当数组使用的长度大于数组长度*负载因子，就需要扩容。
     * 如何扩容呢?
     * 1、得到所有的节点
     * 2、重新put进入数组
     */
    private void dilatation() {
        Entry<K, V>[] newTable = new Entry[defaultLength * 2];
        List<Entry<K, V>> list = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null)
                continue;
            //遍历链表 添加到list
            Entry<K, V> entry = table[i];
            while (entry != null) {
                list.add(entry);
                entry = entry.next;
            }
        }
        if (list.size() > 0) {
            useSize = 0;
            defaultLength = defaultLength * 2;
            table = newTable;
            for (Entry<K, V> entry : list) {
                //分离所有的entry
                if (entry.next != null) {
                    entry.next = null;
                }
                put(entry.getKey(), entry.getValue());
            }
        }
    }

}
