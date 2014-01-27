package TestJxl;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelJxl {

	public static void main(String[] args) throws BiffException, IOException {
		ExcelJxl tester = new ExcelJxl();
		long time = System.currentTimeMillis();
		for(int i =0;i<1;i++){
			tester.test();
		}
		System.out.println(System.currentTimeMillis()-time);
	}
	private void test() throws BiffException, IOException{
		File excel=new File("D:\\MyData\\Download\\中间业务收入 (1).xls");
		Workbook wb = Workbook.getWorkbook(excel);
		Sheet sheet = wb.getSheet(0);
		for(int i =0;i<sheet.getRows();i++){
			for(int j=0;j<sheet.getColumns();j++){
				Cell cell = sheet.getCell(j, i);
				if(cell instanceof NumberCell ||cell instanceof NumberFormulaCell){
						NumberCell n =(NumberCell)cell;
						System.out.println(n.getValue());
				}
			}
		}
	}

}
