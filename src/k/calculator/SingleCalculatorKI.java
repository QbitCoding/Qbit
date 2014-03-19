package k.calculator;

import java.util.concurrent.Callable;


public interface SingleCalculatorKI<V> extends Callable<V> {
	boolean isFormula();
}
