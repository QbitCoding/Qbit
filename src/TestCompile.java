import java.net.URLClassLoader;


public class TestCompile {

	public static void main(String[] args) {
		ClassLoader cl=Thread.currentThread().getContextClassLoader();
		System.out.println(cl instanceof URLClassLoader);
		System.out.println(cl.getClass());
		cl=cl.getParent();
		System.out.println(cl instanceof URLClassLoader);
		System.out.println(cl.getClass());
		cl=cl.getParent();
		System.out.println(cl);
	}

}
