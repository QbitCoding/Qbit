package k.calculator;

import java.util.Map;
import java.util.concurrent.Callable;



import k.utils.common.FactoryKI;
import k.utils.common.GenericsTypeK;

public class FormulaCalculatorKF<O> implements FactoryKI<FormulaCalculator<O>> {
	private GenericsTypeK< O> type;
	@Override
	public FormulaCalculator<O> newInstance() {
		return null;
	}
	public static <O> FormulaCalculatorKF<O> getFactory(GenericsTypeK<O> type){
		FormulaCalculatorKF<O> factory=new FormulaCalculatorKF<O>();
		factory.type=type;
		return factory;
	}

}
interface FormulaCalculator<O> extends Callable<O>{
	FormulaCalculator<O> init(String formula,Map<String,? extends O> parameterKM);
}
