package k.calculator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import k.utils.common.CallbacKI;

import org.apache.commons.collections4.Transformer;

/**
 * 
 * @author Qbit
 *
 * @param <V>
 * 2014-03-11
 */
public abstract class MultiCalculatorKA <V> implements MultiCalculatorKI<V> {
//	protected SingleCalculatorKI<V> singlecalcularor;
	protected CallbacKI<String> callbacker;
	protected MultiCalculatorKA(Map<String,V> resultKM,Collection<String> resultKC,
			Map<String,String> formulaKM,Map<String,? extends Object> parameterKM,
			Transformer<String, V> provider,
//			SingleCalculatorKI<V> singlecalcularor,
			CallbacKI<String> callbacker,List<String> resultKL){
		this.resultKM=resultKM;
		this.resultKC=resultKC;
		this.formulaKM=formulaKM;
		this.parameterKM=parameterKM;
		this.provider=provider;
//		this.singlecalcularor=singlecalcularor;
		this.callbacker=callbacker;
		this.resultKL=resultKL;
	}
	
	/**
	 * the map hold the result
	 */
	protected Map<String,V> resultKM;
	/**
	 * which need to calculate
	 */
	protected Collection<String> resultKC;
	protected Map<String,String> formulaKM;
	protected Map<String,? extends Object> parameterKM;
	protected Transformer<String, V> provider;
	protected List<String> resultKL;
	@Override
	final public boolean equals(Object arg0) {
		if(arg0 instanceof DeepMultiCalculatorKA){
			DeepMultiCalculatorKA mck=(DeepMultiCalculatorKA)arg0;
			return parameterKM.equals(mck.parameterKM)&&formulaKM.equals(mck.formulaKM)&&resultKC.equals(mck.resultKC);
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	final public int hashCode() {
		return parameterKM.hashCode()+formulaKM.hashCode()+resultKC.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	final public String toString() {
		return "resultKC:"+resultKC.toString()+"	formulaKM:"+formulaKM.toString()+"	parameterKM:"+parameterKM.toString();
	}
	
}
