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

import search.exceptions.SearchException;
/**
 * @author Theophine Nwosu
 * 
 */
public class MatcherFactory {
  private static MatcherFactory matcherFactory = null;
  private MatcherFactory(){
  }
  
  public static synchronized MatcherFactory getInstance(){
    if(matcherFactory == null){
      matcherFactory = new MatcherFactory();
    }
    return matcherFactory;
  }
  
  /**
   * @param type
   * @param sensitivity
   * @return
   * @throws SearchException
   */
  public SearchMatcher getMatcher(int type, int sensitivity) throws SearchException{
    if(type == SearchMatcher.EXACT_MATCHER && sensitivity == SearchMatcher.CASE_SENSITIVE){
      return ExactNameCaseSensitiveMatcher.getInstance();
    }
    else if(type == SearchMatcher.EXACT_MATCHER && sensitivity == SearchMatcher.IGNORE_CASE){
      return ExactNameCaseInsensitiveMatcher.getInstance();
    }    
    else if(type == SearchMatcher.CONTAINS_MATCHER && sensitivity == SearchMatcher.CASE_SENSITIVE){
      return ContainsNameCaseSensitiveMatcher.getInstance();
    } 
    else if(type == SearchMatcher.CONTAINS_MATCHER && sensitivity == SearchMatcher.IGNORE_CASE){
      return ContainsNameCaseInsensitiveMatcher.getInstance();
    }   
    else if(type == SearchMatcher.REGEX_MATCHER){
      return RegexMatcher.getInstance(sensitivity);
    }     
    throw new SearchException("Invalid Matcher Type");
  }
}
