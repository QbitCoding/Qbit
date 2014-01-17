package utilsK;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
/**
 * 一个简单的空文件夹删除工具
 * 只删除空文件夹和只有空子文件夹的文件夹(也就是说目录下没有文件),不涉及移动
 * 根据配置文件读出所有参数,包括文件分析
 * @author Qbit
 *
 */
public final class BlankFolderCleaner implements Callable<File>{
	private Map<String,Character> legalExname;
	private BlankFolderCleaner(File file) {
		super();
		propertiesfile=file;
	}
	public static Pattern p;
	public static void main(String[] args) throws Exception {
		
		BlankFolderCleaner sfc=BlankFolderCleaner.getInstance(new File("d:\\txt.txt"));	
		sfc.call();
	}
	private boolean canDel(File f) throws IOException{
		if(f==null) return false;
		if(!f.isDirectory()) return check(f);
		boolean flage = true;
		List<File> list = new ArrayList<File>();
		File[] lf=f.listFiles();
		for(File file:lf){
			boolean b = canDel(file);
			flage=flage&&b;
			if(b) list.add(file);
		}
		if(flage) return true;
		for(File file:list){
			del(file);
		}
		return false;
	}
	FileWriter pw ;
	private void del(File file) throws IOException {
		if(StringUtils.contains(file.getAbsolutePath(), ' ')) return;
		pw.write("rd /S /Q "+file.getAbsolutePath()+"\r\n");
	}
	private Set<String> unknowExname = new HashSet<String>();
	private boolean check(File file){
		String result ="";
		 Matcher m = p.matcher(file.getName());
		 if(m.find()) result=m.group(1);
		 Character c=legalExname.get(result);
		 if(c==null){
			 unknowExname.add(result);
			 return true;
		 }else if(c=='Y'){
			 return true;
		 }else if(c=='N'){
			 return false;
		 }
		 return true;
	}
	private File propertiesfile;
	public static BlankFolderCleaner getInstance(File file){
		if(p==null) p=Pattern.compile("\\.(.+)$");
		return new BlankFolderCleaner(file);
	}
	@Override
	public File call() throws Exception {
		Scanner sc= new Scanner(propertiesfile);
		Pattern pfolder = Pattern.compile("^folder:(.*)");
		Pattern pexname = Pattern.compile("^[YN\\?]{1}\\.(.*)");
		List<File> filelist = new ArrayList<File>();
		legalExname = new HashMap<String,Character>();
		while(sc.hasNext()){
			String s =StringUtils.deleteWhitespace(sc.next());
			s=StringUtils.trimToNull(s);
			if(s==null||s.startsWith("//")) continue;
			Matcher m = pfolder.matcher(s);
			if(m.find()){
				filelist.add(new File(m.group(1)));
				continue;
			}
			m = pexname.matcher(s);
			if(m.find()){
				legalExname.put(m.group(1), s.charAt(0));
				continue;
			}
			continue;	
		}
		File bat = new File("bat.bat");
		pw = new FileWriter(bat);
		for(File file:filelist){
			if(canDel(file)) del(file);
		}
		return bat;
	}
}
