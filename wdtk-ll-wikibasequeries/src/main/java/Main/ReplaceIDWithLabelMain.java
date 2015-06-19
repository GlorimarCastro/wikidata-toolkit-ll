package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utilities.FileHandeler;
import Utilities.GlobalVariables;

public class ReplaceIDWithLabelMain {

	public static void main(String[] inputs) throws FileNotFoundException, IOException {
		if(inputs.length != 3)
		{
			System.out.println("Three arguments needed:");
			System.out.println("1. File with id and labels");
			System.out.println("2. File to modify");
			System.out.println("File to save results.");
			return;
		}
		
		Pattern p = Pattern.compile("([QP]{1}\\d+)\\" + GlobalVariables.propertiesDelimiter);
		Matcher m;
		
		
		File idWithLabelFile 	= new File(inputs[0]);
		File fileToChange 		= new File(inputs[1]);
		File resultfile 		= new File(inputs[2]);
		
		String separator = ",";
		System.out.println("Getting id and labels information");
		HashMap<String, String> idToLabelMap 	= FileHandeler.getLabelToValueMap(idWithLabelFile, separator);
		System.out.println("Getting data to modify");
		ArrayList<String> 		linesInFile 	= FileHandeler.getLinesInFile(fileToChange);
		
		System.out.println("Modifying data");
		
		for(int i = 0; i < linesInFile.size(); i++){
			String lineInFile = linesInFile.get(i);
			String newLine = lineInFile;		//to preserve position
			m = p.matcher(lineInFile);
			while(m.find()) {
		           String wikiId = m.group(1);
		           System.out.println(wikiId);
		           if(idToLabelMap.containsKey(wikiId)){
		        	   newLine = newLine.replaceAll(wikiId, idToLabelMap.get(wikiId).replaceAll(",", " "));
		           }
		     }
			System.out.println(newLine.toString());
			linesInFile.set(i, newLine.toString());
		}
		
		
		
		/*
		 * Read each line
		 * for each line
		 * 	fields[] = split(line, ",")
		 *  field = fields[2];
		 *  ids = split(field, "|")
		 *  List<String> allLines;
		 *  StringBuilder lineToWrite = new StringBuilder(fields[0]).append(",").append( .....)
		 *  for (String id: ids)
		 *  {
		 *    String label = map.get(id);
		 *    if(label == null)
		 *    { label = "no English label"; }
		 *    lineToWrite.append(label);
		 *  }
		 *  lineToWrite.append(suffix);
		 *  allLines.add(lineToWrite);
		 * */
		 
		
//		for(String key : idToLabelMap.keySet()){
//			for(int j = 0; j < linesInFile.size(); j++){
//				System.out.println(key + ":::::::::::::::::" + linesInFile);
//				String newLine = "";
//				String[] enttytiesInLine = linesInFile.get(j).split("|"); //I use | to separate different entity id in the same property
//				for(int i = 0; i < enttytiesInLine.length; i++){
//					enttytiesInLine[i] = enttytiesInLine[i].replaceAll(key, idToLabelMap.get(key));
//					newLine += enttytiesInLine[i];
//				}
//				
//				linesInFile.set(j, newLine);
//			}
//		}
		
		System.out.println("==========================================================================================");
		System.out.println("==========================================================================================");
		System.out.println("==========================================================================================");
		FileHandeler.writeListToFile(linesInFile, resultfile);
		
	}

}
