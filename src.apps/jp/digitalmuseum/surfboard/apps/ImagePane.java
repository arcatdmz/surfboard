package jp.digitalmuseum.surfboard.apps;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;

public class ImagePane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private JPanel jImagePanel_A = null;
	private JPanel jImagePanel_B = null;
	private JPanel jImagePanel_C = null;

	/**
	 * This is the default constructor
	 */
	public ImagePane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.addTab(null, null, getJImagePanel_A(), null);
		this.addTab(null, null, getJImagePanel_B(), null);
		this.addTab(null, null, getJImagePanel_C(), null);
	}

	/**
	 * This method initializes jImagePanel_A
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJImagePanel_A() {
		if (jImagePanel_A == null) {
			jImagePanel_A = new ImagePanel("images/a.jpg");
			jImagePanel_A.setLayout(new GridBagLayout());
		}
		return jImagePanel_A;
	}

	/**
	 * This method initializes jImagePanel_B
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJImagePanel_B() {
		if (jImagePanel_B == null) {
			jImagePanel_B = new ImagePanel("images/a.jpg");
			jImagePanel_B.setLayout(new GridBagLayout());
		}
		return jImagePanel_B;
	}

	/**
	 * This method initializes jImagePanel_C
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJImagePanel_C() {
		if (jImagePanel_C == null) {
			jImagePanel_C = new JPanel();
			jImagePanel_C.setLayout(new GridBagLayout());
		}
		return jImagePanel_C;
	}

}
