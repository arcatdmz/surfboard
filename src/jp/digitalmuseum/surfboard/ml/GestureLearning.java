package jp.digitalmuseum.surfboard.ml;

public interface GestureLearning extends MachineLearning<SampleCase[], SamplePhrase> {

	boolean getLastDetectionStatus();
}
