package k.calculator;

import k.utils.common.FactoryKI;
import k.utils.common.GenericsTypeK;
public class ElementCalculatorKF<E> implements FactoryKI<ElementCalculator<E>> {
	private GenericsTypeK<E> type;

	@SuppressWarnings("unchecked")
	@Override
	public ElementCalculator<E> newInstance() {
		if (Type.DOUBLE == type)
			return (ElementCalculator<E>) new DoubleElementCalculator();
		return null;
	}

	public static class Type {
		public static final GenericsTypeK<Double> DOUBLE = new GenericsTypeK<Double>();
	}

	public static <E> ElementCalculatorKF<E> getFactory(GenericsTypeK<E> type) {
		ElementCalculatorKF<E> factory = new ElementCalculatorKF<E>();
		factory.type = type;
		return factory;
	}

}

class ElementCalculateException extends Exception {

	public ElementCalculateException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}

class OutofBoundException extends ElementCalculateException {

	public OutofBoundException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}

class UnsupportOperationException extends ElementCalculateException {

	public UnsupportOperationException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}

interface ElementCalculator<E> {
	final static class Result<E> {
		private E value;

		public E getValue() {
			return value;
		}

		void setValue(E e) {
			value = e;
		}

		private Boolean b;

		public Boolean getBoolean() {
			return b;
		}

		void setBoolean(boolean bool) {
			b = bool;
		}
	}

	Result<E> getResult(E num1, String op, E num2)
			throws ElementCalculateException;
}

class DoubleElementCalculator implements ElementCalculator<Double> {

	@Override
	public Result<Double> getResult(Double num1, String op, Double num2)
			throws ElementCalculateException {
		Result<Double> result = new Result<Double>();
		double value = 0;
		switch (op) {
		case "==":
			result.setBoolean(num1 == num2);
			break;
		case "!=":
		case "<>":
			result.setBoolean(num1 != num2);
			break;
		case "<=":
			result.setBoolean(num1 <= num2);
			break;
		case ">=":
			result.setBoolean(num1 >= num2);
			break;
		case "<":
			result.setBoolean(num1 < num2);
			break;
		case ">":
			result.setBoolean(num1 > num2);
			break;
		case "+":
			value = num1 + num2;
			if ((num1 > 0 && num2 > 0 && value < 0)
					|| (num1 < 0 && num2 < 0 && value > 0))
				throw new OutofBoundException(num1 + op + num2);
			result.setValue(value);
			break;
		case "-":
			result = getResult(num1, "+", 0 - num2);
			break;
		case "*":
//			result
		default:
			throw new UnsupportOperationException(op);
		}
		return result;
	}

}
