package k.utils.common;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 
 * @author Qbit
 *2014-02-17
 * @param <E>
 */

public class SetTreeK<E> implements Set<E> {
	private static Map<Object, Set> defaultmap =new HashMap<Object, Set>();
	private Map<Object, Set> map;
	private Set<E> set  ;
	private E name;

	public SetTreeK(E name) {
		this(name,defaultmap);
	}
	public SetTreeK(E name,Map<Object, Set> map){
		this.map=map;
		this.name=name;
		set=map.get(name);
		if(set==null){
			set=new HashSet<E>();
			map.put(name, set);
		}
	}

	public static void main(String[] args) {
		SetTreeK<String> asd = new SetTreeK<String>("asd");
		asd.add("qwe");
		asd.add("zxc");
		SetTreeK<String> qwe = new SetTreeK<String>("qwe");
		qwe.add("123");
		SetTreeK<String> q123 = new SetTreeK<String>("123");
		q123.add("345");
		for (String s : qwe) {
			System.out.println(s);
		}
		qwe.size();
	}

	@Override
	public boolean add(E arg0) {
		return set.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		// TODO Auto-generated method stub
		return set.addAll(arg0);
	}

	@Override
	public void clear() {
		set.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		Iterator<E> e = iterator();
		while (e.hasNext()) {
			if (arg0.equals(e.next()))
				return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		for (Object o : arg0) {
			if (contains(o))
				return true;
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		class DeepIterator implements Iterator<E> {
			Set<E> myset;
			Deque<Object> deque;

			public DeepIterator() {
				myset = new HashSet<E>();
				deque = new ArrayDeque<Object>();
				deque.addAll(set);
			}

			@Override
			public boolean hasNext() {
				return deque.size() > 0;
			}

			@Override
			public E next() {
				E e = (E) deque.pollFirst();// 通过getFirst和getLast可以调整深度优先还是广度优先
				Set thisset = map.get(e);
				if (thisset != null) {
					for (Object o : thisset) {
						if (!myset.contains(o))
							deque.add(o);
					}
				}
				return e;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new DeepIterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return set.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean b = false;
		for (Object o : arg0) {
			b = b && remove(o);
		}
		return b;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		boolean b = true;
		for (Object o : arg0) {
			b = b && contains(o);
		}
		return b;
	}
/**
 * 注意这里的size是返回的直接size,如果使用iterator可能会获得更多的元素
 */
	@Override
	public int size() {
		return set.size();
	}

	@Override
	public Object[] toArray() {
		List<Object> list = new ArrayList<Object>();
		for (Object o : this) {
			list.add(o);
		}
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		return name.equals(obj);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name.toString()+":"+set.toString();
	}
}
