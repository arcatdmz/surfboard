package jp.digitalmuseum.surfboard.ml.knn;

import java.awt.Graphics;
import java.util.Collection;
import java.util.List;

import org.ejml.data.DenseMatrix64F;

import jp.digitalmuseum.surfboard.ml.GestureLearning;
import jp.digitalmuseum.surfboard.ml.MachineLearningAbstractImpl;
import jp.digitalmuseum.surfboard.ml.PrincipalComponentAnalysis;
import jp.digitalmuseum.surfboard.ml.SampleCase;
import jp.digitalmuseum.surfboard.ml.SamplePhrase;

public class KNNGestureTraining extends MachineLearningAbstractImpl<SampleCase[], SamplePhrase> implements GestureLearning {
	private static final int numSamplesAlongTime = 4;
	private static final int numSamplesAlongFrequency = 32;
	private boolean pcaReady = false;
	private PrincipalComponentAnalysis pca;

	public KNNGestureTraining(int numElements) {
		super(numElements);
	}

	public SamplePhrase analyze(SampleCase[] rawData) {
		final SamplePhrase samplePhrase = new SamplePhrase();
		samplePhrase.rawData = rawData.clone();
		samplePhrase.ddata = SamplePhrase.resample(
				rawData, numSamplesAlongTime, numSamplesAlongFrequency);
		samplePhrase.result = false;
		return samplePhrase;
	}

	@Override
	public void teach(int index, boolean ok) {
		super.teach(index, ok);
		pcaReady = false;
	}

	@Override
	public void teach(Collection<SamplePhrase> teachingSamples) {
		super.teach(teachingSamples);
		pcaReady = false;
	}

	@Override
	public synchronized boolean judge(SampleCase[] samples) {
		if (!pcaReady) {
			doPCA();
			if (!pcaReady) {
				return false;
			}
		}
		final SamplePhrase sample = analyze(samples);
		sample.rdata = getReducedData(sample.ddata);

		// Look for the nearest sample.
		SamplePhrase nearest = null;
		double distance = Double.MAX_VALUE;
		for (SamplePhrase s : getSamples()) {
			double d = 0;
			for (int i = 0; i < sample.rdata.length; i ++) {
				final double e = sample.rdata[i]-s.rdata[i];
				d += e*e;
			}
			if (d < distance) {
				nearest = s;
				distance = d;
			}
		}
		System.out.println(nearest);
		return nearest.result;
	}

	private void doPCA() {

		System.out.println("Preparing for PCA.");
		List<SamplePhrase> samples = getSamples();
		DenseMatrix64F m = new DenseMatrix64F(
				numSamplesAlongTime*numSamplesAlongFrequency,
				samples.size());
		for (int i = 0; i < samples.size(); i++) {
			SamplePhrase sample = samples.get(i);
			for (int j = 0; j < numSamplesAlongTime; j++) {
				for (int k = 0; k < numSamplesAlongFrequency; k ++) {
					m.set(numSamplesAlongFrequency*j+k,
							i, sample.ddata[j][k]);
				}
			}
		}

		System.out.print("Calculating PCA: ");
		pca = new PrincipalComponentAnalysis(300);
		if (pca.analyze(m, 10e-5)) {
			System.out.println("OK.");

			/*
			// Print eigen values.
			for (int i = 0; i < getNumElements(); i ++) {
				System.out.println(pca.getEigenValue(i));
			}
			*/

			System.out.print("Applying PCA matrix to existing data: ");
			for (int i = 0; i < samples.size(); i++) {
				SamplePhrase sample = samples.get(i);
				sample.rdata = getReducedData(sample.ddata);
			}
			System.out.println("Done.");
			pcaReady = true;
		} else {
			System.out.println("NG.");
		}
	}

	private double[] getReducedData(double[][] ddata) {
		double[] reducedData = new double[getNumElements()];
		for (int j = 0; j < getNumElements(); j ++) {
			reducedData[j] = 0;
			DenseMatrix64F eigenVector = pca.getEigenVector(j);
			for (int k = 0; k < numSamplesAlongTime*numSamplesAlongFrequency; k ++) {
				reducedData[j] += eigenVector.get(k)*
						ddata[k/numSamplesAlongFrequency][k%numSamplesAlongFrequency];
			}
			// System.out.print(" "+reducedData[j]);
		}
		return reducedData;
	}

	@Override
	public void paintSummary(Graphics g, int startIndex, int zoom, int width,
			int height) {
		//
	}

	@Override
	public boolean getLastDetectionStatus() {
		return pcaReady;
	}
}
