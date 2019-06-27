/**
 * Copyright (c) 2008 by Theo Nwosu. All Rights Reserved.
 *  
 * Theo Nwosu grants you a non-exclusive, royalty free, license to use, modify
 * and redistribute this software in source and binary code form provided,
 * that this copyright notice and license appear on all copies of the software. 
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 */
package search.common;

import java.io.*;
import java.math.*;
/**
 * @author Theophine Nwosu
 * 
 */
public class StandardInput {
  public static int getInt() {
    int val = 0;
    String buffer = "";

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    try {
      buffer = bufferedReader.readLine();
      val = Integer.parseInt(buffer);
    }
    catch (Exception e) {
    }
    return val;
  }

  public static char getChar() {
    char val = ' ';
    String buffer = "";

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    try {
      buffer = bufferedReader.readLine();
      val = buffer.charAt(0);
    }
    catch (Exception e) {
    }
    return val;
  }

  public static long getLong() {
    long val = 0;
    String buffer = "";

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    try {
      buffer = bufferedReader.readLine();
      val = Long.parseLong(buffer);
    }
    catch (Exception e) {
    }
    return val;
  }

  public static float getFloat() {
    float val = 0;
    String buffer = "";

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    try {
      buffer = bufferedReader.readLine();
      Float f = new Float(buffer);
      val = f.floatValue();
    }
    catch (Exception e) {
    }
    return val;
  }

  public static double getDouble() {
    double val = 0;
    String buffer = "";
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    try {
      buffer = bufferedReader.readLine();
      BigDecimal f = new BigDecimal(buffer);
      val = f.doubleValue();
    }
    catch (Exception e) {
    }
    return val;
  }

  public static String getString() {
    String buffer = "";
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    try {
      buffer = bufferedReader.readLine();
    }
    catch (Exception e) {
    }
    return buffer;
  }
}
