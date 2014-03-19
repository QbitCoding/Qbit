package formulaK;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utilsK.RegK;
import utilsK.StacK;


public final class SimpleFormulaK extends FormulaKA {
	public static void main(String[] args) throws Exception {
		String  s="a+aa*b>5555?c:aaa";
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "" + 1);
		map.put("aa", "" + 0);
		map.put("b", "" + 99999);
		map.put("c", "" + 90);
		map.put("aaa", "33");
		String result=getInstance(s, map).call().toString();
		System.out.println(result);
	}


	private SimpleFormulaK(String s, Map map) {
		setFormulastring(s);
		setParametermap(map);
	}

	public static FormulaKI getInstance(String s, Map<String, ? extends Object> map) {
		log.debug("formula:"+s);
		log.debug("parametermap:"+map);
		if(pattern==null) pattern=RegK.pNumber();
		return new SimpleFormulaK(s, map);
	}

	@Override
	protected void calculate() throws FormulaExceptionK {
		value = cal1(0, formulastring.length()).toString();
	}

	private Object cal1(int i, int j) throws FormulaExceptionK {
		return cal(formulastring);
	}

	private PriorityMap pm = PriorityMap.getInstance();

	private BigDecimal cal(String s) throws FormulaExceptionK {
		StacK<BigDecimal> nums = new StacK<BigDecimal>();
		StacK<String> ops = new StacK<String>();
		int index = -1;
		Map<Integer, Integer> threemap = getpair(s, '?', ':');
		Map<Integer, Integer> parenmap = getpair(s, '(', ')');
		BigDecimal num2 = null;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(') {
				int j = parenmap.get(i);
				num2 = cal(s.substring(i + 1, j));
				i = j;
			} else if (c == '_' || CharUtils.isAsciiAlphanumeric(c)) {
				num2 = null;
			} else if (pm.getPriority("" + c) < pm.getPriority(""
					+ ops.pop())) {
				ops.push("" + c);
				if (num2 == null)
					num2 = get(s.substring(index + 1, i));
				nums.push(num2);
				index = i;
			} else {
				if (num2 == null) {
					num2 = get(s.substring(index + 1, i));
				}
				while (ops.top() != null
						&& pm.getPriority("" + c) >= pm.getPriority(""
								+ ops.top())) {
					BigDecimal num1 = nums.pop();
					num2 = cal0(num1, ops.pop(), num2);
				}
				ops.push("" + c);
				nums.push(num2);
				index = i;
				if (c == '?') {
					int j = threemap.get(i);
					if (num2 == t) {
						num2 = cal(s.substring(i + 1, j));
						i = j;
					} else {
						num2 = cal(s.substring(j + 1, s.length()));
						i = s.length() - 1;
					}
					i = s.length();
					ops.pop();
					nums.pop();
				}
			}
		}
		if (num2 == null) {
			String ss = s.substring(index + 1, s.length());
			num2 = get(ss);
		}
		BigDecimal num1 = null;
		while ((num1 = nums.pop()) != null) {
			num2 = cal0(num1, ops.pop(), num2);
		}
		return num2;
	}

	private static Pattern pattern ;

	private BigDecimal get(String ss) throws FormulaExceptionK {
		Object o = parametermap.get(ss);
		if (o != null)
			return new BigDecimal(o.toString());
		if (pattern.matcher(ss).matches())
			return new BigDecimal(ss);
		else
			throw new FormulaExceptionK();
	}

	static Log log = LogFactory.getLog("Qbit");

	private Map<Integer, Integer> getpair(String s, char left, char right) throws FormulaExceptionK {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		StacK<Integer> stack = new StacK<Integer>();
		Integer j = null;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == left)
				stack.push(i);
			else if (s.charAt(i) == right ){
				if((j = stack.pop()) != null)
					map.put(j, i);
				else throw new FormulaExceptionK();
			}
		}
		if(stack.top()!=null) throw new FormulaExceptionK();
		return map;
	}

	private static final BigDecimal t = new BigDecimal("50132828");
	private static final BigDecimal f = new BigDecimal("49947883");

	private BigDecimal cal0(BigDecimal num1, String pop, BigDecimal num2)
			throws FormulaExceptionK {
		char op=0;
		try{
		op = pop.charAt(0);
		}catch(Exception e){
			log.warn(pop, e);
			throw new RuntimeException();
		}
		int flage = num1.compareTo(num2);
		switch (op) {
		case '+':
			return num1.add(num2);
		case '*':
			return num1.multiply(num2);
		case '%':
			return num1.remainder(num2);
		case '/':
			return num1.divide(num2);
		case '-':
			return num1.subtract(num2);
		case '<':
			return flage == -1 ? t : f;
		case '>':
			return flage == 1 ? t : f;
		case equ:
			return flage == 0 ? t : f;
		case xiao:
			return (flage <= 0) ? t : f;
		case da:
			return (flage >= 0) ? t : f;
		case unequ:
			return (flage != 0) ? t : f;
		default:
			throw new FormulaExceptionK();
		}
	}

	@Override
	public boolean isFormula() {
		return false;
	}

	private static final char xiao = '{';
	private static final char da = '}';
	private static final char equ = '#';
	private static final char unequ = '!';

	protected void transform0() {
		formulastring = StringUtils.deleteWhitespace(formulastring);
		formulastring = StringUtils.replace(formulastring, "<=", "" + xiao);
		formulastring = StringUtils.replace(formulastring, ">=", "" + da);
		formulastring = StringUtils.replace(formulastring, "==", "" + equ);
		formulastring = StringUtils
				.replace(formulastring, "!=", "" + unequ);
		formulastring = StringUtils
				.replace(formulastring, "<>", "" + unequ);
	}


	protected List<String> transform() {
		List<String> list = new ArrayList<String>();
		transform0();
		Matcher m = RegK.mFormula(formulastring);
		while(m.find()){
			list.add(m.group());
		}
		return list;
	}

}
