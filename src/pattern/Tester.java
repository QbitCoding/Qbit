package pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pattern p =Pattern.compile("[a-z]+(\\d+)");
		String s="1q2we456ty789iu65";
		Matcher m = p.matcher(s);
		while(m.find()){
			System.out.println(m.group(1));
			System.out.println(m.group());
			System.out.println(m.group(1));
			System.out.println(m.group());
			System.out.println("-------");
		}
	}

}
