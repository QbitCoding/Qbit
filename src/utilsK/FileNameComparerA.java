package utilsK;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
/**
 * 使用该类来查询各个硬盘内的文件,结果放在path下的各个文件里
 * 然后使用B
 * @author Qbit
 *
 */
public final class FileNameComparerA implements Runnable{
	
	private FileNameComparerA() {
		// TODO Auto-generated constructor stub
	}
	private File root;
	private FileWriter fw;
	final static String path= "c:\\k\\";
	private FileNameComparerA(File root){
		this.root=root;
		
	}
	public static void main(String[] args) {
		File folder = new File(path);
		if(!folder.exists()) folder.mkdir();
		char c = 'd';
		for(int i =0;i<20;i++){
			new Thread(FileNameComparerA.getInstance(new File((char)(c++)+":\\"))).start();
		}
	}
	private static Pattern p;
	/**
	 * 定义了需要查询多少级
	 */
	private static final int maxlevel=4;
	private void readFolder(File file,int level) throws IOException{
		if(level>maxlevel) return;
		fw.write(file.getAbsolutePath()+"\r\n");
		File[] list = file.listFiles();
		if(list==null) return;
		for(File f:list){
			readFolder(f, level+1);
		}
	}
	static FileNameComparerA getInstance(File root){
		return new FileNameComparerA(root);
	}
	@Override
	public void run() {
		if(!root.exists()) return;
		try {
			fw= new FileWriter(new File(path+root.getName().charAt(0)+".txt"));
			readFolder(root,0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
