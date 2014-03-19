package calculaterK;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Test {
	private static Log log = LogFactory.getLog("Qbit");

	public static void main(String[] args) {
		new Test().test();

	}

	private void test() {
		Map<String,String> formulaKM=new HashMap<String,String>();
		Map<String,Object> parameterKM = new HashMap<String,Object>();
		List<String> resultKL=new ArrayList<String>();
		Map<String,BigDecimal> resultKM = new HashMap<String,BigDecimal>();
		formulaKM.put("A", "a+b+c+d+e");
		formulaKM.put("B", "a*b");
		formulaKM.put("C", "a/b");
		formulaKM.put("D", "a+1");
		formulaKM.put("E", "B+F");
		formulaKM.put("F", "1+1");
		parameterKM.put("a", 1);
		parameterKM.put("b", "2");
		parameterKM.put("c", new Integer(3));
		parameterKM.put("d", 1.1);
		parameterKM.put("e", new BigDecimal(9.99));
		resultKL.add("A");
		resultKL.add("B");
		resultKL.add("C");
		resultKL.add("D");
		resultKL.add("E");
		resultKL.add("F");
		MultiCalculaterK mck = MultiCalculaterK.getInstance(formulaKM, parameterKM, resultKL, resultKM);
		log.info(mck);
		ExecutorService es = Executors.newCachedThreadPool();
		Future<Map<String,BigDecimal>> f = es.submit(mck);
		if(f.isDone()){
			try {
				log.info(f.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
