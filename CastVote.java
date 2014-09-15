import java.util.ArrayList;
import java.util.Scanner;

/**
 * Cast Vote.
 *
 * This is the class that deals with a single cast vote record.
 * In essence, this is the background information (pct, etc.) for this
 * cast vote.
 *
 * Copyright (C) 2013 by Shannon Hood.  All rights reserved.
 *
 * @author Shannon Hood
 * @version 1.00 24 April 2013
**/
public class CastVote implements ICastVote {
	static private int staticVoteNumber = 0;
	private int localVoteNumber = 0;

	private String ballotStyle;
	private String ivo;
	private String pctName;
	private String pctNumber;
	
	private ArrayList<Integer> choices;
	
	/**
	 * Constructor.
	 *
	 * @param pctInfo the 'String' with the pct info in it
	 * @param inputLine the cast vote record input line
	**/
	public CastVote(String pctInfo, String inputLine) {
		this.ballotStyle = "style";
	    this.ivo = "ivo";
	    this.localVoteNumber = staticVoteNumber;
	    this.pctName = "pctname";
	    this.pctNumber = "pctnumber";
	    
	    this.choices = new ArrayList<Integer>();
	    
	    this.parseNewVoteInput(pctInfo, inputLine);
	}

	/**
	 * Method to parse an input line and add to the existing cast vote record.
	 *
	 * We convert the input line to a 'Scanner', read and toss the
	 * first parts (because we already have that from the new vote line)
	 * and then convert the candidateName and contest information.
	 *
	 * @param inputLine the 'String' of the CVR
	**/
	public void addToCVR(String inputLine) {
	    Scanner scanLine = new Scanner(inputLine);
	    
	    // Read and trash the ivo number and the ballot style number.
	    scanLine.next().trim();
	    scanLine.next().trim();

	    // Read and trim the sequence code, name, and contest, and add.
	    Choice newChoice = new Choice(scanLine.nextLine().trim());
	    this.choices.add(Integer.parseInt(newChoice.getCandidateName().substring(0,2).trim()));
	    Choices.AddChoice(newChoice);

	    scanLine.close();
	}

	/**
	 * Method to extract the precinct information from the 'String'.
	 *
	 * Extracting the pct info means getting rid of the "PRECINCT" and
	 * the hyphen from the input, taking the next piece of the 'String'
	 * as the pct number, and then taking rest of the input 'String'
	 * as the name of the pct.
	 *
	 * We make judicious use of 'trim()' in order to limit ourselves
	 * only to real data.
	 *
	 * @param pctInfo the 'String' from which to extract
	**/
	public void extractPctInfo(String pctInfo) {
		pctInfo = pctInfo.trim();
	    Scanner scanLine = new Scanner(pctInfo);

	    scanLine.next(); // Read and toss the "PRECINCT".
	    this.pctNumber = scanLine.next();

	    scanLine.next(); // Read and toss the hyphen.
	    this.pctName = scanLine.nextLine().trim();
	    
	    scanLine.close();
	}

	/**
	 * Accessor method for choices given by ID.
	 * 
	 * @return choices
	 */
	public ArrayList<Integer> getChoices() {
		return this.choices;
	}

	/**
	 * Get the choice in this cast vote record for a certain contest.
	 * 
	 * @param whichContest
	 * @return returnValue the choice for a specific contest
	 */
	public Choice getChoiceIDForContest(String whichContest) {
		Choice returnValue = null;
		for(Integer i : this.choices) {
			if(Choices.GetChoice(i).getContest().contains(whichContest)) {
				returnValue = new Choice(Choices.GetChoice(i));
			}
		}
		return returnValue;
	}

	/**
	 * Method to parse an input line to create a new cast vote record.
	 *
	 * First we extract the pct info from the 'pctInfo' input parameter.
	 * Then we convert the input line to a 'Scanner' and read off the
	 * info one at a time.
	 *
	 * If we have the asterisk at the appropriate location, we assume
	 * that we have a new vote and we bump that counter.
	 * Then we extract the choice information.
	 *
	 * @param pctInfo the 'String' from which to get pct data
	 * @param inputLine the 'String' of the CVR
	**/
	public void parseNewVoteInput(String pctInfo, String inputLine) {
	    Scanner scanLine = new Scanner(inputLine);

	    this.extractPctInfo(pctInfo);
	    this.ivo = scanLine.next().trim();
	    this.ballotStyle = scanLine.next().trim();

	    if(inputLine.indexOf("*") >= 0) {
	    	scanLine.next();
	    	staticVoteNumber++;
	    	this.localVoteNumber = staticVoteNumber;
	    }

	    Choice newChoice = new Choice(scanLine.nextLine().trim());
	    Choices.AddChoice(newChoice);
	    this.choices.add(Integer.parseInt(newChoice.getCandidateName().substring(0,2).trim()));
	    
	    scanLine.close();
	}
	
	/**
	 * Usual 'toString' method.
	 *
	 * @return a formatted 'toString' of the class
	**/
	public String toString() {
	    String s = "";

	    s += String.format("%5d ", this.localVoteNumber);
	    s += String.format("%5s ", this.pctNumber);
	    s += String.format("%-20s ", this.pctName);
	    s += String.format("%6s ", this.ivo);
	    s += String.format("%3s ", this.ballotStyle);
	    s += String.format("* ");

	    boolean firstTime = true;
	    for(Integer i : this.choices) {
	    	if(firstTime)
	    		firstTime = false;
	    	else 
	    		s += String.format("%46s ", " ");
	      
	    	s += String.format("%-40s \n", Choices.GetChoice(i));
	    }
	    return s + "\n";
	}
}
