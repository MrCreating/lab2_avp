import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TransactionalMapOptimistic<K, V> extends BaseTransactionalHashMap<K, V> {
    private final Map<K, AtomicReference<V>> map = new HashMap<>();
    private final Map<K, V> transactionCache = new HashMap<>();

    @Override
    public void beginTransaction(K key) {
        transactionCache.put(key, map.getOrDefault(key, new AtomicReference<>()).get());
    }

    @Override
    public void commitTransaction(K key) {
        AtomicReference<V> atomicReference = map.get(key);
        if (atomicReference != null) {
            atomicReference.set(get(key));
            transactionCache.remove(key);
        }
    }

    @Override
    public void rollbackTransaction(K key) {
        V originalValue = transactionCache.get(key);
        if (originalValue != null) {
            map.put(key, new AtomicReference<>(originalValue));
            transactionCache.remove(key);
        }
    }

    @Override
    public void put(K key, V value) {
        map.put(key, new AtomicReference<>(value));
    }

    @Override
    public V get(K key) {
        AtomicReference<V> atomicReference = map.get(key);
        return atomicReference != null ? atomicReference.get() : null;
    }
}
