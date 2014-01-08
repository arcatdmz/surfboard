package jp.digitalmuseum.surfboard.apps;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import jp.digitalmuseum.surfboard.*;

public class SurfboardForVideoPlay implements SurfingListener {
	private Surfboard surfboard;
	private Window window;
	private Robot robot;

	public static void main(String[] args) {
		new SurfboardForVideoPlay();
	}

	public SurfboardForVideoPlay() {

		initializeGUI();

		// Start surfboard.
		surfboard = new SurfboardImpl();
		surfboard.addEventListener(this);
		surfboard.showFrame(true);

		// Initialize robot.
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void initializeGUI() {

		window = new Window(new Frame());
		window.add(new ImagePanel("./images/screen.png"));
		window.setSize(1400, 1050);
		window.setVisible(true);
		window.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				SurfboardForVideoPlay.this.dispose();
			}
		});
	}

	private void dispose() {
		surfboard.dispose();
		window.dispose();
	}

	public void onSurfingGesture() {
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyPress(KeyEvent.VK_M);
		robot.keyRelease(KeyEvent.VK_M);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
	}

	public void onSurfingGesture(boolean isSurfedToLeft) {
		// Do nothing.
	}

}
