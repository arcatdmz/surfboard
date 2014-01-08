package jp.digitalmuseum.surfboard;

public interface Surfboard {
	public static final int samplingRate = 44100;
	public static final int sampleBits = 11;
	public static final int samples = (int) Math.pow(2, sampleBits);
	public static final int displayRate = 5;
	public static final int lowestFrequency = 3000;
	public static final int monitorWidth = 5;
	public static final int safeTime = 4;
	public static final int minimumEventInterval = 3;
	public void addEventListener(SurfingListener listener);
	public void removeEventListener(SurfingListener listener);
	public void showFrame(boolean b);
	public void dispose();
}
