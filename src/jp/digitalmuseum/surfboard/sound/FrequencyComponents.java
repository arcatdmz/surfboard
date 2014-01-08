package jp.digitalmuseum.surfboard.sound;

import java.awt.Color;
import java.awt.Graphics;

import jp.digitalmuseum.surfboard.Surfboard;

public class FrequencyComponents {
	public static final int numComponents = FastFourierTransformer.numComponents;
	public static final int floorIndexForDiscardingFrequency = numComponents*Surfboard.lowestFrequency/Surfboard.samplingRate;
	public static final int numColors = 1536; // Numbers of steps in the colorscale
	private static Color[] colors;
	private int[] components;

	static {
		// Setting up a color scale which goes along the egdes of the RGB color cube
		// cf http://www.poirrier.be/~jean-etienne/info/clibs/gd-rainbow.php
		colors = new Color[numColors+1];
		for (int i = 0; i <= 256; i++) {
			// Red (255,0,0) to yellow (255,255,0)
			colors[numColors - i] = makeColor(0, 255, i, 0);
			// Yellow to green (0,255,0)
			colors[numColors - (i + 256)] = makeColor(0, 255 - i, 255, 0);
			// Green to cyan (0,255,255)
			colors[numColors - (i + 512)] = makeColor(0, 0, 255, i);
			// Cyan to blue (0,0,255)
			colors[numColors - (i + 768)] = makeColor(0, 0, 255 - i, 255);
			// Blue to mangenta( 255,0,255)
			colors[numColors - (i + 1024)] = makeColor(0, i, 0, 255);
			// Should go back to red, but add the first -i to go down into black (0,0,0)
			// rather than back to red (255,0,0) to make an unique color scale
			colors[numColors - (i + 1280)] = makeColor(0, 255 - i, 0, 255 - i); // Fades down into black
		}
	}

	private static Color makeColor(int a, int r, int g, int b) {
		return new Color(a << 24 | r << 16 | g << 8 | b);
	}

	public FrequencyComponents(int[] frequencyComponents) {
		this.components = frequencyComponents.clone();
	}

	public int get(int index) {
		return components[index];
	}
	public void set(int  d, int i) {
		components[i] = d;
	}

	public int size() {
		return components.length;
	}

	public void paint(Graphics g, int x, int y, int width, int height, int startx, int canvasWidth) {

		// Draw the axis.
		int xAxisHzInterval = (int) Math.pow(10, Math.floor(Math.log10(Surfboard.samplingRate*50/canvasWidth)));
		if (xAxisHzInterval <= 0) xAxisHzInterval = 1;
		int x_ = 0;
		int hz = (int) Math.ceil((double) startx * Surfboard.samplingRate / canvasWidth / xAxisHzInterval) * xAxisHzInterval;
		if (hz < Surfboard.lowestFrequency) {
			g.setColor(Color.gray);
			g.fillRect(x, y, (Surfboard.lowestFrequency * canvasWidth / Surfboard.samplingRate)-startx, height);
		}

		int xAxisY = y+height-getTickAreaHeight();
		g.setColor(Color.white);
		g.drawLine(x, y+xAxisY, x+width, y+xAxisY);
		for (;	hz < Surfboard.samplingRate && x_ < x + width;
				hz += xAxisHzInterval) {
			if (hz >= 0) {
				x_ = x+(hz * canvasWidth / Surfboard.samplingRate)-startx;
				if (hz / xAxisHzInterval % 5 == 0) {
					g.drawString(getHzString(hz), x_ + 2, y+xAxisY+getTickAreaHeight()-5);
					g.drawLine(x_, y, x_, y+xAxisY+getTickAreaHeight()-5);
				} else {
					g.drawLine(x_, y+xAxisY, x_, y+xAxisY+5);
				}
			}
		}

		// Draw the graph.
		double bandWidth = canvasWidth / numComponents;
		int offset = (int) (startx/bandWidth);
		int length = (int) (width/bandWidth);
		g.setColor(Color.green);
		for (int i = 0; i < length; i ++) {
			if (i+offset >= 0 && i+offset < components.length) {
				int h = components[i+offset]*height/65536;
				if (h > height) h = height;
				g.fillRect(
						x+(int) (i*bandWidth),
						height-getTickAreaHeight()-h,
						(int) bandWidth,
						h);
			}
		}
	}

	private String getHzString(int hz) {
		if (hz >= 1000) {
			if (hz % 1000 == 0) {
				return String.valueOf(hz/1000)+"k";
			}
			if (hz % 100 == 0) {
				return String.valueOf(hz/1000)+"."+String.valueOf(hz%1000/100)+"k";
			}
			if (hz % 50 == 0) {
				return String.valueOf(hz/1000)+"."+String.valueOf(hz%1000/100)+String.valueOf(hz%100/10)+"k";
			}
		}
		return String.valueOf(hz);
	}

	public void paintWaterfall(Graphics g, int x, int y, int width, int height) {
		int w = numComponents/height;
		// int max = 0;
		for (int i = 0; i < height; i ++) {
			int v = getAverageValue(w*i, w);
			int y_ = y+height-i-1;
			// if (v > max) max = v;
			if (v > 65536) v = 65535;
			g.setColor(colors[v*colors.length/65536]);
			g.drawLine(x, y_, x+width-1, y_);
		}
		// System.out.println(max);
	}

	public FrequencyComponents clone() {
		FrequencyComponents components = new FrequencyComponents(this.components);
		return components;
	}

	public int getTickAreaHeight() {
		return 40;
	}

	public int getAverageValue(int index, int width) {
		int value = 0;
		if (index < 0) {
			index = 0;
		} else if (index + width >= components.length) {
			width = components.length - index;
		}
		for (int i = 0; i < width; i ++) {
			value += components[index + i];
		}
		return value / width;
	}
}
