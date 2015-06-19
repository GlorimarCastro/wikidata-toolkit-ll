package Utilities;

import java.util.Random;

public class NumberUtility {
	
	public static int getRandomInt(int from, int to){
		Random rand = new Random();
		return rand.nextInt(to - from + 1) + from;
	}
	
}
