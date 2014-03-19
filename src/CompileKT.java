import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class CompileKT {
	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
		JavaFileManager4Compile jfm=new JavaFileManager4Compile(jc.getStandardFileManager(null, null, null));
		String name = "Qbit";
		String src = getSrc(name);
		JavaFileObject4Compile jfo = new JavaFileObject4Compile(name, src);
		System.out.println(jfo);
		Iterable<? extends JavaFileObject> jfoKI = Arrays.asList(jfo);
		List<String> list = new ArrayList<String>();
		list.add("-encoding");
		list.add("UTF-8");
		list.add("-classpath");
		list.add(getClasspath());
		JavaCompiler.CompilationTask jcct = jc.getTask(null, jfm, null, list,
				null, jfoKI);
		boolean b = jcct.call();
		System.out.println(b);
		// ClassLoader cl = Thread.currentThread().getContextClassLoader();
		// cl.loadClass("Qbit");
		jfo=jfm.getJavaFileObject();
		ClassLoaderK cl = new ClassLoaderK(null, new CompileKT().getClass()
				.getClassLoader().getParent());
//		ClassLoader cl=new URLClassLoader(new URL[0], 
//				Thread.currentThread().getContextClassLoader().getParent());
//		System.out.println(jfo);
		Class c = cl.load(name, jfo);
		System.out.println(c.newInstance());
	}
	private static String getClasspath(){
		URL[] urlKA = ((URLClassLoader)Thread.currentThread().getContextClassLoader().getParent()).getURLs();
		StringBuffer sb=new StringBuffer();
		for(URL url:urlKA){
			sb.append(url.getFile());
			sb.append(File.pathSeparator);
		}
		return sb.toString();
	}

	private static String getSrc(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("public class " + name + " {\r\n"+
				"class " + "Kamputer"+ " {\r\n"
				+ "\tpublic String toString(){\r\n"
				+ "\t\treturn \"Kamputer\";\r\n" + "\t}\r\n" + "}\r\n"
				+ "\tpublic String toString(){\r\n"
				+ "\t\treturn \"hello world\";\r\n" + "\t}\r\n" + "}\r\n");
		System.out.println(sb);
		return sb.toString();
	}

}

class ClassLoaderK extends URLClassLoader {

	public ClassLoaderK(URL[] urls, ClassLoader parent) {
		super(new URL[0], parent);
	}

	public Class<?> load(String name, JavaFileObject4Compile jfo) throws IOException {
		byte[] byteKA = ((ByteArrayOutputStream) jfo.openOutputStream()).toByteArray();
		return this.defineClass(name, byteKA, 0, byteKA.length);
	}

	// public Class findClassByClassName(String className)
	// throws ClassNotFoundException {
	// System.out.println("我也被调用了诶");
	// return this.findClass(className);
	// }
}

class JavaFileManager4Compile extends
		ForwardingJavaFileManager<JavaFileManager> {

	protected JavaFileManager4Compile(JavaFileManager fileManager) {
		super(fileManager);
	}
	private JavaFileObject4Compile jfo;
	public JavaFileObject4Compile getJavaFileObject(){
		return jfo;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools
	 * .JavaFileManager.Location, java.lang.String,
	 * javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, Kind kind, FileObject sibling) throws IOException {
		System.out.println("getJavaFileForOutput"+" is called");
		if(className!=null)
		 jfo=new JavaFileObject4Compile(className, kind);
		 return jfo;
	}
}

class JavaFileObject4Compile extends SimpleJavaFileObject {

	private String src;
	private ByteArrayOutputStream baos=new ByteArrayOutputStream();

	protected JavaFileObject4Compile(String name, String src) {
		this(name, JavaFileObject.Kind.SOURCE);
		this.src=src;
	}
	protected JavaFileObject4Compile(String name,Kind kind){
		super(URI.create("string:///" + name.replace('.', '/')
				+ JavaFileObject.Kind.SOURCE.extension),kind);
	}

	/* (non-Javadoc)
	 * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
	 */
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws IOException {
		// TODO Auto-generated method stub
		return src;
	}
	/* (non-Javadoc)
	 * @see javax.tools.SimpleJavaFileObject#openOutputStream()
	 */
	@Override
	public OutputStream openOutputStream() throws IOException {
		return baos;
	}
	
}
