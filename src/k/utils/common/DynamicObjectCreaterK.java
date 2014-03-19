package k.utils.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Qbit 
 * 2014-03-11
 * 2014-03-14 LOG the error message when compile
 */
public final class DynamicObjectCreaterK implements Callable<Object> ,QbitKI{
	private String classname;
	private String classsrc;

	private DynamicObjectCreaterK(String name, String src) {
		this.classname = name;
		this.classsrc = src;
	}

	public static DynamicObjectCreaterK getInstance(String name, String src) {
		return new DynamicObjectCreaterK(name, src);
	}

	@Override
	public Object call() throws Exception {
		return getDynamicObject(classname, classsrc);
	}
	/**
	 * catch any Exception and return null;
	 * @param name
	 * @param src
	 * @return
	 */
	public Object getDynamicObject(String name, String src) {
		Object out = null;
		JavaCompiler javacompiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
		class JavaFileObject4Compile extends SimpleJavaFileObject {
			private String src;

			JavaFileObject4Compile(String name, String src) {
				this(name, JavaFileObject.Kind.SOURCE);
				this.src = src;
			}

			JavaFileObject4Compile(String name, Kind kind) {
				super(URI.create("string:///" + name.replace('.', '/')
						+ JavaFileObject.Kind.SOURCE.extension), kind);
			}

			/*
			 * return the java resource string
			 * 
			 * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
			 */
			@Override
			public CharSequence getCharContent(boolean ignoreEncodingErrors)
					throws IOException {
				return src;
			}

			private ByteArrayOutputStream baos = new ByteArrayOutputStream();

			/*
			 * (non-Javadoc)
			 * 
			 * @see javax.tools.SimpleJavaFileObject#openOutputStream()
			 */
			@Override
			public OutputStream openOutputStream() throws IOException {
				// TODO Auto-generated method stub
				return baos;
			}
		}
		final JavaFileObject4Compile jfo4c = new JavaFileObject4Compile(name,
				src);
		Iterable<? extends JavaFileObject> compilationUnits = Arrays
				.asList(jfo4c);
		class JavaFileManager4Compile extends
				ForwardingJavaFileManager<JavaFileManager> {
			JavaFileObject jfo = null;

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax
			 * .tools.JavaFileManager.Location, java.lang.String,
			 * javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
			 */
			@Override
			public JavaFileObject getJavaFileForOutput(Location location,
					String className, Kind kind, FileObject sibling)
					throws IOException {
				if (className != null)// 如果为空则返回最后一个jfo
					jfo = new JavaFileObject4Compile(className, kind);// 此处必须new一个返回,因为每个class都需要对应一个JavaFileObject
				return jfo;
			}

			protected JavaFileManager4Compile(JavaFileManager fileManager) {
				super(fileManager);
			}

		}
		final JavaFileManager4Compile fileManager = new JavaFileManager4Compile(
				javacompiler.getStandardFileManager(diagnosticListener, null,
						null));
		List<String> options = new ArrayList<String>();
		options.add("-encoding");
		options.add("UTF-8");
		options.add("-classpath");
		options.add(StringUtils.join(CollectionUtils.collect(Arrays
				.asList(((URLClassLoader) (Thread.currentThread()
						.getContextClassLoader().getParent())).getURLs()),
				new Transformer<URL, String>() {
					@Override
					public String transform(URL input) {
						return input.getFile();
					}
				}), File.pathSeparator)
				+ File.pathSeparator);
		JavaCompiler.CompilationTask task = javacompiler.getTask(null,
				fileManager, diagnosticListener, options, null,
				compilationUnits);
		if (task.call()) {
			class ClassLoader4Compile extends URLClassLoader {
				public ClassLoader4Compile(URL[] urls, ClassLoader parent) {
					super(urls, parent);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.ClassLoader#loadClass(java.lang.String)
				 */
				@Override
				public Class<?> loadClass(String name)
						throws ClassNotFoundException {
					synchronized (getClassLoadingLock(name)) {
						try {
							return super.loadClass(name);
						} catch (ClassNotFoundException e) {
							try {
								byte[] b = ((ByteArrayOutputStream) fileManager
										.getJavaFileForOutput(null, null, null,
												null).openOutputStream())
										.toByteArray();
								return defineClass(name, b, 0, b.length);
							} catch (IOException e1) {
								throw new ClassNotFoundException(
										"can't find class", e1);
							}
						}
					}
				}
			}
			try {
				out = new ClassLoader4Compile(new URL[0], Thread
						.currentThread().getContextClassLoader().getParent())
						.loadClass(name).newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			StringBuilder sb=new StringBuilder();
			for(Diagnostic<? extends JavaFileObject> d:diagnosticListener.getDiagnostics()){
				sb.append("-----Error Message-----");
				sb.append("Code:"+d.getCode()+"\r\n");
				sb.append("ColumnNumber:"+d.getColumnNumber()+"\r\n");
				sb.append("EndPosition:"+d.getEndPosition()+"\r\n");
				sb.append("LineNumber:"+d.getLineNumber()+"\r\n");
				sb.append("Message:"+d.getMessage(null)+"\r\n");
				sb.append("Position:"+d.getPosition()+"\r\n");
				sb.append("StartPosition:"+d.getStartPosition()+"\r\n");
				sb.append("Kind:"+d.getKind()+"\r\n");
				sb.append("Source:"+d.getSource()+"\r\n");
			}
			LOG.debug(sb);
		}
		return out;
	}

	public static void main(String[] args) throws Exception {
		String name = "name";
		StringBuilder sb = new StringBuilder();
		sb.append("public class name {"+"\r\n"
				+ "public double calculate(java.util.Map map){"+"\r\n"
				+ "double column23 =Double.valueOf(map.get(\"column23\").toString());"+"\r\n"
				+ "double _column23 =Double.valueOf(map.get(\"_column23\").toString());"+"\r\n"
				+ "double column24 =Double.valueOf(map.get(\"column24\").toString());"+"\r\n"
				+ "double _column24 =Double.valueOf(map.get(\"_column24\").toString());"+"\r\n"
				+ "double column25 =Double.valueOf(map.get(\"column25\").toString());"+"\r\n"
				+ "double _column25 =Double.valueOf(map.get(\"_column25\").toString());"+"\r\n"
				+ "double column26 =Double.valueOf(map.get(\"column26\").toString());"+"\r\n"
				+ "double _column26 =Double.valueOf(map.get(\"_column26\").toString());"+"\r\n"
				+ "double column27 =Double.valueOf(map.get(\"column27\").toString());"+"\r\n"
				+ "double _column27 =Double.valueOf(map.get(\"_column27\").toString());"+"\r\n"
				+ "double column28 =Double.valueOf(map.get(\"column28\").toString());"+"\r\n"
				+ "double _column28 =Double.valueOf(map.get(\"_column28\").toString());"+"\r\n"
				+ "double column29 =Double.valueOf(map.get(\"column29\").toString());"+"\r\n"
				+ "double _column29 =Double.valueOf(map.get(\"_column29\").toString());"+"\r\n"
				+ "double column30 =Double.valueOf(map.get(\"column30\").toString());"+"\r\n"
				+ "double _column30 =Double.valueOf(map.get(\"_column30\").toString());"+"\r\n"
				+ "double column31 =Double.valueOf(map.get(\"column31\").toString());"+"\r\n"
				+ "double _column31 =Double.valueOf(map.get(\"_column31\").toString());"+"\r\n"
				+ "double column32 =Double.valueOf(map.get(\"column32\").toString());"+"\r\n"
				+ "double _column32 =Double.valueOf(map.get(\"_column32\").toString());"+"\r\n"
				+ "double column33 =Double.valueOf(map.get(\"column33\").toString());"+"\r\n"
				+ "double _column33 =Double.valueOf(map.get(\"_column33\").toString());"+"\r\n"
				+ "double column34 =Double.valueOf(map.get(\"column34\").toString());"+"\r\n"
				+ "double _column34 =Double.valueOf(map.get(\"_column34\").toString());"+"\r\n"
				+ "double column35 =Double.valueOf(map.get(\"column35\").toString());"+"\r\n"
				+ "double _column35 =Double.valueOf(map.get(\"_column35\").toString());"+"\r\n"
				+ "return column23 * _column23 /  12d+ column24 * _column24 + column25 * _column25 + column26 * _column26 + column27 * _column27 + column28 * _column28 + column29 * _column29 + column30 * _column30 + column31 * _column31 + column32 * _column32 + column33 * _column33 + column34 * _column34 + column35 * _column35   ;"+"\r\n"
				+ "}"+"\r\n"
				+ "}");
		LOG.debug(sb);
		Object o = DynamicObjectCreaterK.getInstance(name, sb.toString())
				.call();
		System.out.println(o);
	}
}
