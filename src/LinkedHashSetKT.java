import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


public class LinkedHashSetKT {

	public static void main(String[] args) {
		LinkedHashSet<Object> lhs = new LinkedHashSet<Object>();
		Set<Object> set=lhs;
		Iterator<Object>it=lhs.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}

	}

}
