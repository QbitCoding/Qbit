package study;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapAndSet {

	public static void main(String[] args) {
		Set<MapAndSet> set = new HashSet<MapAndSet>();
		Map<MapAndSet,Integer> map = new HashMap<MapAndSet,Integer>();
		MapAndSet mas1 = new MapAndSet();
		mas1.i=1;
		MapAndSet mas2 = new MapAndSet();
		mas2.i=2;
		set.add(mas1);
		set.add(mas2);
		System.out.println(set);//结果为[MapAndSet [i=2]],重复时忽略
		Integer i1=new Integer(1);
		Integer i2=new Integer(2);
		map.put(mas1, i1);
		map.put(mas2, i2);
		System.out.println(map);//结果为{MapAndSet [i=1]=2}

	}
	int i;
	@Override
	public int hashCode() {
		
		return 1;
	}
	@Override
	public boolean equals(Object obj) {
		
		return true;
	}
	@Override
	public String toString() {
		return "MapAndSet [i=" + i + "]";
	}

}
