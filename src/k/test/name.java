package k.test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import k.utils.common.MapDecoraterK;

public class name {
	public double calculate(java.util.Map map){
double column23 =Double.valueOf(map.get("column23").toString());
double _column23 =Double.valueOf(map.get("_column23").toString());
double column24 =Double.valueOf(map.get("column24").toString());
double _column24 =Double.valueOf(map.get("_column24").toString());
double column25 =Double.valueOf(map.get("column25").toString());
double _column25 =Double.valueOf(map.get("_column25").toString());
double column26 =Double.valueOf(map.get("column26").toString());
double _column26 =Double.valueOf(map.get("_column26").toString());
double column27 =Double.valueOf(map.get("column27").toString());
double _column27 =Double.valueOf(map.get("_column27").toString());
double column28 =Double.valueOf(map.get("column28").toString());
double _column28 =Double.valueOf(map.get("_column28").toString());
double column29 =Double.valueOf(map.get("column29").toString());
double _column29 =Double.valueOf(map.get("_column29").toString());
double column30 =Double.valueOf(map.get("column30").toString());
double _column30 =Double.valueOf(map.get("_column30").toString());
double column31 =Double.valueOf(map.get("column31").toString());
double _column31 =Double.valueOf(map.get("_column31").toString());
double column32 =Double.valueOf(map.get("column32").toString());
double _column32 =Double.valueOf(map.get("_column32").toString());
double column33 =Double.valueOf(map.get("column33").toString());
double _column33 =Double.valueOf(map.get("_column33").toString());
double column34 =Double.valueOf(map.get("column34").toString());
double _column34 =Double.valueOf(map.get("_column34").toString());
double column35 =Double.valueOf(map.get("column35").toString());
double _column35 =Double.valueOf(map.get("_column35").toString());
return column23 * _column23 /  12.0+ column24 * _column24 + column25 * _column25 + column26 * _column26 + column27 * _column27 + column28 * _column28 + column29 * _column29 + column30 * _column30 + column31 * _column31 + column32 * _column32 + column33 * _column33 + column34 * _column34 + column35 * _column35  ;
}
	public static void main(String[] args) {
		Map ma=new HashMap<String,Double>();
		ma.put("column35", 67.83);
		ma.put("_column35", 80d);
		ma.put("column33", 26.2);
		ma.put("_column33", -50d);
		ma.put("column29", 4d);
		ma.put("_column29", 300d);
		ma.put("column26", 10d);
		ma.put("_column26", 500d);
		ma.put("column27", 29d);
		ma.put("_column27", 500d);
		ma.put("column25", 28d);
		ma.put("_column25", 500d);
		ma.put("column23", 1d);
		ma.put("_column23", 100d);
		ma.put("column24", 8d);
		ma.put("_column24", 500d);
		Map<String,Double> map=new MapDecoraterK<String, Double>(ma) {

			@Override
			protected Double doGet(Object key) {
				return 0d;
			}
		};
		double d=new name().calculate(map);
		System.out.println(d);
		Matcher m =Pattern.compile("\\w+[a-z]\\w+").matcher("column23 * _column23 /  12.0+ column24 * _column24 + column25 * _column25 + column26 * _column26 + column27 * _column27 + column28 * _column28 + column29 * _column29 + column30 * _column30 + column31 * _column31 + column32 * _column32 + column33 * _column33 + column34 * _column34 + column35 * _column35 ");
		StringBuffer sb=new StringBuffer();
		while(m.find()){
			m.appendReplacement(sb, map.get(m.group()).toString());
		}
		m.appendTail(sb);
		System.out.println(sb);
		d=1.0 * 100.0 / 12.0+ 8.0 * 500.0 + 28.0 * 500.0 + 10.0 * 500.0 + 29.0 * 500.0 + 0.0 * 0.0 + 4.0 * 300.0 + 0.0 * 0.0 + 0.0 * 0.0 + 0.0 * 0.0 + 26.2 * -50.0 + 0.0 * 0.0 + 67.83 * 80.0   ;
		System.out.println(d);
	}
}