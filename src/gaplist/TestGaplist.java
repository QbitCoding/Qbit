package gaplist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.magicwerk.brownies.collections.GapList;

public class TestGaplist {
	private static TestGaplist tester = new TestGaplist();
	private static Log log = LogFactory.getLog("Qbit");
	private static int[] test = new int[100];
	public static void main(String[] args) {
		for (int i = 0; i < 9; i++) {
			log.info(""+i+i+i+i+i+i+i+i+i+i+i+i+i+i+i+i+i+i+i);
			tester.test1();
		}
	}
	private void test1() {
		List<String> arraylist = new ArrayList<String>();
		Random random = new Random();
		for(int i=0;i<8*1024*1024;i++){
			arraylist.add(""+random.nextInt());
		}
		for(int i=0;i<test.length;i++){
			test[i]=random.nextInt(8*1024*1024);
			if(test[i]<0) test[i] = -test[i];
		}
		List<String> gaplist = new GapList<>(arraylist);
		List<String> linkedlist = new LinkedList<String>(arraylist);
		
		log.info("arraylist:");
		tester.test(arraylist);
		log.info("linkerlist:");
		tester.test(linkedlist);
		log.info("gaplist:");
		tester.test(gaplist);
	}
	private void test(List<String> list){
		long start = System.currentTimeMillis();
		for(int i=0;i<test.length;i++){
			list.add(test[i], ""+i);
		}
		long end =System.currentTimeMillis();
		log.info("add:"+(end-start));
		start = System.currentTimeMillis();
		for(int i=0;i<test.length;i++){
			list.add(test[0], ""+test[i]);
		}
		end =System.currentTimeMillis();
		log.info("addrepet:"+(end-start));
		start = System.currentTimeMillis();
		for(int i=0;i<test.length;i++){
			list.get(test[i]);
		}
		end =System.currentTimeMillis();
		log.info("get:"+(end-start));
		start = System.currentTimeMillis();
		for(int i=0;i<test.length;i++){
			list.remove(test[i]);
		}
		end =System.currentTimeMillis();
		log.info("remove:"+(end-start));
		log.info("--------------------------");
	}

}
