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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import search.exceptions.SearchException;
import search.matcher.SearchMatcher;
/**
 * @author Theophine Nwosu
 * 
 */
public class Filter {
  public static final String FILE_TYPES_SEPARATOR = ",";
  public static final String ALL_FILE_TYPES = "*";
  public static final String [] EXCLUDED_FILE_TYPES = {".ZIP",".JAR",".RAR",".ACE",".CAB",".TAR",".7Z",".GZ",".TGZ",".GZIP",".WAR",".EAR",".Z", ".CPIO", ".EXE",".DLL",".BIN", ".JPEG", ".JPG", ".GIF", ".BMP",".PNG", ".RPM",".ISO", ".AVI", ".MPEG", ".MPG", ".MP4", ".VOB", ".WMV", ".SWF", ".MOV", ".WAV", ".WMA"};
  //Can not be instantiated outside of this class
  private Filter(){}
  
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
    patternList = getPatternList(patternList, patternsSeparator);
    Pattern pattern = null;
    try {
      if(SearchMatcher.IGNORE_CASE == caseSensitivity){
        pattern = Pattern.compile(patternList,Pattern.CASE_INSENSITIVE );
      }
      else{
        pattern = Pattern.compile(patternList);
      }
    }
    catch (java.util.regex.PatternSyntaxException e) {
      throw new SearchException("Invalid File Name Pattern");
    }
    Matcher matcher = pattern.matcher(stringToMatch);
    return matcher.matches();    
  }

  /**
   * @param patternList
   * @param patternsSeparator
   * @return
   */
  private static String getPatternList(String patternList, String patternsSeparator) {
    patternList = patternList.replaceAll(" ", "");
    patternList = patternList.replaceAll(patternsSeparator, "|");
    //Escape . otherwise it will  match any single character
    patternList = patternList.replaceAll("\\.", "\\\\.");
    patternList = patternList.replaceAll("\\*", "\\.*");
    return patternList;
  }
  
  /**
   * @param fileName
   * @param container
   * @return
   * @throws SearchException
   */
  public static boolean isPartOf(String fileName, String [] container) throws SearchException{
    if(fileName == null){
      throw new SearchException("File Name is null in Filter.isPartOf() method");
    } 
    if(container == null){
      throw new SearchException("Container is null in Filter.isPartOf() method");
    }    
    for(int i = 0; i < container.length; i++){
      if(fileName.toLowerCase().endsWith(container[i].toLowerCase())){
        return true;
      }
    }
    return false;
  }
  
  /**
   * @param fileName
   * @return
   * @throws SearchException
   */
  public static boolean isExcludedByDefault(String fileName) throws SearchException{
    return isPartOf(fileName, EXCLUDED_FILE_TYPES);
  }
  
  /**
   * @param patternList
   * @param stringToMatch
   * @param patternsSeparator
   * @param caseSensitivity
   * @return
   * @throws SearchException
   */
  public static boolean isExcluded(String patternList,String stringToMatch, String patternsSeparator, int caseSensitivity) throws SearchException{
   return isAllowed(patternList,stringToMatch, patternsSeparator, caseSensitivity);
  }
  
  /**
   * @param args
   * @throws SearchException 
   */
  public static void main(String[] args) throws SearchException {
    String patternList1 = "*.jar, *.xls";
    Filter filter =  new Filter();
    boolean b = Filter.isAllowed(patternList1, "test.XLS", ",",SearchMatcher.IGNORE_CASE);
    System.out.println(b); 
    
    patternList1 = "*.jar, *.xls";
    b = Filter.isAllowed(patternList1, "test1XLS", ",",SearchMatcher.IGNORE_CASE);
    System.out.println(b);    
    
    b = Filter.isAllowed(patternList1, "test.XLS", ",",SearchMatcher.CASE_SENSITIVE);
    System.out.println(b);  
    
    patternList1 = "*.jar, *.xls, test*.XLS";
    b = Filter.isAllowed(patternList1, "test.XLS", ",",SearchMatcher.CASE_SENSITIVE);
    System.out.println(b);    
    
    patternList1 = "*, *.jar, *.xls";
    b = Filter.isAllowed(patternList1, "test.XLS", ",",SearchMatcher.CASE_SENSITIVE);
    System.out.println(b); 
    
    patternList1 = "*.XLS";
    b = Filter.isAllowed(patternList1, "test.XLS", ",",SearchMatcher.CASE_SENSITIVE);
    System.out.println(b);     
  }
}
