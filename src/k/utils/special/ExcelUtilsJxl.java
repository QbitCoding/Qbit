package k.utils.special;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import k.utils.common.RegK;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.font.BidiUtils;


public final class ExcelUtilsJxl extends ExcelUtils{
	private static Log log=LogFactory.getLog("Qbit");
	 private static class ContectSelecter implements Transformer<Cell, String>{
		@Override
		public String transform(Cell e) {
			return e.getContents();
		}
	}
	public static final Transformer<Cell, String> contectSelecter = new ContectSelecter();
	/**
	 * 
	 * @param file the excel file where data read from
	 * @param pagenum start with 0
	 * @param start just give me the index showing in Excel for example "K2";
	 * @param end accord java custom,the mothed do not read the row and the column of the end cell;
	 * @param filter 
	 * @param selecter
	 * @return return the data row by row,the first cell of the next row will follow the last cell of this row
	 * @throws BiffException
	 * @throws IOException
	 */
	public <F> List<F> getList(File file,int pagenum,String start,String end,Predicate<? super Cell> filter,Transformer<? super Cell, F> selecter) throws BiffException, IOException{
		List<F> list = new ArrayList<F>();
		int[] startindex=parseIndex(start);
		int[] endindex=parseIndex(end);
		Workbook excel = Workbook.getWorkbook(file);
		Sheet sheet = excel.getSheet(pagenum);
		log.debug("sheet:"+sheet.getName());
		log.debug("将从第"+startindex[1]+"行读到第"+endindex[1]);
		for(int i = startindex[1];i<endindex[1];i++){
			for(int j=startindex[0];j<endindex[0];j++){
				Cell cell =sheet.getCell(j, i);
				if(filter.evaluate(cell)){
					list.add(selecter.transform(cell));
				}
			}
		}
		return list;
	}
	/**
	 * 
	 * @param index
	 * @return accord jxl.jar,the int[0] is the column index and the int[1] is the row index,they begin with 0;
	 */

	public static String indexParse(Cell cell){
		return indexParse(cell.getColumn(),cell.getRow());
	}

	/**
	 * 
	 * @param file
	 * @param start
	 * @param end accord java custom,the mothed do not read the row and the column of the end cell;
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public List<String> getContectList(File file,String start,String end) throws BiffException, IOException{
		return getList(file,0,start,end,TruePredicate.INSTANCE,contectSelecter);
	}
	

	public static Cell getCell(File file,int pageno,String index){
		int[] num=parseIndex(index);
		try {
			return Workbook.getWorkbook(file).getSheet(pageno).getCell(num[0],num[1]);
		} catch (IndexOutOfBoundsException | BiffException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static Cell[][] toArray(File file,int pageno,String startindex,String endindex){
		int[] start = parseIndex(startindex);
		int[] end =parseIndex(endindex);
		Cell[][] cellss = new Cell[end[0]-start[0]][end[1]-start[1]];
		Sheet sheet;
		try {
			sheet = Workbook.getWorkbook(file).getSheet(pageno);
		} catch (IndexOutOfBoundsException | BiffException | IOException e) {
			e.printStackTrace();
			return null;
		}
		for(int i=0;i<end[0]-start[0];i++){
			for(int j=0;j<end[1]-start[1];j++){
				cellss[i][j]=sheet.getCell(start[0]+i,start[1]+ j);
			}
		}
		return cellss;
	}
	/**
	 * get the cell value by column and row name from user
	 * @param sheet
	 * @param start
	 * @param end
	 * @param co
	 * @return
	 */
	private static Map<String,Cell> helpmap(Sheet sheet,String start,String end,String co){
		Map<String,Cell>helpmap=new HashMap<String,Cell>();
		int[] startindex=parseIndex(start);
		int[] endindex=parseIndex(end);
		int[] coindex=parseIndex(co);
		for(int i=startindex[0];i<endindex[0];i++){
			for(int j=startindex[1];j<endindex[1];j++){
				String name = StringUtils.trim(sheet.getCell(i, coindex[1]).getContents())+'-'
						+StringUtils.trim(sheet.getCell(coindex[0], j).getContents());
				Cell cell=sheet.getCell(i, j);
				helpmap.put(name, cell);
			}
		}
		return helpmap;
	}
	public static Map<String,String> compareCellValue(File file0,int pageno0,String start0,String end0,String co0,
			File file1,int pageno1,String start1,String end1,String co1) throws IndexOutOfBoundsException, BiffException, IOException{
		Map<String, String> map=new HashMap<String,String>();
		Map<String,Cell> map0=helpmap(Workbook.getWorkbook(file0).getSheet(pageno0), start0, end0, co0);
		Map<String,Cell> map1=helpmap(Workbook.getWorkbook(file1).getSheet(pageno1), start1, end1, co1);
		for(String index:map0.keySet()){
			Cell cell0=map0.get(index);
			Cell cell1=map1.get(index);
			if(!isSame(cell0,cell1)){
				map.put(indexParse(cell0),indexParse(cell1));
			}
		}
		for(Map.Entry<String, String> entry:map.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
		return map;
	}
	private static boolean isSame(Cell cell0, Cell cell1) {
		String s0=StringUtils.trim(cell0.getContents());
		String s1=StringUtils.trim(cell1.getContents());
		if(s0.equals(s1)) return true;
		Matcher m0=RegK.mNumber(s0);
		Matcher m1=RegK.mNumber(s1);
		if(m0.matches()&&m1.matches()){
			double d=Double.valueOf(s0)-Double.valueOf(s1);
			return d<1||d>-1;
		}
		return false;
	}
	private static int counter=0;
	public static void main(String[] args) throws IndexOutOfBoundsException, BiffException, IOException {
		File file = new File("D:\\Work\\CcbSalaryManageSystem\\10月分表\\不良个贷经营部.xls");
//		Sheet sheet = Workbook.getWorkbook(file).getSheet(13);
//		System.out.println(sheet.getName());
//		System.out.println(sheet.getColumns());
//		System.out.println(sheet.getRows());
		Sheet sheet0 = Workbook.getWorkbook(file).getSheet(0);
		Map<String,Cell> map0=helpmap(sheet0, "C5", "Q49", "A4");
//		Pattern p =Pattern.compile("(.*)(?:\\(数量\\)（.+）)(.*)");
		Pattern pblank=Pattern.compile("^\\s*$");
		Sheet sheet1=Workbook.getWorkbook(new File("D:\\MyData\\Download\\2013-10 (3).xls")).getSheet(28);
		Map<String,Cell> map1=helpmap(sheet1,"C5","Q49","A4");
		for(String name:new TreeSet<String>(map1.keySet())){
//			Matcher m=p.matcher(name);
//			if(m.find()){
//				String name0=m.group(1)+m.group(2);
			String name0=name;
				Cell cell0=map0.get(name0);
				if(cell0!=null){
					String v0=StringUtils.defaultIfBlank(cell0.getContents(), "0");
					String v=StringUtils.defaultIfBlank(map1.get(name).getContents(), "0");
					double d=Double.valueOf(v)-Double.valueOf(v0);
					if(d>1||d<-1){
//						log.debug(indexParse(cell0));
//						if(name.startsWith("AAB-")||name.endsWith("-")) continue;
						log.debug(name);
//						System.out.println(v0);
//						System.out.println(v);
//						throw new RuntimeException();
						counter++;
					}else{
					}
//					throw new RuntimeException();
				}else{
					System.out.println("没找到");
					System.out.println(name);
//					System.out.println(name0);
					log.debug(map0);
					throw new RuntimeException();
//				}
//			}else{
//				System.out.println("正则没找到");
//				System.out.println(name);
//				throw new RuntimeException();
			}
		}
		System.out.println(counter);
	}
		
}
