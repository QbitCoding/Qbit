package folderUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import k.utils.common.RegK;
/**
 * 查询path下各个文件,将所有文件的Name进行判断,如果相同着在result中记录,忽略有扩展名的文件
 * @author Qbit
 *
 */
public final class FileNameComparerB {

	public static void main(String[] args) throws IOException {
//		p= Pattern.compile("\\\\{1}[.[^\\\\\\.]]+$");
		File root = new File(FileNameComparerA.path);
		for(File file:root.listFiles()){
			try {
				readFile(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = new FileWriter(new File(FileNameComparerA.path+"result.txt"));
		for(String key:map.keySet()){
			if(map.get(key).size()<2) continue;
			if(key.indexOf('.')!=-1) continue;
			fw.write(key+"\r\n");
			for(String value:map.get(key)){
				fw.write(value+"\r\n");
			}
			fw.write("\r\n");
		}
	}
	static Map<String, Set<String>> map = new HashMap<String, Set<String>>();
	private static void readFile(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		String fileabsolutepath = null;
		while(sc.hasNext()){
			fileabsolutepath=sc.next();
			Matcher m = RegK.mFilename(fileabsolutepath);
			while(m.find()) {
				String s = m.group();
				if(map.get(s)==null) map.put(s, new HashSet<String>());
//				if(fileabsolutepath.charAt(0)!='\\')
					map.get(s).add(fileabsolutepath);
			}
		}
	}
}
