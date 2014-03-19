package k.drop;

import java.util.regex.Matcher;

import k.utils.common.RegK;

public class ExcelUtils {
	/**
	 * give a new formula from the sfrom 
	 * @param sfrom
	 * @param x
	 * @param y
	 * @return
	 * @throws FormulaException
	 */
	protected static String cloneFormula20140312(String sfrom, int x,int y) {
		StringBuffer sb = new StringBuffer();
		Matcher m=RegK.mExcelFormulaIndex(sfrom);
		int end=0;
		while(m.find()){
			sb.append(sfrom.substring(end, m.start()));
			end=m.end();
			StringBuffer sb0=new StringBuffer();
			String column=m.group(1);
			String row = m.group(2);
			if(column.startsWith("$")&&row.startsWith("$")){
				sb0.append(m.group());
			}else if(column.startsWith("$")){
				sb0.append("$");
				column=column.substring(1,column.length());
				sb0.append(indexMove(column+row, 0, y));
			}else if(row.startsWith("$")){
				row=row.substring(1,row.length());
				Matcher mm = RegK.mExcelIndex(indexMove(column+row, x, 0));
				if(mm.find())
				sb0.append(mm.group(1));
				sb0.append("$"+row);
			}else{
				sb0.append(indexMove(column+row, x, y));
			}
			sb.append(sb0);
		}
		sb.append(sfrom.substring(end, sfrom.length()));
		return sb.toString();
	}

}
