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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import search.common.UIDGenerator;
import search.common.filters.BinFileNameFilter;
import search.exceptions.SearchException;

/**
 * @author Theophine Nwosu
 * 
 */
public abstract class Util {
  protected final String COMMAND_SWITCH;
  protected final String EXTENSION;
  protected final String JAVA_EXECUTABLE;
  protected final String JAR_EXECUTABLE; 
  
  public Util(String commandSwitch,String extension,String javaExecutable,String jarExecutable){
    COMMAND_SWITCH = commandSwitch;
    EXTENSION = extension;
    JAVA_EXECUTABLE = javaExecutable;
    JAR_EXECUTABLE = jarExecutable;
  }
  
  /**
   * @param java_home
   * @param fileName
   * @param directory
   * @return
   * @throws SearchException 
   */
  public synchronized String generateExecutable(String javaHome, String fileName, String directory) throws SearchException {  
    PrintWriter writer = null;
    String executable = null;
    try {
      executable = "execute" + UIDGenerator.getUniqueLong() + EXTENSION;
      writer = new PrintWriter(new BufferedWriter(new FileWriter(executable)));
      writer.println("cd " + directory);
      writer.println("\"" + javaHome + "/bin/jar\" xf \"" + fileName + "\"");

      String currentDirectory = System.getProperty("user.dir");
      executable = currentDirectory + "/" + executable;
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (writer != null) {
        writer.close();
      }
    }
    return executable;
  }
  
  /**
   * @param executable
   * @return
   */
  public List execute(String executable) {
    List list = new ArrayList();
    File file = new File(executable);

    //Make sure executable can be read before continuing
    while (!file.canRead()) {
      ;
    }
    //System.out.println("Executable file returned = " + executable);

    Process proc;
    try {
      proc = Runtime.getRuntime().exec(COMMAND_SWITCH + executable);
      //Attempt to loop and retrieve all of the output from the spawned process
      BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      String line = null;
      //process the output from the spawned process
      while ((line = reader.readLine()) != null){
        list.add(line);
      }
      
      byte buf[] = new byte[256];
      String temp;
      DataInputStream in = new DataInputStream(proc.getErrorStream());
      while ( (int)in.read(buf) != -1) {
        //Echo the output from the spawned process to the java application's console
        temp = new String(buf);
        System.out.println(temp);
      }

    }
    catch (IOException e1) {
      String info = executable + " batch file might be missing...";
      System.out.println(info);
      return list;
    }

    try {
      //Wait for spawned process to terminate
      proc.waitFor();
    }
    catch (InterruptedException e2) {
      System.out.println("Spawned process was interrupted and exception was handled");
      return list;
    }
    return list;
  }
  
  /**
   * @param command
   * @return
   */
  public static List executeCommand(String command) {
    List list = new ArrayList();
    Process proc;
    try {
      proc = Runtime.getRuntime().exec(command);
      //Attempt to loop and retrieve all of the output from the spawned process
      BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      String line = null;
      //process the output from the spawned process
      while ((line = reader.readLine()) != null){
        list.add(line);
      }

      String temp;
      int c;
      byte buf[] = new byte[256];
      DataInputStream in = new DataInputStream(proc.getErrorStream());
      while ( (c = in.read(buf)) != -1) {
        //Echo the output from the spawned process to the java application's console
        temp = new String(buf);
        System.err.println(temp);
      }
    }
    catch (IOException e1) {
      e1.printStackTrace();
      return list;
    }

    try {
      //Wait for spawned process to terminate
      proc.waitFor();
    }
    catch (InterruptedException e2) {
      System.out.println("Spawned process was interrupted and exception was handled");
      return list;
    }
    return list;
  }
  
  /**
   * @param java_home
   * @throws SearchException
   */
  public String validateJavaHome(String java_home) throws SearchException {
    if (java_home == null || java_home.trim().length() < 1) {
      throw new SearchException("Java Home Can't be empty");
    }
    java_home = removePathSepartorAtEnd(java_home);
    // recommended approach to test for existence for file
    File file = new File(java_home);
    if (!file.exists() || !file.isDirectory()) {
      throw new SearchException("Java Home: " + java_home + " does not exist");
    }
    // test using filter to check for existence file
    if (!containsFile(file)) {
      throw new SearchException("JAVA HOME => " + java_home
          + " does not contain a \"bin\" directory");
    }
    file = new File(java_home + "/bin/"+JAVA_EXECUTABLE);
    if (!file.exists()) {
      throw new SearchException("JAVA HOME/BIN => " + java_home
          + "/bin does not contain a \"java\" executable");
    }
    file = new File(java_home + "/bin/"+JAR_EXECUTABLE);
    if (!file.exists()) {
      throw new SearchException("JAVA HOME/BIN => " + java_home
          + "/bin does not contain a \"jar\" executable");
    }
    return java_home;
  } 
  
  /**
   * @param directory
   * @return
   */
  private String removePathSepartorAtEnd(String directory) {
    if (directory == null || directory.trim().length() < 1) {
      return null;
    }
    directory = directory.trim();
    int lastIndex = directory.length() - 1;
    char val = directory.charAt(lastIndex);
    if ('/' == val || '\\' == val) {
      directory = directory.substring(0, lastIndex);
    }
    return directory;
  }
  
  /**
   * @param directory
   * @return
   */
  private String addPathSepartorAtEnd(String directory) {
    if (directory == null) {
      return null;
    }
    directory = directory.trim();
    int lastIndex = directory.length();
    char val = directory.charAt(lastIndex - 1);
    if ('/' != val && '\\' != val) {
      directory = directory + File.separator;
    }
    return directory;
  }  
  
  /**
   * @param directory
   * @return
   */
  private boolean containsFile(File directory) {
    File[] files = directory.listFiles(new BinFileNameFilter());
    if (files != null && files.length > 0) {
      return true;
    }
    return false;
  }
  
  public static boolean isUnixOS(){
    String value = System.getProperty("file.separator");
    if("/".equals(value)){
      return true;
    } 
    return false;
  }
  
  /**
   * @param javaHome
   * @param jarName
   * @return
   * @throws SearchException
   */
  public abstract List getJarFileList(String javaHome, String jarName) throws SearchException;
}
