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

public class MonitorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private MonitorGraphPanel graphPanel = null;
	private JToolBar jToolBar = null;
	private JButton jVolumeUpButton = null;
	private JButton jVolumeDownButton = null;
	private JLabel jVolumeLabel = null;
	private JLabel jZoomLabel = null;
	private JButton jZoomInButton = null;
	private JButton jZoomOutButton = null;
	private JTextPane jTextPane = null;

	/**
	 * This is the default constructor
	 */
	public MonitorPanel() {
		super();
		initialize();
	}

	public void setText(String text) {
		getJTextPane().setText(text);
	}

	public void setBorderType(boolean b) {
		getGraphPanel().setBorderType(b);
	}

	public void offerValue(double value) {
		getGraphPanel().offerValue(value);
	}

	public int getSafeTime() {
		return getGraphPanel().getSafeTime();
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
	private MonitorGraphPanel getGraphPanel() {
		if (graphPanel == null) {
			graphPanel = new MonitorGraphPanel(this);
		}
		return graphPanel;
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
			jVolumeLabel = new JLabel();
			jVolumeLabel.setText("Volume: ");
			jVolumeLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
			jToolBar = new JToolBar();
			jToolBar.setMargin(new Insets(5, 5, 5, 5));
			jToolBar.setFloatable(false);
			jToolBar.add(jVolumeLabel);
			jToolBar.add(getJVolumeUpButton());
			jToolBar.add(getJVolumeDownButton());
			jToolBar.addSeparator();
			jToolBar.add(jZoomLabel);
			jToolBar.add(getJZoomInButton());
			jToolBar.add(getJZoomOutButton());
		}
		return jToolBar;
	}

	/**
	 * This method initializes jVolumeUpButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJVolumeUpButton() {
		if (jVolumeUpButton == null) {
			jVolumeUpButton = new JButton();
			jVolumeUpButton.setActionCommand("+");
			jVolumeUpButton.addActionListener(getGraphPanel());
			jVolumeUpButton.setIcon(new ImageIcon("./images/Sweetie-BasePack-v3/png-24/24-em-up.png"));
		}
		return jVolumeUpButton;
	}

	/**
	 * This method initializes jVolumeDownButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJVolumeDownButton() {
		if (jVolumeDownButton == null) {
			jVolumeDownButton = new JButton();
			jVolumeDownButton.setActionCommand("-");
			jVolumeDownButton.addActionListener(getGraphPanel());
			jVolumeDownButton.setIcon(new ImageIcon("./images/Sweetie-BasePack-v3/png-24/24-em-down.png"));
		}
		return jVolumeDownButton;
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
			jZoomInButton.setIcon(new ImageIcon("./images/Sweetie-BasePack-v3/png-24/24-zoom-in.png"));
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
			jZoomOutButton.setIcon(new ImageIcon("./images/Sweetie-BasePack-v3/png-24/24-zoom-out.png"));
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
