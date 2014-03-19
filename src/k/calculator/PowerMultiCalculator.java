package k.calculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;

import org.apache.commons.collections4.Transformer;

import k.utils.common.CallbacKI;
import k.utils.common.JobManager;
import k.utils.common.RegK;
import k.utils.common.RelationBuilderKI;
/**
 * 实现了多线程计算
 * @author Qbit
 *
 * @param <V>
 * 2014-03-03
 */
public abstract class PowerMultiCalculator<V> extends MultiCalculatorKA<V> {
	protected PowerMultiCalculator(Map<String,V> resultKM,Collection<String> resultKC,
			Map<String,String> formulaKM,Map<String,? extends Object> parameterKM,
			Transformer<String, V> provider,
//			SingleCalculatorKI<V> singlecalcularor,
			CallbacKI<String> callbacker,List<String> resultKL){
		super(resultKM, resultKC, formulaKM, parameterKM, provider, 
//				singlecalcularor, 
				callbacker, resultKL);
	}
	private RelationBuilderKI.RelationBuilderKA<String> rb=new RelationBuilderKI.QuickRelationBuilder<String>();
	private Map<String,Callable<V>> jobKM=new HashMap<String, Callable<V>>();
	@Override
	public final  List<String> call() throws Exception {
		for(String name:resultKC){
			build(name);
		}
		List<String> list=new ArrayList<>();
		JobManager<String,V> jm=JobManager.newInstance(rb, jobKM, 0, new CallbacKI.VoidCallback<V>() {
		},list);
		return jm.call();
	}
	private void build(String name) {
		Matcher m =RegK.mWord(formulaKM.get(name));
		boolean flage=false;//记录本身是否需要公式得到
		while(m.find()){
			flage=true;
			rb.add(m.group(), name);
			build(m.group());
		}
		if(flage)jobKM.put(name, getSingle(name));
	}
	abstract protected SingleCalculatorKI<V> getSingle(String name);
}
class DoublePowerMultiCK extends PowerMultiCalculator<Double> {

	public DoublePowerMultiCK(Map<String, Double> resultKM,
			Collection<String> resultKC, Map<String, String> formulaKM,
			Map<String, ? extends Object> parameterKM,CallbacKI<String> callbacker,List<String> resultKL
			) {
		super(resultKM, resultKC, formulaKM, parameterKM, new Transformer<String, Double>(){
			@Override
			public Double transform(String input) {
				return Double.valueOf(input);
			}
		},callbacker,resultKL);
	}

	@Override
	protected SingleCalculatorKI<Double> getSingle(String name) {
		return new QuickSingleCalculatorK(formulaKM.get(name), parameterKM);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
class DynamicPowerMultiCK extends DoublePowerMultiCK{
	public DynamicPowerMultiCK(Map<String, Double> resultKM,
			Collection<String> resultKC, Map<String, String> formulaKM,
			Map<String, ? extends Object> parameterKM,
			 CallbacKI<String> callbacker,
			List<String> resultKL) {
		super(resultKM, resultKC, formulaKM, parameterKM,callbacker,
				resultKL);
	}

	@Override
	protected SingleCalculatorKI<Double> getSingle(String name) {
		return DynamicSingleCalculatorK.getInstance(name, formulaKM.get(name), parameterKM);
	}

}