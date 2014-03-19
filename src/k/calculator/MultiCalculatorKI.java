package k.calculator;

import java.util.List;
import java.util.concurrent.Callable;
/**
 * return the the calculated as list;
 * @author Qbit
 *
 * @param <V> V is the type of the result
 */
public interface MultiCalculatorKI<V> extends Callable<List<String>>{
	
}
