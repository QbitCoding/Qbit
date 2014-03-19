package k.utils.special;

import java.util.Arrays;
import java.util.regex.Matcher;

import k.utils.common.RegK;

public class ExcelUtils {
	/**
	 * give a new index of cell move x column and y row
	 * @param index
	 * @param x
	 * @param y
	 * @return
	 */
	static String indexMove(String index,int x,int y){
		int[] old = parseIndex(index);
		int[] fresh = new int[2];
		fresh[0]=old[0]+x;
		fresh[1]=old[1]+y;
		return indexParse(fresh);
	}
	/**
	 * 
	 * @param index
	 * @return accord jxl.jar,the int[0] is the column index and the int[1] is the row index,they begin with 0;
	 */
	public static  int[] parseIndex(String index){
		int[] num = new int[2];
		num[0] =0;
		num[1] =0;
		Matcher m=RegK.mExcelIndex(index);
		if(!m.matches()) return null;
		num[1]=Integer.parseInt(m.group(2));
		String column = m.group(1);
		for(int i=0;i<column.length();i++){
			char c = column.charAt(i);
			if(c<'a'){
				c=(char)(c+'a'-'A');
			}
			num[0]=num[0]*26+c-'a'+1;
		}
		num[1]=num[1]-1;
		num[0]=num[0]-1;
		return num;
	}
	public static String indexParse(int column,int row){
		if(column<0||row<0) throw new RuntimeException();
		StringBuilder sb = new StringBuilder();
		column++;
		do{
			sb.append((char)((--column)%26+'A'));
		}while((column/=26)>0);
		sb.reverse();
		return sb.toString()+(row+1);
	}
	public static String indexParse(int[] num){
		if(num.length!=2) throw new RuntimeException();
		return indexParse(num[0],num[1]);
	}
	/**
	 * give a new formula from the sfrom 
	 * @param sfrom
	 * @param x
	 * @param y
	 * @return
	 * @throws FormulaException
	 * 2014-03-12 use appendReplacement() and appendTail
	 */
	protected static String cloneFormula(String sfrom, int x,int y) {
		StringBuffer sb = new StringBuffer();
		Matcher m=RegK.mExcelFormulaIndex(sfrom);
		while(m.find()){
			String column=m.group(1);
			String row = m.group(2);
			if(column.startsWith("$")&&row.startsWith("$")){
				m.appendReplacement(sb, "\\"+column+"\\"+row);
			}else if(column.startsWith("$")){
				column=column.substring(1,column.length());
				m.appendReplacement(sb, "\\$"+indexMove(column+row, 0, y));
			}else if(row.startsWith("$")){
				row=row.substring(1,row.length());
				Matcher mm = RegK.mExcelIndex(indexMove(column+row, x, 0));
				if(mm.find())
				m.appendReplacement(sb, mm.group(1)+"\\$"+row);
			}else{
				m.appendReplacement(sb, indexMove(column+row, x, y));
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}
	public static void main(String[] args) throws Exception{
		System.out.println(Arrays.toString(parseIndex("AA118")));
		System.out.println(indexParse(26, 117));
		
	}

}
