package Utilities;

public class GlobalVariables {
	/**
	 * This variable is to specify when a property wiki id end. 
	 * This value is introduced because for making query in non 
	 * json format queries can have overlaping stating value e.g.
	 * id Q903 and id Q90356, both have Q903. 
	 */
	public static final String propertiesDelimiter = "|";
	
	
}
