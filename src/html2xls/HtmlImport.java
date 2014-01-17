package html2xls;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class HtmlImport {

	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		File file=new File("D:\\UploadFile\\1389066633586.xls");
		Document document = reader.read(file);
		Element root = document.getRootElement();
		System.out.println("getDate:"+root.getData());
		System.out.println("getName"+root.getName());
//		Element table = getElement(root, "table");
	}

}
