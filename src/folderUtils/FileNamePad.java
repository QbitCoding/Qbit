package folderUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class FileNamePad {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	private static Pattern pattern;
	public int pad(File folder,int index,int length){
		if(folder==null||!folder.isDirectory()) return 0;
		if(pattern==null) pattern=Pattern.compile("\\d+");
		for(File file:folder.listFiles()){
			String name = file.getName();
			Matcher m = pattern.matcher(name);
			int i=0;
			while(m.find()){
				if(i!=index) continue;
				i++;
				String mid = m.group();
				if(mid.length()==length) continue;
				name=name.substring(0, m.start())
						+StringUtils.leftPad(mid, length, '0')
						+name.substring(m.end(),name.length()-1);
				break;
			}
			rename(file,name);
		}
		return length;
	}
	private void rename(File file, String name) {
		// TODO Auto-generated method stub
		
	}

}
