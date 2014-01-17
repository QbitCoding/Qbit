package formulaK;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class NumberK extends BigDecimal {

	private NumberK(char[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private NumberK(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private NumberK(double arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private NumberK(BigInteger arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private NumberK(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private NumberK(long arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private NumberK(char[] arg0, MathContext arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private NumberK(String arg0, MathContext arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private NumberK(double arg0, MathContext arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private NumberK(BigInteger arg0, MathContext arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private NumberK(BigInteger arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private NumberK(int arg0, MathContext arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private NumberK(long arg0, MathContext arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private NumberK(char[] arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	private NumberK(BigInteger arg0, int arg1, MathContext arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	private NumberK(char[] arg0, int arg1, int arg2, MathContext arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}
	protected NumberK(){
		super("0");
	}
	private int base = 10;
	private IntergerK numerator;
	private IntergerK denominator;
	public static NumberK getInstance(String s,int base){
		new
	}
	@Override
	public BigDecimal abs() {
		// TODO Auto-generated method stub
		return super.abs();
	}

	@Override
	public BigDecimal abs(MathContext arg0) {
		// TODO Auto-generated method stub
		return super.abs(arg0);
	}

	@Override
	public BigDecimal add(BigDecimal arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.add(arg0, arg1);
	}

	@Override
	public BigDecimal add(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.add(arg0);
	}

	@Override
	public byte byteValueExact() {
		// TODO Auto-generated method stub
		return super.byteValueExact();
	}

	@Override
	public int compareTo(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.compareTo(arg0);
	}

	@Override
	public BigDecimal divide(BigDecimal arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.divide(arg0, arg1, arg2);
	}

	@Override
	public BigDecimal divide(BigDecimal arg0, int arg1, RoundingMode arg2) {
		// TODO Auto-generated method stub
		return super.divide(arg0, arg1, arg2);
	}

	@Override
	public BigDecimal divide(BigDecimal arg0, int arg1) {
		// TODO Auto-generated method stub
		return super.divide(arg0, arg1);
	}

	@Override
	public BigDecimal divide(BigDecimal arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.divide(arg0, arg1);
	}

	@Override
	public BigDecimal divide(BigDecimal arg0, RoundingMode arg1) {
		// TODO Auto-generated method stub
		return super.divide(arg0, arg1);
	}

	@Override
	public BigDecimal divide(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.divide(arg0);
	}

	@Override
	public BigDecimal[] divideAndRemainder(BigDecimal arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.divideAndRemainder(arg0, arg1);
	}

	@Override
	public BigDecimal[] divideAndRemainder(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.divideAndRemainder(arg0);
	}

	@Override
	public BigDecimal divideToIntegralValue(BigDecimal arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.divideToIntegralValue(arg0, arg1);
	}

	@Override
	public BigDecimal divideToIntegralValue(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.divideToIntegralValue(arg0);
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return super.doubleValue();
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return super.floatValue();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return super.intValue();
	}

	@Override
	public int intValueExact() {
		// TODO Auto-generated method stub
		return super.intValueExact();
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return super.longValue();
	}

	@Override
	public long longValueExact() {
		// TODO Auto-generated method stub
		return super.longValueExact();
	}

	@Override
	public BigDecimal max(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.max(arg0);
	}

	@Override
	public BigDecimal min(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.min(arg0);
	}

	@Override
	public BigDecimal movePointLeft(int arg0) {
		// TODO Auto-generated method stub
		return super.movePointLeft(arg0);
	}

	@Override
	public BigDecimal movePointRight(int arg0) {
		// TODO Auto-generated method stub
		return super.movePointRight(arg0);
	}

	@Override
	public BigDecimal multiply(BigDecimal arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.multiply(arg0, arg1);
	}

	@Override
	public BigDecimal multiply(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.multiply(arg0);
	}

	@Override
	public BigDecimal negate() {
		// TODO Auto-generated method stub
		return super.negate();
	}

	@Override
	public BigDecimal negate(MathContext arg0) {
		// TODO Auto-generated method stub
		return super.negate(arg0);
	}

	@Override
	public BigDecimal plus() {
		// TODO Auto-generated method stub
		return super.plus();
	}

	@Override
	public BigDecimal plus(MathContext arg0) {
		// TODO Auto-generated method stub
		return super.plus(arg0);
	}

	@Override
	public BigDecimal pow(int arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.pow(arg0, arg1);
	}

	@Override
	public BigDecimal pow(int arg0) {
		// TODO Auto-generated method stub
		return super.pow(arg0);
	}

	@Override
	public int precision() {
		// TODO Auto-generated method stub
		return super.precision();
	}

	@Override
	public BigDecimal remainder(BigDecimal arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.remainder(arg0, arg1);
	}

	@Override
	public BigDecimal remainder(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.remainder(arg0);
	}

	@Override
	public BigDecimal round(MathContext arg0) {
		// TODO Auto-generated method stub
		return super.round(arg0);
	}

	@Override
	public int scale() {
		// TODO Auto-generated method stub
		return super.scale();
	}

	@Override
	public BigDecimal scaleByPowerOfTen(int arg0) {
		// TODO Auto-generated method stub
		return super.scaleByPowerOfTen(arg0);
	}

	@Override
	public BigDecimal setScale(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return super.setScale(arg0, arg1);
	}

	@Override
	public BigDecimal setScale(int arg0, RoundingMode arg1) {
		// TODO Auto-generated method stub
		return super.setScale(arg0, arg1);
	}

	@Override
	public BigDecimal setScale(int arg0) {
		// TODO Auto-generated method stub
		return super.setScale(arg0);
	}

	@Override
	public short shortValueExact() {
		// TODO Auto-generated method stub
		return super.shortValueExact();
	}

	@Override
	public int signum() {
		// TODO Auto-generated method stub
		return super.signum();
	}

	@Override
	public BigDecimal stripTrailingZeros() {
		// TODO Auto-generated method stub
		return super.stripTrailingZeros();
	}

	@Override
	public BigDecimal subtract(BigDecimal arg0, MathContext arg1) {
		// TODO Auto-generated method stub
		return super.subtract(arg0, arg1);
	}

	@Override
	public BigDecimal subtract(BigDecimal arg0) {
		// TODO Auto-generated method stub
		return super.subtract(arg0);
	}

	@Override
	public BigInteger toBigInteger() {
		// TODO Auto-generated method stub
		return super.toBigInteger();
	}

	@Override
	public BigInteger toBigIntegerExact() {
		// TODO Auto-generated method stub
		return super.toBigIntegerExact();
	}

	@Override
	public String toEngineeringString() {
		// TODO Auto-generated method stub
		return super.toEngineeringString();
	}

	@Override
	public String toPlainString() {
		// TODO Auto-generated method stub
		return super.toPlainString();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public BigDecimal ulp() {
		// TODO Auto-generated method stub
		return super.ulp();
	}

	@Override
	public BigInteger unscaledValue() {
		// TODO Auto-generated method stub
		return super.unscaledValue();
	}

	@Override
	public byte byteValue() {
		// TODO Auto-generated method stub
		return super.byteValue();
	}

	@Override
	public short shortValue() {
		// TODO Auto-generated method stub
		return super.shortValue();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return this;
	}
	
	
}
