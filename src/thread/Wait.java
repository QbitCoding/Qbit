package thread;

public class Wait {
	public static void main(String[] args) {
		Thread t = new Thread(new Wait0());
		try {
			t.wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Wait() {
		// TODO Auto-generated constructor stub
	}

}
class Wait0 implements Runnable{

	@Override
	public void run() {
		System.out.println("wait0 is runing...");
	}
	
}
