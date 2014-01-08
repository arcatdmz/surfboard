package jp.digitalmuseum.surfboard.apps;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import sun.swing.SwingUtilities2;

import jp.digitalmuseum.surfboard.*;

public class SurfboardForVideoSwitchingTabs implements SurfingListener {
	private Surfboard surfboard;
	private JTabbedPane p;
	private final String[] files;
	private ImagePanel[] panels;

	public static void main(String[] args) {
		new SurfboardForVideoSwitchingTabs();
	}

	public SurfboardForVideoSwitchingTabs() {

		// Initialize GUI.
		files = new String[] {
				"./images/a.jpg",
				"./images/b.jpg",
				"./images/c.jpg"
		};
		initializeGUI();

		// Start surfboard.
		surfboard = new SurfboardImpl();
		surfboard.addEventListener(this);
		surfboard.showFrame(true);
	}

	private void initializeGUI() {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Do nothing.
		}

		p = new JTabbedPane();
		panels = new ImagePanel[files.length];
		int i = 0;
		for (String file : files) {
			panels[i] = new ImagePanel(file);
			p.add(panels[i]);
			p.setTitleAt(i, file);
			i ++;
		}

		final JFrame f = new JFrame();
		f.getContentPane().add(p);
		f.pack();
		f.setSize(800, 600);
		try {
			f.setGlassPane(new JComponent() {
				private static final long serialVersionUID = 1L;
				private BufferedImage forwardImage = ImageIO.read(new File("./images/agt_forward-256.png"));
				private BufferedImage backImage = ImageIO.read(new File("./images/agt_back-256.png"));
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = SwingUtilities2.getGraphics2D(g);
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
					g2.drawImage(
							isSurfedToLeft ? forwardImage : backImage,
							getWidth()/2 - 128, getHeight()/2 - 128,
							null);
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		f.getGlassPane().setVisible(true);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				SurfboardForVideoSwitchingTabs.this.dispose();
			}
		});
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private float alpha = 0.0f;
	private boolean isSurfedToLeft;
	private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> future;
	private void switchTab(final boolean isSurfedToLeft) {
		this.isSurfedToLeft = isSurfedToLeft;
		if (isSurfedToLeft) {
			p.setSelectedIndex(
					(p.getSelectedIndex() + 1) % panels.length);
		} else {
			p.setSelectedIndex(
					(p.getSelectedIndex() + panels.length - 1) % panels.length);
		}
		alpha = 1.0f;
		p.repaint();
		future = service.scheduleAtFixedRate(new Runnable() {
			private int counter = 0;
			public void run() {
				if (counter == 10) {
					alpha = 0.0f;
					p.repaint();
					future.cancel(false);
					return;
				}
				alpha = 1.0f * (10 - counter ++) / 10;
				p.repaint();
			}
		}, 100, 50, TimeUnit.MILLISECONDS);
	}

	private void dispose() {
		surfboard.dispose();
	}

	public void onSurfingGesture() {
		// Do nothing.
	}

	public void onSurfingGesture(boolean isSurfedToLeft) {
		switchTab(isSurfedToLeft);
	}

}
