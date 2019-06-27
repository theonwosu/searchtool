/**
 * Copyright (c) 2008 by Theo Nwosu. All Rights Reserved.
 *  
 * Theo Nwosu grants you a non-exclusive, royalty free, license to use, modify
 * and redistribute this software in source and binary code form provided,
 * that this copyright notice and license appear on all copies of the software. 
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 */
package search.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
/**
 * @author Theophine Nwosu
 * 
 */
public class PatternListDialog extends JDialog {
  private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Cancel");
  private JButton selectAllButton = new JButton("Selct All");
  private JButton deSelectAllButton = new JButton("Deselect All");  
  private String selectedFiles = "";
  private JList patternsList = null;
  private boolean isCancelled = false;

  /**
   * @param owner
   * @param title
   * @param modal
   * @param filePatterns
   */
  public PatternListDialog(Frame owner, String title, boolean modal,FileType[] filePatterns) {
    super(owner, title, modal);
    CheckBoxListCellRenderer renderer = new CheckBoxListCellRenderer();
    patternsList = new JList(filePatterns);
    patternsList.setCellRenderer(renderer);
    CheckBoxListener listener = new CheckBoxListener(patternsList);
    patternsList.addMouseListener(listener);
    patternsList.addKeyListener(listener);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JScrollPane listScrollPane = new JScrollPane();
    listScrollPane.getViewport().add(patternsList);
    mainPanel.add(listScrollPane, BorderLayout.CENTER);

    JPanel selectAllPanel = new JPanel();
    selectAllPanel.add(selectAllButton);
    selectAllPanel.add(deSelectAllButton);
    selectAllButton.addActionListener(new SelectAllListener(patternsList));
    deSelectAllButton.addActionListener(new DeSelectAllListener(patternsList));
    mainPanel.add(selectAllPanel, BorderLayout.NORTH);   
    
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.add(okButton);
    buttonsPanel.add(cancelButton);
    okButton.addActionListener(new OkButtonListener(patternsList));
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        //remember last selection
        setSelected(selectedFiles.split(","));
        reset();
        setCancelled(true);
        dispose();
      }
    });
    mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

    getContentPane().add(mainPanel, BorderLayout.CENTER);
    setSize(250, 400);
    setResizable(false);
    setLocationRelativeTo(owner);
  }

  class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer {
    protected Border defaultBorder = new EmptyBorder(2, 2, 2, 2);

    public CheckBoxListCellRenderer() {
      setOpaque(true);
      setBorder(defaultBorder);
    }

    /* 
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
      if (value != null) {
        setText(value.toString());
      }
      setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
      setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
      FileType type = (FileType) value;
      setSelected(type.isSelected());
      setFont(list.getFont());
      setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : defaultBorder);
      return this;
    }
  }

  public class CheckBoxListener extends MouseAdapter implements KeyListener {
    JList list = null;

    public CheckBoxListener(JList list) {
      this.list = list;
    }

    public void mouseClicked(MouseEvent e) {
      if (e.getX() < 16) {
        check();
      }
    }

    public void keyPressed(KeyEvent e) {
      if (e.getKeyChar() == ' ') {
        check();
      }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    protected void check() {
      if (list != null) {
        int index = list.getSelectedIndex();
        if (index < 0) {
          return;
        }
        FileType type = (FileType) list.getModel().getElementAt(index);
        type.negateSelected();
        list.repaint();
      }
    }
  }
  
  abstract class AbstractSelectListener implements ActionListener {
    protected JList list = null;

    public AbstractSelectListener(JList list) {
      this.list = list;
    }

    public void actionPerformed(ActionEvent e) {
      if (list != null) {
        ListModel model = list.getModel();
        FileType type = null;
        for (int k = 0; k < model.getSize(); k++) {
          type = (FileType) model.getElementAt(k);
          invertSelection(type);       
        }
        list.repaint();
      }  
    }

    abstract protected void invertSelection(FileType type);
  }
  
  class SelectAllListener extends AbstractSelectListener {
    public SelectAllListener(JList list) {
      super(list);
    }

    protected void invertSelection(FileType type) {
      if (!type.isSelected()) {
        type.negateSelected();
      }
    }
  }  

  class DeSelectAllListener extends AbstractSelectListener {
    public DeSelectAllListener(JList list) {
      super(list);
    }
    
    protected void invertSelection(FileType type){
      if (type.isSelected()) {
        type.negateSelected();
      }
    }    
  }  
  
  class OkButtonListener implements ActionListener {
    private JList list = null;

    public OkButtonListener(JList list) {
      this.list = list;
    }

    public void actionPerformed(ActionEvent e) {
      if (list != null) {
        ListModel model = list.getModel();
        FileType type = null;
        String comma = "";
        selectedFiles = "";
        for (int k = 0; k < model.getSize(); k++) {
          type = (FileType) model.getElementAt(k);
          if (type.isSelected()) {
            selectedFiles = selectedFiles + comma + type;
            comma = ",";
          }
        }
      }
      reset();
      setCancelled(false);
      dispose();
    }
  }
  
  protected void reset(){
//    ListModel model = patternsList.getModel();
//    FileType type = null;
//    for (int k = 0; k < model.getSize(); k++) {
//      type = (FileType) model.getElementAt(k);
//      if (type.isSelected()) {
//        type.negateSelected();
//      }
//    } 
    ListSelectionModel listSelectionModel = patternsList.getSelectionModel();
    listSelectionModel.clearSelection();
    //reset scroll bar
    if(patternsList.getModel().getSize() > 0){
      patternsList.ensureIndexIsVisible(0);
    }
  }
  
  private void setSelected(String [] selection){
    ListModel model = patternsList.getModel();
    FileType type = null;
    boolean select = false;
    for (int k = 0; k < model.getSize(); k++) {
      type = (FileType) model.getElementAt(k);
      if (type.isSelected()) {
        for(int i = 0; selection !=  null && i < selection.length; i++){
          if(type.toString().equals(selection[i])){
            select = true;
            break;
          }
        }
      }
      else{
        for(int i = 0; selection !=  null && i < selection.length; i++){
          if(type.toString().equals(selection[i])){
            type.negateSelected();
            select = true;
            break;
          }
        }        
      }
      if(!select){
        if (type.isSelected()) {
          type.negateSelected();
        }        
      }
      select = false;
    }
  }

  public String getSelectedFiles() {
    return selectedFiles;
  }

  public boolean isCancelled() {
    return isCancelled;
  }

  public void setCancelled(boolean isCancelled) {
    this.isCancelled = isCancelled;
  }
}
