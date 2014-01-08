package jp.digitalmuseum.surfboard;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jp.digitalmuseum.surfboard.sound.FrequencyComponentsListener;

public abstract class SurfboardAbstractImpl implements Surfboard, FrequencyComponentsListener {

	private ArrayList<SurfingListener> listeners;

	public SurfboardAbstractImpl() {

		//
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("Exception caught on thread: "+t);
				e.printStackTrace();
			}
		});

		// Setup
		listeners = new ArrayList<SurfingListener>();
	}

	public void addEventListener(SurfingListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeEventListener(SurfingListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected void distributeEvent(boolean isSurfedToLeft) {
		synchronized (listeners) {
			for (SurfingListener listener : listeners) {
				listener.onSurfingGesture(isSurfedToLeft);
			}
		}
	}

	protected void distributeEvent() {
		synchronized (listeners) {
			for (SurfingListener listener : listeners) {
				listener.onSurfingGesture();
			}
		}
	}

	public void showFrame(final boolean show) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				getFrame().setVisible(show);
			}
		});
	}

	abstract protected JFrame getFrame();
}
