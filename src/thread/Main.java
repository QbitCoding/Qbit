package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
																				// 相当于是用来存放Executor执行的结果的一种容器
		List<Callable<String>> callKL = new ArrayList<Callable<String>>();
		for (int i = 0; i < 10; i++) {
			callKL.add(new Call());
		}
		System.out.println("创建完成");
		for(Callable<String> c:callKL){
			results.add(exec.submit(c));
		}
		exec.shutdown();
		System.out.println("---");
		Thread.sleep(10000);
		for (Future<String> fs : results) {
			if (fs.isDone()) {
				System.out.println("get:"+fs.get());
			} else {
				System.out.println("Future result is not yet complete");
			}
		}
	}

}

class Call implements Callable<String> {
	static int num = 0;
	static synchronized int plus(){
		return num++;
	}
	int id;
	Call(){
		id=plus();
		System.out.println("call:"+id);
	}
	@Override
	public String call() throws Exception {
		Random r = new Random(1000);
		long l = r.nextInt();
//		wait(l);
		Thread.sleep(l);
		System.out.println("exec:"+id);
		return "" + id;
	}

}
