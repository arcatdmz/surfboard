package jp.digitalmuseum.surfboard.ml.bayes;

import java.awt.Graphics;

import jp.digitalmuseum.surfboard.ml.GestureLearning;
import jp.digitalmuseum.surfboard.ml.SampleCase;
import jp.digitalmuseum.surfboard.ml.SamplePhrase;

public class NaiveBayesGestureTraining
		extends NaiveBayesAbstractImpl<SampleCase[], SamplePhrase>
		implements GestureLearning {
	private static final int numSamplesAlongTime = 2;
	protected int numSamplesAlongFrequency;

	/**
	 * @param numElements must be a even number. <!-- numElements must be a multiple of numSamplesAlongTime. -->
	 */
	public NaiveBayesGestureTraining(int numElements) {
		super(numElements);
		numSamplesAlongFrequency = numElements / numSamplesAlongTime;
	}

	public SamplePhrase analyze(SampleCase[] rawData) {
		final SamplePhrase samplePhrase = new SamplePhrase();
		samplePhrase.rawData = rawData.clone();
		samplePhrase.data = normalize(SamplePhrase.resample(
				rawData, numSamplesAlongTime, numSamplesAlongFrequency)); // binarize(resample(rawData));
		samplePhrase.result = false;
		return samplePhrase;
	}

	public boolean match(SamplePhrase s, SamplePhrase sample, int e) {
		return s.data[e] == sample.data[e];
	}

	/*
	private int[] binarize(double[][] data) {
		final int[] results = new int[getNumElements()];
		int i = 0;
		for (double[] da : data) {
			final double former = da[0], latter = da[1];
			results[i ++] = former > latter ? 1 : 0;
			results[i ++] = latter > former ? 0 : 1;
		}
		return results;
	}
	*/

	private int[] normalize(double[][] data) {
		final int[] results = new int[getNumElements()];
		int i = 0;
		for (double[] da : data) {
			for (double d : da) {
				results[i ++] = NaiveBayesSurfingTraining.normalize(d);
			}
		}
		return results;
	}

	@Override
	public void paintSummary(Graphics g, int startIndex, int zoom, int width,
			int height) {
		//
	}

	public boolean getLastDetectionStatus() {
		return getLastYesPossibility() != getLastNoPossibility();
	}
}
