package k.calculator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import k.utils.common.CallbacKI;
import k.utils.common.RegK;

import org.apache.commons.collections4.Transformer;

abstract class DeepMultiCalculatorKA<V> extends MultiCalculatorKA<V>{

	protected DeepMultiCalculatorKA(Map<String,V> resultKM,Collection<String> resultKC,
			Map<String,String> formulaKM,Map<String,? extends Object> parameterKM,
			Transformer<String, V> provider,
//			SingleCalculatorKI<V> singlecalcularor,
			CallbacKI<String> callbacker,List<String> resultKL){
		super(resultKM, resultKC, formulaKM, parameterKM, provider, 
//				singlecalcularor, 
				callbacker, resultKL);
	}
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public List<String> call() throws Exception {
		if(formulaKM==null||parameterKM==null||resultKC==null||provider==null||resultKM==null) return null;
		for(String name:resultKC){
			calculate(name);
		}
		return resultKL;
	}
	/**
	 * 采取深度优先的计算策略
	 * @param name
	 * @throws Exception
	 */
	private void calculate(String name) throws Exception {
		if(resultKM.containsKey(name)||provider.transform(name)!=null) return;
			String formula= formulaKM.get(name);
			if(formula==null) throw new Exception();
			Matcher m=RegK.mNumberWord(formula);
			while(m.find()){
				calculate(m.group());
			}
			resultKM.put(name, calculate(formula,parameterKM));
			resultKL.add(name);
			callbacker.callback(name);
	}
	abstract protected V calculate(String formula,
			Map<String, ? extends Object> parameter) throws Exception;

}
class QuickMultiCalculatorK extends DeepMultiCalculatorKA<Double> {

	protected QuickMultiCalculatorK(Map<String,Double> resultKM,Collection<String> resultKC,
			Map<String,String> formulaKM,Map<String,? extends Object> parameterKM,
			Transformer<String, Double> provider,
//			SingleCalculatorKI<V> singlecalcularor,
			CallbacKI<String> callbacker,List<String> resultKL){
		super(resultKM, resultKC, formulaKM, parameterKM, provider, callbacker, resultKL);
	}

	@Override
	protected Double calculate(String formula,
			Map<String, ? extends Object> parameter) throws Exception {
		return new QuickSingleCalculatorK(formula, parameter).call();
	}

}
