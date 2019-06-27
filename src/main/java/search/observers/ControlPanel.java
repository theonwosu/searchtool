package search.observers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import search.agent.SearchAgent;
import search.agent.SearchAgentFactory;
import search.dialogs.ArchiveListDialog;
import search.dialogs.FileListDialog;
import search.dialogs.PatternListDialog;
import search.matcher.SearchMatcher;

abstract class ControlPanel extends JPanel implements ActionListener, Runnable,
    SearchObserver {
  private static final String LEAVE_BLANK_TO_SEARCH_ALL_DIRECTORIES = "Leave Blank to Search All Directories";
  private JButton javaHomeButton = new JButton("Click to select Java Home");
  private JButton directoryButton = new JButton(
      "Click to select Search Archive/Directory");
  private JTextField javaHomeField = new JTextField(25);
  private JTextField directoryField = new JTextField(25);
  private JTextField searchFileNameField = new JTextField(25);
  protected JButton startButton = new JButton("Start Search");
  protected JButton cancelButton = new JButton("Cancel Search");
  protected JButton clearButton = new JButton("Clear Console");
  private final JFileChooser chooser = new JFileChooser();
  private static final String ARCHIVE_PATTERNS = "*.zip, *.ear, *.war, *.jar";
  protected JTextArea textArea = new JTextArea();
  protected JLabel status = new JLabel("");
  private boolean isFinished = true;
  private SearchAgent agent = null;
  private static final String FILE_PATTERNS = "*.java, *.properties, *.xml";
  protected JLabel searchFileNameLabel = null;
  protected final int SEARCH_AGENT_TYPE;
  protected JCheckBox caseSensitivityCheckbox = null;
  protected JButton archivePatternButton = new JButton("Archive Types");
  protected JTextField archivePatternField = new JTextField(ARCHIVE_PATTERNS,
      10);
  protected PatternListDialog archiveListDialog = new ArchiveListDialog(null,
      "Select File Types", true);
  protected PatternListDialog fileListDialog = new FileListDialog(null,
      "Select File Types", true);
  protected PatternListDialog fileListDialog2 = new FileListDialog(null,
      "Select File Types", true);
  protected JTextField filePatternField = new JTextField(FILE_PATTERNS, 10);
  protected JTextField excludePatternField = new JTextField(10);

  protected JRadioButton completeNameRadio = null;
  protected JRadioButton containsRadio = null;
  protected JRadioButton regularExpressionRadio = null;

  private final Highlighter highLighter = new DefaultHighlighter();
  private final Color HIGH_LIGHT_COLOR = Color.CYAN;
  private final Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(
      HIGH_LIGHT_COLOR);
  private int startIndex = 0;
  protected boolean highLightingEnabled = true;
  protected JCheckBox highlightCheckbox = new JCheckBox("Enable Highlighting",
      true);
  protected int count = 0;

  /**
   * @param searchAgentType
   * @param searchFileNameLabelText
   */
  public ControlPanel(int searchAgentType, String searchFileNameLabelText) {
    textArea.setHighlighter(highLighter);
    addItemListner(highlightCheckbox);

    this.searchFileNameLabel = new JLabel(searchFileNameLabelText);
    SEARCH_AGENT_TYPE = searchAgentType;
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel panel = new JPanel();
    panel.setLayout(gbl);
    setLayout(new BorderLayout());

    gbc.insets = new Insets(2, 0, 2, 3);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = 2;
    panel.add(javaHomeButton, gbc);
    javaHomeButton.setHorizontalAlignment(SwingConstants.LEFT);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.VERTICAL;
    gbc.gridwidth = 2;
    panel.add(javaHomeField, gbc);
    addActionListener(javaHomeButton, javaHomeField);

    gbc.insets = new Insets(2, 0, 5, 3);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(directoryButton, gbc);
    directoryButton.setHorizontalAlignment(SwingConstants.LEFT);
    directoryButton.setToolTipText(LEAVE_BLANK_TO_SEARCH_ALL_DIRECTORIES);

    gbc.insets = new Insets(2, 0, 5, 3);
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel.add(directoryField, gbc);
    addActionListener(directoryButton, directoryField);
    directoryField.setToolTipText(LEAVE_BLANK_TO_SEARCH_ALL_DIRECTORIES);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridwidth = 2;
    panel.add(searchFileNameLabel, gbc);

    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    panel.add(searchFileNameField, gbc);

    createPatternFields(gbc, panel);
    archivePatternButton
        .setToolTipText("Click to select archive types to search");
    archivePatternField.setToolTipText("Enter archive types to search");
    archivePatternButton.addActionListener(new FileTypeActionListener(
        archiveListDialog, archivePatternField));

    createSearchControlButtons(gbc, panel);

    createSearchOptions(gbc, panel);
    add(panel, BorderLayout.NORTH);
    add(new JScrollPane(textArea), BorderLayout.CENTER);
    add(status, BorderLayout.SOUTH);
    textArea.setEditable(false);
  }

  abstract protected void createSearchControlButtons(GridBagConstraints gbc,
      JPanel panel);

  /**
   * @param gbc
   * @param panel
   */
  abstract protected void createPatternFields(GridBagConstraints gbc,
      JPanel panel);

  /**
   * @param gbc
   * @param panel
   */
  abstract protected void createSearchOptions(GridBagConstraints gbc,
      JPanel panel);

  /**
   * @param gbc
   * @param panel
   */
  protected void createSearchOptionsLabelAndCheckBox(GridBagConstraints gbc,
      JPanel panel) {
    gbc.gridx = 4;
    gbc.gridy = 0;
    JLabel searchOptionsLabel = new JLabel("Search Options");
    panel.add(searchOptionsLabel, gbc);

    gbc.gridx = 4;
    gbc.gridy = 1;
    caseSensitivityCheckbox = new JCheckBox("Ignore Case");
    panel.add(caseSensitivityCheckbox, gbc);

    gbc.gridx = 4;
    gbc.gridy = 2;
    panel.add(highlightCheckbox, gbc);

    gbc.gridx = 4;
    gbc.gridy = 3;
    regularExpressionRadio = new JRadioButton("Regular Expression");
    panel.add(regularExpressionRadio, gbc);
    addItemListener(regularExpressionRadio, false);

    gbc.gridx = 4;
    gbc.gridy = 4;
    containsRadio = new JRadioButton("Contains Name");
    containsRadio.setSelected(true);
    panel.add(containsRadio, gbc);
    addItemListener(containsRadio, true);

    ButtonGroup group = new ButtonGroup();
    group.add(containsRadio);
    group.add(regularExpressionRadio);
  }

  protected void addItemListner(final JCheckBox cb) {
    cb.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (cb.isSelected()) {
          setHighLightingEnabled(true);
          highlight(searchFileNameField.getText().trim(), 0);
        }
        else {
          setHighLightingEnabled(false);
          highLighter.removeAllHighlights();
        }
      }
    });
  }

  class FileTypeActionListener implements ActionListener {
    private String selectedFiles = null;
    private PatternListDialog patternListDialog = null;
    private JTextField patternField = null;

    /**
     * @param patternListDialog
     * @param patternField
     */
    public FileTypeActionListener(PatternListDialog patternListDialog,
        JTextField patternField) {
      this.patternListDialog = patternListDialog;
      this.patternField = patternField;
    }

    /*
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent evt) {
      patternListDialog.setVisible(true);
      if (!patternListDialog.isCancelled()) {
        selectedFiles = patternListDialog.getSelectedFiles();
        patternField.setText(selectedFiles);
      }
    }
  }

  /*
   * An alternate method to receive events because this JPanel implements
   * ActionListener (non-Javadoc)
   * 
   * @see
   * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    Thread thread = new Thread(this);
    thread.start();
  }

  public void run() {
    try {
      count = 0;
      startIndex = 0;
      textArea.setText("");
      startButton.setEnabled(false);
      cancelButton.setEnabled(true);
      isFinished = false;
      status.setText("Search Started...");
      startObservation(javaHomeField.getText(), directoryField.getText(),
          searchFileNameField.getText(), getMatcherType(),
          getCaseSensitivity(), SEARCH_AGENT_TYPE, archivePatternField
              .getText(), filePatternField.getText());
      finishUp();
    }
    catch (Exception e) {
      finishUp();
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
  }

  private void finishUp() {
    status.setText(getEndStatus());
    isFinished = true;
    startButton.setEnabled(true);
    cancelButton.setEnabled(false);
  }

  private String getEndStatus() {
    return "Search Ended: matched " + count + " item(s)";
  }

  /**
   * @param java_home
   * @param directory
   * @param searchStr
   * @param matcherType
   * @param caseSensitivity
   * @param searchAgentType
   * @param archivesToSearch
   * @param filesToFind
   * @throws Exception
   */
  protected void startObservation(String java_home, String directory,
      String searchStr, int matcherType, int caseSensitivity,
      int searchAgentType, String archivesToSearch, String filesToFind)
      throws Exception {
    if (agent == null) {
      agent = SearchAgentFactory.getInstance().getSearchAgent(searchAgentType,
          archivesToSearch, filesToFind);
      agent.setExcludePatterns(excludePatternField.getText());
      agent.setMatcherType(matcherType);
      agent.setCaseSensitivity(caseSensitivity);
      agent.add(this);
    }
    else {
      agent.reset(archivesToSearch, filesToFind, excludePatternField.getText(),
          matcherType, caseSensitivity);
    }
    if (directory == null || directory.trim().length() == 0) {
      int value = JOptionPane
          .showConfirmDialog(
              null,
              "You did not specify a directory.\nDo you want to search entire file system?",
              "", JOptionPane.YES_NO_OPTION);
      if (value == JOptionPane.NO_OPTION) {
        return;
      }
      agent.processFiles(searchStr, java_home);
    }
    else {
      agent.processFiles(directory, searchStr, java_home);
    }
  }

  protected int getMatcherType() {
    if (completeNameRadio.isSelected()) {
      return SearchMatcher.EXACT_MATCHER;
    }
    else if (regularExpressionRadio.isSelected()) {
      return SearchMatcher.REGEX_MATCHER;
    }
    return SearchMatcher.CONTAINS_MATCHER;
  }

  private int getCaseSensitivity() {
    if (caseSensitivityCheckbox.isSelected()) {
      return SearchMatcher.IGNORE_CASE;
    }
    return SearchMatcher.CASE_SENSITIVE;
  }

  /**
   * @param button
   */
  protected void addActionListener(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        endSearch();
        status.setText("Please wait, cleaning up...");
      }
    });
  }

  /**
   * @param button
   * @param textField
   */
  private void addActionListener(JButton button, final JTextField textField) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int state = chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        // chooser.setAcceptAllFileFilterUsed(false);
        if (file != null && state == JFileChooser.APPROVE_OPTION) {
          textField.setText(file.getPath());
        }
        else if (state == JFileChooser.ERROR_OPTION) {
          JOptionPane.showMessageDialog(null, "Error!");
        }
      }
    });
  }

  public void setCurrentFocus() {
    javaHomeField.requestFocus();
  }

  private JButton getStartButton() {
    return startButton;
  }

  /*
   * @see search.observers.SearchObserver#updateResult(java.lang.String)
   */
  public void updateResult(String value) {
    // Toolkit.getDefaultToolkit().beep();
    if (value != null) {
      String[] temp = value.split(SearchAgent.FILE_INFO);
      if (temp != null && temp.length == 2) {
        value = temp[1];
      }
      else {
        count++;
      }
      textArea.append(value + "\n");
      if (isHighLightingEnabled()) {
        highlight(searchFileNameField.getText().trim(), startIndex);
      }
    }
  }

  /*
   * @see search.observers.SearchObserver#updateStatus(java.lang.String)
   */
  public void updateStatus(String value) {
    status.setText(value);
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean isFinished) {
    this.isFinished = isFinished;
  }

  public JLabel getStatus() {
    return status;
  }

  public void setStatus(JLabel status) {
    this.status = status;
  }

  public void endSearch() {
    if (agent != null) {
      agent.cancel();
    }
  }

  private void highlight(String text, int startIndex_p) {
    if (text.length() <= 0) {
      return;
    }

    String content = textArea.getText();
    int index = getIndex(text, content, startIndex_p);
    int end = 0;
    while (index >= 0) {
      try {
        end = index + text.length();
        highLighter.addHighlight(index, end, painter);
        startIndex = end;
        index = getIndex(text, content, startIndex);
      }
      catch (BadLocationException e) {
        e.printStackTrace();
      }
    }
  }

  private int getIndex(String text, String content, int startIndex) {
    int index = getCaseSensitivity() == SearchMatcher.IGNORE_CASE ? content
        .toLowerCase().indexOf(text.toLowerCase(), startIndex) : content
        .indexOf(text, startIndex);
    return index;
  }

  protected boolean isHighLightingEnabled() {
    return highLightingEnabled;
  }

  protected void setHighLightingEnabled(boolean highLightingEnabled) {
    this.highLightingEnabled = highLightingEnabled;
  }

  protected void addItemListener(final JRadioButton radioButton,
      final boolean action) {
    radioButton.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (radioButton.isSelected()) {
          if (!action) {
            highlightCheckbox.setSelected(action);
          }
          highlightCheckbox.setEnabled(action);
        }
      }
    });
  }
}
