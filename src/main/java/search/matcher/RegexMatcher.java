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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import search.exceptions.SearchException;

/**
 * @author Theophine Nwosu
 * 
 */
public class RegexMatcher implements SearchMatcher {
  private int caseSensitivity;
  
  private RegexMatcher(int caseSensitivity){
    this.caseSensitivity = caseSensitivity;
  }
  
  /* 
   * @see search.matcher.SearchMatcher#match(java.lang.String, java.lang.String)
   */
  public boolean match(String stringToMatch, String regularExpression) throws SearchException {
    if(regularExpression == null || stringToMatch == null){
      return false;
    }
 
    //regularExpression = escapeCharacters(regularExpression);
    Pattern pattern = null;
    try {
      if(SearchMatcher.IGNORE_CASE == caseSensitivity){
        pattern = Pattern.compile(regularExpression,Pattern.CASE_INSENSITIVE );
      }
      else{
        pattern = Pattern.compile(regularExpression);
      }
    }
    catch (java.util.regex.PatternSyntaxException e) {
      throw new SearchException("Invalid pattern: "+regularExpression);
    }
    Matcher matcher = pattern.matcher(stringToMatch);
    
    if(matcher.find()){
    	return true;
    }
    return false;
    //return matcher.matches(); 
  }
  
  private String escapeCharacters(String regularExpression){
    regularExpression = regularExpression.replaceAll("\\.", "\\\\.");
    regularExpression = regularExpression.replaceAll("\\*", "\\.*");
    return regularExpression;    
  }
  
  public static synchronized RegexMatcher getInstance(int caseSensitivity){    
    return new RegexMatcher(caseSensitivity);
  } 
}
