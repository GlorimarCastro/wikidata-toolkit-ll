package entityDocumentProcessors;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.examples.ExampleHelpers;
import org.wikidata.wdtk.examples.JsonSerializationInstanceOfProcessor;

import Utilities.FileHandeler;
import Interfaces.EntityDocumentProcessorExtended;

public class DocumentProcessorLauncher {
	//Dumpfiles parent directory path. 
	//It is important that the json dump file to be use is saved in a directory with the next format:+
	//		[dumpFilesParentDirectoryPath]\dumpfiles\[wikimedia project name]\json-[date]
	//Where:
	//	wikimedia project 		-	is the wiki project from where the dump file were downloaded (e.g wikidatawiki)
	//	json-date				-	is the json dump file name, which fallow the next format json-[date], where the date have to be YYYYMMDD (e.g json-20150608)
	static final String dumpFileDirectory = "H:\\EclipseWorkspace\\RolesAnnotation\\wdtk-parent\\wdtk-examples"; 
	
	//Results file name
	static final String OUTPUT_FILE_NAME = "json-wikidata-allpeople.json.gz";
	
	//Project name
	static final String PROJECT_NAME = "wikidatawiki";
	
	
	
	//***************************************************************
	//							MAIN METHOD
	//***************************************************************
	public static void main(String[] input) throws IOException{
		
		//Set Logger configuration.
		ExampleHelpers.configureLogging();
		Scanner scanner = new Scanner(System.in);
		
		//Print basic documentation in the console
		//printDocumentation();
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Choose document processor to use:");
		System.out.println("1. EntityDescriptionDocumentProcessor");
		System.out.println("2. EntityIdToStringDocumentProcessor");
		System.out.print("enter option number: ");
		int documentProcessortouse = scanner.nextInt();
		scanner.nextLine();
		
		EntityDocumentProcessorExtended documentProcessor = null;
		
		switch(documentProcessortouse){
			case 1:								//entityDescriptionDcumentProcessor
				System.out.println("-----------EntityDescriptionDocumentProcessor-----------");
				System.out.print("Did you pass file arguments in the jar call? (true or false):");
				boolean isArgumentPass = scanner.nextBoolean();
				if(isArgumentPass){
					if(input.length != 2){
						System.out.println("To use EntityDescriptionDocumentProcessor you need to pass two arguments:");
						System.out.println("1. file to save the result\n2. file with the names to annotate");
						return;
					}
					documentProcessor = new EntityDescriptionDocumentProcessor(input[1]);
					ExampleHelpers.processEntitiesFromWikidataDump(documentProcessor);
					FileHandeler.writeListToFile(documentProcessor.getCSVListResultForAllDataInFile(), new File(input[0]));
					
				}else{
					System.out.println("File path where the result are going to be saved: ");
					File outputFile = new File(scanner.nextLine());
					System.out.println("File with names to annotate: ");
					documentProcessor = new EntityDescriptionDocumentProcessor(scanner.nextLine());
					ExampleHelpers.processEntitiesFromWikidataDump(documentProcessor);
					FileHandeler.writeListToFile(documentProcessor.getCSVListResultForAllDataInFile(), outputFile);
				}

				break;
				
			case 2:								//entityIdtoStringDocumentProcessor
				documentProcessor = new EntityIdToStringDocumentProcessor();
				ExampleHelpers.processEntitiesFromWikidataDump(documentProcessor);
				FileHandeler.writeListToFile(documentProcessor.getCSVListResult(), new File(input[0]));
				break;
			default:
				break;
		}
		 
		
		

		

		

		//printcompletedMessage();
		
		
	}
	
	
	
	
	/**
	 * Prints some basic documentation about this program.
	 */
//	public static void printDocumentation() {
//		System.out
//				.println("********************************************************************");
//		System.out.println("*** Wikidata Toolkit: GetAllPersons");
//		System.out.println("*** ");
//		System.out
//				.println("*** This program will extract all persons from a local dumps from Wikidata.");
//		System.out
//				.println("*** It will filter the data and store the results in a new JSON file.");
//		System.out.println("*** See source code for further details.");
//		System.out
//				.println("********************************************************************");
//	}
//	
//	/**
//	 * Prints completed message.
//	 */
//	public static void printcompletedMessage() {
//		System.out
//				.println("\n\n********************************************************************");
//		System.out.println("*** Wikidata Toolkit: GetAllPersons");
//		System.out.println("*** ");
//		System.out
//				.println("*** Process completed");
//		System.out.println("*** ");
//		System.out
//				.println("********************************************************************");
//	}
	

	

}