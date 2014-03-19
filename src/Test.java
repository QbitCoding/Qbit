import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Callable;


public class Test implements Callable<Object>{
	static int counter;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		int i = true?false?0:1:999;
//		int j = true?1:false?999:0;
//		for(int k =0;i<10;i++){
//			new Test().call();
//		}
//		List list = new ArrayList();
//		list.add(null);
//		list.add("hello");
//		System.out.println(list);
//		System.out.println("\\\\n");
//		System.out.println(list.get(3));
//		Integer[] array = new Integer[3];
//		List <Integer> list = Arrays . asList ( array );
//		List<Integer> list=new ArrayList<Integer>();
//		list.add(1);
//		list.add(2);
//		System.out.println(list.remove(-1));
		Deque<Integer> que = new ArrayDeque<Integer>();
		que.add(1);
		que.add(2);
		que.add(3);
		System.out.println(que.pollFirst());
		System.out.println(que.pollLast());
		System.out.println(que.pollFirst());
		System.out.println(que.pollLast());
	}
	@Override
	public Object call() throws Exception {
		name();
		return null;
	}
	private void name() throws InterruptedException {
		counter++;
		System.out.println("现在有"+counter+"个name方法在调用");
		wait(1000000);
		counter--;
	}
}
