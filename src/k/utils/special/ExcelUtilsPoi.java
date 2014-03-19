package k.utils.special;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtilsPoi extends ExcelUtils {
	public static List<Cell> isSameFormula(File file, int pageno, String start,
			String end, boolean rowbyrow) throws FileNotFoundException,
			IOException {
		int[] startindex = parseIndex(start);
		int[] endindex = parseIndex(end);
		List<Cell> outKL = new ArrayList<Cell>();
		Sheet sheet = new HSSFWorkbook(new FileInputStream(file))
				.getSheetAt(pageno);
		if (rowbyrow) {
			for (int i = startindex[1]; i < endindex[1]; i++) {
				List<Cell> list = new ArrayList<Cell>();
				for (int j = startindex[0]; j < endindex[0]; j++) {
					list.add(sheet.getRow(i).getCell(j));
				}
				outKL.addAll(isSameFormula(list));
			}
		} else {
			for (int i = startindex[0]; i < endindex[0]; i++) {
				List<Cell> list = new ArrayList<Cell>();
				for (int j = startindex[1]; j < endindex[1]; j++) {
					list.add(sheet.getRow(j).getCell(i));
				}
				List<Cell> list0=isSameFormula(list);
				if(list0.size()<10)
				outKL.addAll(list0);
			}
		}
		return outKL;
	}

	/**
	 * 
	 * @param list
	 * @return the list of cell is not same with the first one
	 */
	private static List<Cell> isSameFormula(List<Cell> list) {
		if (list == null || list.size() == 0)
			return null;
		Cell first = list.get(0);
		List<Cell> outKL = new ArrayList<Cell>();
		for (Cell cell : list) {
			if (!isSameFormula(first, cell)) {
//				System.out.println(indexParse(first));
//				System.out.println(indexParse(cell));
//				System.out.println(getCellmessage(first));
//				System.out.println(getCellmessage(cell));
//				String s=cloneFormula(first.getCellFormula(),
//						cell.getColumnIndex() - first.getColumnIndex(),
//						cell.getRowIndex() - first.getRowIndex());
//				System.out.println(s);
//				if(1==1) throw new RuntimeException();
				outKL.add(cell);
			}
		}
		return outKL;
	}

	/**
	 * warning:if the cell is not formula or number return true
	 * @param first
	 * @param cell
	 * @return whether the two cell is same first of all the two cell should
	 *         have the same type if the cell is formulacell,then compare the
	 *         formula if the cell is numbercell ,compare the number,the number
	 *         should be same without increasement.may there is something wrong.
	 *         else compare the contents;
	 */
	private static boolean isSameFormula(Cell first, Cell cell) {
//		if (first.getCellType() != cell.getCellType())
//			return false;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
		case Cell.CELL_TYPE_BOOLEAN:
		case Cell.CELL_TYPE_ERROR:
		case Cell.CELL_TYPE_STRING:
			return true;
		case Cell.CELL_TYPE_NUMERIC:
			if (first.getCellType() != cell.getCellType())
				return false;
			return true;
		case Cell.CELL_TYPE_FORMULA:
			if (first.getCellType() != cell.getCellType())
				return false;
			return cloneFormula(first.getCellFormula(),
					cell.getColumnIndex() - first.getColumnIndex(),
					cell.getRowIndex() - first.getRowIndex()).equals(
					cell.getCellFormula());
		}
		throw new RuntimeException("unknow cell type!");
	}

	private static String indexParse(Cell cell) {
		return indexParse(cell.getColumnIndex(), cell.getRowIndex());
	}
	private static String getCellmessage(Cell cell){
		switch(cell.getCellType()){
		case Cell.CELL_TYPE_BLANK:return "";
		case Cell.CELL_TYPE_BOOLEAN:return ""+cell.getBooleanCellValue();
		case Cell.CELL_TYPE_ERROR:return ""+cell.getErrorCellValue();
		case Cell.CELL_TYPE_FORMULA:return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:return ""+cell.getNumericCellValue();
		case Cell.CELL_TYPE_STRING:return cell.getStringCellValue();
		default:throw new RuntimeException();
		}
	}

	private static Log log = LogFactory.getLog("Qbit");

	public static void main(String[] args) throws FileNotFoundException, IOException, IndexOutOfBoundsException, BiffException {
		File file = new File("D:\\Work\\CcbSalaryManageSystem\\10-12月绩效\\trimed---2013年绩效考核含量计算表（十月有效客户挂钩后）.xls");
		int pageno=5;
		System.out.println(Workbook.getWorkbook(file).getSheet(pageno).getName());	
		int counter=0;
		for (Cell cell : isSameFormula(file, pageno, "C6", "J49", false)) {
			counter++;
				log.info(indexParse(cell));
				System.out.println(indexParse(cell));
			}
		System.out.println(counter);
	}
}
