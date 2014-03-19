package formulaK;

import java.math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public final class IntegerK {
//	public static IntegerK ZERO;

	
	/**
	 * 唯一有效的初始化构造
	 * @param list
	 * @param base
	 * @param isPositive
	 */
	private IntegerK(List<Integer> list,int base,boolean isPositive){
		this.base=base;
		this.list=list;
		this.isPositive=isPositive;
	}
	
	private int base;
	private final List<Integer> list;
	private final  boolean isPositive;
	public static IntegerK getInstance(int base,String s){
		List<Integer> list = new ArrayList<>();
		boolean isPositive = true;
		int j=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='-') isPositive=!isPositive;
			else{
				j=i;
				break;
			}
		}
		long swap =0;
		list.add(0);
		for(int i=s.length()-1;i>j;i--){
			char c = s.charAt(i);
			if(c>='0'&&c<='9') swap=c-'0';
			else if(c>='A'&&c<='Z') swap=c-'A'+10;
			else if(c>='a'&&c<='z') swap=c-'a'+36;
			else throw new NumberFormatException();
			if(swap<=Integer.MAX_VALUE) continue;
			long _swap = swap/Integer.MAX_VALUE;
			swap %=Integer.MAX_VALUE;
		}
		ListHelperK.reverse(list);
		IntegerK instance = new IntegerK(list,base,isPositive);
		return instance;
	}
	private IntegerK newInstance(List<Integer> list,int base,boolean isPositive){
		return new IntegerK(list, base, isPositive);
	}
	public IntegerK abs() {
		return newInstance(list, base,true);
	}

	public IntegerK add(IntegerK arg0) {
		List<Integer> newlist=null;
		if(isPositive==arg.isPositive){
			newlist= add(list,arg0.list);
			return newInstance(newlist, base, isPositive);
		}else{
			newlist=subtract(list, arg0.list);
			if(newlist.get(newlist.size()-1)!=-1) return newInstance(newlist, base, isPositive);
			newlist.remove(newlist.size()-1);return newInstance(newlist, base,! isPositive);
		}
	}
	private static List<Integer>  add(List<Integer> list1,List<Integer> list2){
		long swap = 0;
		List l = new ArrayList<>();
		int i=0;
		boolean flage=true;
		while(true){	
			swap/=Integer.MAX_VALUE;
			int num1=0;
			if(i>=list1.size()) {
				flage=false;
			}
			else{
				num1=list1.get(i);
				flage=true;
			}
			int num2=0;
			if(i>=list2.size()) {
				if(!flage){
					if(swap!=0) l.add((int)swap);
					break;
				}
			}
			else{
				num2=list2.get(i);
				flage=true;
			}
			swap += num1+num2;
			l.add(swap%Integer.MAX_VALUE);
		}
		return l;
	}
	
	public int compareTo(IntegerK arg0) {
		return subtract(arg0);
	}

	@Override
	public IntegerK divide(IntegerK arg0) {
		// TODO Auto-generated method stub
		return super.divide(arg0);
	}

	@Override
	public IntegerK[] divideAndRemainder(IntegerK arg0) {
		// TODO Auto-generated method stub
		return super.divideAndRemainder(arg0);
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
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return list.get(0).intValue();
	}

	@Override
	public IntegerK max(IntegerK arg0) {
		// TODO Auto-generated method stub
		return super.max(arg0);
	}
	public IntegerK min(IntegerK arg0) {
		if(isPositive){
			if(arg0.isPositive){
				return min(list,arg0.list)?this:arg0;
			}else return this;
		}else{
			if(arg0.isPositive){
				return arg0;
			}
			return max(list,arg0.list)?this:arg0;
		}
	}

	@Override
	public IntegerK mod(IntegerK arg0) {
		// TODO Auto-generated method stub
		return super.mod(arg0);
	}

	@Override
	public IntegerK multiply(IntegerK arg0) {
		List<Integer> newlist
	}
	private static List<Integer> multiply(List<Integer> list0,int num){
		List<Integer> newlist = new ArrayList<>();
		long swap=0;
		for(Integer i:list0){
			swap+=i*num;
			newlist.add((int) (swap%Integer.MAX_VALUE));
			swap/=Integer.MAX_VALUE;	
		}
		if(swap!=0) newlist.add((int)swap);
		return newlist;
	}

	public IntegerK negate() {
		// TODO Auto-generated method stub
		return newInstance(list, base, !isPositive);
	}

//	@Override
//	public IntegerK nextProbablePrime() {
//		// TODO Auto-generated method stub
//		return super.nextProbablePrime();
//	}

	public IntegerK pow(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntegerK remainder(IntegerK arg0) {
		// TODO Auto-generated method stub
		return super.remainder(arg0);
	}

	public int signum() {
		if(list.size()==1&&list.get(0)==0) return 0;
		return isPositive?1:-1;
	}

	public IntegerK subtract(IntegerK arg0) {
		List<Integer> newlist=null;
		if(isPositive!=arg0.isPositive){
			newlist=add(list, arg0.list);
			return newInstance(newlist, base, isPositive);
		}else{
			newlist=subtract(list, arg0.list);
			if(newlist.get(newlist.size()-1)!=-1) return newInstance(newlist, base, isPositive);
			newlist.remove(newlist.size()-1);
			return newInstance(newlist, base, !isPositive);
		}
	} 
	private List<Integer> subtract(List<Integer> list1,List<Integer> list2){
		long swap = 0;
		List<Integer> l = new ArrayList<Integer>();
		int i=0;
		while(true){	
			boolean flage=true;
			int num1=0;
			if(i>=list1.size()) {
				flage=false;
			}
			else{
				num1=list1.get(i);
				flage=true;
			}
			int num2=0;
			if(i>=list2.size()) {
				if(!flage){
//					if(swap!=0) list.add((int)swap);
					break;
				}
			}
			else{
				num2=list2.get(i);
				flage=true;
			}
			if(num1+swap>=num2){
				l.add((int) (num1-num2+swap));
				swap=0;
			}
			else{
				l.add((int) (Integer.MAX_VALUE-num2+num1+swap));
				swap=-1;
			}
		}
		if(swap==-1){
			boolean flage = true;//标记是否被后面借了一位
			for(Integer num:l){
				if(num==0&&flage) continue;
				if(flage){
					num = Integer.MAX_VALUE;
					flage=false;
					continue;
				}
				num=Integer.MAX_VALUE-num-1;
			}
		}
		l.add(-1);
		return l;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (isPositive?"":"-")+ListHelperK.reverse(list);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return this;
	}
	

}
