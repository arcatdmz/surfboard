package jp.digitalmuseum.surfboard.ml.bayes;

import jp.digitalmuseum.surfboard.ml.MachineLearningAbstractImpl;
import jp.digitalmuseum.surfboard.ml.MachineLearningElement;

public abstract class NaiveBayesAbstractImpl<C,D extends MachineLearningElement<C>> extends MachineLearningAbstractImpl<C,D> {
	private double py, pn;

	public NaiveBayesAbstractImpl(int numElements) {
		super(numElements);
	}

	public boolean judge(C components) {
		final D sample = analyze(components);
		int cyTotal = 0;
		final int[] cy = new int[getNumElements()];
		final int[] cn = new int[getNumElements()];
		for (D s : getSamples()) {
			if (s.result) {
				cyTotal ++;
			}
			for (int e = 0; e < getNumElements(); e ++) {
				if (match(s, sample, e)) {
					if (s.result) {
						cy[e] ++;
					} else {
						cn[e] ++;
					}
				}
			}
		}

		int cnTotal = getSampleCaseSize() - cyTotal;
		if (cyTotal == 0 || cnTotal == 0) {
			return cnTotal == 0;
		}

		// double py = cyTotal/samples.size(), pn = cnTotal/samples.size();
		py = cyTotal;
		pn = cnTotal;
		for (int e = 0; e < getNumElements(); e ++) {
			py = py * cy[e] / cyTotal;
			pn = pn * cn[e] / cnTotal;
		}
		return py >= pn;
	}

	public abstract boolean match(D sample, D sample2, int e);

	public double getLastYesPossibility() {
		return py;
	}

	public double getLastNoPossibility() {
		return pn;
	}

}
