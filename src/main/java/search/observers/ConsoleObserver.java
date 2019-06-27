/**
 * Copyright (c) 2008 by Theo Nwosu. All Rights Reserved.
 *  
 * Theo Nwosu grants you a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that this copyright notice and license appear on all copies of
 * the software.
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 */
package search.observers;

import search.agent.FindFileSearchAgent;
import search.agent.SearchAgent;
import search.common.StandardInput;
import search.matcher.SearchMatcher;


/**
 * @author Theo Nwosu
 *
 */
public class ConsoleObserver implements SearchObserver{
  SearchAgent agent = null;
  
  private void endSearch(){
    if(agent != null){
      agent.cancel();
    }
  }
  
  private void startObservation() throws Exception{
    System.out.println("Search Started");
    agent = new FindFileSearchAgent();
    agent.add(this);
    //String java_home = "C:/bea/8.1.6/bea/jdk142_11";
    //String directory = "C:/delete/hold/delete";
    //String searchStr = "Action.class";
    
    System.out.print("Enter Java Home=>");
    String java_home = StandardInput.getString();
    
    System.out.print("Enter Search Archive/Directory=>");
    String directory = StandardInput.getString();
    
    System.out.print("Enter File to Find=>");
    String searchStr = StandardInput.getString();
    agent.setMatcherType(SearchMatcher.EXACT_MATCHER);
    //agent.processFiles(directory, searchStr, java_home, SearchMatcher.CASE_SENSITIVE);  
    System.out.println("Search Ended");
  }
  
  public static void main(String args[]) throws Exception {
    ConsoleObserver f = new ConsoleObserver();
    f.startObservation();  
  }

  /* 
   * @see search.observers.SearchObserver#updateResult(java.lang.String)
   */
  public void updateResult(String value) {
    System.out.println(value);
  }

  /* 
   * @see search.observers.SearchObserver#updateStatus(java.lang.String)
   */
  public void updateStatus(String value) { 
    //System.out.println(value);
  }
}
