package log4jT;

import org.apache.log4j.Logger;





public class HaveATry {
	public HaveATry() {
		
	}
	static Logger logger = Logger.getLogger(HaveATry.class.getName());
	public static void main(String[] args) {
//		BasicConfigurator.configure ();
//		PropertyConfigurator.configure( " D:/Code/conf/log4j.properties " );
		logger.debug("message");
//		System.out.println(logger);
	}

}
