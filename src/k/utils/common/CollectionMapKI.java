package k.utils.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Qbit
 *
 * @param <K> key
 * @param <C> collection<V>
 * @param <V> value
 */
public interface CollectionMapKI<K, C extends Collection<V>, V> {
	boolean put(K key, V value);

	C get(K key);

	abstract class CollectionMapKA<K, C extends Collection<V>, V> implements
			CollectionMapKI<K, C, V> {
		abstract C getCollection();

		Map<K, C> map = new HashMap<>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see k.utils.common.CollectionMapKI#put(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public boolean put(K key, V value) {
			C c = map.get(key);
			if (c == null) {
				c = getCollection();
				map.put(key, c);
			}
			return c.add(value);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see k.utils.common.CollectionMapKI#get(java.lang.Object)
		 */
		@Override
		public C get(K key) {
			return map.get(key);
		}
	}
	class CollectionMapK<K,C extends Collection<V>,V> extends CollectionMapKA<K, Collection<V>, V>{
		private CollectionProvider<V> provider;
		@Override
		Collection<V> getCollection() {
			return provider.provide();
		}
		protected CollectionMapK(CollectionProvider<V> provider) {
			super();
			this.provider = provider;
		}
	}
	class ListMapK<K,V> extends CollectionMapK<K, List<V>, V>{

		protected ListMapK() {
			super(new ArraylistProvider<V>());
			// TODO Auto-generated constructor stub
		}
		
	}
}
abstract class CollectionProvider<K>{
	abstract Collection provide();
}
class ArraylistProvider<K> extends CollectionProvider<K>{

	@Override
	ArrayList<K> provide() {
		return new ArrayList<K>();
	}
	
}
