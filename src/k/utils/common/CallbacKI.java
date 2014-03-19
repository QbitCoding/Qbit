package k.utils.common;

import java.util.concurrent.Callable;
/**
 * 每当子线程执行玩后都会调用callback方法
 * 将原callable包装为NewCallable即可
 * @author Qbit
 *
 */
public interface CallbacKI<K> {
	K callback(K t) throws Exception;
	class VoidCallback<K> implements CallbacKI<K>{
		@Override
		public K callback(K t) throws Exception {
			return null;
		}
		
	}
	class NewCallable<T> implements Callable<T>{
		NewCallable(CallbacKI c,Callable<T> ca){
			this.c=c;
			this.ca=ca;
		}
		CallbacKI<T> c;
		Callable<T> ca;
		@Override
		public T call() throws Exception {
			T t=ca.call();
			return  c.callback(t);
		}
	}
}