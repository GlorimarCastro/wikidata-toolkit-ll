package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Utilities.FileHandeler;

/**
 * This class receives a file with names of people and check if the 
 * person meet a specific requirements. If true, then save the information 
 * of the person to another file. The data to be saved in the resulting 
 * file is the same as the one in the inputFile
 * @author GL26163
 *
 */
public class ExtractEntitiesFromFile {

	public static void main(String[] inputs) throws FileNotFoundException, IOException{
		
		if(inputs.length != 3){
			System.out.println("Not enough arguments. This program need a total of 3 arguments");
			System.out.println("1. File with the dataBase (csv). The name of the person have to be in the first column");
			System.out.println("2. Path to file with the entities to verify (csv)");
			System.out.println("3. Path to file were the results are going to be saved.");
			return;
		}
		
		int					nameColumn		= 0;
		String				separator		= ",";
		File 				dataBaseFile 	= new File(inputs[0]);
		BufferedReader 		reader 			= new BufferedReader(new FileReader(new File(inputs[1])));
		BufferedWriter		writer			= new BufferedWriter(new FileWriter(new File(inputs[2])));
		HashSet<String>		keywordToLook 	= new HashSet<String>();
		ArrayList<String>	peopleIgnored	= new ArrayList<String>();
		
		
		//for general sport:
		//keywordToLook.add("player");
		//keywordToLook.add("sport");
		
		//for hockey
		//keywordToLook.add("ice hockey");
		//keywordToLook.add("businessman");
		//keywordToLook.add("businessperson");
		
		//for politician
		keywordToLook.add("politician");
		keywordToLook.add("diplomat");
		keywordToLook.add("governor");
		
		
		//get the data base information to memory. This map suppose to be Name of person --> other information in file
		HashMap<String, String> dataBase = FileHandeler.getLabelsToLineMap(dataBaseFile, separator);
		
		String line = null;
		//iterate through all the people in the file
		while((line = reader.readLine()) != null){
			String[] columns = line.split(separator);
			if(columns.length > 0){
				String name = columns[nameColumn].toUpperCase();
				if(dataBase.containsKey(name)){ //check if there is any information available for the person being analyze 
					boolean meetReq = false;
					System.out.println("encontro dentro");
					for(String s : keywordToLook){ //check if the person meet the requirements
						if(dataBase.get(name).contains(s)){
							System.out.println("es politico");
							writer.write(line);
							writer.newLine();
							meetReq = true;
							break;
						}
					}
					if(!meetReq){
						peopleIgnored.add(name);
					}
				}else{
					peopleIgnored.add(name);
					continue;
				}
			}
			
		}
		
		
		reader.close();
		writer.close();
		
//		System.out.println("Ignored People:");
//		for(String name : peopleIgnored){
//			System.out.println(name);
//		}
		System.out.println("Finished");
		
	}
}
