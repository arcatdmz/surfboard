package jp.digitalmuseum.surfboard.ml.bayes;

import java.awt.Color;
import java.awt.Graphics;

import jp.digitalmuseum.surfboard.ml.SampleCase;
import jp.digitalmuseum.surfboard.ml.SurfingLearning;
import jp.digitalmuseum.surfboard.sound.FrequencyComponents;

public class NaiveBayesSurfingTraining
		extends NaiveBayesAbstractImpl<FrequencyComponents, SampleCase>
		implements SurfingLearning {

	public NaiveBayesSurfingTraining(int numElements) {
		super(numElements);
	}

	public SampleCase analyze(FrequencyComponents rawData) {
		final int[] data = new int[getNumElements()];
		final int offset = FrequencyComponents.floorIndexForDiscardingFrequency;
		final int length = FrequencyComponents.numComponents - offset;
		final int width = length / getNumElements();
		int index = 0; //, sum = 0;
		for (int i = 0; i < getNumElements(); i ++) {
			final double value = rawData.getAverageValue(index + offset, width);
			data[i] = NaiveBayesSurfingTraining.normalize(value);
			//sum += data[i];
			index += width;
		}

		final SampleCase sample = new SampleCase();
		sample.rawData = rawData;
		sample.data = data;
		sample.result = false;
		return sample;
	}

	public boolean match(SampleCase s, SampleCase sample, int index) {
		return s.data[index] == sample.data[index];
	}

	/**
	 * rawValue varies approximately from 0 to 50,000.
	 * Returned value varies from 0 to 5.
	 */
	public static int normalize(double rawValue) {
		final int value = (int) (rawValue / 10000);
		if (value > 5) {
			return 5;
		}
		return value;
	}

	public void paintSummary(Graphics g, int startIndex, int zoom, int width, int height) {
		for (int i = startIndex; i < width; i ++) {
			if (i < 0 || i >= getSamples().size()) {
				final int w = (i - startIndex)*zoom;
				g.setColor(Color.white);
				if (i == -1) {
					g.fillRect(0, 0, w, height);
				} else if (i == getSamples().size()) {
					g.fillRect(w, 0, width - w, height);
				}
				continue;
			}
			final SampleCase sample = getSamples().get(i);
			final int x = (i - startIndex)*zoom;
			g.setColor(Color.black);
			g.fillRect(x, 0, zoom, height);
			g.setColor(sample.result ? Color.green : Color.red);
			g.fillRect(x, height-5, zoom, 5);
			sample.rawData.paintWaterfall(g, x, 0, zoom, height-6);
		}
	}
}
