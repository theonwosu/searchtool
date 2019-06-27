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
public interface SearchMatcher {
  public static final int IGNORE_CASE = 0;
  public static final int CASE_SENSITIVE = 1; 
  public static final int EXACT_MATCHER = 2;
  public static final int CONTAINS_MATCHER = 3;
  public static final int REGEX_MATCHER = 4;
  public boolean match(String sourceString, String stringToMatch) throws SearchException;
}
