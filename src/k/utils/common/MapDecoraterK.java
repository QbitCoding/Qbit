package k.utils.common;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

abstract public class MapDecoraterK<K,V>extends AbstractMap<K, V>{
	private Map<K,V> map;
	protected MapDecoraterK(Map<K,V> map) {
		this.map=map;
	}
	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		V value=map.get(key);
		if(value!=null) return value;
		return doGet(key);
	}

	abstract protected V doGet(Object key);

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		return map.put(key, value);
	}
	
}
