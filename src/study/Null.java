package study;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Null {

	public static void main(String[] args) {
		Set set = new HashSet<>();
		set.add(null);
		System.out.println(set.size());
		set.add(null);
		System.out.println(set.size());
//		
		Map map = new HashMap<>();
		map.put(0, null);
		System.out.println(map);
//		map.put(null, 0);
		System.out.println(map);
		System.out.println(map.put(null, 1));
		System.out.println(map);
	}

}
