import java.io.PrintWriter;
import java.util.Scanner;
/*********************************************************************
 * Parsing an EL155 Cast Vote Record and counting votes.
 * We read the EL155 and create an 'ArrayList' of each complete
 * cast vote record. Then we count the votes for President and for
 * the U.S. House.
 *
 * Copyright (C) 2012 by Duncan A. Buell.  All rights reserved.
 * Used with permission by Shannon Hood.
 * 
 * @author Duncan A. Buell
 * @version 1.00 2012-12-22
**/
public class Driver
{
/*********************************************************************
 * main method.
**/
  public static void main (String[] args)
  {
    Scanner inFile = null;
    PrintWriter outFile = null;
    DoTheWork doTheWork = null;
    Choices choices = null;

    inFile = FileUtils.ScannerOpen("EL155");
    outFile = FileUtils.PrintWriterOpen("zout.txt");
    FileUtils.SetLogFile("zlog.txt");

    System.out.println("begin execution");

    choices = new Choices();

    doTheWork = new DoTheWork(inFile);

    outFile.printf("%s%n", doTheWork);
    outFile.flush();

//    outFile.printf("%s%n", choices.toString());
//    outFile.flush();

    outFile.printf("Votes for President\n");
    outFile.printf("%s%n", doTheWork.countTheVotes("President"));
    outFile.flush();

    outFile.printf("Votes for Congress, District 2\n");
    outFile.printf("%s%n", doTheWork.countTheVotes("CONG002"));
    outFile.flush();

    outFile.printf("Votes for Congress, District 6\n");
    outFile.printf("%s%n", doTheWork.countTheVotes("CONG006"));
    outFile.flush();

    outFile.printf("Most common pairs and triples of votes\n");
    outFile.printf("%s%n", doTheWork.findPatterns());
    outFile.flush();

    FileUtils.logFile.flush();

    System.out.printf("end execution%n");
  }
} // public class Driver

