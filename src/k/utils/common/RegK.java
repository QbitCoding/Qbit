package k.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
/**
 * 
 * @author Qbit
 *2014-02-17
 *2014-03-14 sWord(): change to the exclude pure word
 */

public final class RegK {
	private static String sIp(){
		return "(?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
	}
	private static String sNumber(){
		return "(?<!\\.)(?:(?:[1-9]{1}[0-9]*)|0){1}(?:\\.[0-9]+)?(?!\\.)";
	}
	private static Pattern pNumber ;
	private static Pattern pNumber(){
		if(pNumber==null) pNumber = Pattern.compile(sNumber());
		return pNumber;
	}
	/**
	 * 匹配所有非负小数,包括整数
	 * @param s
	 * @return
	 * 2014-03-03 修复多个小数点匹配时的bug
	 */
	public static Matcher mNumber(String s){
		return pNumber().matcher(s);
	}
	private static final String sWord="\\w*[a-zA-Z]\\w*";
	private static Pattern pWord;
	private static Pattern pWord(){
		if(pWord==null) pWord=Pattern.compile(sWord);
		return pWord;
	}
	/**
	 * not pure number
	 * @param s
	 * @return
	 */
	public static Matcher mWord(String s){
		return pWord().matcher(s);
	}
	private static String sNumberWord(){
		return "\\w+";
	}
	private static Pattern pNumberWord;
	private static Pattern pNumberWord(){
		if(pNumberWord==null) pNumberWord=Pattern.compile(sNumberWord());
		return pNumberWord;
	}
	public static Matcher mNumberWord(String input){
		return pNumberWord().matcher(input);
	}
	private static String sCompare(){
		return "[<>]=?|[!=]=";
	}
	private static Pattern pCompare;
	private static Pattern pCompare(){
		if(pCompare==null) pCompare=Pattern.compile(sCompare());
		return pCompare;
	}
	public static Matcher mCompaer(CharSequence string){
		return pCompare().matcher(string);
	}
	private static String sOp(){
		return sCompare()+"|"+"[\\+\\*\\-/%\\(\\)?:]";
	}
	private static Pattern pOp;
	private static Pattern pOp(){
		if(pOp==null) pOp=Pattern.compile(sOp());
		return pOp;
	}
	public static Matcher mOp(CharSequence string){
		return pOp().matcher(string);
	}
	private static String sFormula(){
		return sNumberWord()+"|"+sOp();
	}
	private static Pattern pFormula;
	private static Pattern pFormula(){
		if(pFormula==null) pFormula=Pattern.compile(sFormula());
		return pFormula;
	}
	public static Matcher mFormula(String input){
		return pFormula().matcher(input);
	}
	private static final String sExcelIndex="^([a-zA-Z]+)([1-9][0-9]*)$";
	private static Pattern pExcelIndex;
	private static Pattern pExcelIndex(){
		if(pExcelIndex==null) pExcelIndex=Pattern.compile(sExcelIndex);
		return pExcelIndex;
	}
	public static Matcher mExcelIndex(String s){
		return pExcelIndex().matcher(s);
	}
	private static final String sExcelFormulaIndex="(\\$?[A-Z]+)(\\$?[1-9][0-9]*)";
	private static Pattern pExcelFormulaIndex;
	private static Pattern pExcelFormulaIndex(){
		if(pExcelFormulaIndex==null) pExcelFormulaIndex=Pattern.compile(sExcelFormulaIndex);
		return pExcelFormulaIndex;
	}
	public static Matcher mExcelFormulaIndex(String s){
		return pExcelFormulaIndex().matcher(s);
	}
	private static final String sFilename="[.[^\\\\]]+$";
	private static Pattern pFilename;
	private static Pattern pFilename(){
		if(pFilename==null) pFilename=Pattern.compile(sFilename);
		return pFilename;
	}
	public static Matcher mFilename(String s){
		return pFilename().matcher(s);
	}
	private static final String sInteger="(?<!\\.)(?:[1-9][0-9]*|0)(?!\\.)";
	private static Pattern pInteger;
	private static Pattern pInteger(){
		if(pInteger==null) pInteger=Pattern.compile(sInteger);
		return pInteger;
	}
	/**
	 * 匹配正整数和0,不包括小数中的整数部分,也不包括0.0
	 * @param input
	 * @return
	 * @since 2014-03-03
	 */
	public static Matcher mInteger(String input) {
		return pInteger().matcher(input);
	}
	/**
	 * 获得扩展名
	 * @param name
	 * @return
	 */
	public static String sExname(String name){
		if(name==null) return null;
		if(name.contains(".")) return StringUtils.substringAfterLast(name, ".");
		else return null;
	}
	/**
	 * 获得前名
	 * @param name
	 * @return
	 */
	public static String sRealname(String name){
		if(name==null) return null;
		if(name.contains(".")) return StringUtils.substringBeforeLast(name, ".");
		else return null;
	}
	public static void main(String[] args) {
	//		String s="$A$3";
	//		Matcher m=mExcelFormulaIndex(s);
	//		StringBuffer sb=new StringBuffer();
	//		while(m.find()){
	//			m.appendReplacement(sb, "\\$\\$");
	//		}
	//		System.out.println(sb.toString());
	//		String s="";
	//		System.out.println(StringUtils.isBlank(s));
	//		s="  ";
	//		System.out.println(StringUtils.isEmpty(s));
	//		s=null;
			System.out.println(StringUtils.defaultIfBlank("", "0"));
			System.out.println(StringUtils.defaultIfBlank(" ", "1"));
			System.out.println(StringUtils.defaultIfBlank("  ", "2"));
			System.out.println(StringUtils.defaultIfBlank(null, "3"));
		}
}
