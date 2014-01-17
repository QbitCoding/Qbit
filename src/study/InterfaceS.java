package study;


public class InterfaceS {
	/**
	 * 总的来说在调用m()时看参数被声明为什么类型,看是否有匹配的,如果有匹配的,则直接调用,不管参数实际是什么类型
	 * 如果没有直接匹配的,则看是否有父类可以调用,如果有多个,则困惑,因为无法确定返回的到底是什么类型
	 * 方法的调用与方法出现的位置无关;
	 * 
	 */
	public static void main(String[] args) {
		n(m(getc()));

	}
	/**
	 * 
	 * @return 如果限制了返回为I1,则调用m(I1),即使它的代码位置偏后
	 * 如果返回为I,即使则调用m(I),不管实际返回的是什么
	 */
	private static  I2 getc() {
		return new C();
	}
//	static void m(I2 i) {
//		System.out.println("m2");
//	}
	/**
	 * 如果没有该方法,main方法调用m(new C())时会报错
	 * @param i
	 */
	static Integer m(I i) {
		System.out.println("m");
		return 1;
	}
	static void m(I1 i) {
		System.out.println("m1");
	}
	static void m(C i){
		System.out.println("mc");
	}
	static void n(int o){
		System.out.println("n-int");
	}
	/**
	 * 当传入Integer时优先调用该方法而不是上面一个
	 * @param o
	 */
	static void n(Object o){
		System.out.println("n-obj");
	}

}

interface I {

}

interface I1 extends I {

}

interface I2 extends I {

}

class C  implements I1, I2 {

}

class C1 implements I1 {

}


