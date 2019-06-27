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

import java.io.File;
import java.util.List;


import search.common.filters.ArchiveFilter;
import search.common.filters.Filter;
import search.exceptions.SearchException;
import search.matcher.MatcherFactory;
import search.matcher.SearchMatcher;
import search.util.UtilFactory;
/**
 * @author Theophine Nwosu
 * 
 */
public class FindFileSearchAgent extends SearchAgent {
  /**
   * @param archivesToSearch
   * @param filesToFind
   */
  public FindFileSearchAgent(String archivesToSearch, String filesToFind) {
    super(archivesToSearch, filesToFind);
  }

  public FindFileSearchAgent() {
    super();
  }
  
  /**
   * @param filePath
   * @param searchStr
   * @return
   * @throws SearchException
   */
  private boolean match(String filePath, String searchStr) throws SearchException {
    notifyObservers(filePath, STATUS);

    if (isDirectory(filePath)) {
      return false;
    }
    File file = new File(filePath);
    String name = file.getName();
    SearchMatcher matcher = MatcherFactory.getInstance().getMatcher(getMatcherType(), getCaseSensitivity());
    return matcher.match(name, searchStr);
    //if (matcher.match(name, searchStr)) {
    //  return true;
    //}
    //return false;    
  }  
  
  /**
   * @param filePath
   * @param searchStr
   * @return
   * @throws SearchException
   */
  protected void matchFile(String filePath, String searchStr) throws SearchException {
    String temp = null;
    if (match(filePath, searchStr)) {      
      if (compressedFile != null) {
        map.put(filePath, levelMap.get(level + ""));
        temp = getPathToFile(filePath, "");
        notifyObservers(temp, RESULT);
      }
      else {
        notifyObservers(filePath, RESULT);
      }
    }
  }  

  /* 
   * @see search.agent.SearchAgent#processArchive(java.lang.String, java.lang.String, java.lang.String, int, int)
   */
  protected void processArchive(String name, String searchStr, String javaHome) throws SearchException {
    String temp = null;
    String fileInJarFile = "";
    if(!ArchiveFilter.isAllowed(getArchivesToSearch(), getName(name), Filter.FILE_TYPES_SEPARATOR, getCaseSensitivity())){
      return;
    }

    List listOfContentOfJarFile = UtilFactory.getInstance().getUtil().getJarFileList(javaHome, name);
    if (!isNestedArchive(listOfContentOfJarFile)) {
      for (int j = 0; j < listOfContentOfJarFile.size(); j++) {
        if (CANCEL) {
          return;
        }
        fileInJarFile = (String) listOfContentOfJarFile.get(j);
        if (match(fileInJarFile, searchStr)) {
          if (compressedFile != null) {
            map.put(name, levelMap.get(level + ""));
            temp = getPathToFile(name, fileInJarFile);
            notifyObservers(temp, RESULT);
          }
          else {
            temp = getPathToFile2(name, fileInJarFile);
            notifyObservers(temp, RESULT);
          }
        }
      }
    }
    else if (name.toLowerCase().endsWith(JAR_EXTENSION) || name.toLowerCase().endsWith(ZIP_EXTENSION) || name.toLowerCase().endsWith(EAR_EXTENSION) || name.toLowerCase().endsWith(WAR_EXTENSION)) {
      processZip(name, javaHome, searchStr);
    }    
  }
}
