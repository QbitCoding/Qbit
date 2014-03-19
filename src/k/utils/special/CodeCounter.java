package k.utils.special;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public final class CodeCounter {

	private static final String countinue = null;

	public static void main(String[] args) throws IOException {
		CodeCounter cc=new CodeCounter();
		int i=cc.count(new File("D:\\Work\\CcbSalaryManageSystem\\Source\\JHJX\\web\\"));
		i+=cc.count(new File("D:\\Work\\CcbSalaryManageSystem\\Source\\JHJX\\src\\"));
		System.out.println(cc.exnameKS);
		System.out.println(i);
	}

	private int counter = 0;

	public int count(File folder) throws IOException {
		if (countFile(folder))
			return counter;
		else if (folder.isDirectory()) {
			File[] fileKA = folder.listFiles();
			if (fileKA == null)
				return counter;
			for (File file : fileKA) {
				count(file);
			}
			return counter;
		} else {
			return counter;
		}

	}

	private Set<String> exnameKS=new HashSet<String>();
	private boolean countFile(File folder) throws IOException {
		if (folder.isFile()) {
			String name = folder.getName();
			if (StringUtils.endsWithAny(name, ".java", ".xml", ".jsp", ".js",".css","php","htm","html",
					".properties")) {
				BufferedReader br = new BufferedReader(new FileReader(folder));
				String string = null;
				while ((string = br.readLine()) != null) {
					if (StringUtils.isBlank(string)
							|| StringUtils.startsWithAny(string, "//", "/*","*", "#")) {
						continue;
					} else {
						counter++;
					}
				}
			} else {
				exnameKS.add(StringUtils.substringAfterLast(name, "."));
			}
			return true;
		}else
		return false;
	}

}
