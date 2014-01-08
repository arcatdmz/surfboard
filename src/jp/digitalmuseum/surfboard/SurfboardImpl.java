package jp.digitalmuseum.surfboard;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import jp.digitalmuseum.surfboard.gui.SurfboardPanel;
import jp.digitalmuseum.surfboard.ml.GestureLearning;
import jp.digitalmuseum.surfboard.ml.SampleCase;
import jp.digitalmuseum.surfboard.ml.SurfingLearning;
import jp.digitalmuseum.surfboard.ml.bayes.NaiveBayesGestureTraining;
import jp.digitalmuseum.surfboard.ml.bayes.NaiveBayesSurfingTraining;
import jp.digitalmuseum.surfboard.sound.*;

public class SurfboardImpl extends SurfboardAbstractImpl implements ActionListener {
	private static final String TEXT_RECORDING_NOISE = "Recording environmental noise.";
	private static final String TEXT_RECORDING_NOISE_FINISHED = "Finished recording environmental noise; Ready to record and learn samples.";
	private static final String TEXT_RECORDING = "Recording sample data.";
	private static final String TEXT_RECORDING_FINISHED = "Finished recording sample data.";
	protected static final int MAX_RECORDING = 200;
	protected static final int MAX_SAMPLE_ELEMENTS_SURFING = 50;
	protected static final int MAX_SURFING = 20;
	protected static final int MAX_SAMPLE_ELEMENTS_GESTURE = 10;

	private JFrame surfboardFrame;
	private SurfboardPanel surfboardPanel;
	private boolean isShiftDown;

	final SoundRecorder recorder;
	final ScheduledFuture<?> futureGUI;

	private boolean environmentalNoiseRecording;
	private double[] environmentalNoise;
	private int environmentalNoiseIteration;

	protected SurfingLearning surfingTraining;
	private boolean isTrainingSurfing;
	private int recordingIteration;
	private int detectedSurfing;
	private ArrayList<SampleCase> detectingSamples;
	private ArrayList<SampleCase> lastDetectedSamples;

	protected GestureLearning gestureTraining;
	private boolean isTrainingGesture;
	private int surfingIteration;

	private boolean eventFiring = false;
	private int judgedTime;
	private int timeSinceLastEvent;

	public static void main(String[] args) {
		run(new SurfboardImpl());
	}

	protected static void run(final SurfboardImpl surfboard) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				surfboard.showFrame(true);
				surfboard.getFrame().addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						surfboard.dispose();
					}
				});
				surfboard.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

	protected SurfingLearning getSurfingTrainer() {
		return new NaiveBayesSurfingTraining(MAX_SAMPLE_ELEMENTS_SURFING);
	}

	protected GestureLearning getGestureTrainer() {
		return new NaiveBayesGestureTraining(MAX_SAMPLE_ELEMENTS_GESTURE);
	}

	public SurfboardImpl() {

		// Prepare for noise canceling.
		environmentalNoiseRecording = true;
		environmentalNoiseIteration = 0;
		environmentalNoise = new double[samples/2];

		// Prepare for naive bayes learning.
		detectingSamples = new ArrayList<SampleCase>();
		lastDetectedSamples = new ArrayList<SampleCase>();
		surfingTraining = getSurfingTrainer();
		gestureTraining = getGestureTrainer();

		// Start monitoring Control key.
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				isShiftDown = e.isShiftDown();
				return false;
			}
		});

		// Start recording.
		recorder = new SoundRecorderDNR2(new FastFourierTransformer(this));
		final ExecutorService executorService = Executors.newFixedThreadPool(2);
		final Future<?> futureFFT = executorService.submit(recorder);

		// Start GUI updater.
		final ScheduledExecutorService scheduledExecutorService =
				Executors.newScheduledThreadPool(1);
		futureGUI =
				scheduledExecutorService.scheduleAtFixedRate(
			new GUIUpdater(),
			1000/displayRate, 1000/displayRate, TimeUnit.MILLISECONDS);

		// Start visualization.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				surfboardPanel = new SurfboardPanel(
						surfingTraining, SurfboardImpl.this);
				surfboardPanel.getJProgressBar().setMaximum(MAX_RECORDING);
				surfboardPanel.getJProgressBar().setVisible(false);

				surfboardFrame = new JFrame();
				surfboardFrame.setTitle("Surfboard");
				surfboardFrame.getContentPane().add(surfboardPanel);
				surfboardFrame.pack();
				surfboardFrame.setSize(790, 560);
				surfboardFrame.setLocation(5, 5);

				surfboardPanel.setText(TEXT_RECORDING_NOISE);
			}
		});

		// Do the rest in another thread.
		executorService.submit(new Runnable() {
			public void run() {

				// Record environmental noise.
				setRecordingButtonEnabled(false);
				setDoGestureButtonEnabled(false);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				environmentalNoiseRecording = false;
				surfboardPanel.setText(TEXT_RECORDING_NOISE_FINISHED);
				setRecordingButtonEnabled(true);

				// Catch runtime exceptions.
				try {
					futureFFT.get();
					futureGUI.get();
				} catch (ExecutionException e) {
					System.err.print("Thread killed by: ");
					e.getCause().printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (CancellationException e) {
					// Do nothing.
				}
			}
		});
	}

	/**
	 * This method might return null immediately after instantiation.
	 * To avoid return of null, use SwingUtilities.invokeLater(runnable).
	 */
	public JFrame getFrame() {
		return surfboardFrame;
	}

	public void frequencyComponentsUpdated(FrequencyComponents components) {
		if (surfboardPanel == null) {
			return;
		}

		if (environmentalNoise != null) {

			// Record environmental noise.
			if (environmentalNoiseRecording) {
				for (int i = 0; i < components.size(); i ++) {
					environmentalNoise[i] = environmentalNoise[i]*environmentalNoiseIteration + components.get(i);
					environmentalNoise[i] = environmentalNoise[i]/(environmentalNoiseIteration + 1);
				}
				environmentalNoiseIteration ++;
			}

			// Cancel noise.
			else {
				for (int i = 0; i < components.size(); i ++) {
					components.set((int) (components.get(i) - environmentalNoise[i]), i);
					if (components.get(i) < 0) {
						components.set(0, i);
					}
				}
			}
		}

		// Record current data.
		if (isTrainingSurfing) {
			surfingTraining.teach(
					surfingTraining.record(components), isShiftDown);
			if ((++ recordingIteration) >= MAX_RECORDING) {
				onFinishTrainingSurfing();
			}
			surfboardPanel.getJProgressBar().setValue(recordingIteration);
			surfboardPanel.repaint();
		} else {

			// Fire an event if needed.
			if (surfingTraining.getSampleCaseSize() > 0 &&
					surfingTraining.judge(components)) {
				final SampleCase sample = surfingTraining.analyze(components);
				detectingSamples.add(sample);
				judgedTime ++;
				if (judgedTime > SurfboardImpl.safeTime
						&& timeSinceLastEvent > SurfboardImpl.minimumEventInterval) {
					if (!eventFiring) {
						distributeEvent();
						surfboardPanel.setText("Surfing detected ["+(++ detectedSurfing)+"].");
						eventFiring = true;
					}
					timeSinceLastEvent = 0;
				} else {
					timeSinceLastEvent ++;
				}
			} else {
				if (eventFiring) {
					final ArrayList<SampleCase> samples = lastDetectedSamples;
					lastDetectedSamples = detectingSamples;
					detectingSamples = samples;
					detectingSamples.clear();
					SampleCase[] sa = lastDetectedSamples.toArray(new SampleCase[0]);
					if (isTrainingGesture) {
						gestureTraining.teach(
								gestureTraining.record(sa),
								surfingIteration % 2 == 0);
						if ((++ surfingIteration) >= MAX_SURFING) {
							onFinishTrainingGesture();
						} else {
							updateDoGestureLabelText();
						}
					} else {
						if (gestureTraining.getSampleCaseSize() > 0) {
							final boolean isSurfedToLeft =
									gestureTraining.judge(sa);
							if (gestureTraining.getLastDetectionStatus()) {
								distributeEvent(isSurfedToLeft);
								surfboardPanel.setText("Surfing recognized as " +
										(isSurfedToLeft ? "right to left" : "left to right")
										+ " gesture.");
							} else {
								surfboardPanel.setText("Surfing couldn't be recognized as gesture.");
							}
						}
					}
				}
				judgedTime = 0;
				timeSinceLastEvent ++;
				eventFiring = false;
			}
		}

		// Update information for the panel.
		surfboardPanel.frequencyComponentsUpdated(components);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(">")) {
			onStartTrainingSurfing();
		} else if (e.getActionCommand().equals("<")) {
			onStartTrainingGesture();
		} else if (e.getActionCommand().equals("-")) {
			markLastDetection(false);
			surfboardPanel.setText("Last surfing marked as mistake.");
		} else if (e.getActionCommand().equals("+")) {
			markLastDetection(true);
		} else if (e.getActionCommand().equals("+L")) {
			markLastGesture(true);
			surfboardPanel.setText("Last surfing marked as right to left gesture.");
		} else if (e.getActionCommand().equals("+R")) {
			markLastGesture(false);
			surfboardPanel.setText("Last surfing marked as left to right gesture.");
		}
	}

	public void dispose() {
		recorder.stop();
		futureGUI.cancel(false);
	}

	private void onStartTrainingSurfing() {
		if (environmentalNoiseRecording ||
				isTrainingSurfing ||
				isTrainingGesture) {
			return;
		}
		surfboardPanel.setText(TEXT_RECORDING);
		setRecordingButtonEnabled(false);
		setDoGestureButtonEnabled(false);
		isTrainingSurfing = true;
		recordingIteration = 0;
		surfboardPanel.getJProgressBar().setVisible(true);
	}

	private void onFinishTrainingSurfing() {
		surfboardPanel.setText(TEXT_RECORDING_FINISHED);
		setRecordingButtonEnabled(true);
		setDoGestureButtonEnabled(true);
		isTrainingSurfing = false;
		surfboardPanel.getJProgressBar().setVisible(false);
	}

	private void onStartTrainingGesture() {
		if (environmentalNoiseRecording ||
				isTrainingSurfing ||
				isTrainingGesture) {
			return;
		}
		setRecordingButtonEnabled(false);
		setDoGestureButtonEnabled(false);
		isTrainingGesture = true;
		surfingIteration = 0;
		updateDoGestureLabelText();
	}

	private void onFinishTrainingGesture() {
		setRecordingButtonEnabled(true);
		setDoGestureButtonEnabled(true);
		isTrainingGesture = false;
		surfboardPanel.setDoGestureLabelText("");
	}

	private void markLastDetection(boolean ok) {
		synchronized (lastDetectedSamples) {
			for (SampleCase sample : lastDetectedSamples) {
				sample.result = ok;
			}
			surfingTraining.teach(lastDetectedSamples);
		}
		surfboardPanel.repaint();
	}

	private void markLastGesture(boolean isSurfingToLeft) {
		synchronized (lastDetectedSamples) {
			final SampleCase[] sampleCases = new SampleCase[0];
			gestureTraining.teach(
					gestureTraining.record(lastDetectedSamples.toArray(sampleCases)),
					isSurfingToLeft);
		}
	}

	private void updateDoGestureLabelText() {
		surfboardPanel.setDoGestureLabelText("Please surf your hand "+
				(surfingIteration % 2 == 0 ? "from right to left" : "from left to right")+
				".");
	}

	private void setRecordingButtonEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				surfboardPanel.setRecordingButtonEnabled(enabled);
			}
		});
	}

	private void setDoGestureButtonEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				surfboardPanel.setDoGestureButtonEnabled(enabled);
			}
		});
	}

	private class GUIUpdater implements Runnable {
		public void run() {

			// Repaint GUI components.
			if (surfboardPanel != null) {
				surfboardPanel.repaint();
			}
		}
	}
}