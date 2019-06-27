/**
 * Copyright (c) 2008 by Theo Nwosu. All Rights Reserved.
 *  
 * Theo Nwosu grants you a non-exclusive, royalty free, license to use, modify
 * and redistribute this software in source and binary code form provided,
 * that this copyright notice and license appear on all copies of the software. 
 * 
 * This software is provided "AS IS" without a warranty of any kind.
 */
package search.observers;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import search.agent.SearchAgent;
import search.agent.SearchGlobalVariables;
import search.util.Util;

/**
 * @author Theo Nwosu
 * 
 */
public class GUIObserver extends JFrame {
  private static final String SEARCH_FOR_A_FILE_TABBED_PANE_NAME = "Search For A File";
  private static final String SEARCH_INSIDE_A_FILE_TABBED_PANE_NAME = "Search Inside A File";
  private ControlPanel searchForAFileControlPanel = new SearchForAFileControlPanel(SearchAgent.SEARCH_AGENT);
  private ControlPanel searchInsideAFileControlPanel = new SearchWithinAFileControlPanel(SearchAgent.INSIDE_FILE_SEARCH_AGENT);
  private ArrayList listOfPanels = new ArrayList();

  public GUIObserver() {
    super("Archive Search Engine");
    Container contentPane = getContentPane();
    JMenuBar menuBar = createMenuBar();
    setJMenuBar(menuBar);
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.add(SEARCH_FOR_A_FILE_TABBED_PANE_NAME, searchForAFileControlPanel);
    tabbedPane.add(SEARCH_INSIDE_A_FILE_TABBED_PANE_NAME, searchInsideAFileControlPanel);
    panel.add(tabbedPane, BorderLayout.CENTER);
    contentPane.add(panel);

    listOfPanels.add(searchForAFileControlPanel);
    listOfPanels.add(searchInsideAFileControlPanel);
  }

  private void centerFrame() {
    Dimension dim = getToolkit().getScreenSize();
    setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);
  }

  private void addClosingListener() {
    addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        ControlPanel panel = null;
        for (int j = 0; j < listOfPanels.size(); j++) {
          panel = (ControlPanel) listOfPanels.get(j);
          panel.endSearch();
        }
        exitGraceFully();
      }
    });
  }

  private void exitGraceFully() {
    Thread stopThread = new Thread() {
      public void run() {
        ControlPanel panel1 = null;
        ControlPanel panel2 = null;
        for (int i = 0; i < listOfPanels.size(); i++) {
          panel1 = (ControlPanel) listOfPanels.get(i);
          while (!panel1.isFinished()) {
            for (int j = 0; j < listOfPanels.size(); j++) {
              panel2 = (ControlPanel) listOfPanels.get(j);
              panel2.getStatus().setText("Please wait...cleaning up");
            }
          }
        }
        System.exit(0);
      }
    };
    stopThread.start();
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('f');

    JMenuItem exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.setMnemonic('x');
    ActionListener exitListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    };
    exitMenuItem.addActionListener(exitListener);
    fileMenu.add(exitMenuItem);
    menuBar.add(fileMenu);

    JMenu helpMenu = new JMenu("Help");
    helpMenu.setMnemonic('h');

    JMenuItem aboutMenuItem = new JMenuItem("About");
    aboutMenuItem.setMnemonic('a');

    ActionListener aboutListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, SearchGlobalVariables.LICENSE);
      }
    };
    aboutMenuItem.addActionListener(aboutListener);
    helpMenu.add(aboutMenuItem);
    menuBar.add(helpMenu);

    return menuBar;
  }

  private static void setLookAndFeel() throws UnsupportedLookAndFeelException {
    // JDialog.setDefaultLookAndFeelDecorated(true);
    if (Util.isUnixOS()) {
      UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
    }
    else {
      UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
      // UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
    }
  }

  public static void main(String args[]) throws UnsupportedLookAndFeelException {
    setLookAndFeel();
    final GUIObserver f = new GUIObserver();
    f.initialize();
  }

  private void initialize() {
    setBounds(0, 0, 740, 540);
    centerFrame();
    addClosingListener();
    addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
        setCurrentFocus();
      }
    });
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setVisible(true);
  }

  public void setCurrentFocus() {
    searchForAFileControlPanel.setCurrentFocus();
  }
}
