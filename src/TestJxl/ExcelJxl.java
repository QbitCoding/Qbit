package TestJxl;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelJxl {

	public static void main(String[] args) throws BiffException, IOException {
		File excel=new File("D:\\UploadFile\\中间业务收入 (4)13.xlsx");
		Workbook wb = Workbook.getWorkbook(excel);
		Sheet sheet = wb.getSheet(0);
		for(int i =0;i<sheet.getRows();i++){
			for(int j=0;j<sheet.getColumns();j++){
				Cell cell = sheet.getCell(j, i);
				if(cell.getType()==CellType.NUMBER){
//					System.out.println(cell.getClass());
					try{
						NumberCell n =(NumberCell)cell;
						if(n.getValue()!=0)
						System.out.println(n.getContents());
					}catch(Exception e){
						System.out.println("cast NumberCell Exception");
					}

					
//					System.out.println("-----------------------------------");
				}
			}
		}
	}

}
