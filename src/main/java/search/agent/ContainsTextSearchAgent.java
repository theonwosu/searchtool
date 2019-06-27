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
package search.agent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import search.common.filters.ArchiveFilter;
import search.common.filters.Filter;
import search.exceptions.SearchException;
import search.matcher.MatcherFactory;
import search.matcher.SearchMatcher;


/**
 * @author Theophine Nwosu
 * 
 */
public class ContainsTextSearchAgent extends SearchAgent {
  
  public ContainsTextSearchAgent() {
    super();
  }

  /**
   * @param archivesToSearch
   * @param filesToFind
   */
  public ContainsTextSearchAgent(String archivesToSearch, String filesToFind) {
    super(archivesToSearch, filesToFind);
  }

  /* 
   * @see search.agent.SearchAgent#matchFile(java.lang.String, java.lang.String, int, int)
   */
  protected void matchFile(String filePath, String searchStr) throws SearchException {
    if(isDirectory(filePath)){
      return;
    }
    if(!Filter.isAllowed(getFilesToFind(), getName(filePath), Filter.FILE_TYPES_SEPARATOR, getCaseSensitivity())){
      return;
    }
    if(Filter.isExcludedByDefault(filePath)){
      return;
    }
    if(Filter.isExcluded(getExcludePatterns(),getName(filePath),Filter.FILE_TYPES_SEPARATOR,SearchMatcher.IGNORE_CASE )){
      return;
    }
    
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(filePath));
      String line = null;
      int lineNumber = 1;
      String value = null;
      boolean first1 = true;
      boolean first2 = true;
      SearchMatcher matcher = MatcherFactory.getInstance().getMatcher(getMatcherType(), getCaseSensitivity());
      notifyObservers(filePath, STATUS);
      while((line = reader.readLine()) != null){
        if (CANCEL) {
          //Not necessary handled by finally block
          //cleanUp(reader);          
          return;
        }        
        if(matcher.match(line, searchStr)){
          if (compressedFile != null) {
            map.put(filePath, levelMap.get(level + ""));
            value = getPathToFile(filePath, "");
            first1 = notifyObservers(value, line, lineNumber, first1);
          }
          else {
            first2 = notifyObservers(filePath, line, lineNumber, first2);
          }          
        }
        lineNumber++;
      }
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    } 
    finally{
      cleanUp(reader);
    }
  }

  private boolean notifyObservers(String filePath, String line, int lineNumber, boolean first) {
    if(first){
      notifyObservers(SearchAgent.FILE_INFO+filePath, RESULT);
      first = false;
    }
    notifyObservers("\tLine "+ lineNumber + ":  "+ line, RESULT);
    return first;
  }

  private void cleanUp(BufferedReader reader) {
    if(reader != null){
      try {
        reader.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  /* 
   * @see search.agent.SearchAgent#processArchive(java.lang.String, java.lang.String, java.lang.String, int, int)
   */
  protected void processArchive(String name, String searchStr, String javaHome) throws SearchException {
    if(!ArchiveFilter.isAllowed(getArchivesToSearch(), getName(name), Filter.FILE_TYPES_SEPARATOR, getCaseSensitivity())){
      return;
    } 
    if(CANCEL){
      return;
    }    
    notifyObservers(name, STATUS);
    if (name.toLowerCase().endsWith(ZIP_EXTENSION) || name.toLowerCase().endsWith(EAR_EXTENSION) || name.toLowerCase().endsWith(WAR_EXTENSION) || name.toLowerCase().endsWith(JAR_EXTENSION)) {
      processZip(name, javaHome, searchStr);
    }
  }
}
