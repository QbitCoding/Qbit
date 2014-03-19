package k.folder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class FolderNameComparerK {

	private FolderNameComparerK(Collection<File> c) {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	private static Map<String,List<String>> resultKM = new HashMap<>();
	public static File read(Collection<File> fildKC,File folder) throws Exception{
		if(!folder.isDirectory()) return null;
		folder.mkdirs();
		File file4out = new File(folder, "Result.xml");
		Document doc4out=DocumentHelper.createDocument();
		Element root4out=doc4out.addElement("Root");
		for(File file:fildKC){
			Element root = new SAXReader().read(file).getRootElement();
			for(Object element:root.elements()){
				read((Element)element);
			}
		}
		for(Map.Entry<String, List<String>> entry:resultKM.entrySet()){
			List<String> list=entry.getValue();
			if(list.size()<2) continue;
			Element element = root4out.addElement(FolderNameReader2XmlK.fileS);
			element.addAttribute(FolderNameReader2XmlK.nameS, entry.getKey());
			for(String s:list){
				element.addText(s);
			}
		}
		new XMLWriter(new FileOutputStream(file4out),OutputFormat.createPrettyPrint()).write(doc4out);
		return file4out;
	}


	private static void read(Element element) {
		String name=element.attributeValue(FolderNameReader2XmlK.nameS);
		String path=element.attributeValue(FolderNameReader2XmlK.pathS);
		List<String> set=resultKM.get(name);
		if(set==null){
			set=new ArrayList<>();
			set.add(path);
			resultKM.put(name, set);
			for(Object obj:element.elements())
				read((Element)obj);
		}else{
			if(path.startsWith(set.get(set.size()-1))) return;//对于父子文件夹重名的情况跳过
			set.add(path);
		}
	}
}
