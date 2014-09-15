import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
/*********************************************************************
 * Application class for reading the EL155 and parsing into an
 * 'LinkedList' of cast vote records, then counting the votes for
 * President.
 *
 * This class is passed a file as an input 'Scanner' and reads that
 * file into an 'ArrayList', each entry of which is a 'CastVote'.
 * Because the precinct name and number come only from the header
 * line, this 'String' is also passed in to the constructor and is
 * parsed. 
 *
 * Rules: parsing the header lines is done in this class.
 *        parsing the actual cast vote lines is done in 'CastVote'.
 *
 * Copyright (C) 2012 by Duncan A. Buell.  All rights reserved.
 * Used with permission by Shannon Hood.
 *
 * @author Duncan A. Buell
 * @version 1.00 2012-12-20
**/
public class DoTheWork implements IDoTheWork
{
/*********************************************************************
 * Instance variables for the class.
**/
  private final int DUMMYINT = -999;
  private final String DUMMYSTRING = "dummystring";

  private ArrayList<CastVote> castVotes;

/*********************************************************************
 * Constructor.
 *
 * @param inFile the 'Scanner' from which to create the list
**/
  public DoTheWork(Scanner inFile)
  {
    this.castVotes = new ArrayList<CastVote>();

    this.readFile(inFile);
  } // public ReadTheFile(Scanner inFile)

/*********************************************************************
 * Accessors and mutators.
**/

/*********************************************************************
 * General methods.
**/

/*********************************************************************
 * Method to count the votes for a contest.
 *
 * Note that we use the term 'rawContest' to refer to a contest.
 * This is so we don't need the precise name. The lookups are all
 * done by matching presence of the raw contest string in the choice,
 * not by matching that the contest in the choice equals the string.
 *
 * @return the 'String' version of the counts for this contest.
**/
  public String countTheVotes(String rawContest)
  {
    Choice choice = null;

    VoteCounts voteCounts = new VoteCounts(this.castVotes.size(), rawContest);
    for(CastVote castVote : this.castVotes)
    {
      // now we count the votes for president
//      choice = castVote.getChoiceForContest(rawContest);
      choice = castVote.getChoiceIDForContest(rawContest);
      if(null != choice)
      {
        voteCounts.addToCount(choice);
      } // if(null != choice)

    } // for(CastVote castVote : this.castVotes)

    String result = String.format("%s\n", voteCounts);

    return result;
  } // public String countTheVotes(String rawContest)

/*********************************************************************
 * Method to find patterns in the voting.
 *
 * At the moment all we look for, in a naive way, are the top five
 * most frequent pairs and triples of voting occurrences.
 *
 * @return a 'String' version of the results suitable for printing
**/
  public String findPatterns()
  {
    String s = "";
    TreeMap<String, Integer> patterns2 = null;
    TreeMap<String, Integer> patterns3 = null;

    patterns2 = new TreeMap<String, Integer>();
    patterns3 = new TreeMap<String, Integer>();

    for(CastVote cv : this.castVotes)
    {
      ArrayList<Integer> choices = cv.getChoices();
      for(int i1 = 0; i1 < choices.size(); ++i1)
      {
        for(int i2 = 0; i2 < choices.size(); ++i2)
        {
          if(i1 == i2) continue;
          String key2 = String.format("%4d %4d", choices.get(i1), choices.get(i2));
          Integer count2 = patterns2.get(key2);
          if(null == count2)
          {
            patterns2.put(key2, 1);
          }
          else
          {
            patterns2.put(key2, count2+1);
          }

          // we will embed the third loop inside the second
          for(int i3 = 0; i3 < choices.size(); ++i3)
          {
            if(i1 == i3) continue;
            if(i2 == i3) continue;
            String key3 = String.format("%4d %4d %4d",
                                choices.get(i1), choices.get(i2), choices.get(i3));
            Integer count3 = patterns3.get(key3);
            if(null == count3)
            {
              patterns3.put(key3, 1);
            }
            else
            {
              patterns3.put(key3, count3+1);
            }
          } // for(int i3 = 0; i3 < choices.size(); ++i3)

        } // for(int i2 = 0; i2 < choices.size(); ++i2)
      } // for(int i1 = 0; i1 < choices.size(); ++i1)
    } // for(CastVote cv : this.castVotes)

    Integer maxCount2 = 0;
    String maxKey2 = "dummy";
    for(String key2 : patterns2.keySet())
    {
      Integer count2 = patterns2.get(key2);
      if(count2 > maxCount2)
      {
        maxKey2 = key2;
        maxCount2 = count2;
      } 
    } // for(String key2 : patterns2)

    s = "";
    Integer first2 = Integer.valueOf(maxKey2.substring(0,5).trim());
    Integer second2 = Integer.valueOf(maxKey2.substring(5).trim());
    s += String.format("%4d %s\n     %s\n\n", patterns2.get(maxKey2),
                      Choices.GetChoice(first2), Choices.GetChoice(second2));

    Integer maxCount3 = 0;
    String maxKey3 = "dummy";
    for(String key3 : patterns3.keySet())
    {
      Integer count3 = patterns3.get(key3);
      if(count3 > maxCount3)
      {
        maxKey3 = key3;
        maxCount3 = count3;
      } 
    } // for(String key3 : patterns3)

    Integer first3 = Integer.valueOf(maxKey3.substring(0,5).trim());
    Integer second3 = Integer.valueOf(maxKey3.substring(5,10).trim());
    Integer third3 = Integer.valueOf(maxKey3.substring(10).trim());
    s += String.format("%4d %s\n     %s\n     %s", patterns3.get(maxKey3),
                      Choices.GetChoice(first3), Choices.GetChoice(second3),
                      Choices.GetChoice(third3));
    return s;
  } // public String findPatterns()

/*********************************************************************
 * Method to get the precinct from the input header line.
 * This method knows that precinct info starts with the 'String'
 * "PRECINCT" and ends before the 'String' "ELECTION".
 *
 * @param inputLine the line to parse
 * @return the precinct
**/
  public String getPctInfo(String inputLine)
  {
    int index = DUMMYINT;
    String subLine = DUMMYSTRING;

    // first we strip away the stuff leading up to the pct info
    index = inputLine.indexOf("PRECINCT");
    subLine = inputLine.substring(index);

    // now we strip away the stuff beyond the pct info
    index = subLine.indexOf("ELECTION");
    subLine = subLine.substring(0,index).trim();

    return subLine;
  } // public String getPctInfo(String inputLine)

/*********************************************************************
 * Method to determine if the line is a cast vote line.
 *
 * The definition of being a cast vote line is that the zeroth char 
 * is a leading '5' in an iVotronic serial number.
 *
 * @param inputLine the line to parse
 * @return the answer to the question
**/
  public boolean isCastVote(String inputLine)
  {
    if(0 == inputLine.indexOf("5"))
      return true;
    else
      return false;
  } // public boolean isCastVote(String inputLine)

/*********************************************************************
 * Method to determine if the line is a header line.
 *
 * The definition of being a header line is that the line begins
 * with the 'String' "RUN DATE".
 *
 * @param inputLine the line to parse
 * @return the answer to the question
**/
  public boolean isHeader(String inputLine)
  {
    if(inputLine.indexOf("RUN DATE") >= 0)
      return true;
    else
      return false;
  } // public boolean isHeader(String inputLine)

/*********************************************************************
 * Method to determine if the line is a new vote.
 *
 * The definition of being a cast vote line is that there is an
 * asterisk in the line.
 *
 * @param inputLine the line to parse
 * @return the answer to the question
**/
  public boolean isNewVote(String inputLine)
  {
    if(0 <= inputLine.indexOf("*"))
      return true;
    else
      return false;
  } // public boolean isNewVote(String inputLine)

/*********************************************************************
 * Method to read the cast vote records into the 'ArrayList'.
 *
 * We read line by line. If it's a header, we pull off the pct info.
 * If not a header but is a cast vote line, we create a new 'CastVote'
 * with the pct data and the line and add the 'CastVote' to the
 * 'ArrayList'. 
 *
 * @param inFile the Scanner from which to read.
**/
  public void readFile(Scanner inFile)
  {
    String inputLine = DUMMYSTRING;
    String pctInfo = DUMMYSTRING;
    CastVote cvr = null;

    // Read line by line.
    // if we have a header line, we need to pull off the pct info.
    // if we have a CV line:
    //   if it's a new vote
    //     close off the old CVR
    //     store the CVR
    //     create a new CVR
    //   else
    //     add the line to the current CVR
    //   endIf
    // endIf
    while(inFile.hasNext())
    {
      inputLine = inFile.nextLine();
      if(this.isHeader(inputLine))
      {
        pctInfo = this.getPctInfo(inputLine);
      }
      else if(this.isCastVote(inputLine))
      {
        if(this.isNewVote(inputLine))
        {
          if(null != cvr)
          {
            this.castVotes.add(cvr);
          }
          cvr = new CastVote(pctInfo, inputLine);
        }
        else
        {
          cvr.addToCVR(inputLine);
        }
      } // if(this.isHeader(inputLine))
    } // while(inFile.hasNext())

    // DO NOT FORGET to add the last vote 
    if(null != cvr)
    {
      this.castVotes.add(cvr);
    }

  } // public void readFile(Scanner inFile)

/*********************************************************************
 * Method to <code>toString</code> the class.
 *
 * @return the <code>toString</code> of the list.
**/
  public String toString()
  {
    String s = "";

    s += String.format("Beginning of data of %d lines of CVR\n",
                        this.castVotes.size());

    for(CastVote cvr: this.castVotes)
    {
      s += cvr.toString();
      s += "\n";
    }

    s += String.format("End of data of %d lines of CVR\n",
                        this.castVotes.size());

    return s;
  } // public String toString()

} // public class ReadTheFile
