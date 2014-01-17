package TestJxl;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.FormulaCell;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Jxl {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File file = new File("d:/test/jxl.xls");
		Workbook excel = Workbook.getWorkbook(file);
		Sheet sheet = excel.getSheet(0);
		for(int i=2;i<3;i++){
			Cell cell = sheet.getCell(0, i);
			Cell newcell=cell;
			try{
				System.out.println("-------------"+i+"-----------------");
				System.out.println(newcell.getContents());
				System.out.println("newcell.getContents()");
				System.out.println(newcell.getCellFeatures());
				System.out.println("newcell.getCellFeatures()");
				System.out.println(newcell.getType());
				
				System.out.println("newcell.getType()");
				System.out.println(newcell.getCellFormat());
				System.out.println("newcell.getCellFormat()");
//				newcell=(NumberFormulaCell)cell;
//				System.out.println("NumberFormulaCell");
				System.out.println(((NumberFormulaCell)cell).getFormula());
				System.out.println("((NumberFormulaCell)cell).getFormula()");
				System.out.println(((NumberFormulaCell)cell).get());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

}
