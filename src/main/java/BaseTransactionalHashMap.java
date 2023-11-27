abstract class BaseTransactionalHashMap<K, V> {
    abstract public void beginTransaction (K key);

    abstract public void commitTransaction(K key);

    abstract public void rollbackTransaction(K key);

    abstract public void put(K key, V value);

    abstract public V get(K key);
}
