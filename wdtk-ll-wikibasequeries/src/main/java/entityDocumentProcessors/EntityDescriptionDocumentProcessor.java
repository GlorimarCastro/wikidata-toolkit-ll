package entityDocumentProcessors;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.wikidata.wdkt.enums.Language;
import org.wikidata.wdkt.enums.Property;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;

import Interfaces.EntityDocumentProcessorExtended;
import Utilities.GlobalVariables;

/**
 *Given a list of people names it save in a file the name and description of the person.
 * It assign a null description if the entity were not found. The method setEntityInformationMap is the one
 * that is going to set all the properties to look for. If this method is changed the results array list header should be changed as well.
 * @author GL26163
 */
public class EntityDescriptionDocumentProcessor implements EntityDocumentProcessorExtended{

	//===========================================================================================================================
	//=================FILEDS=======================FIELDS=======================FIELDS=================================FIELDS===
	//===========================================================================================================================	
	private File inputFile = null;
	/**
	 * This map contain the information in the file. The keys are the first row of the csv or txt file,
	 *  and the value is the complite line.
	 */
	private HashMap<String, String> fileDataMap = null;
	
	/**
	 * This array list is going to contain string with the names and descriptions for
	 * the desired entities. Is different from fileDataMap to preserve the cases
	 * with ambiguities.
	 */
	private ArrayList<String> results = null;
	
	private  HashMap<String, String> entityInformattionMap = null;
	private ArrayList<String> enttitiesSet = null;
	private final String ENTITY_ID = "entityid";
	
	//public static String resultFilename = "roleAnnotation.csv";
	
	
	
	//===========================================================================================================================
	//==================================CONSTRUCTOR====================================CONSTRUCTOR===============================
	//===========================================================================================================================
	public EntityDescriptionDocumentProcessor(String inputFile) throws FileNotFoundException, IOException {
		if(inputFile == null){
			throw new IllegalArgumentException("Input file cannot be null.");
		}
		
		this.inputFile 				= new File(inputFile);
		fileDataMap 			= getLabelsToLineMap(this.inputFile);
		results					= new ArrayList<String>();
		enttitiesSet			= new ArrayList<String>();
		entityInformattionMap 	= new HashMap<String, String>();
		results.add("");
    	
		
		setEntityInformationMap();
	}
	
	
	
	//===========================================================================================================================
	//==================================METHODS=======================================METHODS====================================
	//===========================================================================================================================	
	/**
	 * This method received a item from the wiki dump file and look for it in the 
	 * input file with the names. If the entity is mention in the input file 
	 * then extract the properties that were set on the entityInformattionMap
	 * hash map. This method is not going to resolve any ambiguity problem. 
	 * If a name in the input file refer to more than one entity, this
	 * method is going to include all of them in the result. 
	 */
    public void processItemDocument(ItemDocument itemDocument) {
    	Map<String, MonolingualTextValue> labelsMap = itemDocument.getLabels();
    	//iterate trought all names for the entity, in the different languages.
    	//s is the language key
    	for(String s : labelsMap.keySet()){
    		//Get entity name for the language s
    		String name = labelsMap.get(s).getText().toUpperCase();
    		
    		//If the entity is in the input file then process it
    		if(fileDataMap.containsKey(name)){
    			enttitiesSet.add(name);
    			//add entity name
    			entityInformattionMap.put("name", name);
    			System.out.println(name);
    			
    			//add entity id
    			entityInformattionMap.put(ENTITY_ID, itemDocument.getEntityId() + "");
    			
    			//Add english description
    			if(itemDocument.getDescriptions().containsKey(Language.ENGLISH.toString())){
    				entityInformattionMap.put("description", itemDocument.getDescriptions().get(Language.ENGLISH.toString()).getText());
    			}else{
    				//busca algun idioma
    				if(!itemDocument.getDescriptions().isEmpty()){
    					for(String language : itemDocument.getDescriptions().keySet()){
        					entityInformattionMap.put("description", itemDocument.getDescriptions().get(language).getText());
        				}
        				
    				}//else description is ""
    				
    			}
    			
    			
    			/**
    			 * A StatementGroup contains all the statement that are part of 
    			 * the same property for the entity being analyze. E.g. Property Member of
    			 * can contains different statement for each organization that the entity
    			 * is member of.
    			 * 
    			 */
    			//Add properties value
    			for(StatementGroup sg : itemDocument.getStatementGroups()){
    				//ignore all the properties statements that are not desired 
    	    	   if(!entityInformattionMap.containsKey(sg.getProperty().getId())){
    	    		   continue;
    	    	   }
    	    	   
    	    	   String temp = ""; //para cuando hay mas de una descripcion
    	    	   for(Statement s1 : sg.getStatements()){
    	    		   //Make sure that the main snak is in the format property-value
    	    		   if(s1.getClaim().getMainSnak() instanceof ValueSnak){
    	    			   ValueSnak snak = (ValueSnak) s1.getClaim().getMainSnak();
    	    			   Value v = snak.getValue();
    	    			   
    	    			   if (v instanceof ItemIdValue) {
    							temp+= ((ItemIdValue) v).getId() + GlobalVariables.propertiesDelimiter;
    						}
    	    		   }
    	    	   }
    	    	   entityInformattionMap.put(sg.getProperty().getId(), temp);
    	    	   
    			}
    			
    			addMapToResultList();
    			setEntityInformationMap();
    			
    		
    			break; //ignore other language 
    		}//else silently ignore it
    	
    	}
    	
    	
    	
    }

    public void processPropertyDocument(PropertyDocument propertyDocument) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void clearHashMapValues(HashMap<Object, Object> map){
    	for(Object obj : map.keySet()){
    		map.put(obj, null);
    	}
    }
    
    public static HashMap<String, String> getLabelsToLineMap(File input) throws FileNotFoundException, IOException{
        final String SEPARATOR = ",";
        HashMap<String, String> result = new HashMap();
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        
        while((line = reader.readLine()) != null){
            int     commaIndx = line.contains(SEPARATOR) ? line.indexOf(SEPARATOR) : line.length();
            String  nodeLabel = line.substring(0, commaIndx).trim().toUpperCase();
            result.put(nodeLabel, line);
        }
        
        reader.close();
        return result;       
    }

    public File getInputFile(){
    	return inputFile;
    }
    
    /**
     * This method make sure that all the entity have their values in the same order (e.g name, description, ocuppation)
     * All the "," are replaces with "|" to maintein the csv order.
     */
    private void addMapToResultList(){
    	String line = entityInformattionMap.get("name") + "," + 
    				entityInformattionMap.get("description").replaceAll(",", GlobalVariables.propertiesDelimiter) + "," +
    				entityInformattionMap.get(Property.OCCUPATION.toString()).replaceAll(",", GlobalVariables.propertiesDelimiter) + "," +
    				entityInformattionMap.get(Property.MEMBER_OF.toString()).replaceAll(",", GlobalVariables.propertiesDelimiter) + "," +
    				entityInformattionMap.get(Property.RELIGION.toString()).replaceAll(",", GlobalVariables.propertiesDelimiter);
    	results.add(line);
    }
    
    /**
     * Set the entity information map. All the properties to be extracted, from the 
     * wikidata dump file, need to be included in this map 
     */
    private void setEntityInformationMap(){
    	results.set(0, "Name,Description,Ocupation Wikidata Ids, Member of, Religion");
    	String defaultStr = "";
    	entityInformattionMap.put("name",  defaultStr);
    	entityInformattionMap.put(ENTITY_ID, defaultStr);
    	entityInformattionMap.put("description", defaultStr);
    	entityInformattionMap.put(Property.MEMBER_OF.toString(), defaultStr);
    	entityInformattionMap.put(Property.RELIGION.toString(), defaultStr);
    	entityInformattionMap.put(Property.OCCUPATION.toString(), defaultStr);
    }
    
    public ArrayList<String> getCSVListResult(){
    	return results;
    }
    
    /**
     * Return all the data in file annotates, if a entity were not found then returned it too with a not found
     * message in the description
     * @return
     */
    public ArrayList<String> getCSVListResultForAllDataInFile(){
    	for(String s : enttitiesSet){
    		fileDataMap.remove(s);
    	}
    	//all the elements still in the fileDataMap were not found
    	for(String name : fileDataMap.keySet()){
    		String line = name + "," + 
    				"not found" + ",,,,";
    	results.add(line);
    	}
    	return results;
    }
    
//    private String setFileLineData(String line){
//    	String result;
//    	
//    	return result;
//    }
}