package Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import Utilities.FileHandeler;
import Utilities.InstanseOfUtility;
import Utilities.NumberUtility;

public class TestAndTrainingDataDivider {

	public static void main(String[] args) throws IOException {
		
		printDocumentation();
		if(args.length != 4){
			System.out.println("This program need you to pass the next argument(s)");
			System.out.println("1. Input file with the data to be divided.");
			System.out.println("2. Number of test and training partitions");
			System.out.println("3. Percentage of test data to be extracted for each partition");
			System.out.println("4. Column in file with the role annotation");
			return;
		}
		File 	inputFile		= new File(args[0]);
		String 	inputFileName 	= inputFile.getName().substring(0, inputFile.getName().indexOf("."));
		File 	resultDirectory = new File(System.getProperty("user.dir") + "/Test_Training_Data" );
		File 	testDirectory;
		File 	trainingDirectory;
		File[] 	testDirectories;
		File[]	trainingDirectories;
		File[]	featuresFiles;
		String	fileExtension	= ".csv";
		int 	numberOfPartitions;
		int 	testPercentage;
		int		column; 			
		
		
		
		if(InstanseOfUtility.isInteger(args[1]) && InstanseOfUtility.isInteger(args[2])){
			numberOfPartitions 	= Integer.parseInt(args[1]);
			testPercentage 		= Integer.parseInt(args[2]);
			column				= Integer.parseInt(args[3]);
			
		}else{
			System.err.println("Second and third argument have to be numbers");
			return;
		}
		
		
		testDirectories 	= new File[numberOfPartitions];
		trainingDirectories = new File[numberOfPartitions];
		
		//create needed directories
		resultDirectory.mkdir();
		System.out.println("Directory Test_Training_Data in: " + resultDirectory.getPath());
		testDirectory 		= new File(resultDirectory.getPath() + "/Test");
		trainingDirectory 	= new File(resultDirectory.getPath() + "/Training");
		testDirectory.mkdir();
		trainingDirectory.mkdir();
		System.out.println("Test directory in: " + testDirectory.getPath());
		System.out.println("Training directory in: " + trainingDirectory.getPath());
		
	
		
		//Upload to memory the input file
		ArrayList<String> 	fileDataList = FileHandeler.getLinesInFile(inputFile);
		double 				linesForTest = fileDataList.size() - 1 * (testPercentage/100.0); //se descuenta el header
		String[]			fields		 = fileDataList.get(0).split(",");
		System.out.println("Each test file has going to have " + linesForTest + " lines from the input file.");
		
		//Loop for wanted partitions
		for(int i = 0; i < numberOfPartitions; i++){
			System.out.println("Working on partition number: " + i);
			
			//Extracting test and training data
			int amntOfLinesExtractedForTest = (int)linesForTest;
			ArrayList<String> trainingData	= new ArrayList<String>(fileDataList);  //a copy of the original data
			ArrayList<String> testData 		= new ArrayList<String>();
			
			trainingData.remove(0); //remove header
			while(amntOfLinesExtractedForTest > 0){
				int start 	= NumberUtility.getRandomInt(0, 	trainingData.size()); 
				int end = (start + amntOfLinesExtractedForTest < trainingData.size())? start + amntOfLinesExtractedForTest: trainingData.size();
				end		= NumberUtility.getRandomInt(start, end);			//start + lines for test se asegura de que de primera instancia no extraiga mas de lo pedido
				for(int j = start; j < end; j++){
					testData.add(trainingData.get(start));
					System.out.println(trainingData.remove(start));
				}
				
				amntOfLinesExtractedForTest -= end-start;
			}
			
			//extracting features 
			ArrayList<String>[] testFeatures 	 = getFeautureSet(fields.length);
			ArrayList<String>[] trainingFeatures = getFeautureSet(fields.length);
			
			for(String s : trainingData){
				System.out.println(s);
				String[] featureValue = s.split(",");
				for(int j = 0; j <fields.length; j++){
					trainingFeatures[j].add(featureValue[column] + " " + featureValue[j]);
				}
			}
			
//			for(String s : testData){
//				
//			}
			//Saving the result
			testDirectories[i] = new File(testDirectory.getAbsolutePath() + "/" + inputFileName + "TestData" + i); 
			trainingDirectories[i] = new File(trainingDirectory.getAbsolutePath() + "/" + inputFileName + "TrainingData" + i);
			testDirectories[i].mkdir();
			trainingDirectories[i].mkdir();
			
			//Write result
			for(int j = 0; j < fields.length; j++){
				File resultFile = new File(trainingDirectories[i].getPath() + "/" + fields[j]+ fileExtension);
				resultFile.createNewFile();
				FileHandeler.writeListToFile(trainingFeatures[j], resultFile );
			}
			
			
			//FileHandeler.writeListToFile(trainingData, trainingDirectories[i]);
			
		}
		

		
		endMessage();
		
	}
	
	/**
	 * This hash set is going to contain the information for each feature in a separate
	 * ArrayList, the order is going to be the same as in the input file
	 * @param size
	 * @return
	 */
	public static ArrayList<String>[] getFeautureSet(int size){
		String[] h = new String[54];
		ArrayList<String>[] result = new ArrayList[size];
		for(int i = 0; i <size; i++){
			result[i] = new ArrayList<String>();
		}
		
		return result;
	}
	
	
	/**
	 * 
	 */
	public static void printDocumentation(){
		System.out.println("===============================================================");
		System.out.println("==================TestAndTrainingDataDivider===================");
		System.out.println("===============================================================");
		System.out.println("This program is specific to divede a csv with graph metric \nvalues into a subset of training and testing data sets "
				+ "\nspecific for the role clissifier");
		System.out.println();
	}
	
	public static void endMessage(){
		System.out.println("===============================================================");
		System.out.println("======TestAndTrainingDataDivider		Finish  ===============");
		System.out.println("===============================================================");		
	}

}
