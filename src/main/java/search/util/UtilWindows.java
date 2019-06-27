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
package search.util;

import java.util.List;

import search.exceptions.SearchException;

/**
 * @author Theophine Nwosu
 * 
 */
public class UtilWindows extends Util {
  private static UtilWindows utilWindows = null;
  
  private UtilWindows(){
    super("cmd /c ",".bat","java.exe","jar.exe");
  }
  
  public synchronized static UtilWindows getInstance() {
    if(utilWindows == null){
      utilWindows = new UtilWindows();
    }
    return utilWindows;
  }
  
  /* 
   * @see search.util.Util#getJarFileList(java.lang.String, java.lang.String)
   */
  public List getJarFileList(String javaHome, String jarName) throws SearchException{   
    javaHome = validateJavaHome(javaHome);
    return executeCommand("\"" + javaHome + "/bin/jar\" tf \"" + jarName + "\"");
  }  
}
