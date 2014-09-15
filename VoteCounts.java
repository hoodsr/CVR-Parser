import java.util.ArrayList;
/**
 * VoteCounts.
 *
 * This is the class that deals with the vote counts. All this
 * really does is encapsulate the vote counting stuff into a separate
 * class so the details are not exposed in 'DoTheWork'. 
 *
 * Copyright (C) 2013 by Shannon Hood.  All rights reserved.
 *
 * @author Shannon Hood
 * @version 1.00 23 April 2013
**/

public class VoteCounts implements IVoteCounts {
	private int totalVotes;
	private String contestName;
	private ArrayList<Integer> choices;

	/**
	 * Constructor.
	 * 
	 * @param totalVotes total number of votes
	 * @param contestName
	**/
	public VoteCounts(int totalVotes, String contestName) {
	  this.totalVotes = totalVotes;
	  this.contestName = contestName;
	  this.choices = new ArrayList<Integer>();
	}

	/**
	 * Method to add a 'Choice' to the list of choices made by some voter.
	 *
	 * If the choice already exists in our list, we increment the count.
	 * Otherwise, we add to the list and set the vote count to 1.
	 *
	 * @param choice the 'Choice' to be added
	**/
	public void addToCount(Choice choice) {
	    boolean foundChoice = false;
	
	    for(Integer i : this.choices) {
	    	if(Choices.GetChoice(i).compareTo(choice) == 0) {
	    		foundChoice = true;
	    		Choices.GetChoice(i).increment();
	    		this.contestName = Choices.GetChoice(i).getContest();
	    	}
	    }
	
	    if(!foundChoice) {
	    	Choice newChoice = new Choice(choice);
	    	newChoice.increment();
	    	this.choices.add(Choices.GetID(choice));
	    }
	
	  }
	
	/**
	 * Usual 'toString' method.
	 *
	 * @return a formatted 'toString' of the class
	**/
	public String toString() {
	    int totalForCandidates = 0;
	    String s = "";
	
	    for(Integer i : this.choices) {
	    	s += String.format("%5d %-30s %s\n",
	                         Choices.GetChoice(i).getCount(),
	                         Choices.GetChoice(i).getCandidateName().substring(2).trim(),
	                         Choices.GetChoice(i).getContest());
	     	totalForCandidates += Choices.GetChoice(i).getCount();
	    }
	
	    s += String.format("%5d %-30s %s\n", totalVotes - totalForCandidates, "Undervote", contestName);
	    return s;
	} 
}
