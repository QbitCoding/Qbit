package poitest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class POIT {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream("D:\\Work\\JHJX\\11月数据trimed.xls");
		Workbook wb = new HSSFWorkbook(fis);
		Sheet sheet=wb.getSheetAt(13);
		Row row=sheet.getRow(56);
		Cell cell=row.getCell(4);
		String s=cell.getCellFormula();
		System.out.println(s);
	}

}
