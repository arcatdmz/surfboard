package jp.digitalmuseum.surfboard.ml.bayes;

import jp.digitalmuseum.surfboard.ml.MachineLearning;

public interface NaiveBayes<C, D> extends MachineLearning<C, D> {

	public void reset();

	public double getLastYesPossibility();

	public double getLastNoPossibility();

	public boolean match(D sample, D sample2, int e);
}
