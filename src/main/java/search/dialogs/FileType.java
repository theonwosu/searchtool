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
package search.dialogs;
/**
 * @author Theophine Nwosu
 * 
 */
public class FileType {
  protected boolean selected = false;
  protected String name;

  /**
   * @param name
   */
  public FileType(String name) {
    this.name = name;
  }

  public void negateSelected() {
    selected = !selected;
  }

  public boolean isSelected() {
    return selected;
  }

  public String toString() {
    return name;
  }
}
