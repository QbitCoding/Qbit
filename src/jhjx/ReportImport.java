package jhjx;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pnbclient.helper.StringHelper;




import com.yuanqi.iproject.logic.DeptManager;
import com.yuanqi.iproject.logic.EmployeeManager;
import com.yuanqi.iproject.logic.ParameterManager;


public final class ReportImport {
	private static File file;
	private static String date;
	private static Integer tableid;
	private static int row0;
	private static int column0;
	private static List<String> errlist;
	private static List<Map> maplist;
	private static Map<String, String> columnids;
	private static Map<String, String> deptids;

	public static void main(String[] args) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("file", new File("D:\\newFolder\\2014011009451810.xls"));
		map.put("date", "2013-12");
		map.put("index0", "C5");
		map.put("tableid", "2");
		fromAll(map);
	}
	// import data from the excel file uploaded
	
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
			HashMap<String,Object> map=new HashMap<String,Object>();
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
		tableid = new Integer(map.get("tableid").toString());
		char[] chars = index0.toCharArray();
		row0=column0=0;
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
		String type =(String) map.get("type");
		String tableids = tableid.toString();
		if(type.equals("1")){
			columnids = new ParameterManager().getcodeid(tableids);
			deptids = new DeptManager().getcodeid(tableids);
		}else if(type.equals("2")){
			columnids = new ParameterManager().getcodeid(tableids);
			deptids = new EmployeeManager().getcodeid(tableids);
		}
//		try {
			fromExcel();
//		} catch (Exception e) {
//			fromHtml();
//		}
		return errlist.size()==0;
	}

	private static boolean fromExcel() throws Exception {
//		System.out.println("fromExcel()");
		fromat();
		Workbook excel = Workbook.getWorkbook(file);
		Sheet[] sheets = excel.getSheets();
		for (int k = 0; k < sheets.length; k++) {
			Sheet sheet = sheets[k];
			boolean flage = false;
			int begin = column0;
			int start = row0;
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
//			StringBuilder columns = new StringBuilder();	
//			Set<Integer> legalcolumns = new HashSet<Integer>();
//			for (int j = begin; j < sheet.getColumns(); j++) {
//				String column = columnids.get(StringUtils.trim(sheet.getCell(j,start - 1).getContents()));
//				if(StringHelper.isEmpty(column)) continue;
//				columns.append(column);
//				columns.append(",");
//				legalcolumns.add(j);
//			}
			for (int i = start; i < sheet.getRows(); i++) {
				String row = deptids.get(sheet.getCell(0, i).getContents());
				if(StringHelper.isEmpty(row)) continue;
				StringBuilder columns = new StringBuilder();	
				StringBuilder values = new StringBuilder();
				HashMap<String,Object> map4manager= new HashMap<String,Object>();
				for (int j = begin; j < sheet.getColumns(); j++) {
					String column = columnids.get(StringUtils.trim(sheet.getCell(j,start - 1).getContents()));
					if(StringHelper.isEmpty(column)) continue;
//					if(!legalcolumns.contains(j)) continue;
					Cell cell = sheet.getCell(j,i);
					CellType type = cell.getType();
					if (type == CellType.EMPTY) {
						continue;
					} else if (cell instanceof NumberCell ||cell instanceof NumberFormulaCell) {
//					} else if (cell instanceof NumberCell){
						if(errlist.size()!=0) continue;
						NumberCell nc = (NumberCell)cell;
						columns.append(column);
						columns.append(",");
						values.append(nc.getValue());
						values.append(",");
						continue;
//					}else if(cell instanceof NumberFormulaCell){
//						NumberFormulaCell nc = (NumberFormulaCell)cell;
					} else {
						errlist.add((k+1)+"~"+(i+1)+"~"+(j+1));
					}
				}
				String columnss =StringUtils.chop(columns.toString());
				map4manager.put("columns", columnss);
				map4manager.put("values", StringUtils.chop(values.toString()));
				map4manager.put("tableid", tableid);
				map4manager.put("empid", row);
				map4manager.put("date", date);
				maplist.add(map4manager);	
			}
		}
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
	public static String errInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("{\"form.paginate.pageNo\":1,\"form.paginate.totalRows\":"+errlist.size()+",\"rows\":[");
//		sb.append("{\"errlist\":[");
		for(String s:errlist){
			String[] ss = s.split("~");		
			sb.append("{\"cellid\":\"");
			int i = Integer.parseInt(ss[2]);
			i--;
			StringBuilder sb0 = new StringBuilder();
			do{
				sb0.append((char)(i%26+'A'));
				i/=26;
			}while(i>0);
			sb.append(sb0.reverse());
			sb.append(ss[1]);
			sb.append("\",\"reason\":\"不是数字");
			sb.append("\"},");
		}
		sb.setCharAt(sb.length()-1, ']');
		sb.append('}');
		System.out.println(sb);
		return sb.toString();
	}
	public static String errInfo2(){
		StringBuilder sb = new StringBuilder();
//		sb.append("{\"pager.pageNo\":1,\"pager.totalRows\":23,\"rows\":[");
		sb.append("{\"errlist\":[");
		for(String s:errlist){
			String[] ss = s.split("~");
			sb.append("{\"tableid\":\"");
			sb.append(ss[0]);
			sb.append("\",\"rowid\":\"");
			sb.append(ss[1]);
			sb.append("\",\"columnid\":\"");
			sb.append(ss[2]);
			sb.append("\"},");
		}
		sb.setCharAt(sb.length()-1, ']');
		sb.append('}');
		System.out.println(sb);
		return sb.toString();
	}
	public static String errInfo3(){
		StringBuilder sb = new StringBuilder();
		for(String s:errlist){
			String[] ss = s.split("~");		
			int i = Integer.parseInt(ss[2]);
			i--;
			StringBuilder sb0 = new StringBuilder();
			do{
				sb0.append((char)(i%26+'A'));
				i/=26;
			}while(i>0);
			sb.append(sb0.reverse());
			sb.append(ss[1]);
			sb.append("~");
		}
		sb.deleteCharAt(sb.length()-1);
		System.out.println(sb);
		return sb.toString();
	}


}
