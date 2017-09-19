package com.batsw.anonimitychat.util;

/**
 * Created by tudor on 9/19/2017.
 */

public class KeyValuePair<K, V> {
    private K mK;
    private V mV;

    public KeyValuePair(K k, V v) {
        this.mK = k;
        this.mV = v;
    }

    public K getK() {
        return mK;
    }

    public V getV() {
        return mV;
    }

    public void setK(K k) {
        this.mK = k;
    }

    public void setV(V v) {
        this.mV = v;
    }

    @Override
    public String toString() {
        return "KeyValuePair{" +
                "K=" + mK +
                ", V=" + mV +
                '}';
    }
}
