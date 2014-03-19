package generics;

import java.util.*;

public class Provider<E> {

	public Provider() {
		// TODO Auto-generated constructor stub
	}
	Map<E,Set<E>> staticmap;
	public Get<E> provide(E name){
		if(staticmap==null) staticmap= new HashMap<E,Set<E>>();
		return new Get<E>(name,staticmap);
	}

}
