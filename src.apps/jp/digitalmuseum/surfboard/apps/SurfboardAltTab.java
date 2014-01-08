package jp.digitalmuseum.surfboard.apps;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import jp.digitalmuseum.surfboard.*;

public class SurfboardAltTab implements SurfingListener {
	private Surfboard surfboard;
	private Robot robot;

	public static void main(String[] args) {
		new SurfboardAltTab();
	}

	public SurfboardAltTab() {

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

	public void onSurfingGesture() {
		try {
			robot.keyPress(KeyEvent.VK_ALT);
			Thread.sleep(10);
			robot.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(10);
			robot.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(10);
			robot.keyRelease(KeyEvent.VK_ALT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onSurfingGesture(boolean isSurfedToLeft) {
		// Do nothing.
	}

}
