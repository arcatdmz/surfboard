package jp.digitalmuseum.surfboard.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jp.digitalmuseum.surfboard.sound.FastFourierTransformer;
import jp.digitalmuseum.surfboard.sound.FrequencyComponents;
import jp.digitalmuseum.surfboard.sound.FrequencyComponentsListener;

public class SpectrumGraphPanel extends JPanel implements ActionListener, FrequencyComponentsListener {

	private static final long serialVersionUID = 1L;
	private FrequencyComponents components;

	private boolean isDragging = false;
	private int offsetX;
	private int zoomX;
	private int mouseX = 0;

	public SpectrumGraphPanel(final SpectrumPanel spectrumPanel) {
		super();
		offsetX = 0;
		zoomX = 1;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				setBackground(Color.black);

				addMouseMotionListener(new MouseMotionListener() {

					public void mouseDragged(MouseEvent e) {
						final int x = e.getX();
						if (isDragging) {
							setOffsetX(offsetX + x - mouseX);
						}
						isDragging = true;
						mouseX = x;
					}

					public void mouseMoved(MouseEvent e) {
						isDragging = false;
						mouseX = e.getX();
					}

				});
			}
		});
	}

	public boolean setOffsetX(int offsetX) {
		this.offsetX = offsetX;
		return true;
	}

	public void setZoom(boolean zoomIn) {
		final int graphWidth = getWidth();
		if (zoomIn) {
			zoomX = zoomX * 2;
			setOffsetX(offsetX*2 + graphWidth/4);
		} else if (zoomX > 1) {
			zoomX = zoomX / 2;
			setOffsetX(offsetX/2 - graphWidth/4);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(">"))
			setZoom(true);
		else if (e.getActionCommand().equals("<"))
			setZoom(false);
		repaint();
	}

	public void frequencyComponentsUpdated(FrequencyComponents components) {
		this.components = components;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (components != null) {
			components.paint(g, 0, 0, getWidth(), getHeight(),
					offsetX,
					FastFourierTransformer.numComponents*zoomX);
		}
	}
}
