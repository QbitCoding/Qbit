package generics;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		new Test().test();
//		set.add("set");
//		big.add(set);
//		big.add(hashset);
//		set=hashset;
//		big.add(set);
//		System.out.println(big);
		
//		Set<Object> oset = new HashSet<Integer>();
//		oset.add("string");
//		System.out.println(oset);
		
		
	}

	private void test(List<List<? extends Sup>> big) {
		List<Sub> sublist = new ArrayList<>();
		List<Object> objlist = new ArrayList<>();
		big.add(sublist);
		List<ArrayList<? extends Sup>> newlist = new ArrayList<>();
		test(newlist);
		List<? extends Sup> suplist = big.get(0);
		Sup sup = suplist.get(0);
		suplist.add(new Sub(null));
	}

}
