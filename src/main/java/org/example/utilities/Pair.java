package org.example.utilities;

public class Pair<T, T1> {

    public T key;
    public T1 value;

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public Pair(T key, T1 value) {
        this.key = key;
        this.value = value;
    }
}
