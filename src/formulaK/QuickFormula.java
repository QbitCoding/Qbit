package formulaK;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utilsK.RegK;
import utilsK.StacK;

public class QuickFormula extends FormulaKA<Double> {

	@Override
	protected void calculate() throws FormulaExceptionK {
		Matcher m = RegK.mFormula(formulastring);
		List<String> list = new ArrayList<String>();
		list.add("(");
		while(m.find()){
			list.add(m.group());
		}
		list.add(")");
//		Map<Integer,Integer> pairKM=getPair(list);
		StacK<Double> numbers=new StacK<Double>();
		StacK<String> ops=new StacK<String>();
		double num2=0;
		boolean skip = false;
		int counter=0;
		PriorityMap map =PriorityMap.getInstance();
		for(int i=0;i<list.size();i++){
			int priority = Integer.MAX_VALUE;
			String s=list.get(i);
			switch(s){
			case "(":
				if(skip) counter++;
				else ops.push(s);
				continue;
			case")":
				if(skip&&--counter>=0) continue;
				skip=false;
				break;
			case"?":
				if(flage) ops.push("(");
				else skip=true;
				break;
			case":":
				skip=!skip;
				if(!skip) {
					ops.push("(");
					continue;
				}
				break;
			default:
				Double o=null;
				if((o=parametermap.get(s))!=null) {
					num2=o;
					continue;
				}
				else if(RegK.mOp(s).matches()){
					priority=map.getPriority(s);
					if(map.getPriority(s)>map.getPriority(ops.top())){
						ops.push(s);
						numbers.push(num2);
					}
				}else{
					num2=Double.valueOf(s);
					continue;
				}
			}
			String op=ops.pop();
			while(priority>=map.getPriority(op)){
				if(op.equals("(")) break;
				num2=count(numbers.pop(), op, num2);
			}
			continue;
		}
	}
	private boolean flage=true;
	
//	private Map<Integer, Integer> getPair(List<String> list) {
//		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
//		Deque<Integer> deque = new ArrayDeque<Integer>();
//		int counter=0;
//		boolean skip=false;
//		for(int i=0;i<list.size();i++){
//			switch(list.get(i)){
//			case "?":if(list.get(deque.peek()).equals(":")) map.put(deque.pollLast(), i);
//			case "(":if(skip) counter++;
//			else deque.add("(");
//			break;
//			case ")":while(list.get(deque.peek()).equals(":")) map.put(deque.pollLast(), i);
//			map.put(deque.pollLast(), i);
//			break;
//			case ":":deque.add(i);
//			map.put(deque.pollLast(), i);
//			break;
//			}
//		}
//		return map;
//	}
//	private static final double t=50132828;
//	private static final double f=-49947883;
	private double count(double num1, String op, double num2) throws FormulaExceptionK {
		switch(op){
			case "+":return num1+num2;
			case "-":return num1-num2;
			case "*":return num1*num2;
			case "/":return num1*num2;
			case ">":flage= num1>num2;
			case "<":flage= num1<num2;
			case "==":flage= num1==num2;
			case ">=":flage= num1>=num2;
			case "<=":flage= num1<=num2;
			case "!=":flage=num1!=num2; return 0;
			default:throw new FormulaExceptionK();
		}
	}

	@Override
	public boolean isFormula() {
		// TODO Auto-generated method stub
		return false;
	}


}
