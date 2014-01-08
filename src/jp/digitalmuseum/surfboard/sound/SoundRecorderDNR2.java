package jp.digitalmuseum.surfboard.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import jp.digitalmuseum.surfboard.Surfboard;

public class SoundRecorderDNR2 extends SoundRecorder {
	private final float samplingRate = Surfboard.samplingRate;
	private final int samples = Surfboard.samples;
	private final SoundListener listener;
	private boolean isRecording;

	public SoundRecorderDNR2(SoundListener listener) {
		super(listener);
		this.listener = listener;
	}

	public void run() {

		// Set up format information.
		final  AudioFormat af = new AudioFormat(samplingRate, 16, 2, true, false);
		final DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
		if (!AudioSystem.isLineSupported(info)) {
			shutDown(info + " not supported.");
		}

		// Open suitable data line.
		final TargetDataLine tdline;
		try {
			tdline = (TargetDataLine) AudioSystem.getLine(info);
			tdline.open(af);
		} catch (LineUnavailableException e) {
			shutDown(e.getMessage());
			return;
		}

		// Start recording.
		tdline.start();
		byte[] rawDataInByteArray = new byte[samples*2*2];
		byte[] rawDataInByteArrayMono = new byte[samples*2];
		isRecording = true;
		System.out.println("Monitoring started.");
		while (isRecording) {
			if (tdline.read(rawDataInByteArray, 0, rawDataInByteArray.length) == -1) {
				break;
			}
			for (int i = 0; i < rawDataInByteArrayMono.length; i ++) {
				int data = - rawDataInByteArray[i]
						+ rawDataInByteArray[rawDataInByteArrayMono.length + i];
				if (data < 0) {
					data = 0;
				}
				rawDataInByteArrayMono[i] = (byte) data;
			}
			listener.soundUpdated(
					SoundUtility.byte2data(rawDataInByteArrayMono, af, af.getFrameSize()/2));
		}

		// Stop recording.
		tdline.stop();
		tdline.close();
		listener.soundStopped();
		System.out.println("Monitoring stopped.");
	}

	public void stop() {
		isRecording = false;
	}

	public boolean isStopped() {
		return !isRecording;
	}

	private void shutDown(String error) {
		System.err.println(error);
	}
}