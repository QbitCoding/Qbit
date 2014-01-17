/**
 * 
 */
package formulaK;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//import utilsK.PriorityMap;
//import utilsK.StackK;

/**
 * @author Qbit
 * 
 */
public interface FormulaKI extends Callable<String>, Serializable {
	boolean isFormula();
	void setFormulastring(String formulastring);
	public  String getValue();
	void setParametermap(Map<String, String> parametermap);

	Map<String, String> getParametermap();

	String getFormulastring();

	public static class FormulaFactory {
		private FormulaFactory() {
		}
		 private static FormulaFactory ff;
		 public static FormulaFactory getInstance(String name){
		 if(ff==null) ff=new FormulaFactory();
		 return ff;
		 }
		public FormulaKI getFormula(String s,Map m) {
			// FormulaKI formula = null;
//			switch (name) {
//			case "SimpleFormulaK":
				return SimpleFormulaK.getInstance(s,m);
//			}
//			return null;

		}

	}
	final class SimpleFormulaK extends FormulaKA{
		public static void main(String[] args) throws Exception {
//			FormulaKI k = SimpleFormulaK.getInstance();
//			k.setFormulastring("a+aa*b>5555?c:aaa");
			Map<String,String> map = new HashMap<String,String>();
			map.put("a", ""+1);
			map.put("aa", ""+0);
			map.put("b", ""+99999);
			map.put("c", ""+90);
			map.put("aaa", "33");
//			k.setParametermap(map);
//			System.out.println(k.call());
		}

		private SimpleFormulaK() {
			// TODO Auto-generated constructor stub
		}
		private SimpleFormulaK(String s,Map map) {
			setFormulastring(s);
			setParametermap(map);
		}
		public static FormulaKI getInstance(String s,Map map){
			return new SimpleFormulaK(s,map);
		}
		@Override
		protected void calculate() throws FormulaExceptionK {
			value=cal1(0,formulastring.length()).toString();	
		}

		private Object cal1(int i,int j) throws FormulaExceptionK {
			return cal(formulastring);
		}

		private PriorityMap pm =PriorityMap.getInstance();
		private BigDecimal cal(String s) throws FormulaExceptionK {
			StackK<BigDecimal> nums = new StackK<BigDecimal>();
			StackK<String> ops=new StackK<String>();
			int index=-1;
			Map<Integer,Integer> threemap = getpair(s, '?', ':'); 
			Map<Integer, Integer> parenmap = getpair(s,'(',')');
			BigDecimal num2 = null;
			for(int i=0;i<s.length();i++){
//				System.out.println("nums"+i+":"+nums);
//				System.out.println("ops"+i+":"+ops);
				char c=s.charAt(i);
				if(c=='('){
					int j = parenmap.get(i);
					num2=cal(s.substring(i+1, j));
					i=j;
				}else if(c=='_'||CharUtils.isAsciiAlphanumeric(c)){
					num2=null;
				}else if(pm.getPriority(""+c)<pm.getPriority(""+ops.pop())){
					ops.push(""+c);
					if(num2==null) num2=get(s.substring(index+1,i));
					nums.push(num2);
					index=i;
				}else{
					if(num2==null) {
						num2=get(s.substring(index+1,i));
					}
					while(ops.pop()!=null&&pm.getPriority(""+c)>=pm.getPriority(""+ops.pop())){
						BigDecimal num1=nums.pop();
						num2=cal0(num1,ops.pop(),num2);
					}
					ops.push(""+c);
					nums.push(num2);
					index=i;
					if(c=='?'){
						int j = threemap.get(i);
						if(num2==t) {
							num2=cal(s.substring(i+1, j));
							i=j;
						}
						else{
							num2=cal(s.substring(j+1, s.length()));
							i=s.length()-1;
						}
						i=s.length();
						ops.pop();
						nums.pop();
					} 
				}
			}
			if(num2==null) {
				String ss = s.substring(index+1,s.length());
				num2= get(ss);
			}
			BigDecimal num1=null;
			while((num1=nums.pop())!=null){
				num2=cal0(num1, ops.pop(), num2);
			}
			return num2;
		}
		private Pattern pattern = Pattern.compile("^[[[1-9]{1}[0-9]*]0]\\.?[0-9]+$");
		private BigDecimal get(String ss) throws FormulaExceptionK {
			try {
				if(pattern.matcher(ss).matches()) return new BigDecimal(ss);
				else return new BigDecimal(parametermap.get(ss));
			} catch (Exception e) {
				log.error(formulastring);
				log.error("parametermap:"+parametermap);
				log.error("key:"+ss, e);
				throw new FormulaExceptionK();
			}
		}
		static Log log = LogFactory.getLog("Qbit");
		private Map<Integer,Integer> getpair(String s,char left,char right){
			Map<Integer, Integer> map = new HashMap<Integer,Integer>();
			StackK<Integer> stack = new StackK<Integer>();
			Integer j = null;
			for(int i = 0;i<s.length();i++){
				if(s.charAt(i)==left) stack.push(i);
				if(s.charAt(i)==right&&(j = stack.pop())!=null) map.put(j,i);
			}
			return map;	
		}
		private final BigDecimal t = new BigDecimal("501328287");
		private final BigDecimal f = new BigDecimal("499478836");
		private BigDecimal cal0(BigDecimal num1, String pop, BigDecimal num2) throws FormulaExceptionK {
			char op = pop.charAt(0);
			int flage =num1.compareTo(num2);
			switch(op){
			case '+':return num1.add(num2);
			case '*':return num1.multiply(num2);
			case '%':return num1.remainder(num2);
			case '/':return num1.divide(num2);
			case '-':return num1.subtract(num2);
			case '<':return flage==-1?t:f;
			case '>':return flage==1?t:f;
			case '=':return flage==0?t:f;
			case '{':return (flage<=0)?t:f;
			case '}':return (flage>=0)?t:f;
			case '!':return (flage!=0)?t:f;
			default:throw new FormulaExceptionK();
			}
		}

		@Override
		public boolean isFormula() {
			// TODO Auto-generated method stub
			return false;
		}

		private final char spit='~';
		private final char xiao='{';
		private final char da='}';
		private final char equ='=';
		private final char unequ='!';
		@Override
		protected void transform() {
			formulastring=StringUtils.deleteWhitespace(formulastring);
			StringBuilder sb = new StringBuilder();
			formulastring=StringUtils.replace(formulastring, "<=", ""+spit+xiao+spit);
			formulastring=StringUtils.replace(formulastring, ">=", ""+spit+da+spit);
			formulastring=StringUtils.replace(formulastring, "==", ""+spit+equ+spit);
			formulastring=StringUtils.replace(formulastring, "!=", ""+spit+unequ+spit);
			formulastring=StringUtils.replace(formulastring, "<>", ""+spit+unequ+spit);
			for(int i=formulastring.length()-1;i>=0;i--){
				char c = formulastring.charAt(i);
				if(CharUtils.isAsciiAlphanumeric(c)||c=='_'){
					sb.append(c);
//				}else if(c=='='){
//					sb.append(""+spit);
//					sb.append(c);
//					sb.append(formulastring.charAt(--i));
//					sb.append(""+spit);
				}else{
					sb.append(""+spit);
					sb.append(c);
					sb.append(""+spit);
				}
			}
			formulastring=sb.reverse().toString();
			formulastring=StringUtils.replace(formulastring, ""+spit, "");
		}
	}
	abstract class FormulaKA implements FormulaKI{
		public final static class PriorityMap {
			private Map<String,Integer> prioritymap ;
			private static PriorityMap instance;

			private PriorityMap() {
				super();
				prioritymap=new HashMap<String,Integer>();
				prioritymap.put("(", 1);
				prioritymap.put(")", 1);
				prioritymap.put("[", 1);
				prioritymap.put("]", 1);
				prioritymap.put(".", 1);
				prioritymap.put("*", 3);
				prioritymap.put("/", 3);
				prioritymap.put("%", 3);
				prioritymap.put("+", 4);
				prioritymap.put("-", 4);
				prioritymap.put("<=", 6);
				prioritymap.put(">=", 6);
				prioritymap.put("<", 6);
				prioritymap.put(">", 6);
				prioritymap.put("{", 6);//
				prioritymap.put("}", 6);//
				prioritymap.put("=", 7);//
				prioritymap.put("!", 7);//
				prioritymap.put("==", 7);
				prioritymap.put("!=", 7);
				prioritymap.put("|", 10);
				prioritymap.put("&&", 11);
				prioritymap.put("||", 12);
				prioritymap.put("?", 13);
				prioritymap.put(":", 13);
			}
			public int getPriority(String s){
				if(s.equals(""+null)) return Integer.MAX_VALUE;
				return prioritymap.get(s);
			}
			public static PriorityMap getInstance(){
				if(instance==null) instance=new PriorityMap();
				return instance;
			}
		}
		private static Map<FormulaKA,String> formulamap = new HashMap<FormulaKA,String>();
		protected String formulastring;
		private String oldformulastring;
		protected Map<String,String> parametermap;
		protected String value;

		protected FormulaKA() {

		}

		/* �ö����hashcode�����Ź�ʽ�ĸı�Ͳ���ĸı��ı䣬�������������ڲ�
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((formulastring == null) ? 0 : formulastring.hashCode());
			result = prime * result
					+ ((parametermap == null) ? 0 : parametermap.hashCode());
			return result;
		}

		/* �Ƚϵ��������������ܼ����ֵ֮����ܱȽϣ����򷵻�false
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public final boolean equals(Object obj) {	
			if (!(obj instanceof FormulaKA)) {
				return false;
			}
			FormulaKA other = (FormulaKA) obj;
				return (this.oldformulastring.equals(other.oldformulastring))&&(this.parametermap.equals(other.parametermap));
		}

		public final String getValue() {
			if(value!=null) return value;
			Set<FormulaKA> formulaset = formulamap.keySet();
			if(formulaset.contains(this)) return formulamap.get(this);
			try {
				calculate();
			} catch (FormulaExceptionK e) {
				value=null;
				return null;
			}
			formulamap.put(this, value);
			return value;
		}
		/*
		 * ����ʵ�ָ÷��������ʵ�ʼ���
		 */
		abstract protected void calculate() throws FormulaExceptionK ;

		@Override
		abstract public boolean isFormula();

		public static final Map<FormulaKA, String> getFormulamap() {
			return formulamap;
		}

		public static final void setFormulamap(Map<FormulaKA, String> formulamap) {
			FormulaKA.formulamap = formulamap;
		}

		@Override
		public String toString() {
			return "formulastring:"+oldformulastring+"\n"+"parameters:"+parametermap+"\n"+"value:"+value;
		}

		public final String getFormulastring() {
			return oldformulastring;
		}

		public final void setFormulastring(String formulastring) {
			this.formulastring = formulastring;
			transform();
			oldformulastring=formulastring;
		}
		protected abstract void transform();

		public final Map<String, String> getParametermap() {
			return parametermap;
		}

		public final void setParametermap(Map<String, String> parametermap) {
			this.parametermap = parametermap;
		}

		protected final void setValue(String value) {
			this.value = value;
		}
		@Override
		public String call() throws Exception {
			return getValue();
		}
	}


}

