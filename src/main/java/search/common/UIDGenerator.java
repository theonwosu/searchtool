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

import java.util.Random;
/**
 * @author Theophine Nwosu
 * 
 */
public class UIDGenerator {
  private static Random    random = new Random();
  public static long getUniqueLong(){
    long value = random.nextLong();
    if(value < 0){
      value = -value;
    }
    return  value;
  }
  
  public static double getUniqueDouble(){
    return Math.random();
  }
  
  public static String getUniqueID(){
    String temp = ""+getUniqueDouble();
    return temp.substring(2);
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("UIDGenerator.getUniqueLong() ////////////////////////////");
    for(int i = 0; i < 10; i++){
      System.out.println(UIDGenerator.getUniqueLong());
    }
    System.out.println("UIDGenerator.getUniqueID() ////////////////////////////");
    for(int i = 0; i < 10; i++){
      System.out.println(UIDGenerator.getUniqueID());
    }    
    System.out.println("UIDGenerator.getUniqueDouble() ////////////////////////////");
    for(int i = 0; i < 10; i++){
      System.out.println(UIDGenerator.getUniqueDouble());
    }    
  }

}
