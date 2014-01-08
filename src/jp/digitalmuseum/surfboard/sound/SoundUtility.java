package jp.digitalmuseum.surfboard.sound;

import javax.sound.sampled.AudioFormat;

public class SoundUtility {

	public static int[] byte2data(byte[] ab, AudioFormat af, int frameSize) {
		int mo = frameSize;
		int len = ab.length / mo;
		int cnt;
		int msb, lsb, mbb, lbb;
		int[] cd = new int[len];

		if (mo > 2) {
			if (af.isBigEndian()) {
				for (int i = 0; i < len; i++) {
					cnt = mo * i;
					msb = (int) ab[cnt];
					lsb = (int) ab[cnt + 1];
					mbb = (int) ab[cnt + 2];
					lbb = (int) ab[cnt + 3];
					cd[i] = ((msb << 8 | (255 & lsb)) + (mbb << 8 | (255 & lbb))) / 2;
				}
			} else {
				for (int i = 0; i < len; i++) {
					cnt = mo * i;
					lsb = (int) ab[cnt];
					msb = (int) ab[cnt + 1];
					lbb = (int) ab[cnt + 2];
					mbb = (int) ab[cnt + 3];
					cd[i] = ((msb << 8 | (255 & lsb)) + (mbb << 8 | (255 & lbb))) / 2;
				}
			}
		} else {
			if (af.isBigEndian()) {
				for (int i = 0; i < len; i++) {
					cnt = mo * i;
					msb = (int) ab[cnt];
					lsb = (int) ab[cnt + 1];
					cd[i] = msb << 8 | (255 & lsb);
				}
			} else {
				for (int i = 0; i < len; i++) {
					cnt = mo * i;
					lsb = (int) ab[cnt];
					msb = (int) ab[cnt + 1];
					cd[i] = msb << 8 | (255 & lsb);
				}
			}
		}
		return cd;
	}
}
