package k.utils.common;

import org.apache.commons.lang3.StringUtils;
/**
 * 用于不断自动生成
 * @author Qbit
 *
 * @param <T>
 */
public interface Generater<T> {
	T generate();
	public class NumberGenerater implements Generater<String>{
		private int index;
		private int maxlength;
		private String prefix="";
		private String suffix="";
		private NumberGenerater(int maxlength,int index) {
			this.maxlength=maxlength;
			this.index=index;
		}
		public static NumberGenerater newInstance(int maxlength,int startindex){
			if(maxlength<0) return null;
			return new NumberGenerater(maxlength,startindex);
		}
		
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
		@Override
		public String generate(){
			return prefix+StringUtils.leftPad(""+index++, maxlength, "0")+suffix;
		}

		public static void main(String[] args) {
			NumberGenerater ng = NumberGenerater.newInstance(4, 0);
			for(int i=0;i<10;i++){
				System.out.println(ng.generate());
			}
		}

	}
}
