package generics;

import java.util.Comparator;

public class MySet<E> {
	MySet(Comparator<Object> comparator){
		
	}
	public static void main(String[] args) {
		new MySet<String>(new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
	}

}
