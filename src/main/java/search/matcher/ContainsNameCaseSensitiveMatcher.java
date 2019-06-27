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
public class ContainsNameCaseSensitiveMatcher implements SearchMatcher {
  private static ContainsNameCaseSensitiveMatcher matcher = null;
  
  private ContainsNameCaseSensitiveMatcher(){
  }
  
  /* 
   * @see search.matcher.SearchMatcher#match(java.lang.String, java.lang.String)
   */
  public boolean match(String sourceString, String stringToMatch) {   
    if(sourceString == null || stringToMatch == null){
      return false;
    }
    if(sourceString.indexOf(stringToMatch) > -1){
      return true;
    } 
    return false;
  }
  
  public static synchronized ContainsNameCaseSensitiveMatcher getInstance(){
    if(matcher == null){
      matcher = new ContainsNameCaseSensitiveMatcher();
    }
    return matcher;
  }  
}
