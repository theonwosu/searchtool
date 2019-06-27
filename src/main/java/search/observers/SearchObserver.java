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
package search.observers;
/**
 * @author Theophine Nwosu
 * 
 */
public interface SearchObserver {
  public void updateResult(String value);
  public void updateStatus(String value);
}
