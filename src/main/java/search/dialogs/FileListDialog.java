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
public class FileListDialog extends PatternListDialog {
  
  /**
   * @param owner
   * @param title
   * @param modal
   */
  public FileListDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal,new FileType[] { 
        new FileType("*"), 
        new FileType("*.ant"),
        new FileType("*.bat"),        
        new FileType("*.class"),
        new FileType("*.chm"),
        new FileType("*.cmd"),        
        new FileType("*.css"),
        new FileType("*.datagraph"),
        new FileType("*.ddl"),
        new FileType("*.doc"),
        new FileType("*.docx"),
        new FileType("*.dtd"),
        new FileType("*.ent"),
        new FileType("*.htm"),
        new FileType("*.html"),
        new FileType("*.htpl"),
        new FileType("*.jardesc"),
        new FileType("*.java"),
        new FileType("*.jpage"),
        new FileType("*.js"),
        new FileType("*.jsf"),
        new FileType("*.jsp"),
        new FileType("*.jspf"),
        new FileType("*.jspx"),
        new FileType("*.jsv"),
        new FileType("*.jtpl"),
        new FileType("*.log"),
        new FileType("*.logic"),
        new FileType("*.macrodef"),
        new FileType("*.mod"),
        new FileType("*.override"),
        new FileType("*.pdf"),
        new FileType("*.prefs"),
        new FileType("*.product"),
        new FileType("*.properties"),
        new FileType("*.server"),
        new FileType("*.shapes"),
        new FileType("*.shtm"),
        new FileType("*.shtml"),
        new FileType("*.sql"),
        new FileType("*.sqlpage"),
        new FileType("*.tag"),
        new FileType("*.tagf"),
        new FileType("*.tagx"),
        new FileType("*.target"),
        new FileType("*.text"),
        new FileType("*.tld"),
        new FileType("*.txt"),
        new FileType("*.wml"),
        new FileType("*.wsdd"),
        new FileType("*.wsdl"),
        new FileType("*.wsil"),
        new FileType("*.wsimsg"),
        new FileType("*.xhtml"),
        new FileType("*.xls"),
        new FileType("*.xlsx"),
        new FileType("*.xmi"),
        new FileType("*.xml"),
        new FileType("*.xpt"),
        new FileType("*.xsd"),
        new FileType("*.xsl"),
        new FileType("*.xslt")
      });  
  }
  
  public static void main(String args[]) {
    PatternListDialog patternListDialog = new FileListDialog(null,"Select File Types", true);
    patternListDialog.setVisible(true);
  }  
}
