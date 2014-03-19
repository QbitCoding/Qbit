package thread;

public class SynchronizedKT {

	private static Object mutex="mutex";
	public SynchronizedKT() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		loop0(0);
	}
	private static void loop0(int i) {
		synchronized (mutex) {
			System.out.println("loop0 start");
			loop(i);
			System.out.println("loop0 end");
		}
		
	}

	public static void loop(int i){
		synchronized (mutex) {
			if(i>5) return;
			System.out.println("into"+i+++"loop");
			loop(i);
		}
	}

}
