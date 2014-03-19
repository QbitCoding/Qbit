package folderUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.codec.digest.DigestUtils;


public final class FileComparerK implements Callable<Set<List<File>>> {
	private static Map<Long,Set<File>> allfiles = new HashMap<Long,Set<File>>();
	private static Map<File,List<File>> samefiles=new HashMap<File,List<File>>();
	private static FilenameFilter filter = null;
	private static long maxsize = 8L*1024*1024*100; 
	private static long minsize=0;
	private FileComparerK() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		File file = new File("D:\\UploadFile\\201401091129380.xls");
		System.out.println(file.getFreeSpace()/8/1024);
		System.out.println(file.getTotalSpace()/8/1024);
		System.out.println(file.getUsableSpace()/8/1024);
		System.out.println(file.length());
	}

	@Override
	public Set<List<File>> call() throws Exception {
		compare(root);
		return null;
	}
	private void compare(File root2) throws Exception {
		if(root2.isDirectory()){
			for(File file:root.listFiles(filter)){
				FileComparerK.getInstance(file,null).call();
			}
		}else realCompare(root2);
		
	}
	private static String compareType;
	private void realCompare(File file) throws IOException {
		Long size = file.length();
		Set<File> set = allfiles.get(size);
		if(set!=null){
			for(File f:set){
				boolean b = false;
				switch(compareType){
				case "io":b=ioCompare(f,file);
				case "md5":b=md5Compare(f, file);
//				case "smart":b=smartCompare(f,file);
				}
				if(b) {
					List<File> list = samefiles.get(f);
					if(list==null){
						list = new ArrayList<>();
						list.add(f);
					}
					list.add(file);
					return;
				}	
			}
		}else{
			set = new HashSet<>();
		}
		set.add(file);
	}
	
	/**
	 * 
	 * @param f
	 * @param file
	 * @return 在文件大小一致的前提下比较两个文件是否一致,如果文件过大则不比较,直接认为一致
	 * @throws IOException
	 */
	private boolean ioCompare(File f, File file) throws IOException {
		if(f.length()>maxsize) return true;
		BufferedInputStream io1 = new BufferedInputStream(new FileInputStream(f));
        BufferedInputStream io2 = new BufferedInputStream(new FileInputStream(file));
		if(io1.available()!=io2.available()) return false;
		int i1 = 0;
		int i2=0;
		while(true){
			i1=io1.read();
			i2=io2.read();
			if(i1!=i2) return false;
			if(i1==-1) return true;
		}
	}
	/**
	 * 
	 * @param f1
	 * @param f2
	 * @return 根据md5来比较文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private boolean md5Compare(File f1,File f2) throws FileNotFoundException, IOException{
		return DigestUtils.md5Hex(new FileInputStream(f1))== DigestUtils.md5Hex(new FileInputStream(f2));
	}
//	private boolean sha1Compare(File f1,File f2) throws NoSuchAlgorithmException{
//		MessageDigest digest =MessageDigest.getInstance("SHA-1");
//	}
	private File root;
	public static FileComparerK getInstance(File root,String type){
		FileComparerK instance = new FileComparerK();
		instance.root=root;
		instance.compareType="io";
		return instance;
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof FileComparerK){
			return this.root.equals(((FileComparerK) arg0).root);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return root.hashCode();
	}

	@Override
	public String toString() {
		return "path:"+root.toString()+"\n"+"filter:"+filter.toString();
	}
	
}
