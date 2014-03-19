package TestJxl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import utilsK.PassFilterK;
import utilsK.SelecterKI;
import jxl.Cell;
import jxl.read.biff.BiffException;

public class Tester {

	public Tester() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws BiffException, IOException {
		List<String> list=new ExcelUtilsJxl().getList(new File("D:\\Work\\JHJX\\10-12月绩效\\clerk.xls"), 0, "A1","B642", new PassFilterK<Cell>(), ExcelUtilsJxl.contectSelecter);
//		new Tester().mothed(new Tester().new DefaultSelecter());
		System.out.println(list);
		System.out.println(list.size());
	}
	<F> void mothed(SelecterKI<? super Cell, F> selecter){
		Cell cell = cellprovider();
		F f = selecter.select(cell);
		System.out.println(f);
	}
	private Cell cellprovider() {
		// TODO Auto-generated method stub
		return null;
	}
	class DefaultSelecter implements SelecterKI<Cell, String>{

		@Override
		public String select(Cell e) {
			return e.getContents();
		}
		
	}

}
