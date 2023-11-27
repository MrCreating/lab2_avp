import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionalMapPessimistic<K, V> extends BaseTransactionalHashMap<K, V> {

    private final Map<K, V> map = new HashMap<>();
    private final Map<K, V> transactionCache = new HashMap<>();

    private final Map<K, Lock> locks = new HashMap<>();

    @Override
    public void beginTransaction(K key) {
        Lock lock = new ReentrantLock();
        locks.put(key, lock);
        lock.lock();

        transactionCache.put(key, map.get(key));
    }

    @Override
    public void commitTransaction(K key) {
        Lock lock = locks.get(key);
        if (lock != null) {
            lock.unlock();
            locks.remove(key);
            transactionCache.remove(key);
        }
    }

    @Override
    public void rollbackTransaction(K key) {
        V originalValue = transactionCache.get(key);
        if (originalValue != null) {
            map.put(key, originalValue);
            commitTransaction(key);
        }
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }
}
