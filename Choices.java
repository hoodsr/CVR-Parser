import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Choices.
 * Copyright (C) 2013 by Shannon Hood.  All rights reserved.
 *
 * @author Shannon Hood
 * @version 1.00 April 23 2013
**/

public class Choices implements IChoices {
	private static TreeMap<Choice, Integer> mapIDToChoice;
	private static TreeMap<Integer, Choice> mapChoiceToID;
	private static Integer key = 0;
	
	/**
	 * Choices constructor.
	 */
	public Choices() {
		mapIDToChoice = new TreeMap<Choice, Integer>();
		mapChoiceToID = new TreeMap<Integer, Choice>();
	}
	
	/**
	 * Adds a choice to both TreeMaps using the candidate number as the key/value.
	 * 
	 * @param choice
	 */
	static public void AddChoice(Choice choice) {
		key = Integer.parseInt(choice.getCandidateName().substring(0,2).trim());
		mapIDToChoice.put(choice, key);
		mapChoiceToID.put(key, choice);
	}
	
	/**
	 * Get the choice given a specific id.
	 * 
	 * @param id
	 * @return choice
	 */
	static public Choice GetChoice(Integer id) {
		return mapChoiceToID.get(id);
	}
	
	/**
	 * Get the id given a specific choice.
	 * 
	 * @param choice
	 * @return id
	 */
	static public Integer GetID(Choice choice) {
		return mapIDToChoice.get(choice);
	}
	
	/**
	 * Usual 'toString' method.
	 *
	 * @return a formatted 'toString' of the class
	**/
	public String toString() {
		String tostring = "";
		for (Entry<Integer, Choice> entry : mapChoiceToID.entrySet()) {
		     tostring += String.format("\n%s",  entry.getValue());
		}
		return tostring;
	}
}
