package poitest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class POIT {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream("D:\\UploadFile\\1389066633586.xls");
		HSSFWorkbook wb = new HSSFWorkbook(fis);
	}

}
