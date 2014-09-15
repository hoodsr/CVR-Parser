/**
 * This class deals with a single choice in a cast vote.
 * Copyright (C) 2013 by Shannon Hood.  All rights reserved.
 *
 * @author Shannon Hood
 * @version 1.00 23 April 2013
**/

public class Choice implements IChoice, Comparable<Choice> {
	private String candidate;
	private String contest;
	private int voteCount;
	
	/**
	 * Constructor.
	 *
	 * @param s the 'String' with both candidate name and contest
	**/
	public Choice(String s) {
	    this.voteCount = 1;
	    this.candidate = s.substring(0, 42).trim();
	    this.contest = s.substring(42).trim();
	}

	/**
	 * Constructor.
	 *
	 * @param who the candidate name
	 * @param what the contest
	**/
	public Choice(String who, String what) {
	    this.voteCount = 0;
	    this.candidate= who;
	    this.contest = what;
	  }

	/**
	 * Constructor.
	 *
	 * @param what the choice from which to clone a new choice
	**/
	public Choice(Choice c) {
	    this.voteCount = 0;
	    this.candidate = c.getCandidateName();
	    this.contest = c.getContest();
	  }


	/**
	 * Accessor method to get the candidate name.
	 *
	 * @return candidate
	**/
	public String getCandidateName() {
		return this.candidate;
	}

	/**
	 * Accessor method to get the contest name.
	 *
	 * @return the contest
	**/
	public String getContest() {
		return this.contest;
	}
	
	/**
	 * Accessor method to get the count.
	 *
	 * @return the count
	**/
	public int getCount() {
		return this.voteCount;
	}
	
	/**
	 * Method to compare two choices.
	 * We choose to compare first on contest, then on candidate name.
	 *
	 * @return the usual -1, 0, +1 of a comparison
	**/
	public int compareTo(Choice that) {
		if(this.getContest().compareTo(that.getContest()) < 0)
			return -1;
		
	    else if(this.getContest().compareTo(that.getContest()) > 0)
	    	return 1;
		
	    else {
	      if(this.getCandidateName().compareTo(that.getCandidateName()) < 0)
	    	  return -1;
	      
	      else if(this.getCandidateName().compareTo(that.getCandidateName()) > 0)
	    	  return +1;
	    }
	    return  0;
	}

	/**
	 * Method to increment the count.
	**/
	public void increment() {
		this.voteCount++;
	}
	
	/**
	 * Usual 'toString' method.
	 *
	 * @return a formatted 'toString' of the class
	**/
	public String toString() {
	    return String.format("%-40s %s", this.candidate.substring(2).trim(), this.contest);
	} 
}
