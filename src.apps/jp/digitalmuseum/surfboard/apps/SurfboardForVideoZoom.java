package jp.digitalmuseum.surfboard.apps;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import jp.digitalmuseum.surfboard.*;

public class SurfboardForVideoZoom implements SurfingListener {
	private Surfboard surfboard;
	private Window window;
	private Window window2;

	public static void main(String[] args) {
		new SurfboardForVideoZoom();
	}

	public SurfboardForVideoZoom() {

		initializeGUI();

		// Start surfboard.
		surfboard = new SurfboardImpl();
		surfboard.addEventListener(this);
		surfboard.showFrame(true);
	}

	private void initializeGUI() {

		window = new Window(new Frame());
		window.add(new ImagePanel("./images/zoom-out.png"));
		window.setSize(1400, 1050);
		window.setVisible(true);
		window.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				SurfboardForVideoZoom.this.dispose();
			}
		});

		window2 = new Window(new Frame());
		window2.add(new ImagePanel("./images/zoom-in.png"));
		window2.setSize(1400, 1050);
		window2.setVisible(true);
		window2.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				SurfboardForVideoZoom.this.dispose();
			}
		});
	}

	private void dispose() {
		surfboard.dispose();
		window.dispose();
		window2.dispose();
	}

	public void onSurfingGesture() {
		// Do nothing.
	}

	public void onSurfingGesture(boolean isSurfedToLeft) {
		if (isSurfedToLeft) {
			window.toFront();
		} else {
			window2.toFront();
		}
	}

}
