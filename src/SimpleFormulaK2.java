/**
 * 
 */


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


/**
 * @author Administrator
 * 
 */
public class SimpleFormulaK2 extends FormulaKA {
	private char spit = '~';

	@Override
	public boolean isFormula() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void calculate() throws FormulaExceptionK {
		transform();
		int left = 0, right = 0, begin = 0, end = 0;
		StringBuilder newformulastring = new StringBuilder();
		while ((right = StringUtils.indexOf(getFormulastring(), ')', end)) != -1) {
			left = StringUtils.lastIndexOf(getFormulastring(), '(');
			if (left == -1)
				throw new FormulaExceptionK();
			FormulaKI subformula = SimpleFormulaK2.getInstance();
			subformula.setFormulastring(StringUtils.substring(
					getFormulastring(), left + 1, right));
			subformula.setParametermap(getParametermap());
			newformulastring.append(StringUtils.substring(getFormulastring(),
					begin, left));
			newformulastring.append(subformula.getValue());
			begin = right + 1;
			end = StringUtils.indexOf(getFormulastring(), '(', begin);
		}
		newformulastring.append(StringUtils.substring(getFormulastring(),
				begin, getFormulastring().length()));
		FormulaKI newformula = SimpleFormulaK2.getInstance();
		newformula.setFormulastring(newformulastring.toString());
		newformula.setParametermap(getParametermap());
		if (StringUtils.contains(newformulastring, ')')) {
			setValue(newformula.getValue());
		} else if (StringUtils.contains(newformulastring, '(')) {
			throw new FormulaExceptionK();
		} else {
			four();
		}
	}

	protected void transform() {
		// legalparameterregex="[\u0021\003c\003d\003e\003f\u0025\u0028\u0029\u002a\u002b\u002d\u002f]{1}";
		formulastring.replaceAll(" ", "");
		Set<Character> set = new HashSet<Character>();
		set.add('+');
		set.add('-');
		set.add('*');
		set.add('/');
		set.add('%');
		set.add('(');
		set.add(')');
		set.add('?');
		set.add(':');
		set.add('<');
		set.add('>');
		set.add('=');
		set.add('!');
		// Pattern p = Pattern.compile(legalparameterregex);
		// Matcher m = p.matcher(formulastring);
		// m.
		StringBuilder sb = new StringBuilder();
		int index = 0;
		sb.append(spit);
		for (int i = 0; i < formulastring.length(); i++) {
			if (set.contains(formulastring.charAt(i))) {
				sb.append(StringUtils.substring(formulastring, index, i));
				index = i;
				sb.append(spit);
				sb.append(formulastring.charAt(i));
				sb.append(spit);
			}
		}
		sb.append(StringUtils.substring(formulastring, index,
				formulastring.length()));
		sb.append(spit);
		formulastring = sb.toString();
		Set<String> keyset = getParametermap().keySet();
		for (String key : keyset) {
			formulastring.replaceAll("(" + spit + key + spit + "){1}", spit
					+ parametermap.get(key) + spit);
		}
		// formulastring.replaceAll(""+spit, "");
	}

	private void four() {// 进行四则混合运算
		// Pattern p =
		// Pattern.compile(spit+"[0-9a-zA-Z[_]]+^{1}[0-9a-zA-Z[_]]+"+spit);
		formulastring.replace("--", "");
		int start = 0, middle = 0, end = 0;
		while ((middle = formulastring.indexOf('^')) != -1) {
			start = StringUtils.lastIndexOf(formulastring, spit, middle - 2);
			BigDecimal num1 = new BigDecimal(formulastring.substring(start + 1,
					middle - 1));
			end = StringUtils.indexOf(formulastring, spit, middle + 2);
			Integer num2 = Integer.getInteger(formulastring.substring(
					middle + 2, end));
			// if (formulastring.charAt(middle + 1) == '-')
			// num2 = 0 - num2;
			num1 = num1.pow(num2);
			formulastring = formulastring.substring(0, start + 1)
					+ num1.toString()
					+ formulastring.substring(end, formulastring.length());
			formulastring.replace("--", "");
		}
		start = 0;
		middle = 0;
		end = 0;
		while ((middle = StringUtils.indexOfAny(formulastring, '*', '/', '%')) != -1) {
			start = StringUtils.lastIndexOf(formulastring, spit, middle - 2);
			BigDecimal num1 = new BigDecimal(formulastring.substring(start + 1,
					middle - 1));
			end = StringUtils.indexOf(formulastring, spit, middle + 2);
			BigDecimal num2 = new BigDecimal(formulastring.substring(
					middle + 2, end));
			// if (formulastring.charAt(middle + 1) == '-')
			// num2 = num2.negate();
			switch (formulastring.charAt(middle)) {
			case '*':
				num1 = num1.multiply(num2);
				formulastring = formulastring.substring(0, start + 1)
						+ num1.toString()
						+ formulastring.substring(end, formulastring.length());
				break;
			case '/':
				num1 = num1.divide(num2);
				formulastring = formulastring.substring(0, start + 1)
						+ num1.toString()
						+ formulastring.substring(end, formulastring.length());
				break;
			case '%':
				formulastring = formulastring.substring(0, start + 1)
						+ (num1.longValue() % num2.longValue())
						+ formulastring.substring(end, formulastring.length());
				break;
			}
			formulastring.replace("--", "");
		}
		formulastring.replace("+-", "-");
		start = 0;
		middle = 0;
		end = 0;
		while ((middle = StringUtils.indexOfAny(formulastring, '+', '-')) != -1) {
			start = StringUtils.lastIndexOf(formulastring, spit, middle - 2);
			BigDecimal num1 = new BigDecimal(formulastring.substring(start + 1,
					middle - 1));
			end = StringUtils.indexOf(formulastring, spit, middle + 2);
			BigDecimal num2 = new BigDecimal(formulastring.substring(
					middle + 2, end));
			switch (formulastring.charAt(middle)) {
			case '+':
				num1 = num1.add(num2);
				formulastring = formulastring.substring(0, start + 1)
						+ num1.toString()
						+ formulastring.substring(end, formulastring.length());
				break;
			case '-':
				num1 = num1.multiply(num2);
				formulastring = formulastring.substring(0, start + 1)
						+ num1.toString()
						+ formulastring.substring(end, formulastring.length());
				break;
			}
			formulastring.replace("--", "");
		}
		bool();

	}

	private final char trueflage = 'T';
	private final char falseflage = 'F';

	private void bool() {
		int start = 0, middle = 0, end = 0;
		BigDecimal num1 = null;
		BigDecimal num2 = null;
		while ((middle = StringUtils.indexOfAny(formulastring, '<', '>', '=',
				'!')) != -1) {
			if (formulastring.charAt(middle + 3) != '=') {
				switch (formulastring.charAt(middle)) {
				case '>':
					start = StringUtils.lastIndexOf(formulastring, spit,
							middle - 2);
					num1 = new BigDecimal(formulastring.substring(start + 1,
							middle - 1));
					end = StringUtils.indexOf(formulastring, spit, middle + 2);
					num2 = new BigDecimal(formulastring.substring(middle + 2,
							end));
					formulastring = formulastring.substring(0, start + 1)
							+ (!num1.min(num2).equals(num1) ? trueflage
									: falseflage)
							+ formulastring.substring(end,
									formulastring.length());
					break;
				case '<':
					if (formulastring.charAt(middle + 3) != '>') {
						start = StringUtils.lastIndexOf(formulastring, spit,
								middle - 2);
						num1 = new BigDecimal(formulastring.substring(
								start + 1, middle - 1));
						end = StringUtils.indexOf(formulastring, spit,
								middle + 2);
						num2 = new BigDecimal(formulastring.substring(
								middle + 2, end));
						formulastring = formulastring.substring(0, start + 1)
								+ (!num1.min(num2).equals(num2) ? trueflage
										: falseflage)
								+ formulastring.substring(end,
										formulastring.length());
						break;
					}else{
						start = StringUtils.lastIndexOf(formulastring, spit,
								middle - 2);
						num1 = new BigDecimal(formulastring.substring(
								start + 1, middle - 1));
						end = StringUtils.indexOf(formulastring, spit,
								middle +5);
						num2 = new BigDecimal(formulastring.substring(
								middle + 5, end));
						formulastring = formulastring.substring(0, start + 1)
								+ (num1.equals(num2) ? trueflage
										: falseflage)
								+ formulastring.substring(end,
										formulastring.length());
						break;
					}
					
				}
			}else{
				switch (formulastring.charAt(middle)) {
				case '>':
					start = StringUtils.lastIndexOf(formulastring, spit,
							middle - 2);
					num1 = new BigDecimal(formulastring.substring(start + 1,
							middle - 1));
					end = StringUtils.indexOf(formulastring, spit, middle + 5);
					num2 = new BigDecimal(formulastring.substring(middle + 5,
							end));
					formulastring = formulastring.substring(0, start + 1)
							+ (!num1.min(num2).equals(num1) ? trueflage
									: falseflage)
							+ formulastring.substring(end,
									formulastring.length());
					break;
			}

		}

	}

	public static FormulaKI getInstance() {
		// TODO Auto-generated method stub
		return new SimpleFormulaK2();
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(FormulaKI arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int compare(FormulaKI arg0, FormulaKI arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

}
