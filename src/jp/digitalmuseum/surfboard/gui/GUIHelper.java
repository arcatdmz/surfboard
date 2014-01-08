package jp.digitalmuseum.surfboard.gui;

import java.awt.Graphics;

public class GUIHelper {

	public static void paintHorizontalAxis(Graphics g, int width, int height, double coeffFrequencyComponent) {
		int y = 0;
		for (int vol = 1; y < height; vol ++) {
			y = (int) (1000 * vol * coeffFrequencyComponent);
			if (vol % 10 == 0) {
				if (10000 * coeffFrequencyComponent > 20 ||
						vol % 50 == 0) {
					g.drawLine(0, height - y, width + 5, height - y);
					g.drawString(String.valueOf(vol), width + 5, height - y + 5);
				}
			}
			if (1000 * coeffFrequencyComponent > 20) {
				g.drawLine(0, height - y, width + 2, height - y);
				g.drawString(String.valueOf(vol), width + 5, height - y + 5);
			}
		}

		g.drawLine(width, 0, width, height + 30);
	}
}
