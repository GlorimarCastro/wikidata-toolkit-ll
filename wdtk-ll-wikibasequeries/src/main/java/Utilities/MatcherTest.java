package Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherTest {

	public static void main(String[] args) {
		String testS = "Q5632|Q12345|";
		String test2 = "eQP632|" + testS;
		//\\d+" + GlobalVariables.propertiesDelimiter + "{1}")
		Pattern p = Pattern.compile("([QP]{1}\\d+)\\|");
		Matcher m = p.matcher(test2);
		
		System.out.println(test2.replaceAll("Q5632", "hola"));
		//System.out.println(m.find());
		int count = 0;
		while(m.find()) {
	           count++;
	           String temp = test2.substring(m.start(), m.end());
	           String myMatch = m.group(1);
	           System.out.println("HERE: " + myMatch);
	           System.out.println(temp);
	           System.out.println("Match number "
	                              + count);
	           System.out.println("start(): "
	                              + m.start());
	           System.out.println("end(): "
	                              + m.end());
	      }
		
		//System.out.println(m.find());

	}

}
