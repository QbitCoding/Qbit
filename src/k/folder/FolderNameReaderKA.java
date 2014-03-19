package k.folder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import k.utils.common.StacK;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
/**
 * 使用该类来查询各个硬盘内的文件,结果放在path下的各个文件里 然后使用FolderNameComparerK
 * 
 * @author Qbit
 * 
 */
public abstract class FolderNameReaderKA implements Callable<File> {

	private File root;
	protected File folder4out;

	protected FolderNameReaderKA(File root, File folder4out) {
		this.root = root;
		this.folder4out = folder4out;
	}

	public static FolderNameReaderKA newInstance(File root, File folder4out) {
		if (folder4out.isDirectory())
			return new FolderNameReader2XmlK(root, folder4out);
		return null;
	}

	@Override
	public final File call() throws Exception {
		if (folder4out == null)
			return null;
		folder4out.mkdirs();
		File file = getFile();
		read(root,0);
		close();
		return file;
	}

	protected abstract File getFile();

	protected abstract void close() throws Exception;
	/**
	 * 定义了需要查询多少级
	 */
	private static final int maxlevel = 3;
	private final void read(File folder,int level) {
		if (level==maxlevel||skip(folder))
			return;
		String name = folder.getName();
		String path = folder.getAbsolutePath();
		write(name, path);
		if (folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				read(file,level+1);
			}
		}
		closewrite();
	}

	protected abstract void write(String name, String path);

	protected abstract void closewrite();

	protected abstract boolean skip(File folder);

	public static String transFileName(String name) {
		Matcher m = Pattern.compile("\\" + File.pathSeparator + "|:").matcher(
				name);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "-");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}

class FolderNameReader2XmlK extends FolderNameReaderKA {

	protected FolderNameReader2XmlK(File root, File folder4out) {
		super(root, folder4out);
	}

	private Document doc;
	private File file;

	@Override
	protected File getFile() {
		file = new File(transFileName(folder4out.getAbsolutePath()) + ".xml");
		doc = DocumentHelper.createDocument();
		return file;
	}

	@Override
	protected void close() throws Exception {
		new XMLWriter(new FileOutputStream(file),
				OutputFormat.createPrettyPrint()).write(doc);
	}

	private StacK<Element> stack = new StacK<Element>();
	public static final String fileS="File";
	public static final String nameS="name";
	public static final String pathS="path";
	@Override
	protected void write(String name, String path) {
		Element root = stack.top();
		Element element = null;
		if (root == null) {
			element = doc.addElement(fileS);
		} else {
			element = root.addElement(fileS);
		}
		element.addAttribute(nameS, name);
		element.addAttribute(pathS, path);
		stack.push(element);
	}

	@Override
	protected void closewrite() {
		stack.pop();
	}

	@Override
	protected boolean skip(File folder) {
		if (folder.isDirectory())
			return false;
		else {
			if (folder.length() < 1024 * 1024)
				return true;
		}
		return false;
	}

}
