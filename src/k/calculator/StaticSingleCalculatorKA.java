package k.calculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import k.utils.common.RegK;
import k.utils.common.StacK;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @author Qbit
 *2014-03-14 the parameterKM can be null
 * @param <V>
 */
public abstract class StaticSingleCalculatorKA<V> implements SingleCalculatorKI<V> {
	private static Log log = LogFactory.getLog("Qbit");
	public static QuickSingleCalculatorK getQuickSingleCalculatorK(String formula,
			Map<String, ? extends Object> parameterKM){
		return new QuickSingleCalculatorK(formula, parameterKM);
	}
	StaticSingleCalculatorKA(String formula,
			Map<String, ? extends Object> parameterKM,
			Transformer<String, V> provider) {
		this.formula = formula;
		this.parameterKM = parameterKM;
		if(parameterKM==null)
			this.parameterKM=new HashMap<String,Object>();
		this.pvProvider = provider;
	}

	private static Map<String, Integer> prioritymap;

	private static Integer oprank(String op) {
//		if (op == null)
//			return Integer.MAX_VALUE;
		if (prioritymap == null) {
			prioritymap = new HashMap<String, Integer>();
			prioritymap.put("(", Integer.MAX_VALUE);
			prioritymap.put(")", Integer.MAX_VALUE);
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
		return prioritymap.get(op);
	}

	private String formula;
	private Transformer<String, V> pvProvider;
	private Map<String, ? extends Object> parameterKM;

	abstract protected V calculate(V num1, String op, V num2);

	private V query(String num) {
		Object p = parameterKM.get(num);
		if (p != null)
			return pvProvider.transform(p.toString());
		Matcher m = RegK.mNumber(num);
		if (m.matches())
			return pvProvider.transform(num);
		return null;
	}

	List<String> formulalist = null;

	private List<String> formulaToList() {
		formulalist = new LinkedList<String>();
		Matcher m = RegK.mFormula(formula);
		while (m.find()) {
			formulalist.add(m.group());
		}
		return formulalist;
	}

	@Override
	public V call() throws Exception {
		formulaToList();
		getPair();
		return getResult(0, formulalist.size());
	}

	private void getPair() {
		StacK<Integer> far = new StacK<Integer>();
		StacK<Integer> near = new StacK<Integer>();
		for (int i = 0; i < formulalist.size(); i++) {
			switch (formulalist.get(i)) {
			case "(":
				far.push(i);
				break;
			case ")":
				pairKM.put(far.pop(), i);
				break;
			case "?":
				near.push(i);
				break;
			case ":":
				pairKM.put(near.pop(), i);
				break;
			default:
			}
		}
	}

	Map<Integer, Integer> pairKM = new HashMap<Integer, Integer>();
	boolean flage;

	private V getResult(int begin, int end) {
		log.debug("-------");
		V num2 = null;
		StacK<V> values = new StacK<V>();
		StacK<String> ops = new StacK<String>();
		for (int index = begin; index < end; index++) {
			String s = formulalist.get(index);
			log.debug("values:" + values);
			log.debug("ops:" + ops);
			log.debug("num2:" + num2);
			log.debug(s);
			if (s.equals("(")) {
				num2 = getResult(index + 1, pairKM.get(index));
				index = pairKM.get(index);
				continue;
			} else if (query(s) != null) {
				num2 = query(s);
				continue;
			} else {
				while (oprank(s) >= oprank(ops.top())) {
					num2 = calculate(values.pop(), ops.pop(), num2);
				}
				if (s.equals("?")) {
					if (flage) {
						num2 = getResult(index + 1, pairKM.get(index));
						index = getNext(pairKM.get(index),end);
					} else {
						return getResult(pairKM.get(index) + 1, end);
					}
				} else {
					values.push(num2);
					ops.push(s);
					num2 = null;
				}
			}
		}
		while (ops.top() != null) {
			num2 = calculate(values.pop(), ops.pop(), num2);
		}
		return num2;
	}

	private int getNext(int begin, int end) {
		int index = 0;
		for (index = begin + 1; index < end; index++) {
			switch (formulalist.get(index)) {
			case "(":
				index = pairKM.get(index);
				break;
			case "?":
				return index;
			}
		}
		return index;
	}

	@Override
	public boolean isFormula() {
		// TODO Auto-generated method stub
		return false;
	}

}
class QuickSingleCalculatorK extends StaticSingleCalculatorKA<Double> {
	private static Log log=LogFactory.getLog("Qbit");
	public QuickSingleCalculatorK(String formula, Map<String, ? extends Object> parameterKM) {
		super(formula, parameterKM, new Transformer<String, Double>(){
			@Override
			public Double transform(String input) {
				return Double.valueOf(input);
			}
		});
	}

	@Override
	protected Double calculate(Double num1, String op, Double num2) {
		log.debug(num1+op+num2);
		switch (op) {
		case "+":
			return num1 + num2;
		case "-":
			return num1 - num2;
		case "*":
			return num1 * num2;
		case "/":
			return num1 / num2;
		case "==":
			flage = (num1 == num2);
			break;
		case ">":
			flage = (num1 > num2);
			break;
		case "<":
			flage = (num1 < num2);
			break;
		case "<=":
			flage = (num1 <= num2);
			break;
		case ">=":
			flage = (num1 >= num2);
			break;
		default:
			throw new RuntimeException();
		}
		return null;
	}
	
}
