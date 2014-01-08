package jp.digitalmuseum.surfboard;

import jp.digitalmuseum.surfboard.ml.GestureLearning;
import jp.digitalmuseum.surfboard.ml.knn.KNNGestureTraining;

public class SurfboardImplKNN extends SurfboardImpl {

	public static void main(String[] args) {
		run(new SurfboardImplKNN());
	}

	@Override
	protected GestureLearning getGestureTrainer() {
		return new KNNGestureTraining(24);
	}

}