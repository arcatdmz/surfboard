package jp.digitalmuseum.surfboard.sound;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Line.Info;

import jp.digitalmuseum.surfboard.Surfboard;

public class SoundRecorderDNR extends SoundRecorder {
	private final float samplingRate = Surfboard.samplingRate;
	private final int samples = Surfboard.samples;
	private final SoundListener listener;
	private boolean isRecording;

	public SoundRecorderDNR(SoundListener listener) {
		super(listener);
		this.listener = listener;
	}

	@Override
	public void run() {

		// Set up format information.
		final  AudioFormat af = new AudioFormat(samplingRate, 16, 1, true, false);
		final DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
		if (!AudioSystem.isLineSupported(info)) {
			shutDown(info + " not supported.");
		}

		// Open suitable data line.
		ArrayList<TargetDataLine> ls = new ArrayList<TargetDataLine>();
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixerInfo : mixers) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Info[] lines = mixer.getTargetLineInfo(info);
			for (Info tdline : lines) {
				try {
					Line line = mixer.getLine(tdline);
					if (line instanceof TargetDataLine) {
						ls.add((TargetDataLine) line);
						System.out.println(line);
					}
				} catch (LineUnavailableException e) {
					//
				}
			}
		}
		TargetDataLine[] tdlines = new TargetDataLine[2];
		tdlines[0] = ls.get(2);
		tdlines[1] = ls.get(3);

		// Start recording.
		for (TargetDataLine tdline : tdlines) {
			tdline.start();
		}
		byte[] rawDataInByteArray = new byte[samples*2];
		byte[] rawDataInByteArray2 = new byte[samples*2];
		isRecording = true;
		System.out.println("Monitoring started.");
		while (isRecording) {
			if (tdlines[0].read(rawDataInByteArray, 0, rawDataInByteArray.length) == -1 ||
					tdlines[1].read(rawDataInByteArray2, 0, rawDataInByteArray.length) == -1) {
				break;
			}
			for (int j = 0; j < rawDataInByteArray.length; j ++) {
				rawDataInByteArray[j] -= rawDataInByteArray2[j];
				if (rawDataInByteArray[j] < 0) {
					rawDataInByteArray[j] = 0;
				}
			}
			listener.soundUpdated(
					SoundUtility.byte2data(rawDataInByteArray, af, af.getFrameSize()));
		}

		// Stop recording.
		for (TargetDataLine tdline : tdlines) {
			tdline.stop();
			tdline.close();
		}
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