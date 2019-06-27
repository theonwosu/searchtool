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
public class UtilUnix extends Util {
  private static UtilUnix utilUnix = null;
  private UtilUnix(){
    super("",".sh","java","jar");
  }
  
  /* 
   * @see search.util.Util#generateExecutable(java.lang.String, java.lang.String, java.lang.String)
   */
  public String generateExecutable(String java_home, String fileName, String directory) throws SearchException {
    String executable = super.generateExecutable(java_home, fileName, directory);
    executeCommand("chmod 755 "+executable);
    return executable;
  }
  
  public synchronized static UtilUnix getInstance(){
    if(utilUnix == null){
      utilUnix = new UtilUnix();
    }
    return utilUnix;
  }
  
  /*
   * @see search.util.Util#getJarFileList(java.lang.String, java.lang.String)
   */
  public List getJarFileList(String javaHome, String jarName) throws SearchException{
    javaHome = validateJavaHome(javaHome);
    return executeCommand(javaHome + "/bin/jar tf " + jarName);
  }
}
