package jp.digitalmuseum.surfboard.gui;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import java.awt.Dimension;

import jp.digitalmuseum.surfboard.ml.SurfingLearning;
import jp.digitalmuseum.surfboard.sound.FrequencyComponents;
import jp.digitalmuseum.surfboard.sound.FrequencyComponentsListener;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

public class SurfboardPanel extends JPanel implements FrequencyComponentsListener {

	private static final long serialVersionUID = 1L;
	private JSplitPane jSplitPane = null;
	private JPanel jSpectrumPanel1 = null;
	private JPanel jTrainingDataPanel1 = null;
	private JPanel jStatusBarPanel = null;
	private JLabel jStatusBarLabel = null;
	private JProgressBar jProgressBar = null;
	private SpectrumPanel jSpectrumPanel = null;
	private TrainingPanel jTrainingDataPanel = null;
	private SpectrumPanel jTrainingSpectrumPanel = null;

	private final SurfingLearning ml;
	private JLabel jSpectrumLabel = null;
	private JLabel jTrainingDataLabel = null;
	private JLabel jTrainingSpectrumLabel = null;
	private JSplitPane jLeftVerticalSplitPane = null;
	private JPanel jLogPanel = null;
	private JLabel jLogLabel = null;
	private JTextArea jLogTextArea = null;
	private JToolBar jToolBar = null;

	private StringBuilder logText;
	private JScrollPane jScrollPane = null;
	private JSplitPane jSurfingSplitPane = null;
	private JPanel jTrainingSpectrumPanel1 = null;
	private JButton jTrainingSurfingButton = null;
	private JButton jMarkPositiveButton = null;
	private JButton jMarkNegativeButton = null;

	private ActionListener actionListener;
	private JTabbedPane jTabbedPane = null;
	private JLabel jTrainingLabel = null;
	private JLabel jDetectionLabel = null;
	private JPopupMenu jMarkPositivePopupMenu = null;
	private JMenuItem jLeftMenuItem = null;
	private JMenuItem jRightMenuItem = null;
	private JPanel jGesturePanel = null;
	private JLabel jDoGestureLabel = null;
	private JButton jDoGestureButton = null;

	/**
	 * This is the default constructor
	 */
	public SurfboardPanel(final SurfingLearning ml, final ActionListener actionListener) {
		super();
		this.ml = ml;
		this.actionListener = actionListener;
		logText = new StringBuilder();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(640, 480);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(800, 600));
		this.add(getJSplitPane(), BorderLayout.CENTER);
		this.add(getJStatusBarPanel(), BorderLayout.SOUTH);
		this.add(getJToolBar(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes jSplitPane
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getJLeftVerticalSplitPane());
			jSplitPane.setRightComponent(getJTabbedPane());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jSpectrumPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJSpectrumPanel1() {
		if (jSpectrumPanel1 == null) {
			jSpectrumLabel = new JLabel();
			jSpectrumLabel.setText("Real-time spectrum analysis:");
			jSpectrumLabel.setFont(new Font("Arial", Font.BOLD, 14));
			jSpectrumLabel.setToolTipText("Spectrum analysis of current recording sound.");
			jSpectrumLabel.setName("jSpectrumPanelLabel");
			GridBagConstraints constraint1 = new GridBagConstraints();
			constraint1.gridx = 0;
			constraint1.gridy = 0;
			constraint1.weightx = 1;
			constraint1.weighty = 0;
			constraint1.fill = GridBagConstraints.BOTH;
			constraint1.insets = new Insets(5, 5, 5, 15);
			GridBagConstraints constraint2 = new GridBagConstraints();
			constraint2.gridx = 0;
			constraint2.gridy = 1;
			constraint2.weightx = 1;
			constraint2.weighty = 1;
			constraint2.fill = GridBagConstraints.BOTH;
			jSpectrumPanel1 = new JPanel();
			jSpectrumPanel1.setLayout(new GridBagLayout());
			jSpectrumPanel1.setPreferredSize(new Dimension(300, 240));
			jSpectrumPanel1.setMinimumSize(new Dimension(300, 240));
			jSpectrumPanel1.add(jSpectrumLabel, constraint1);
			jSpectrumPanel1.add(getJSpectrumPanel(), constraint2);
		}
		return jSpectrumPanel1;
	}

	/**
	 * This method initializes jTrainingDataPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTrainingDataPanel1() {
		if (jTrainingDataPanel1 == null) {
			jTrainingDataLabel = new JLabel();
			jTrainingDataLabel.setText("Training data:");
			GridBagConstraints constraint1 = new GridBagConstraints();
			constraint1.gridx = 0;
			constraint1.gridy = 0;
			constraint1.weightx = 1;
			constraint1.weighty = 0;
			constraint1.fill = GridBagConstraints.BOTH;
			constraint1.insets = new Insets(5, 5, 5, 15);
			GridBagConstraints constraint2 = new GridBagConstraints();
			constraint2.gridx = 0;
			constraint2.gridy = 1;
			constraint2.weightx = 1;
			constraint2.weighty = 1;
			constraint2.fill = GridBagConstraints.BOTH;
			jTrainingDataPanel1 = new JPanel();
			jTrainingDataPanel1.setLayout(new GridBagLayout());
			jTrainingDataPanel1.setPreferredSize(new Dimension(300, 240));
			jTrainingDataPanel1.setMinimumSize(new Dimension(300, 240));
			jTrainingDataPanel1.add(jTrainingDataLabel, constraint1);
			jTrainingDataPanel1.add(getJTrainingDataPanel(), constraint2);
		}
		return jTrainingDataPanel1;
	}

	/**
	 * This method initializes jStatusBarPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJStatusBarPanel() {
		if (jStatusBarPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			jStatusBarPanel = new JPanel();
			jStatusBarPanel.setLayout(flowLayout);
			jStatusBarPanel.add(getJStatusBarLabel(), null);
			jStatusBarPanel.add(getJProgressBar(), null);
		}
		return jStatusBarPanel;
	}

	/**
	 * This method initializes jStatusBarLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private JLabel getJStatusBarLabel() {
		if (jStatusBarLabel == null) {
			jStatusBarLabel = new JLabel();
			jStatusBarLabel.setText("Ready.");
			jStatusBarLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			jStatusBarLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		}
		return jStatusBarLabel;
	}

	/**
	 * This method initializes jProgressBar
	 *
	 * @return javax.swing.JProgressBar
	 */
	public JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setPreferredSize(new Dimension(100, 14));
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jSpectrumPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private SpectrumPanel getJSpectrumPanel() {
		if (jSpectrumPanel == null) {
			jSpectrumPanel = new SpectrumPanel(true);
		}
		return jSpectrumPanel;
	}

	/**
	 * This method initializes jTrainingDataPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private TrainingPanel getJTrainingDataPanel() {
		if (jTrainingDataPanel == null) {
			jTrainingDataPanel = new TrainingPanel(ml, getJTrainingSpectrumPanel(), true);
			jTrainingDataPanel.setText("While recording, pressing Shift key will mark current recording data as positive sample.");
		}
		return jTrainingDataPanel;
	}

	/**
	 * This method initializes jTrainingSpectrumPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private SpectrumPanel getJTrainingSpectrumPanel() {
		if (jTrainingSpectrumPanel == null) {
			jTrainingSpectrumPanel = new SpectrumPanel(true);
			jTrainingSpectrumPanel.setText("Right click to select a sample and show its spectrum analysis.");
		}
		return jTrainingSpectrumPanel;
	}

	public void setText(String text) {
		getJStatusBarLabel().setText(text);
		logText.insert(0, "\n");
		logText.insert(0, text);
		getJLogTextArea().setText(logText.toString());
		JScrollBar jScrollBar = getJScrollPane().getVerticalScrollBar();
		jScrollBar.setValue(jScrollBar.getMinimum());
	}

	public void setDoGestureLabelText(String text) {
		getJDoGestureLabel().setText(text);
	}

	public void setDoGestureButtonEnabled(boolean enabled) {
		getJDoGestureButton().setEnabled(enabled);
	}

	public void setRecordingButtonEnabled(boolean enabled) {
		getJTrainingSurfingButton().setEnabled(enabled);
	}

	public void frequencyComponentsUpdated(FrequencyComponents components) {
		getJSpectrumPanel().frequencyComponentsUpdated(components);
	}

	/**
	 * This method initializes jLeftVerticalSplitPane
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJLeftVerticalSplitPane() {
		if (jLeftVerticalSplitPane == null) {
			jLeftVerticalSplitPane = new JSplitPane();
			jLeftVerticalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jLeftVerticalSplitPane.setBottomComponent(getJLogPanel());
			jLeftVerticalSplitPane.setTopComponent(getJSpectrumPanel1());
		}
		return jLeftVerticalSplitPane;
	}

	/**
	 * This method initializes jLogPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJLogPanel() {
		if (jLogPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 0;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 15);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			jLogLabel = new JLabel();
			jLogLabel.setText("Activity log:");
			jLogLabel.setFont(new Font("Arial", Font.BOLD, 14));
			jLogPanel = new JPanel();
			jLogPanel.setLayout(new GridBagLayout());
			jLogPanel.setPreferredSize(new Dimension(300, 280));
			jLogPanel.add(jLogLabel, gridBagConstraints1);
			jLogPanel.add(getJScrollPane(), gridBagConstraints2);
		}
		return jLogPanel;
	}

	/**
	 * This method initializes jLogTextArea
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJLogTextArea() {
		if (jLogTextArea == null) {
			jLogTextArea = new JTextArea();
			jLogTextArea.setLineWrap(true);
			jLogTextArea.setEditable(false);
		}
		return jLogTextArea;
	}

	/**
	 * This method initializes jToolBar
	 *
	 * @return javax.swing.JToolBar
	 */
	public JToolBar getJToolBar() {
		if (jToolBar == null) {
			jTrainingLabel = new JLabel();
			jTrainingLabel.setText("Training:");
			jDetectionLabel = new JLabel();
			jDetectionLabel.setText("Detection:");
			jToolBar = new JToolBar();
			jToolBar.add(jTrainingLabel);
			jToolBar.add(getJTrainingSurfingButton());
			jToolBar.addSeparator();
			jToolBar.add(jDetectionLabel);
			jToolBar.add(getJMarkPositiveButton());
			jToolBar.add(getJMarkNegativeButton());
		}
		return jToolBar;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJLogTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jSurfingSplitPane
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSurfingSplitPane() {
		if (jSurfingSplitPane == null) {
			jSurfingSplitPane = new JSplitPane();
			jSurfingSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSurfingSplitPane.setBottomComponent(getJTrainingSpectrumPanel1());
			jSurfingSplitPane.setTopComponent(getJTrainingDataPanel1());
		}
		return jSurfingSplitPane;
	}

	/**
	 * This method initializes jTrainingSpectrumPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTrainingSpectrumPanel1() {
		if (jTrainingSpectrumPanel1 == null) {
			jTrainingSpectrumLabel = new JLabel();
			jTrainingSpectrumLabel.setText("Spectrum analysis of selected training data:");
			GridBagConstraints constraint1 = new GridBagConstraints();
			constraint1.gridx = 0;
			constraint1.gridy = 0;
			constraint1.weightx = 1;
			constraint1.weighty = 0;
			constraint1.fill = GridBagConstraints.BOTH;
			constraint1.insets = new Insets(5, 5, 5, 15);
			GridBagConstraints constraint2 = new GridBagConstraints();
			constraint2.gridx = 0;
			constraint2.gridy = 1;
			constraint2.weightx = 1;
			constraint2.weighty = 1;
			constraint2.fill = GridBagConstraints.BOTH;
			jTrainingSpectrumPanel1 = new JPanel();
			jTrainingSpectrumPanel1.setLayout(new GridBagLayout());
			jTrainingSpectrumPanel1.setPreferredSize(new Dimension(300, 240));
			jTrainingSpectrumPanel1.setMinimumSize(new Dimension(300, 240));
			jTrainingSpectrumPanel1.add(jTrainingSpectrumLabel, constraint1);
			jTrainingSpectrumPanel1.add(getJTrainingSpectrumPanel(), constraint2);
		}
		return jTrainingSpectrumPanel1;
	}

	/**
	 * This method initializes jTrainingSurfingButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJTrainingSurfingButton() {
		if (jTrainingSurfingButton == null) {
			jTrainingSurfingButton = new JButton();
			jTrainingSurfingButton.setActionCommand(">");
			jTrainingSurfingButton.setToolTipText("Start recording samples for surfing detection.");
			jTrainingSurfingButton.setText("Surfing");
			jTrainingSurfingButton.setIcon(new ImageIcon("./images/Sweetie-BasePack-v3/png-24/24-book-blue-add.png"));
			jTrainingSurfingButton.addActionListener(actionListener);
		}
		return jTrainingSurfingButton;
	}

	/**
	 * This method initializes jMarkPositiveButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJMarkPositiveButton() {
		if (jMarkPositiveButton == null) {
			jMarkPositiveButton = new JButton();
			jMarkPositiveButton.setActionCommand("+");
			jMarkPositiveButton.setToolTipText("Mark the last detected sample as a gesture.");
			jMarkPositiveButton.setText("Mark as gesture");
			jMarkPositiveButton.setIcon(new ImageIcon("./images/Sweetie-BasePack-v3/png-24/24-book-blue-check.png"));
			jMarkPositiveButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					final JButton button = getJMarkPositiveButton();
					getJMarkPositivePopupMenu().show(
							button,
							button.getWidth()/2,
							button.getHeight()/2);
				}
			});
		}
		return jMarkPositiveButton;
	}

	/**
	 * This method initializes jMarkNegativeButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJMarkNegativeButton() {
		if (jMarkNegativeButton == null) {
			jMarkNegativeButton = new JButton();
			jMarkNegativeButton.setActionCommand("-");
			jMarkNegativeButton.setToolTipText("Mark the last detected sample as mistake (negative).");
			jMarkNegativeButton.setText("Mark as mistake");
			jMarkNegativeButton.setIcon(new ImageIcon("./images/Sweetie-BasePack-v3/png-24/24-book-blue-remove.png"));
			jMarkNegativeButton.addActionListener(actionListener);
		}
		return jMarkNegativeButton;
	}

	/**
	 * This method initializes jTabbedPane
	 *
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Surfing detection", null, getJSurfingSplitPane(), null);
			jTabbedPane.addTab("Gesture recognition", null, getJGesturePanel(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jMarkPositivePopupMenu
	 *
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJMarkPositivePopupMenu() {
		if (jMarkPositivePopupMenu == null) {
			jMarkPositivePopupMenu = new JPopupMenu();
			jMarkPositivePopupMenu.add(getJLeftMenuItem());
			jMarkPositivePopupMenu.add(getJRightMenuItem());
		}
		return jMarkPositivePopupMenu;
	}

	/**
	 * This method initializes jLeftMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJLeftMenuItem() {
		if (jLeftMenuItem == null) {
			jLeftMenuItem = new JMenuItem();
			jLeftMenuItem.setActionCommand("+L");
			jLeftMenuItem.addActionListener(actionListener);
			jLeftMenuItem.setText("Surfing to the left");
		}
		return jLeftMenuItem;
	}

	/**
	 * This method initializes jRightMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJRightMenuItem() {
		if (jRightMenuItem == null) {
			jRightMenuItem = new JMenuItem();
			jRightMenuItem.setActionCommand("+R");
			jRightMenuItem.addActionListener(actionListener);
			jRightMenuItem.setText("Surfing to the right");
		}
		return jRightMenuItem;
	}

	/**
	 * This method initializes jGesturePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJGesturePanel() {
		if (jGesturePanel == null) {
			jGesturePanel = new JPanel();
			jGesturePanel.setLayout(new FlowLayout());
			jGesturePanel.add(getJDoGestureLabel(), null);
			jGesturePanel.add(getJDoGestureButton(), null);
		}
		return jGesturePanel;
	}

	/**
	 * This method initializes jDoGestureLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private JLabel getJDoGestureLabel() {
		if (jDoGestureLabel == null) {
			jDoGestureLabel = new JLabel();
			jDoGestureLabel.setText("");
		}
		return jDoGestureLabel;
	}

	/**
	 * This method initializes jDoGestureButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJDoGestureButton() {
		if (jDoGestureButton == null) {
			jDoGestureButton = new JButton();
			jDoGestureButton.setActionCommand("<");
			jDoGestureButton.addActionListener(actionListener);
			jDoGestureButton.setText("Do gesture detection training");
		}
		return jDoGestureButton;
	}
}
