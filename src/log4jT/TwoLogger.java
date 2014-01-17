package log4jT;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TwoLogger {
	public static void main(String[] args) {
		Log qbit = LogFactory.getLog("Qbit");
		Log kamputer = LogFactory.getLog("Kamputer");
		qbit.debug("i am qibt");
		kamputer.debug("i am kamputer");
		

	}

}
