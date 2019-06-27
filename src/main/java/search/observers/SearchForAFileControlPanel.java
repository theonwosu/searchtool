package search.observers;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import search.observers.ControlPanel;

public class SearchForAFileControlPanel extends ControlPanel {
  private static final String SEARCH_FILE_NAME_LABEL_TEXT = "Enter file name to find";

  public SearchForAFileControlPanel(int searchAgentType) {
    super(searchAgentType, SEARCH_FILE_NAME_LABEL_TEXT);
    setHighLightingEnabled(false);
    highlightCheckbox.setSelected(false);
  }

  /**
   * @param gbc
   * @param panel
   */
  protected void createSearchOptions(GridBagConstraints gbc, JPanel panel) {
    // createSearchOptionsLabelAndCheckBox(gbc, panel);
    // gbc.gridx = 4;
    // gbc.gridy = 0;
    // JLabel searchOptionsLabel = new JLabel("Search Options");
    // panel.add(searchOptionsLabel, gbc);

    gbc.gridx = 4;
    gbc.gridy = 0;
    caseSensitivityCheckbox = new JCheckBox("Ignore Case");
    panel.add(caseSensitivityCheckbox, gbc);

    gbc.gridx = 4;
    gbc.gridy = 1;
    panel.add(highlightCheckbox, gbc);

    gbc.gridx = 4;
    gbc.gridy = 2;
    regularExpressionRadio = new JRadioButton("Regular Expression");
    panel.add(regularExpressionRadio, gbc);
    addItemListener(regularExpressionRadio, false);

    gbc.gridx = 4;
    gbc.gridy = 3;
    completeNameRadio = new JRadioButton("Exact Name");
    completeNameRadio.setSelected(true);
    panel.add(completeNameRadio, gbc);
    addItemListener(completeNameRadio, true);

    gbc.gridx = 4;
    gbc.gridy = 4;
    containsRadio = new JRadioButton("Contains Name");
    panel.add(containsRadio, gbc);
    addItemListener(containsRadio, true);

    ButtonGroup group = new ButtonGroup();
    group.add(completeNameRadio);
    group.add(containsRadio);
    group.add(regularExpressionRadio);
  }

  protected void createPatternFields(GridBagConstraints gbc, JPanel panel) {
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    archivePatternButton.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(archivePatternButton, gbc);

    gbc.gridx = 2;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    panel.add(archivePatternField, gbc);
  }

  protected void createSearchControlButtons(GridBagConstraints gbc, JPanel panel) {
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(startButton, gbc);
    // An alternate method to receive events because this JPanel implements
    // ActionListener
    startButton.addActionListener(this);
    startButton.setHorizontalAlignment(SwingConstants.LEFT);

    gbc.gridx = 2;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    panel.add(cancelButton, gbc);
    cancelButton.setEnabled(false);
    addActionListener(cancelButton);

    gbc.gridx = 3;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    panel.add(clearButton, gbc);
    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("");
        status.setText("");
      }
    });
  }
}