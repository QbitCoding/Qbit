package log4jT;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class LogAndLogger {
	static Logger logger = Logger.getLogger("logger");
	static Log log = LogFactory.getLog("log");

	public static void main(String[] args) {
		logger.debug("i am logger");
		log.debug("i am log");
		System.out.println("i am syso");
		Log newlog = LogFactory.getLog("Qbit");
		newlog.debug("i am qbit");
		Logger newlogger = Logger.getLogger("Qbit");
		newlogger.debug("i'm newlogger in qbit");

	}

}
