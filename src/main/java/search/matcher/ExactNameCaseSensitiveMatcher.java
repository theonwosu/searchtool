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
package search.matcher;
/**
 * @author Theophine Nwosu
 * 
 */
public class ExactNameCaseSensitiveMatcher implements SearchMatcher {
  private static ExactNameCaseSensitiveMatcher matcher = null;
  
  private ExactNameCaseSensitiveMatcher(){
  }
  
  /* 
   * @see search.matcher.SearchMatcher#match(java.lang.String, java.lang.String)
   */
  public boolean match(String sourceString, String stringToMatch) {
    if(sourceString == null || stringToMatch == null){
      return false;
    }   
    if(stringToMatch.equals(sourceString)){
      return true;
    }     
    return false;
  }
  
  public static synchronized ExactNameCaseSensitiveMatcher getInstance(){
    if(matcher == null){
      matcher = new ExactNameCaseSensitiveMatcher();
    }
    return matcher;
  }
}
