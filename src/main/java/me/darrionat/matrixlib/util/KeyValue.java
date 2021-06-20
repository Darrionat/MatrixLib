package me.darrionat.matrixlib.util;

/**
 * A util class that is used for storing a single pair of values together.
 *
 * @param <K> The type of the key.
 * @param <V> The type of the value.
 */
public class KeyValue<K, V> {
    private final K key;
    private final V value;

    /**
     * Constructs a new {@code KeyValue} pair to store a pair of values together.
     *
     * @param key   The key to store.
     * @param value The value to store.
     */
    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key of this pair.
     *
     * @return The key of the pair.
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets the value of this pair.
     *
     * @return The value of the pair.
     */
    public V getValue() {
        return value;
    }
}