package k.calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import k.utils.common.DynamicObjectCreaterK;
import k.utils.common.MapDecoraterK;
import k.utils.common.RegK;
/**
 * 
 * @author Qbit
 *2014-03-11
 */
public class DynamicSingleCalculatorK implements SingleCalculatorKI<Double> {
	private String formula;
	private Map<String, ? extends Object> parameterKM;
	private String name;
	private DynamicSingleCalculatorK(String name,String formula,
			Map<String, ? extends Object> parameterKM) {
		this.name=name;
		this.formula = formula;
		this.parameterKM = parameterKM;
	}
	/**
	 * 
	 * @param name can't be null,and can't repeat;
	 * @param formula can't be null
	 * @param parameterKM null is legal if no parameter
	 * @return
	 */
	public static DynamicSingleCalculatorK getInstance(String name,String formula,
			Map<String, ? extends Object> parameterKM){
		if(name==null||formula==null) return null;
		return new DynamicSingleCalculatorK(name, formula, parameterKM);
	}

	/* catch any Exception by self and return null;
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Double call(){
		try{
			return getResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	private static Map<String,Object> objKM=new HashMap<String, Object>();
	private double getResult() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object o = objKM.get(formula);
		if(o==null){
			StringBuffer src = new StringBuffer();
			StringBuffer formulaSB = new StringBuffer();
			String classname = getName();
			src.append("public class "+classname+" {\r\n");
			src.append("\t"+"public double calculate(java.util.Map map){\r\n");
			Matcher m=Pattern.compile("\\w+").matcher(formula);
			while(m.find()){
				if(RegK.mWord(m.group()).matches())
				src.append("double "+m.group()+" =Double.valueOf(map.get(\""+m.group()+"\").toString());\r\n");
				else m.appendReplacement(formulaSB, m.group()+"d");
			}
			m.appendTail(formulaSB);
			src.append("return "+formulaSB.toString()+";\r\n}\r\n}");
			try {
				o=DynamicObjectCreaterK.getInstance(classname, src.toString()).call();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println(src);
			objKM.put(classname, o);
		}
		Method m =o.getClass().getMethod("calculate", Map.class);
		return (double) m.invoke(o, parameterKM);
	}

	private String getName() {
//		return "K"+formula.hashCode();
		return name;
	}

	@Override
	public boolean isFormula() {
		return false;
	}
	public static void main(String[] args) {
		String fromul="column23 * _column23 /  12+ column24 * _column24 + column25 * _column25 + column26 * _column26 + column27 * _column27 + column28 * _column28 + column29 * _column29 + column30 * _column30 + column31 * _column31 + column32 * _column32 + column33 * _column33 + column34 * _column34 + column35 * _column35  ";
		String name="name";
		Map ma=new HashMap<String,Double>();
		ma.put("column24", 67.83);
		ma.put("_column24", 80d);
		ma.put("column26", 26.2);
		ma.put("_column26", -50d);
		ma.put("column30", 4d);
		ma.put("_column30", 300d);
		ma.put("column31", 10d);
		ma.put("_column31", 500d);
		ma.put("column32", 29d);
		ma.put("_column32", 500d);
		ma.put("column33", 28d);
		ma.put("_column33", 500d);
		ma.put("column34", 1d);
		ma.put("_column34", 100d);
		ma.put("column35", 8d);
		ma.put("_column35", 500d);
		Map<String,Double> map=new MapDecoraterK<String, Double>(ma) {

			@Override
			protected Double doGet(Object key) {
				return 0.0;
			}
		};
		double d=new DynamicSingleCalculatorK(name, fromul, map).call();
		System.out.println(d);
//		String.valueOf(null);
	}
		
}
