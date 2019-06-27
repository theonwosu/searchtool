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
package search.common;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Theophine Nwosu
 * 
 */
public class ExecuteCommand {
  /**
   * @param executable
   */
  public static void execute(String[] executable) {
    for (int i = 0; i < executable.length; i++) {
      execute(executable[i]);
    }
  }

  /**
   * @param executable
   * @return
   */
  public static ArrayList execute(String executable) {
    ArrayList list = new ArrayList();
    File file = new File(executable);

    //Make sure executable can be read before continuing
    while (!file.canRead()) {
      ;
    }
    //System.out.println("Executable file returned = " + executable);

    Process proc;
    try {
      proc = Runtime.getRuntime().exec("cmd /c " + executable);
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
      System.out.println(
          "Spawned process was interrupted and exception was handled");
      return list;
    }
    return list;
  }

  /**
   * @param command
   * @return
   */
  public static ArrayList executeCommand(String command) {
    ArrayList list = new ArrayList();
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
      System.out.println(
          "Spawned process was interrupted and exception was handled");
      return list;
    }
    return list;
  }

  public static void main(String args[]) {
    ArrayList list = ExecuteCommand.executeCommand("jar tf c:/common.jar");
    for( int i = 0; i < list.size(); i++){
      System.out.println((String)list.get(i));
    }
  }
}
