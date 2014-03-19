package clastudy;

public class Polymorphism {

	public static void main(String[] args) {
		Sub sub=new Polymorphism().new Sub();
		Sub1 sub1=sub;
		Sub2 sub2=sub;
		Sup sup=sub;
		mothed(sub2);
	}
	interface Sup{
		
	}
	interface Sub1 extends Sup{
		
	}
	interface Sub2 extends Sup{
		
	}
	class Sub implements Sub1,Sub2{
		
	}
	private static void mothed(Sub1 s){
		System.out.println("1");
	}
	private static void mothed(Sub2 s){
		System.out.println("2");
	}

}
