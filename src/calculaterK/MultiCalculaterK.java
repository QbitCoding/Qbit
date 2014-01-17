package calculaterK;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

public final class MultiCalculaterK implements CalculaterKI{
	private Map<String,String> formulaKM;
	private Map<String,Object> parameterKM;
	private Map<String,BigDecimal> resultKM;
	private static Pattern pattern;
	private MultiCalculaterK(Map<String, String> formulaKM,
			Map<String, Object> parameterKM) {
		this();
		this.formulaKM = formulaKM;
		this.parameterKM = parameterKM;
	}

	private MultiCalculaterK() {
		// TODO Auto-generated constructor stub
	}
	public static MultiCalculaterK getInstance(Map<String, String> formulaKM,
			Map<String, Object> parameterKM) {
		if(pattern==null) pattern=Pattern.compile(")
		return new MultiCalculaterK(formulaKM, parameterKM);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public BigDecimal call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
