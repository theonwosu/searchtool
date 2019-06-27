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

import java.awt.Frame;
/**
 * @author Theophine Nwosu
 * 
 */
public class ArchiveListDialog extends PatternListDialog {
  /**
   * @param owner
   * @param title
   * @param modal
   */
  public ArchiveListDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal,new FileType[]{
        new FileType("*.ear"), 
        new FileType("*.jar"),
        new FileType("*.war"),
        new FileType("*.zip")
    } );
    setSize(220,220);
  }
  
  public static void main(String args[]) {
    PatternListDialog patternListDialog = new ArchiveListDialog(null,"Select File Types", true);
    patternListDialog.setVisible(true);
  }  
}
