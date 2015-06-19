package Utilities;

public class InstanseOfUtility {
	
	
    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        try
        {
           Integer.parseInt(s);
   
           // s is a valid integer
   
           isValidInteger = true;
        }
        catch (NumberFormatException ex)
        {
           // s is not an integer
        }
   
        return isValidInteger;
     }
    
    public static boolean isBoolean(String s) {
        boolean isValidBoolean = false;
        try
        {
        	Boolean.parseBoolean(s);
        	isValidBoolean = true;
        }
        catch (Exception ex)
        {
           // s is not an integer
        }
   
        return isValidBoolean;
     }
    
}
