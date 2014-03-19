package pattern;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class Oro {

	public static void main(String[] args) throws MalformedPatternException {
		PatternCompiler compiler=new Perl5Compiler(); 
		Pattern p = compiler.compile("(?:(?:[1-9]{1}[0-9]*)||0){1}(?:\\.[0-9]+)?");
//		Pattern p = compiler.compile("(?:[1-9]{1}[0-9]*)||0");
		PatternMatcher matcher=new Perl5Matcher(); 
		boolean b=matcher.contains("a0+1.1+b.1", p);
		System.out.println(b);
	}

}
