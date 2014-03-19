package generics;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Get<E> {
	private E e;
	private Map<E,Set<E>> map;
	protected Get(E name,Map<E,Set<E>> map){
		e=name;
		this.map=map;
	}
	public Object doGet(){
		if(map.get(e)==null) map.put(e,getFromDB());
		return map.get(e);
	}
	private Set<E> getFromDB() {
		return null;
	}
}
