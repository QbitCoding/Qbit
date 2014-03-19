package k.utils.common;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class SetCollectionKA<K> extends AbstractCollection<K> implements
		SetCollectionKI<K> {
	private Set<K> set;
	private Collection<K> collection;
	public final String name;
	private SetCollectionKA(Set<K> set,Collection<K> collection) {
		super();
		this.set=set;
		this.collection=collection;
		name=collection.getClass().getName();
	}
	public static <K>SetCollectionKA<K> getInstance(Set<K> set,Collection<K> collection){
		if(set==null||collection==null) return null;
		return new SetCollectionKA<K>(set, collection);
	}

	@Override
	public Collection<K> getCollection() {
		return collection;
	}
	private class MyIterator<K> implements Iterator<K>{
		private Collection<K> collection;
		private Set<K> set;
		private Iterator<K> iterator;
		private K now;
		public MyIterator(Set<K> set,Collection<K> collection) {
			super();
			this.set=set;
			this.collection=collection;
			iterator=collection.iterator();
		}
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public K next() {
			return now= iterator.next();
		}

		@Override
		public void remove() {
			iterator.remove();
			set.remove(now);
		}
		
	}
	@Override
	public Iterator<K> iterator() {
		return new MyIterator<>(set, collection);
	}

	@Override
	public int size() {
		return set.size();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(K e) {
		if(set.contains(e)) return false;
		boolean b=collection.add(e);
		if(b) set.add(e);
		return b;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends K> c) {
		boolean b=true;
		for(K k:c){
			b=b&add(k);//此处不应该使用短路
		}
		return b;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear() {
		set.clear();
		collection.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		boolean b=collection.remove(o);
		if(b) set.remove(o);
		return b;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean b=true;
		for(Object o:c){
			b=b&remove(o);
		}
		return b;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean b=collection.retainAll(c);
		set.clear();
		set.addAll(collection);
		return b;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return collection.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Please Generics");
//		return collection.toArray(a);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		return collection.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return collection.equals(obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return collection.hashCode();
	}
	
}
