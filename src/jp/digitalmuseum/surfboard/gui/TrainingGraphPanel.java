package jp.digitalmuseum.surfboard.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import jp.digitalmuseum.surfboard.ml.SurfingLearning;

public class TrainingGraphPanel extends JPanel implements ActionListener, AdjustmentListener {

	private static final long serialVersionUID = 1L;
	private final SurfingLearning ml;
	private final SpectrumPanel spectrumPanelForLearning;
	private int startIndex;
	private int monitoringCase = -1;
	private int zoom = 2;
	private float scroll;
	private int mouseX;
	private boolean isLeftClicking = false;
	private boolean isRightClicking = false;
	private boolean mode;

	public TrainingGraphPanel(final SurfingLearning ml, final SpectrumPanel spectrumPanelForLearning) {
		this.ml = ml;
		this.spectrumPanelForLearning = spectrumPanelForLearning;

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {
						if (isLeftClicking &&
								(e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0) {
							teach(mouseX, e.getX(), mode);
							isLeftClicking = false;
						} else if (isRightClicking &&
								(e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == 0) {
							monitorCase(e.getX());
							isRightClicking = false;
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {
						if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0) {
							isLeftClicking = true;
							mouseX = e.getX();
						} else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0) {
							isRightClicking = true;
						}
					}
				});

				addMouseMotionListener(new MouseMotionAdapter() {

					@Override
					public void mouseDragged(MouseEvent e) {
						if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0) {
							final int currentX = e.getX();
							teach(mouseX, e.getX(), mode);
							mouseX = currentX;
						} else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0) {
							monitorCase(e.getX());
						}
					}
				});
			}
		});
	}

	public int getMonitoringCase() {
		return monitoringCase;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(">"))
			setZoom(true);
		else if (e.getActionCommand().equals("<"))
			setZoom(false);
		else if (e.getActionCommand().equals("+")) {
			mode = true;
		} else if (e.getActionCommand().equals("-")) {
			mode = false;
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource() instanceof JScrollBar) {
			final JScrollBar scrollBar = (JScrollBar) e.getSource();
			scroll = (float) e.getValue() / (scrollBar.getMaximum() - scrollBar.getVisibleAmount());
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		startIndex = (int) (scroll * (ml.getSampleCaseSize()*zoom - getWidth()) / zoom);
		ml.paintSummary(g, startIndex, zoom, getWidth(), getHeight());
		if (monitoringCase >= 0) {
			g.setColor(Color.pink);
			g.drawRect((monitoringCase - startIndex)*zoom, -1, zoom, getHeight()+2);
		}
	}

	private void setZoom(boolean zoomIn) {
		if (zoomIn) {
			zoom ++;
		} else {
			zoom --;
			if (zoom <= 0) {
				zoom = 1;
			}
		}
	}

	private void teach(int start, int end, boolean mode) {
		if (start <= end) {
			for (int x = start/zoom; x <= end/zoom; x ++) {
				ml.teach(x + startIndex, mode);
			}
		} else {
			for (int x = end/zoom; x <= start/zoom; x ++) {
				ml.teach(x + startIndex, mode);
			}
		}
	}

	private void monitorCase(int x) {
		final int index = x/zoom + startIndex;
		if (index >= 0 &&
				index < ml.getSampleCaseSize()) {
			spectrumPanelForLearning.frequencyComponentsUpdated(
					ml.getSampleCase(index));
			spectrumPanelForLearning.repaint();
			monitoringCase = index;
		}
	}
}
