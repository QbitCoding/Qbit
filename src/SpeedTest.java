import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpeedTest {
	private static Log log = LogFactory.getLog("Qbit");
	public static void main(String[] args) {
		SpeedTest st = new SpeedTest();
		int num = 123456;
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			num = r.nextInt();
			st.testInt(num);
			st.testLong(num);
			st.testDouble(num);
			st.testInt(num);
			log.info("----------"+i+"------"+num);
		}
	}

	private void testDouble(int num0) {
		double num = num0;
		long timer = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			num = num * 3445 / 2 - 8 + num*num / 555%7;
		}
		timer = System.currentTimeMillis() - timer;
		log.info("double:" + timer);
	}

	private void testLong(int num0) {
		long num = num0;
		long timer = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			num = num * 3445 / 2 - 8 + num*num / 555%7;
		}
		timer = System.currentTimeMillis() - timer;
		log.info("long:" + timer);
	}

	int time =100000;

	private void testInt(int num0) {
		int num = num0;
		long timer = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			num = num * 3445 / 2 - 8 + num*num / 555%7;
		}
		timer = System.currentTimeMillis() - timer;
		log.info("int:" + timer);
	}

}
