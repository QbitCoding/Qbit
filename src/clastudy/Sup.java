package clastudy;

public class Sup implements Outer{
	class Sub implements Outer.Inner{
	}
	class Sub0 implements Inner{
		
	}
	public Sup() {
		System.out.println("Sup");
	}
	public int number1 ;
	public final int number2 =10;
	public static void main(String[] args) {
		Sup s = new Sub1();
		change(s);
		System.out.println(s.number1);
	}
	private static void change(Sup s) {
		// TODO Auto-generated method stz
		//中文
		s=new Sub2();
	}

}
