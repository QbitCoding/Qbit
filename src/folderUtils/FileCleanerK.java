package folderUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class FileCleanerK implements Callable<File> {
	private PrintWriter outer;
	private File outerfile;
	private String path;
	private List<File> realfiles;

	// private static File bat;

	public static void main(String[] args) {
		// List<File> filelist = new ArrayList<>();
		// for (String arg : args) {
		// FileCleanerK instance = FileCleanerK.getInstance(arg);
		// try {
		// File f = instance.call();
		// filelist.add(f);
		// } catch (Exception e) {
		// System.out.println(arg + " error!");
		// } finally {
		// instance.cancle();
		// }
		// }
		// try {
		// File file = new File("bat.bat");
		// if (file.exists())
		// file.delete();
		// FileWriter fw = new FileWriter(file);
		// for (File f : filelist) {
		// fw.write(f.getName());
		// }
		// fw.close();
		// System.out.println("please execute the bat file.");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			new FileCleanerK().getInstance("M:\\StormMedia").call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private FileCleanerK() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void cancle() {
		try {
			outer.close();
			outerfile.delete();
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	@Override
	public File call() throws Exception {
		if (path == null)
			return null;
		File root = new File(path);
		path = StringUtils.replaceChars(path, '\'', '-');
		path = StringUtils.replaceChars(path, '/', '-');
		outerfile = new File("d:\\" +path+ ".bat");
		outer = new PrintWriter(new FileWriter(outerfile));
		if (keepfile(root) == null) {
			del(root);
		}
		outer.close();
		return outerfile;
	}

	private void del(File root) {
		// root.delete();
		if (root.isFile())
			outer.println("del /F " + root.getAbsolutePath() + "/n");
		else
			outer.println("rd /S /Q " + root.getAbsolutePath() + "/n");
	}

	private File keepfile(File root) {
		if (root.isDirectory()) {
			File[] files = root.listFiles();
			List<File> keepfiles = new ArrayList<>();
			List<File> todelfiles = new ArrayList<>();
			int counter = 0;// 记录需要被保留的子文件夹
			File single = null;
			for (File file : files) {
				File keepfile = keepfile(file);
				if (keepfile == null)
					continue;
				counter++;
				if (single == null)
					single = keepfile;
				if (keepfile != file) {
					keepfiles.add(keepfile);
					todelfiles.add(file);
				}

			}
			if (counter == 0)
				return null;
			if (counter == 1)
				return single;
			for (File file : keepfiles) {
				move(file, root);
			}
			for (File file : todelfiles) {
				del(file);
			}
			return root;
		}
		if (isFile(root))
			return root;
		return root;
	}

	private void move(File file, File root) {
		String s = file.
		outer.println("move " + file.getAbsolutePath() + " "
				+ root.getAbsolutePath());
	}
	
	/**
	 * //判断一个文件是否是一个保留的文件,如果为需要的文件则返回true,
	 * 如果不是需要的文件,则将该文件加入realfiles中,返回false以表示该文件可删除
	 * 
	 * @param root
	 * @return
	 */
	private boolean isFile(File root) {
		if(!root.isFile()) return true;
		Matcher m = pattern.matcher(root.getName());
		String exname = "";
		if(m.find()) exname = m.group();
		switch(exname){
		case ".txt":
		case ".jpg":
		case ".jpeg":
		case ".exe":return true;
		case "":
		case ".db":return false;
		default:
			System.out.println(exname); 
		}
		return true;
		
//		return root.isFile();
	}
	private Pattern pattern= null;
	public static FileCleanerK getInstance(String path) {
		FileCleanerK instance = new FileCleanerK();
		instance.path = path;
		instance.pattern=Pattern.compile("\\.\\w*$");
		return instance;
	}

}
