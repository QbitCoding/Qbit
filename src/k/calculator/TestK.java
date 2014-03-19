package k.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import k.utils.common.MapDecoraterK;

public class TestK {

	public static void main(String[] args) throws Exception {
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
		double d=DynamicSingleCalculatorK.getInstance(name, fromul, map).call();
		System.out.println(d);
		Matcher m =Pattern.compile("\\w+[a-z]\\w+").matcher(fromul);
		StringBuffer sb=new StringBuffer();
		while(m.find()){
			m.appendReplacement(sb, map.get(m.group()).toString());
		}
		m.appendTail(sb);
		System.out.println(sb);
		d=0.0 * 0.0 /  12+ 67.83 * 80.0 + 0.0 * 0.0 + 26.2 * -50.0 + 0.0 * 0.0 + 0.0 * 0.0 + 0.0 * 0.0 + 4.0 * 300.0 + 10.0 * 500.0 + 29.0 * 500.0 + 28.0 * 500.0 + 1.0 * 100.0 + 8.0 * 500.0  ;
		System.out.println(d);
		d=StaticSingleCalculatorKA.getQuickSingleCalculatorK(fromul, map).call();
		System.out.println("d:"+d);
		d=StaticSingleCalculatorKA.getQuickSingleCalculatorK(sb.toString(), null).call();
		System.out.println(d);
	}

}
