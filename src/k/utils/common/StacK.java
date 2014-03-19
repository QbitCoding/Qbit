package k.utils.common;

import java.util.ArrayList;
import java.util.List;
/**
 * 1.1
 * @author Qbit
 *
 * @param <E>
 */
public final class StacK<E> {
	private List<E> list = new ArrayList<E>();
	/**
	 * since 1.1
	 */
	public void clear(){
		list=new ArrayList<E>();
	}
	/**
	 * since 1.1
	 * @return
	 */
	public boolean isEmpty(){
		return list.size()==0;
	}
	public void push(E e){
		list.add(e);
	}
	public E top(){
		int size=list.size();
		if(size==0) return null;
		return list.get(size-1);
	}
	public E pop(){
		int size=list.size();
		if(size==0) return null;
		return list.remove(size-1);
	}
	public static void main(String[] args) {
		System.out.println(new StacK<>().pop()==null);
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return list.equals(obj);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return list.hashCode();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return list.toString();
	}
	
}
