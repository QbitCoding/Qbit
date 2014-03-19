package study;

import org.apache.commons.lang3.StringUtils;

public class StringUtilsKT {

	public StringUtilsKT() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
//		String s="1";
//		String ss=StringUtils.substringAfterLast(s, ".");
//		System.out.println(ss);
//		System.out.println(ss.equals(""));
		String sss =StringUtils.difference("abbccc", "addccc");
		System.out.println(sss);
	}

}
