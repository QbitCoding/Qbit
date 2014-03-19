package folderUtils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class FileNameTrimer {

	public static void main(String[] args) {
		File folder = new File("");
		new FileNameTrimer().trim(folder);
	}

	public String trim(File folder) {
		String begin = null;
		String end = null;
		if (folder != null && folder.isDirectory()) {
			File[] fileA = folder.listFiles();
			if (fileA.length < 2)
				return "";
			for (File file : fileA) {
				if (begin == null) {
					begin = end = StringUtils.substringBeforeLast(
							file.getName(), ".");
					continue;
				}
				String name = StringUtils.substringBeforeLast(file.getName(),
						".");
				begin=getSame(true,begin, name);
				end = getSame(false,end,name);
				if(begin==""&&end=="") break;
			}
		} else {
		}
		return begin;
	}
/**
 * 
 * @param flage from begin or end
 * @param sA the strings need to compare
 * @return the same string
 */
	private String getSame(boolean flage,String... sA) {
		char illegal ='*';
		StringBuilder sb = new StringBuilder();
		k: for (int i = 0;; i++) {
			int j=i;
			if(!flage) j=0-i-1;
			char c =illegal;
			for (String s : sA) {
				if (i >= s.length())
					break k;
				else if(c==illegal){
					c=s.charAt(j);
				}else if(c!=s.charAt(j)){
					break k;
				}else{
					
				}
			}
			sb.append(c);
		}
		if(!flage) sb.reverse();
		return sb.toString();
	}

}
