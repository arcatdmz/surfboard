package jp.digitalmuseum.surfboard.apps;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private int zoom = 1;

	public ImagePanel(String path) {
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			image = null;
		}
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public void zoomIn() {
		zoom ++;
	}

	public void zoomOut() {
		zoom --;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image,
				getWidth()/2 - image.getWidth()*zoom/2,
				getHeight()/2 - image.getHeight()*zoom/2,
				image.getWidth()*zoom,
				image.getHeight()*zoom,
				null);
	}
}
