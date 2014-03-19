package utilsK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class SetList<E> implements List<E>, Set<E> {
	private Set<E> set = new HashSet<E>();
	private List<E> list=new ArrayList<E>();

	@Override
	public boolean add(E arg0) {
		if(set.contains(arg0)) return false;
		set.add(arg0);
		list.add(arg0);
		return true;
	}

	@Override
	public void add(int arg0, E arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		if(arg0==null) return false;
		for(E e:arg0){
			add(e);
		}
		return true;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		set.clear();
		list.clear();

	}

	@Override
	public boolean contains(Object arg0) {
		return set.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		if(arg0==null) return true;
		boolean b=true;
		for(Object e:arg0){
			b=b&&set.contains(e);
		}
		return b;
	}

	@Override
	public E get(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int indexOf(Object arg0) {
		return list.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return list.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object arg0) {
		set.remove(arg0);
		return list.remove(arg0);
	}

	@Override
	public E remove(int arg0) {
		E e = list.remove(arg0);
		set.remove(e);
		return e;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean b=true;
		for(Object e:arg0){
			b=b&&remove(e);
		}
		return b;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return set.contains(arg0);
	}

	@Override
	public E set(int index, E element) {
		E e = list.set(index, element);
		if(e==null) 
		return null;
		else{
			set.add(element);
			return e;
		}
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<E> subList(int arg0, int arg1) {
		SetList<E> setlist = new SetList<E>();
		setlist.list=list.subList(arg0, arg1);
		setlist.set=new HashSet<E>(setlist.list);
		return setlist;
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		throw new UnsupportedOperationException();
	}

}
