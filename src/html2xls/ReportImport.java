package html2xls;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yuanqi.iproject.logic.DeptManager;
import com.yuanqi.iproject.logic.ParameterManager;

public final class ReportImport {
	private static File file;
	private static String date;
	private static String tableid;
	private static int row0;
	private static int column0;
	private static List<String> errlist;
	private static List<Map> maplist;
	private static Map<String, String> columnids;
	private static Map<String, String> deptids;

	public static void main(String[] args) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("file", new File("D:\\UploadFile\\bbb.xls"));
		map.put("date", null);
		map.put("index0", "E7");
		map.put("tableid", null);
		fromAll(map);
		System.out.println(getList());
	}

	private static boolean fromHtml() throws DocumentException {
		fromat();
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		Element root = document.getRootElement();
		Element table = getElement(root, "table");
		String columns = getColumns(table);
		addValue(table,columns);
		return true;
	}

	private static void addValue(Element table, String columns) {
		Element tr =null;
		Iterator<Element> it = table.elementIterator();
		for(int i=0;i<row0;i++){
			tr=it.next();
		}
		while(it.hasNext()){
			tr=it.next();
			StringBuilder values = new StringBuilder();
			Iterator<Element> tds = tr.elementIterator();
			String row=deptids.get(tds.next().getText());
			for(int i=1;i<column0;i++){
				tr=it.next();
			}
			while(tds.hasNext()){
				values.append(tds.next().getText()+",");
			}
			HashMap map=new HashMap();
			map.put("columns", columns);
			map.put("values", StringUtils.chop(values.toString()));
			map.put("tableid", tableid);
			map.put("deptid", row);
			map.put("date", date);
			maplist.add(map);
		}
	}

	private static String getColumns(Element table) {
		StringBuilder columns = new StringBuilder();
		Element tr =null;
		Iterator<Element> it = table.elementIterator();
		for(int i=0;i<row0;i++){
			tr=it.next();
		}
		it=tr.elementIterator();
		while(it.hasNext()){
			columns.append(columnids.get(it.next().getText())+",");
		}
		return StringUtils.chop(columns.toString());
	}

	private static Element getElement(Element root, String string) {
		if (root.getName() == string)
			return root;
		Iterator<Element> eleIt = root.elementIterator();
		while (eleIt.hasNext()) {
			Element ee = getElement((Element) eleIt.next(), string);
			if (ee != null)
				return ee;
		}
		return null;
	}

	public static boolean fromAll(Map<String, Object> map) throws Exception {
		file = (File) map.get("file");
		date = (String) map.get("date");
		String index0 = (String) map.get("index0");
		tableid = (String) map.get("tableid");
		System.out.println(index0);
		char[] chars = index0.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c >= '0' && c <= '9') {
				row0 = row0 * 10 + (c - '0');
			} else if (c >= 'a' && c <= 'z') {
				column0 = column0 * 26 + (c - 'a');
			} else if (c >= 'A' && c <= 'Z') {
				column0 = column0 * 26 + (c - 'A');
			} else {
				return false;
			}
		}
		row0--;
		columnids = new ParameterManager().getcodeid(tableid);
		deptids = new DeptManager().getcodeid(tableid);
		System.out.println("tableid:"+tableid);
		System.out.println("file:"+file);
		System.out.println("date:"+date);
		System.out.println("index0:"+index0);
		try {
			fromExcel();
		} catch (Exception e) {
			fromHtml();
		}
		return errlist.size()==0;
	}

	private static boolean fromExcel() throws Exception {
		fromat();
		Workbook excel = Workbook.getWorkbook(file);
		Sheet[] sheets = excel.getSheets();
		List<String> errlist = new ArrayList<String>();
		List<HashMap> maplist = new ArrayList<HashMap>();
		for (int k = 0; k < sheets.length; k++) {
			Sheet sheet = sheets[k];
			boolean flage = false;
			int begin = row0;// 记录横坐�?
			int start = column0;// 记录纵坐�?
//			while (!flage) {// 该wile结束后得到表左上角有效数据的坐标
//				begin++;
//				if (begin >= sheet.getRows())
//					return null;
//				if (!StringUtils.isEmpty(sheet.getCell(0, begin).getContents())) {
//					continue;
//				}
//				start = -1;
//				while (!flage) {
//					start++;
//					if (start >= sheet.getColumns())
//						break;
//					if (StringUtils.isEmpty(sheet.getCell(start, begin)
//							.getContents())) {
//						continue;
//					} else if (!StringUtils.isAlpha(StringUtils.trim(sheet
//							.getCell(start, begin).getContents()))) {
//						break;
//					} else {
//						flage = true;
//						begin++;
//						break;
//					}
//				}
//			}
			StringBuilder columns = new StringBuilder();
			StringBuilder values = new StringBuilder();
			Map<String, String> columnids = new ParameterManager()
					.getcodeid(tableid);
			Map<String, String> deptids = new DeptManager().getcodeid(tableid);
			for (int i = start; i < sheet.getRows(); i++) {
				HashMap map = new HashMap();// 用于向manager传�?数据
				String row = deptids.get(sheet.getCell(0, i).getContents());
				for (int j = begin; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					CellType type = cell.getType();
					if (type == CellType.EMPTY) {
						break;
					} else if (type == CellType.NUMBER) {
						String column = columnids.get(sheet.getCell(j,
								start - 1).getContents());
						columns.append(column);
						columns.append(",");
						values.append(cell.getContents());
						values.append(",");
						break;
					} else {
						errlist.add("�? + k + "�?�? + i + "�?�? + j+"列错�?+"/r/n");
					}
				}
				map.put("columns", StringUtils.chop(columns.toString()));
				map.put("values", StringUtils.chop(values.toString()));
				map.put("tableid", tableid);
				map.put("deptid", row);
				map.put("date", date);
				maplist.add(map);
				
			}
		}
//		throw new Exception();
		return false;
	}

	private static void fromat() {
		errlist = new ArrayList<String>();
		maplist = new ArrayList<Map>();
	}
	public static List getList(){
		if(errlist.size()==0) return maplist;
		return errlist;
	}

}
