package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ReplaceIDWithLabelMain {

	public static void main(String[] inputs) throws FileNotFoundException, IOException {
//		if(inputs.length == 1)
//		{
//			throw new IllegalArgumentException("No arguments passed. Two file path needed");
//		}
		
		File idWithLabelFile 	= new File(inputs[0]);
		File fileToChange 		= new File(inputs[1]);
		
		
		String separator = ",";
		HashMap<String, String> idToLabelMap 	= FileHandeler.getLabelsToLineMap(idWithLabelFile, separator);
		ArrayList<String> 		linesInFile 	= FileHandeler.getLinesInFile(fileToChange);
		
		for(String key : idToLabelMap.keySet()){
			for(int j = 0; j < linesInFile.size(); j++){
				System.out.println(key + ":::::::::::::::::" + linesInFile);
				String newLine = "";
				String[] enttytiesInLine = linesInFile.get(j).split("|"); //I use | to separate different entity id in the same property
				for(int i = 0; i < enttytiesInLine.length; i++){
					enttytiesInLine[i] = enttytiesInLine[i].replaceAll(key, idToLabelMap.get(key));
					newLine += enttytiesInLine[i];
				}
				
				linesInFile.set(j, newLine);
			}
		}
		
		System.out.println("==========================================================================================");
		System.out.println("==========================================================================================");
		System.out.println("==========================================================================================");
		for(String line : linesInFile){
			System.out.println(line);
		}
		
		
	}

}
