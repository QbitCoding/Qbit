package html2xls;

import java.io.File;
import java.util.Iterator;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class HtmlToXls {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static File transfer(File html) throws Exception{
		File xls = new File(System.currentTimeMillis()+".xls");
		WritableWorkbook excel = Workbook.createWorkbook(xls);
		Element table = getTable(html);
		String columns=getcolumn(table);
		return xls;
	}
	private static String getcolumn(Element table) {
		StringBuilder columns= new StringBuilder();
		
		return null;
	}
	private static Element getTable(File html) throws DocumentException {
		SAXReader reader = new SAXReader();
		File file = new File("books.xml");
		Document document = reader.read(file);
		Element root = document.getRootElement();
		return get(root,"table");
	}
	private static Element get(Element root, String string) {
		Iterator<Element> eleIt = root.elementIterator();
		while (eleIt.hasNext()) {
			Element e = (Element) eleIt.next();
			if(e.getName()==string) return e;
			Element ee=get(e,string);
			if(ee!=null) return ee;
		}
		return null;
	}

}
