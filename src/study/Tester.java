package study;

public class Tester {

	public Tester() {

}
	public static void main(String[] args) {
		{
			
			StaticKT sk = new StaticKT();
			StaticKT.i=10;
			sk=null;
			int[][] k = new int[8*1024][8*1024];
			System.gc();
	}
		System.out.println(StaticKT.i);
	}

}
