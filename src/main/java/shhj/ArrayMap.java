package shhj;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ArrayMap<V> extends AbstractMap<Integer, V> implements Map<Integer, V> {

    private static final Object NULL = new Object();
    private int size;
    private Object[] positives;
    private Object[] negatives;


    public ArrayMap() {
        positives = new Object[10];
        negatives = new Object[10];
    }


    @Override public int size() {
        return size;
    }

    @Override public boolean isEmpty() {
        return size == 0;
    }

    @Override public boolean containsKey(Object key) {
        if (!(key instanceof Integer))
            return false;
        int idx = (Integer) key;
        if (idx >= 0) {
            return idx < positives.length && positives[idx] != null;
        } else {
            idx = ~idx;
            return idx < negatives.length && negatives[idx] != null;
        }
    }

    @Override public boolean containsValue(Object value) {
        if (value == null)
            value = NULL;
        for (Object val : positives) {
            if (value.equals(val)) {
                return true;
            }
        }
        for (Object val : negatives) {
            if (value.equals(val)) {
                return true;
            }
        }
        return false;
    }

    @Override public V get(Object key) {
        return null;
    }

    @Override public V put(Integer key, V value) {
        int idx = key;
        Object val = value == null ? NULL : value;
        if (idx >= 0) {
            if (positives.length <= idx) {
                positives = Arrays.copyOf(positives, idx);
            }
            if (positives[idx] != null)
                size++;
            positives[idx] = val;
        } else {
            idx = ~idx;
            if (negatives.length <= idx) {
                positives = Arrays.copyOf(positives, idx);
            }
            if (negatives[idx] != null)
                size++;
            negatives[idx] = val;
        }
        return value;
    }

    @Override public V remove(Object key) {
        return null;
    }

    @Override public void putAll(@NotNull Map<? extends Integer, ? extends V> m) {

    }

    @Override public void clear() {

    }

    @NotNull @Override public Set<Integer> keySet() {
        return null;
    }

    @NotNull @Override public Collection<V> values() {
        return null;
    }


    @NotNull @Override public Set<Entry<Integer, V>> entrySet() {
        return null;
    }

    @Override public V getOrDefault(Object key, V defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    @Override public void forEach(BiConsumer<? super Integer, ? super V> action) {
        super.forEach(action);
    }

    @Override public void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> function) {
        super.replaceAll(function);
    }

    @Nullable @Override public V putIfAbsent(Integer key, V value) {
        return super.putIfAbsent(key, value);
    }

    @Override public boolean remove(Object key, Object value) {
        return super.remove(key, value);
    }

    @Override public boolean replace(Integer key, V oldValue, V newValue) {
        return super.replace(key, oldValue, newValue);
    }

    @Nullable @Override public V replace(Integer key, V value) {
        return super.replace(key, value);
    }

    @Override public V computeIfAbsent(Integer key,
            @NotNull Function<? super Integer, ? extends V> mappingFunction) {
        return super.computeIfAbsent(key, mappingFunction);
    }

    @Override public V computeIfPresent(Integer key,
            @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        return super.computeIfPresent(key, remappingFunction);
    }

    @Override public V compute(Integer key,
            @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        return super.compute(key, remappingFunction);
    }

    @Override public V merge(Integer key, @NotNull V value,
            @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return super.merge(key, value, remappingFunction);
    }
}
