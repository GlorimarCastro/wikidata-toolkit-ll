package entityDocumentProcessors;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.wikidata.wdkt.enums.Language;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;

/**
 * This Document Processor is to create a connection between entities, 
 * or properties, ids and their labels (its String representation). The next fields are
 * used:
 * 		
 * @author GL26163
 *
 */
public class EntityIdToStringDocumentProcessor implements EntityDocumentProcessor{

	/**
	 * HashMap with the result. the keys are the String (e.g. name)
	 * and the values are the entity id
	 */
	private HashMap<String, String> labelToEntityIdMap;

	
	/**
	 * HashMap with the result. The keys are the wiki Entity Id and
	 * the value is the label corresponding to that entity
	 */
	private HashMap<String, String> idToLabelMap;

	//=============================================================================================
	//=====CONSTRUCTOR====================CONSTRUCTOR===========================CONSTRUCTOR========
	//=============================================================================================
	public EntityIdToStringDocumentProcessor(){
		setFields();
	}

	
	//=============================================================================================
	//=====PUBLIC METHODS==================PUBLIC METHODS=====================PUBLIC METHODS=======
	//=============================================================================================
	/**
	 * For each ItemDocument extract the Entity Id and 
	 * the label corresponding to it
	 */
	public void processItemDocument(ItemDocument itemDocument) {
		String entityId = itemDocument.getEntityId().getId();
		
		Map<String, MonolingualTextValue> lablesMap = itemDocument.getLabels();
		String label  = "no label";
		//Check if the property have a label in English
		if(lablesMap.containsKey(Language.ENGLISH.toString())){
			label = lablesMap.get(Language.ENGLISH.toString()).getText();
		}else{//if not then try to find the label in another language
			
			for(String  s : lablesMap.keySet()){
				if(lablesMap.get(s) != null){
					//label = lablesMap.get(s).getText();
					label = "no English label";
					break;
				}
				
			}
			
		}
		
		addToMaps(entityId, label);
		System.out.println(entityId + ":" + label);
		
	}

	/**
	 * For each PropertyDocument extract the Property Id 
	 * and its corresponding label
	 */
	public void processPropertyDocument(PropertyDocument propertyDocument) {
		String propertyId = propertyDocument.getEntityId().getId();
		
		Map<String, MonolingualTextValue> lablesMap = propertyDocument.getLabels();
		String label  = "no label";
		//Check if the property have a label in English
		if(lablesMap.containsKey(Language.ENGLISH.toString())){
			label = lablesMap.get(Language.ENGLISH.toString()).getText();
		}else{//if not then try to find the label in another language
			for(String  s : lablesMap.keySet()){
				if(lablesMap.get(s)!= null){
					label = lablesMap.get(s).getText();
					break;
				}
			}
		}
		
		addToMaps(propertyId, label);
		System.out.println(propertyId + ":" + label);
		
	}



	public ArrayList<String> getCSVListResult(){
		ArrayList<String> result = new ArrayList<String>();
		result.add("ID,Label");
		for(String key : idToLabelMap.keySet()){
			result.add(key + "," + idToLabelMap.get(key));
		}
		
		return result;
	}

	/**
	 * Return a HashMap that map entities label to their id
	 * @return
	 */
	public HashMap<String, String> getEntityIdMap() {
		return labelToEntityIdMap;
	}



	/**
	 * Return a HashMap that map entities id to their labels
	 * @return
	 */
	public HashMap<String, String> getLabelMap() {
		return idToLabelMap;
	}


	
	//=============================================================================================
	//=====PRIVATE METHODS================PRIVATE METHODS====================PRIVATE METHODS=======
	//=============================================================================================
	private void setFields(){
		labelToEntityIdMap = new HashMap<String, String>();
		idToLabelMap = new HashMap<String, String>();
	}
	
	private void addToMaps(String id, String label){
		labelToEntityIdMap.put(label, id);
		idToLabelMap.put(id, label);
	}

}
