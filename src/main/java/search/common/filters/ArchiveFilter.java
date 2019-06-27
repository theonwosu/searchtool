/**
 * Copyright (c) 2008 by Theo Nwosu. All Rights Reserved.
 *  
 * Theo Nwosu grants you a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that this copyright notice and license appear on all copies of
 * the software.
 * 
 * This software is provided "AS IS" without a warranty of any kind.
 */
package search.common.filters;

import search.exceptions.SearchException;
import search.matcher.SearchMatcher;
/**
 * @author Theophine Nwosu
 * 
 */
public class ArchiveFilter {
  public static final String SUPPORTED_ARCHIVE_TYPES = "*.jar, *.ear, *.war, *.zip";
  public static final String [] ARCHIVE_TYPES = {".ZIP",".JAR",".RAR",".ACE",".CAB",".TAR",".7Z",".GZ",".TGZ",".GZIP",".WAR",".EAR",".Z"};
  private ArchiveFilter(){
    
  }
  
  /**
   * @param patternList
   * @param stringToMatch
   * @param patternsSeparator
   * @param caseSensitivity
   * @return
   * @throws SearchException
   */
  public static boolean isAllowed(String patternList,String stringToMatch, String patternsSeparator, int caseSensitivity) throws SearchException{
    if(patternList == null || patternList.trim().length() == 0 || stringToMatch == null || stringToMatch.trim().length() == 0 || patternsSeparator == null || patternsSeparator.trim().length() == 0){
      return false;
    }    
    String [] patternListArray = patternList.split(patternsSeparator);
    for(int i = 0; patternListArray!= null && i < patternListArray.length; i++){
      if(!Filter.isAllowed(SUPPORTED_ARCHIVE_TYPES, patternListArray[i], patternsSeparator, SearchMatcher.IGNORE_CASE)){
        throw new SearchException(patternListArray[i] +" archive pattern is not supported");
      }
    }
    return Filter.isAllowed(patternList, stringToMatch, patternsSeparator, caseSensitivity);
  }
  
  /**
   * @param fileName
   * @return
   * @throws SearchException
   */
  public static boolean isArchive(String fileName) throws SearchException{
    return Filter.isPartOf(fileName, ARCHIVE_TYPES);
  }
  
  public static void main(String[] args) throws SearchException {
    //String patternList1 = "*, *ssss.zip";
    String patternList1 = "*.JAR, *ssss.zip";
    boolean b = ArchiveFilter.isAllowed(patternList1, "*e.jar", Filter.FILE_TYPES_SEPARATOR,SearchMatcher.IGNORE_CASE);
    //boolean b = ArchiveFilter.isAllowed(patternList1, "*e.jar", Filter.FILE_TYPES_SEPARATOR,SearchMatcher.CASE_SENSITIVE);
    System.out.println(b);     
  }
}
