package jp.digitalmuseum.surfboard.sound;

import jp.digitalmuseum.surfboard.Surfboard;

public class FastFourierTransformer implements SoundListener {
	public static final int numComponents = Surfboard.samples/2;
	private static final int samples = Surfboard.samples;
	private final FrequencyComponentsListener listener;
	private FFT fft;
	private int[] frequencyComponents;
	private double[] gaussianWindow;
	private double[] re;
	private double[] im;

	public FastFourierTransformer(FrequencyComponentsListener listener) {
		this.listener = listener;
		frequencyComponents = new int[numComponents];
		re = new double[samples];
		im = new double[samples];
		fft = new FFT(samples);

		// Initialize Gaussian window.
		gaussianWindow = new double[samples];
		final double reverseN = 1.0 / samples;
		for (int i = 0; i < samples; i++) {
			final double in = (2.0 * i - samples) * reverseN;
			gaussianWindow[i] = Math.exp(-4.5 * in * in);
		}
	}

	public void soundUpdated(int[] data) {

		// Gaussian windowing
		for (int i = 0; i < samples; i++) {
			re[i] = data[i] * gaussianWindow[i];
			im[i] = 0;
		}

		// Apply "Fast Fourier Transform".
		fft.fft(re, im);
		for (int i = 0; i < frequencyComponents.length; i++) {
			frequencyComponents[i] = (int) Math.sqrt(re[i] * re[i] + im[i] * im[i]);
		}
		listener.frequencyComponentsUpdated(
				new FrequencyComponents(frequencyComponents));
	}

	public void soundStopped() {
		// Do nothing.
	}

	/**
	 * Fast Fourier Transform
	 *
	 * cf. Javaによるアルゴリズム辞典 奥村 晴彦(他) 技術評論社 2003年
	 */
	public class FFT {
		int n;
		int[] bitrev;
		double[] sintbl;

		public FFT(int n) {
			this.n = n;
			sintbl = new double[n + n / 4];
			bitrev = new int[n];

			double t = Math.sin(Math.PI / n);
			double dc = 2 * t * t;
			double ds = Math.sqrt(dc * (2 - dc));
			t = 2 * dc;
			double c = sintbl[n / 4] = 1;
			double s = sintbl[0] = 0;
			for (int i = 1; i < n / 8; i++) {
				c -= dc;
				dc += t * c;
				s += ds;
				ds -= t * s;
				sintbl[i] = s;
				sintbl[n / 4 - i] = c;
			}
			if (n / 8 != 0)
				sintbl[n / 8] = Math.sqrt(0.5);
			for (int i = 0; i < n / 4; i++)
				sintbl[n / 2 - i] = sintbl[i];
			for (int i = 0; i < n / 2 + n / 4; i++)
				sintbl[i + n / 2] = -sintbl[i];

			int i = 0;
			int j = 0;
			int k;
			bitrev[0] = 0;
			while (++i < n) {
				k = n / 2;
				while (k <= j) {
					j -= k;
					k /= 2;
				}
				j += k;
				bitrev[i] = j;
			}
		}

		public void fftsub(double[] x, double[] y, int sign) {
			int i, j;
			double t;
			for (i = 0; i < n; i++) {
				j = bitrev[i];
				if (i < j) {
					t = x[i];
					x[i] = x[j];
					x[j] = t;
					t = y[i];
					y[i] = y[j];
					y[j] = t;
				}
			}
			int h, d, ik;
			double c, s, dx, dy;
			for (int k = 1; k < n; k *= 2) {
				h = 0;
				d = n / (k * 2);
				for (j = 0; j < k; j++) {
					c = sintbl[h + n / 4];
					s = sign * sintbl[h];
					for (i = j; i < n; i += k * 2) {
						ik = i + k;
						dx = s * y[ik] + c * x[ik];
						dy = c * y[ik] - s * x[ik];
						x[ik] = x[i] - dx;
						x[i] += dx;
						y[ik] = y[i] - dy;
						y[i] += dy;
					}
					h += d;
				}
			}
		}

		public void fft(double[] x, double[] y) {
			fftsub(x, y, 1);
		}

		public void ifft(double[] x, double[] y) {
			fftsub(x, y, -1);
			for (int i = 0; i < n; i++) {
				x[i] /= n;
				y[i] /= n;
			}
		}
	}
}