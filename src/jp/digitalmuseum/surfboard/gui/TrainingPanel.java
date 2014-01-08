package jp.digitalmuseum.surfboard.gui;

import java.awt.Insets;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import java.awt.SystemColor;

import jp.digitalmuseum.surfboard.ml.SurfingLearning;

import javax.swing.JScrollBar;

public class TrainingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JToolBar jToolBar = null;
	private JButton jPositiveButton = null;
	private JButton jNegativeButton = null;
	private JLabel jTeachLabel = null;
	private JLabel jZoomLabel = null;
	private JButton jZoomInButton = null;
	private JButton jZoomOutButton = null;
	private JTextPane jTextPane = null;
	private TrainingGraphPanel jLearningGraphPanel = null;
	private SpectrumPanel spectrumPanelForLearning = null;

	private transient final SurfingLearning ml;
	private JPanel jCenterPanel = null;
	private JScrollBar jScrollBar = null;
	private boolean smallIcon;

	/**
	 * This is the default constructor
	 * @param spectrumPanelForLearning
	 */
	public TrainingPanel(final SurfingLearning bayes, final SpectrumPanel spectrumPanelForLearning) {
		this(bayes, spectrumPanelForLearning, false);
	}

	/**
	 * This is the default constructor
	 * @param spectrumPanelForLearning
	 */
	public TrainingPanel(final SurfingLearning ml, final SpectrumPanel spectrumPanelForLearning, final boolean smallIcon) {
		super();
		this.ml = ml;
		this.spectrumPanelForLearning = spectrumPanelForLearning;
		this.smallIcon = smallIcon;
		initialize();
	}

	public void setText(String text) {
		getJTextPane().setText(text);
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getJToolBar(), BorderLayout.NORTH);
		this.add(getJTextPane(), BorderLayout.SOUTH);
		this.add(getJCenterPanel(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jToolBar
	 *
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			jZoomLabel = new JLabel();
			jZoomLabel.setText("Zoom: ");
			jZoomLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
			jTeachLabel = new JLabel();
			jTeachLabel.setText("Mark: ");
			jTeachLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
			jToolBar = new JToolBar();
			jToolBar.setMargin(new Insets(5, 5, 5, 5));
			jToolBar.setFloatable(false);
			jToolBar.add(jTeachLabel);
			jToolBar.add(getJPositiveButton());
			jToolBar.add(getJNegativeButton());
			jToolBar.addSeparator();
			jToolBar.add(jZoomLabel);
			jToolBar.add(getJZoomInButton());
			jToolBar.add(getJZoomOutButton());
		}
		return jToolBar;
	}

	/**
	 * This method initializes jPositiveButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJPositiveButton() {
		if (jPositiveButton == null) {
			jPositiveButton = new JButton();
			jPositiveButton.setActionCommand("+");
			jPositiveButton.setToolTipText("Positive samples");
			jPositiveButton.addActionListener(getLearningGraphPanel());
			jPositiveButton.setIcon(new ImageIcon(smallIcon ?
					"./images/Sweetie-BasePack-v3/png-24/16-em-check.png" :
					"./images/Sweetie-BasePack-v3/png-24/24-em-check.png"));
		}
		return jPositiveButton;
	}

	/**
	 * This method initializes jNegativeButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJNegativeButton() {
		if (jNegativeButton == null) {
			jNegativeButton = new JButton();
			jNegativeButton.setActionCommand("-");
			jNegativeButton.setToolTipText("Negative samples");
			jNegativeButton.addActionListener(getLearningGraphPanel());
			jNegativeButton.setIcon(new ImageIcon(smallIcon ?
					"./images/Sweetie-BasePack-v3/png-24/16-em-cross.png" :
					"./images/Sweetie-BasePack-v3/png-24/24-em-cross.png"));
		}
		return jNegativeButton;
	}

	/**
	 * This method initializes jZoomInButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJZoomInButton() {
		if (jZoomInButton == null) {
			jZoomInButton = new JButton();
			jZoomInButton.setActionCommand(">");
			jZoomInButton.addActionListener(getLearningGraphPanel());
			jZoomInButton.setIcon(new ImageIcon(smallIcon ?
					"./images/Sweetie-BasePack-v3/png-24/16-zoom-in.png" :
					"./images/Sweetie-BasePack-v3/png-24/24-zoom-in.png"));
		}
		return jZoomInButton;
	}

	/**
	 * This method initializes jZoomOutButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJZoomOutButton() {
		if (jZoomOutButton == null) {
			jZoomOutButton = new JButton();
			jZoomOutButton.setActionCommand("<");
			jZoomOutButton.addActionListener(getLearningGraphPanel());
			jZoomOutButton.setIcon(new ImageIcon(smallIcon ?
					"./images/Sweetie-BasePack-v3/png-24/16-zoom-out.png" :
					"./images/Sweetie-BasePack-v3/png-24/24-zoom-out.png"));
		}
		return jZoomOutButton;
	}

	/**
	 * This method initializes jTextPane
	 *
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getJTextPane() {
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			jTextPane.setEditable(false);
			jTextPane.setBackground(SystemColor.controlHighlight);
		}
		return jTextPane;
	}

	/**
	 * This method initializes jNaiveBayesPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private TrainingGraphPanel getLearningGraphPanel() {
		if (jLearningGraphPanel == null) {
			jLearningGraphPanel = new TrainingGraphPanel(ml, spectrumPanelForLearning);
		}
		return jLearningGraphPanel;
	}

	/**
	 * This method initializes jCenterPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJCenterPanel() {
		if (jCenterPanel == null) {
			jCenterPanel = new JPanel();
			jCenterPanel.setLayout(new BorderLayout());
			jCenterPanel.add(getLearningGraphPanel(), BorderLayout.CENTER);
			jCenterPanel.add(getJScrollBar(), BorderLayout.SOUTH);
		}
		return jCenterPanel;
	}

	/**
	 * This method initializes jScrollBar
	 *
	 * @return javax.swing.JScrollBar
	 */
	private JScrollBar getJScrollBar() {
		if (jScrollBar == null) {
			jScrollBar = new JScrollBar();
			jScrollBar.setValue(100);
			jScrollBar.addAdjustmentListener(jLearningGraphPanel);
			jScrollBar.setOrientation(JScrollBar.HORIZONTAL);
		}
		return jScrollBar;
	}

}
