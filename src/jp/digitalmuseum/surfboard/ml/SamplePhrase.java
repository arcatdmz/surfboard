package jp.digitalmuseum.surfboard.ml;

import jp.digitalmuseum.surfboard.sound.FrequencyComponents;

public class SamplePhrase extends MachineLearningElement<SampleCase[]> {
	public int[] data;			// For naive bayes.
	public double[][] ddata;	// For k-NN.
	public double[] rdata;		// For k-NN, reduced data.

	public static double[][] resample(SampleCase[] source, int numSamplesAlongTime, int numSamplesAlongFrequency) {
		final double[][] destination = new double[numSamplesAlongTime][numSamplesAlongFrequency];
		final int offset = FrequencyComponents.floorIndexForDiscardingFrequency;
		final int length = FrequencyComponents.numComponents - offset;
		final int width = length / numSamplesAlongFrequency;

		for (int destinationIndex = 0; destinationIndex < destination.length; destinationIndex ++) {
			final int startSourceIndex = source.length * destinationIndex / destination.length;
			final int endSourceIndex = source.length * (destinationIndex + 1) / destination.length;
			double coeff = 0.0;
			double c = (double) destination.length / source.length;
			for (int sourceIndex = startSourceIndex;
					sourceIndex <= endSourceIndex
							&& sourceIndex < source.length;
					sourceIndex ++) {

				// Calculate coefficient for bilinear interpolation.
				if (sourceIndex > startSourceIndex && sourceIndex < endSourceIndex) {
					coeff = c;
				} else {
					if (sourceIndex == startSourceIndex) {
						coeff = (double) (sourceIndex + 1) * c - (double) destinationIndex;
					} else if (sourceIndex + 1 == endSourceIndex) {
						coeff = (double) (destinationIndex + 1) - (double) sourceIndex * c;
					}
				}

				// Calculate destination value.
				for (int i = 0; i < numSamplesAlongFrequency; i ++) {
					destination[destinationIndex][i] +=
						source[sourceIndex].rawData.getAverageValue(
								offset+width*i, width) * coeff;
				}
			}
		}
		return destination;
	}
}
