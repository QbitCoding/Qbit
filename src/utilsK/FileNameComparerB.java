package utilsK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 查询path下各个文件,将所有文件的Name进行判断,如果相同着在result中记录,忽略有扩展名的文件
 * @author Qbit
 *
 */
public final class FileNameComparerB {

	public static void main(String[] args) throws IOException {
		p= Pattern.compile("\\\\{1}[.[^\\\\]]+$");
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
			fw.write(key+"\r\n");
			for(String value:map.get(key)){
				fw.write(value+"\r\n");
			}
			fw.write("\r\n");
		}
	}
	private static Pattern p;
	static Map<String,List<String>> map = new HashMap<String, List<String>>();
	private static void readFile(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		String fileabsolutepath = null;
		while(sc.hasNext()){
			fileabsolutepath=sc.next();
			Matcher m = p.matcher(fileabsolutepath);
			while(m.find()) {
				String s = m.group();
				if(map.get(s)==null) map.put(s, new ArrayList<String>());
				map.get(s).add(fileabsolutepath);
			}
		}
	}
}
