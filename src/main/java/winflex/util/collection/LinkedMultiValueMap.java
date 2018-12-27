package winflex.util.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable, Cloneable {

	private static final long serialVersionUID = 3801124242820219131L;

	private final Map<K, List<V>> targetMap;


	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}.
	 */
	public LinkedMultiValueMap() {
		this.targetMap = new LinkedHashMap<>();
	}

	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}
	 * with the given initial capacity.
	 * @param initialCapacity the initial capacity
	 */
	public LinkedMultiValueMap(int initialCapacity) {
		this.targetMap = new LinkedHashMap<>(initialCapacity);
	}

	/**
	 * Copy constructor: Create a new LinkedMultiValueMap with the same mappings as
	 * the specified Map. Note that this will be a shallow copy; its value-holding
	 * List entries will get reused and therefore cannot get modified independently.
	 * @param otherMap the Map whose mappings are to be placed in this Map
	 * @see #clone()
	 * @see #deepCopy()
	 */
	public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
		this.targetMap = new LinkedHashMap<>(otherMap);
	}


	// MultiValueMap implementation

	@Override
	
	public V getFirst(K key) {
		List<V> values = this.targetMap.get(key);
		return (values != null ? values.get(0) : null);
	}

	@Override
	public void add(K key,  V value) {
		// List<V> values = this.targetMap.computeIfAbsent(key, k -> new LinkedList<>());
		// values.add(value);
		List<V> values = this.targetMap.get(key);
		if (values == null) {
		    this.targetMap.put(key, values = new LinkedList<>());
		}
		values.add(value);
	}

	@Override
	public void addAll(K key, List<? extends V> values) {
		// List<V> currentValues = this.targetMap.computeIfAbsent(key, k -> new LinkedList<>());
		// currentValues.addAll(values);
		List<V> oldValues = this.targetMap.get(key);
        if (oldValues == null) {
            this.targetMap.put(key, new LinkedList<>(values));
        } else {
            oldValues.addAll(values);
        }
	}

	@Override
	public void addAll(MultiValueMap<K, V> values) {
		for (Entry<K, List<V>> entry : values.entrySet()) {
			addAll(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void set(K key,  V value) {
		List<V> values = new LinkedList<>();
		values.add(value);
		this.targetMap.put(key, values);
	}

	@Override
	public void setAll(Map<K, V> values) {
		// values.forEach(this::set);
		for (Map.Entry<K, V> entry : values.entrySet()) {
		    set(entry.getKey(), entry.getValue());
        }
	}

	@Override
	public Map<K, V> toSingleValueMap() {
		LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.targetMap.size());
		//this.targetMap.forEach((key, value) -> singleValueMap.put(key, value.get(0)));
		for (Map.Entry<K, List<V>> entry : this.targetMap.entrySet()) {
		    singleValueMap.put(entry.getKey(), entry.getValue().get(0));
        }
		return singleValueMap;
	}


	// Map implementation

	@Override
	public int size() {
		return this.targetMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.targetMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.targetMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.targetMap.containsValue(value);
	}

	@Override
	
	public List<V> get(Object key) {
		return this.targetMap.get(key);
	}

	@Override
	
	public List<V> put(K key, List<V> value) {
		return this.targetMap.put(key, value);
	}

	@Override
	
	public List<V> remove(Object key) {
		return this.targetMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends List<V>> map) {
		this.targetMap.putAll(map);
	}

	@Override
	public void clear() {
		this.targetMap.clear();
	}

	@Override
	public Set<K> keySet() {
		return this.targetMap.keySet();
	}

	@Override
	public Collection<List<V>> values() {
		return this.targetMap.values();
	}

	@Override
	public Set<Entry<K, List<V>>> entrySet() {
		return this.targetMap.entrySet();
	}


	/**
	 * Create a deep copy of this Map.
	 * @return a copy of this Map, including a copy of each value-holding List entry
	 * @since 4.2
	 * @see #clone()
	 */
	public LinkedMultiValueMap<K, V> deepCopy() {
		LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap<>(this.targetMap.size());
		//this.targetMap.forEach((key, value) -> copy.put(key, new LinkedList<>(value)));
		for (Map.Entry<K, List<V>> entry : this.targetMap.entrySet()) {
		    copy.put(entry.getKey(), new LinkedList<>(entry.getValue()));
		}
		return copy;
	}

	/**
	 * Create a regular copy of this Map.
	 * @return a shallow copy of this Map, reusing this Map's value-holding List entries
	 * @since 4.2
	 * @see LinkedMultiValueMap#LinkedMultiValueMap(Map)
	 * @see #deepCopy()
	 */
	@Override
	public LinkedMultiValueMap<K, V> clone() {
		return new LinkedMultiValueMap<>(this);
	}

	@Override
	public boolean equals(Object obj) {
		return this.targetMap.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.targetMap.hashCode();
	}

	@Override
	public String toString() {
		return this.targetMap.toString();
	}

}