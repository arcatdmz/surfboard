package jp.digitalmuseum.surfboard.ml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class MachineLearningAbstractImpl<C,D extends MachineLearningElement<C>> implements MachineLearning<C, D> {
	private int numElements;
	private ArrayList<D> samples;

	public MachineLearningAbstractImpl(int numElements) {
		this.numElements = numElements;
		reset();
	}

	public void reset() {
		samples = new ArrayList<D>();
	}

	public int record(C recordingCase) {
		final D analyzedCase = analyze(recordingCase);
		samples.add(analyzedCase);
		return samples.size()-1;
	}

	public void teach(int index, boolean ok) {
		if (index < 0 || index >= samples.size()) {
			return;
		}
		samples.get(index).result = ok;
	}

	public void teach(Collection<D> teachingSamples) {
		samples.addAll(teachingSamples);
	}

	public abstract boolean judge(C components);

	public C getSampleCase(int monitoringCase) {
		if (monitoringCase < 0 || monitoringCase >= getSamples().size()) {
			return null;
		}
		return samples.get(monitoringCase).rawData;
	}

	public int getSampleCaseSize() {
		return samples.size();
	}

	protected int getNumElements() {
		return numElements;
	}

	protected List<D> getSamples() {
		return samples;
	}

	public abstract D analyze(C data);
}
