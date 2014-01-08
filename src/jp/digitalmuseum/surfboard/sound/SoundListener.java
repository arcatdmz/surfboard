package jp.digitalmuseum.surfboard.sound;

public interface SoundListener {

	public void soundUpdated(int[] data);

	public void soundStopped();
}
