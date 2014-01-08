package jp.digitalmuseum.surfboard.gui;

import java.awt.GridBagLayout;
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

import jp.digitalmuseum.surfboard.sound.FrequencyComponents;
import jp.digitalmuseum.surfboard.sound.FrequencyComponentsListener;

public class SpectrumPanel extends JPanel implements FrequencyComponentsListener {

	private static final long serialVersionUID = 1L;
	private SpectrumGraphPanel graphPanel = null;
	private JToolBar jToolBar = null;
	private JLabel jZoomLabel = null;
	private JButton jZoomInButton = null;
	private JButton jZoomOutButton = null;
	private JTextPane jTextPane = null;

	private transient final boolean smallIcon;

	/**
	 * This is the default constructor
	 */
	public SpectrumPanel() {
		this(false);
	}

	public SpectrumPanel(final boolean smallIcon) {
		super();
		this.smallIcon = smallIcon;
		initialize();
	}

	public void setText(String text) {
		getJTextPane().setText(text);
	}

	public void frequencyComponentsUpdated(FrequencyComponents components) {
		getGraphPanel().frequencyComponentsUpdated(components);
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
		this.add(getGraphPanel(), BorderLayout.CENTER);
		this.add(getJTextPane(), BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jGraphPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private SpectrumGraphPanel getGraphPanel() {
		if (graphPanel == null) {
			graphPanel = new SpectrumGraphPanel(this);
			graphPanel.setLayout(new GridBagLayout());
		}
		return graphPanel;
	}

	/**
	 * This method initializes jToolBar
	 *
	 * @return javax.swing.JToolBar
	 */
	public JToolBar getJToolBar() {
		if (jToolBar == null) {
			jZoomLabel = new JLabel();
			jZoomLabel.setText("Zoom: ");
			jZoomLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
			jToolBar = new JToolBar();
			jToolBar.setMargin(new Insets(5, 5, 5, 5));
			jToolBar.setFloatable(false);
			jToolBar.add(jZoomLabel);
			jToolBar.add(getJZoomInButton());
			jToolBar.add(getJZoomOutButton());
		}
		return jToolBar;
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
			jZoomInButton.addActionListener(getGraphPanel());
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
			jZoomOutButton.addActionListener(getGraphPanel());
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
}
