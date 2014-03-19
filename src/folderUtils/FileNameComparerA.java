package folderUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 使用该类来查询各个硬盘内的文件,结果放在path下的各个文件里 然后使用B
 * 
 * @author Qbit
 * 
 */
public final class FileNameComparerA implements Runnable {

	private FileNameComparerA() {
		// TODO Auto-generated constructor stub
	}

	private File root;
	private FileWriter fw;
	final static String path = "c:\\k\\";

	private FileNameComparerA(File root) {
		this.root = root;

	}

	public static void main(String[] args) {
		File folder = new File(path);
		if (!folder.exists())
			folder.mkdir();
		new Thread(FileNameComparerA.getInstance(new File("j:\\"))).start();
		new Thread(FileNameComparerA.getInstance(new File("k:\\"))).start();
		new Thread(FileNameComparerA.getInstance(new File("l:\\"))).start();
		new Thread(FileNameComparerA.getInstance(new File("m:\\"))).start();
		new Thread(FileNameComparerA.getInstance(new File("s:\\"))).start();
	}

	/**
	 * 定义了需要查询多少级
	 */
	private static final int maxlevel = 3;

	private void readFolder(File file, int level) throws IOException {
		if (level > maxlevel)
			return;
		fw.write(StringUtils.repeat("\t", level)+file.getAbsolutePath() + "\r\n");
		if(file.isFile()) return;
		File[] list = file.listFiles();
		if (list == null)
			return;
		for (File f : list) {
			readFolder(f, level + 1);
		}
	}

	static FileNameComparerA getInstance(File root) {
		return new FileNameComparerA(root);
	}

	@Override
	public void run() {
//		System.out.println(root.getAbsolutePath() + ");
		if (!root.exists())
			return;
		 try {
		 fw= new FileWriter(new File(path+root.getAbsolutePath().charAt(0)+".txt"));
		 readFolder(root,0);
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
	}
}
