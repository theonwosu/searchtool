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

import java.io.File;
import java.io.FileFilter;
/**
 * @author Theophine Nwosu
 * 
 */
public class JarFileFilter implements FileFilter {

  private static final String JAR_EXTENSION = ".jar";

  /* 
   * @see java.io.FileFilter#accept(java.io.File)
   */
  public boolean accept(File file) {
    if(file == null){
      return false;
    }
    String name = file.getAbsolutePath();
    if(name != null && (name.toLowerCase().endsWith(JAR_EXTENSION))){
      return true;
    }
    return false;
  }

}
