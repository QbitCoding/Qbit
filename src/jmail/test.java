package jmail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class test {

	public static void main(String[] args) {
		SimpleEmail email = new SimpleEmail();
	     email.setHostName("smtp.163.com");//设置使用发电子邮件的邮件服务器
	     try {
	       email.addTo("499478836@qq.com");
	       email.setAuthentication("idinternet@163.com","");
	       email.setFrom("idinternet@163.com");
	       email.setSubject("Test apache.commons.mail message");
	       email.setMsg("This is a simple test of commons-email");
	       email.send();
	     }
	     catch (EmailException ex) {
	       ex.printStackTrace();
	     }
	   }
}
