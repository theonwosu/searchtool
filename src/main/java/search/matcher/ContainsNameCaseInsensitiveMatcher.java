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
public class ContainsNameCaseInsensitiveMatcher implements SearchMatcher {
  private static ContainsNameCaseInsensitiveMatcher matcher = null;
  
  private ContainsNameCaseInsensitiveMatcher(){
  }
  
  /* 
   * @see search.matcher.SearchMatcher#match(java.lang.String, java.lang.String)
   */
  public boolean match(String sourceString, String stringToMatch) {
    if(sourceString == null || stringToMatch == null){
      return false;
    }
 
    if(sourceString.toLowerCase().indexOf(stringToMatch.toLowerCase()) > -1){
      return true;
    }
    return false;
  }
  
  public static synchronized ContainsNameCaseInsensitiveMatcher getInstance(){
    if(matcher == null){
      matcher = new ContainsNameCaseInsensitiveMatcher();
    }
    return matcher;
  }  
}
