package search.observers;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import search.matcher.SearchMatcher;
import search.observers.ControlPanel.FileTypeActionListener;

class SearchWithinAFileControlPanel extends ControlPanel {
  private static final String SEARCH_FILE_NAME_LABEL_TEXT = "Enter text to find in the content of a file";

  // private PatternListDialog fileListDialog = new FileListDialog(null,
  // "Select File Types", true);

  /**
   * @param searchAgentType
   */
  public SearchWithinAFileControlPanel(int searchAgentType) {
    super(searchAgentType, SEARCH_FILE_NAME_LABEL_TEXT);
    startButton.setText("Start");
    cancelButton.setText("Cancel");
    clearButton.setText("Clear");
  }

  protected int getMatcherType() {
    if (regularExpressionRadio.isSelected()) {
      return SearchMatcher.REGEX_MATCHER;
    }
    return SearchMatcher.CONTAINS_MATCHER;
  }

  /*
   * @see
   * search.observers.GUIObserver.ControlPanel#createSearchOptions(java.awt.
   * GridBagConstraints, javax.swing.JPanel)
   */
  protected void createSearchOptions(GridBagConstraints gbc, JPanel panel) {
    createSearchOptionsLabelAndCheckBox(gbc, panel);
  }

  /*
   * @see
   * search.observers.GUIObserver.ControlPanel#createPatternFields(java.awt.
   * GridBagConstraints, javax.swing.JPanel)
   */
  protected void createPatternFields(GridBagConstraints gbc, JPanel panel) {
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    panel.add(archivePatternButton, gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.BOTH;
    panel.add(archivePatternField, gbc);

    JButton filePatternButton = new JButton("File Types");
    filePatternButton.addActionListener(new FileTypeActionListener(
        fileListDialog, filePatternField));

    gbc.gridx = 2;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    panel.add(filePatternButton, gbc);

    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.BOTH;
    panel.add(filePatternField, gbc);
    filePatternButton.setToolTipText("Click to select file types to search");
    filePatternField.setToolTipText("Enter file types to search");
  }

  protected void createSearchControlButtons(GridBagConstraints gbc, JPanel panel) {
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    // An alternate method to receive events because this JPanel implements
    // ActionListener
    startButton.addActionListener(this);
    startButton.setHorizontalAlignment(SwingConstants.LEFT);

    cancelButton.setEnabled(false);
    addActionListener(cancelButton);

    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("");
        status.setText("");
      }
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(startButton);
    buttonPanel.add(cancelButton);
    buttonPanel.add(clearButton);
    panel.add(buttonPanel, gbc);

    gbc.gridx = 2;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    JButton fileExcludeButton = new JButton("Exclude Types");
    fileExcludeButton.setToolTipText("Click to select files to exclude");
    excludePatternField
        .setToolTipText("Enter file types to exclude separated by comma");
    fileExcludeButton.addActionListener(new FileTypeActionListener(
        fileListDialog2, excludePatternField));
    panel.add(fileExcludeButton, gbc);

    gbc.gridx = 3;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(excludePatternField, gbc);
  }
}
