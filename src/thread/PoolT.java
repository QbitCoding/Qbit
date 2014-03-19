package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PoolT {

	public static void main(String[] args) {
		ExecutorService pool = Executors.newCachedThreadPool();
		Future<Boolean> f1=pool.submit(new CallT());
		pool.shutdown();
		pool.
		Future<Boolean> f2=pool.submit(new CallT());
	}
	static class CallT implements Callable<Boolean>{
		private static int index=0;
		@Override
		public Boolean call() throws Exception {
			System.out.println(++index);
			return true;
		}
		
	}

}
