package jp.digitalmuseum.surfboard.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JPanel;

public class MonitorGraphPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int minimumBandWidth = 1;
	private static final double minimumCoeffFrequencyComponent = 0.0003;

	private int bandWidth = minimumBandWidth;
	private double coeffFrequencyComponent = minimumCoeffFrequencyComponent*8;
	private LinkedList<Double> values;
	private double borderValue = Double.MAX_VALUE;
	private boolean hasPositiveBorder = true;

	private int safeTime = 0;

	public MonitorGraphPanel(final MonitorPanel monitorPanel) {
		values = new LinkedList<Double>();
		setBackground(Color.black);

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0) {
					borderValue = (getHeight() - e.getY()) / coeffFrequencyComponent;
					monitorPanel.setText(
							"Border value is set to "+((int) borderValue)+".");
				}
			}
		});
	}

	public void offerValue(double value) {
		synchronized (values) {
			while (values.size() + 1 >= getWidth() / bandWidth) {
				values.poll();
			}
			values.offer(value);
			if ((hasPositiveBorder && value > borderValue) ||
					(!hasPositiveBorder && value < borderValue)) {
				safeTime ++;
			} else {
				safeTime = 0;
			}
		}
	}

	public int getSafeTime() {
		return safeTime;
	}

	public double getBorderValue() {
		return borderValue;
	}

	public boolean hasPositiveBorder() {
		return hasPositiveBorder;
	}

	public boolean hasNegativeBorder() {
		return !hasPositiveBorder;
	}

	/**
	 * @param b Positive (true) or negative (false).
	 */
	public void setBorderType(boolean b) {
		if (hasPositiveBorder && borderValue == Double.MAX_VALUE && !b) {
			borderValue = Double.MIN_VALUE;
		} else if (!hasPositiveBorder && borderValue == Double.MAX_VALUE && b) {
			borderValue = Double.MAX_VALUE;
		}
		hasPositiveBorder = b;
	}

	public void setVolume(boolean volumeUp) {
		coeffFrequencyComponent = volumeUp ?
				coeffFrequencyComponent * 2 : coeffFrequencyComponent / 2;
		if (coeffFrequencyComponent < minimumCoeffFrequencyComponent) {
			coeffFrequencyComponent = minimumCoeffFrequencyComponent;
		}
	}

	public void setZoom(boolean zoomIn) {
		bandWidth = zoomIn ?
				bandWidth * 2 : bandWidth / 2;
		if (bandWidth < minimumBandWidth) {
			bandWidth = minimumBandWidth;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(">"))
			setZoom(true);
		else if (e.getActionCommand().equals("<"))
			setZoom(false);
		else if (e.getActionCommand().equals("-"))
			setVolume(false);
		else if (e.getActionCommand().equals("+"))
			setVolume(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintValues(g);
		paintAxis(g);
	}

	private void paintValues(Graphics g) {
		g.setColor(Color.green);
		synchronized (values) {
			if (values.isEmpty()) {
				return;
			}
			int x = 0, y = getHeight() - (int) (values.peek() * coeffFrequencyComponent);
			for (double vol : values) {
				int v = getHeight() - (int) (vol * coeffFrequencyComponent);

				//
				if (hasPositiveBorder && vol > borderValue) {
					g.setColor(Color.orange);
					g.drawLine(x, y, x, getHeight());
					g.setColor(Color.green);
				} else if (!hasPositiveBorder && vol < borderValue) {
					g.setColor(Color.cyan);
					g.drawLine(x, y, x, getHeight());
					g.setColor(Color.green);
				} else {
					g.drawLine(x, y, x, getHeight());
				}
				g.drawLine(x, y, x+bandWidth, v);
				x += bandWidth;
				y = v;
			}
		}
	}

	private void paintAxis(Graphics g) {

		// Y axis.
		g.setColor(Color.lightGray);
		GUIHelper.paintHorizontalAxis(g, getWidth() - 30, getHeight(), coeffFrequencyComponent);

		// Border line.
		g.setColor(hasPositiveBorder ? Color.orange : Color.cyan);
		int y = getHeight() - (int) (borderValue * coeffFrequencyComponent);
		g.drawLine(0, y, getWidth() - 30, y);
	}

}
