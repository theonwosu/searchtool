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
package search.common.filters;

import java.io.File;
import java.io.FilenameFilter;
/**
 * @author Theophine Nwosu
 * 
 */
public class BinFileNameFilter implements FilenameFilter {

  /* 
   * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
   */
  public boolean accept(File directory, String filename) {
    if(filename != null && filename.equalsIgnoreCase("bin")){
      return true;
    }
    return false;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    File file = new File("C:/bea/8.1.6/bea/jdk142_11/bin");
    File [] files = file.listFiles(new BinFileNameFilter());
    if(files != null && files.length > 0){
      System.out.println("Found it");
    }
    else{
      System.out.println("BIN not found");
    }
  }
}
