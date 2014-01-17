package Json;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public interface ToJson<E> extends Serializable {
	String tojson();

	E from(String json);

	E from(Map map);

	public static final class Utils {
		public static String map2json(Map map) {
			return null;
		}

		public static String getJson(Object o, boolean markflage)
				throws Exception {
			String mark = markflage ? "'" : "\"";
			if (o == null) {
				return mark + "null" + mark;
			} else if (o instanceof ToJson) {
				return ((ToJson) o).tojson();
			} else if (o instanceof String) {
				String s = (String) o;
				s = s.replaceAll("\\\\", "\\\\\\\\");
				s = s.replaceAll("\n", "\\\\n");
				s = s.replaceAll("\r", "\\\\r");
				s = s.replaceAll("\"", "\\\\\"");
				s = s.replaceAll("'", "\\\\\'");
				return mark + s + mark;
			} else if (o instanceof Boolean || o instanceof Integer
					|| o instanceof Long || o instanceof Float
					|| o instanceof Double || o instanceof Short
					|| o instanceof java.math.BigInteger
					|| o instanceof java.math.BigDecimal) {
				return o.toString();
			} else if (o instanceof Collection) {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				int i = 0;
				for (Object oo : (Collection) o) {
					sb.append(getJson(oo, !markflage));
					sb.append(",");
				}
				sb.setCharAt(sb.length() - 1, ']');
				return sb.toString();
				// }else if(o instanceof Set){
				// StringBuilder sb = new StringBuilder();
				// sb.append("[");
				// for(Object oo:(Set)o){
				// sb.append(getJson(oo,!markflage));
				// sb.append(",");
				// }
				// sb.setCharAt(sb.length()-1, ']');
				// return sb.toString();
			} else if (o instanceof Map) {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				Map map = (Map) o;
				for (Object key : map.keySet()) {
					sb.append('{' + mark);
					sb.append(key.toString());// 这个方法有问题,当key有单引号,双引号和各种括号时就会有问题
					sb.append(mark + ':');
					sb.append(getJson(map.get(key), !markflage));
					sb.append("},");
				}
				sb.setCharAt(sb.length() - 1, ']');
				return sb.toString();
			} else if (o instanceof java.sql.Date) {
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
						"yyyy-MM-dd");
				java.sql.Date v = (java.sql.Date) o;
				String s = df.format(new java.util.Date(v.getTime()));
				return "\"" + s + "\"";
			} else if (o instanceof java.util.Date) {
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
						"yyyy-MM-dd");
				java.util.Date v = (java.util.Date) o;
				String s = df.format(v);
				return "\"" + s + "\"";
			} else if (o instanceof java.sql.Timestamp) {
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				java.sql.Timestamp v = (java.sql.Timestamp) o;
				String s = df.format(new java.util.Date(v.getTime()));
				return "\"" + s + "\"";
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append('{');
				Class c = o.getClass();
				for (Field f : o.getClass().getDeclaredFields()) {
					sb.append(mark + f.getName() + mark);
					switch (f.getType().getSimpleName()) {
					case "int":
					case "long":
					case "byte":
					case "float":
					case "short":
					case "double":
					case "char":
						sb.append(f.get(o));
					default:
						sb.append(getJson(f.get(o), !markflage));
					}
					sb.append(',');
				}
				sb.setCharAt(sb.length() - 1, '}');
				return sb.toString();
			}
		}

		public static String getJson(int o, boolean maekflage) {
			return "" + o;
		}

		public static String getJson(long o, boolean maekflage) {
			return "" + o;
		}

		public static String getJson(byte o, boolean maekflage) {
			return "" + o;
		}

		public static String getJson(double o, boolean maekflage) {
			return "" + o;
		}

		public static String getJson(float o, boolean maekflage) {
			return "" + o;
		}

		public static String getJson(short o, boolean maekflage) {
			return "" + o;
		}

		public static String getJson(char o, boolean maekflage) {
			return "" + o;
		}
	}
}
