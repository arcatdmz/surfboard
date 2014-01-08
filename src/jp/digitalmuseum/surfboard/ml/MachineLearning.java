package jp.digitalmuseum.surfboard.ml;

import java.awt.Graphics;
import java.util.Collection;

public interface MachineLearning<C, D> {

	public void paintSummary(Graphics g, int startIndex, int zoom, int width, int height);

	public void teach(int index, boolean ok);
	public void teach(Collection<D> samples);

	public int getSampleCaseSize();

	public C getSampleCase(int index);

	public int record(C sampleCase);

	public boolean judge(C sampleCase);
	public D analyze(C sampleCase);
}
