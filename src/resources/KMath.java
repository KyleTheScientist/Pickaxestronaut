package resources;

import java.lang.reflect.Array;
import java.util.Random;

public class KMath {

	public static int roundToGrid(int x, int cellSize) {
		x /= cellSize;
		x *= cellSize;
		return x;
	}

	public static int[] distributeValues(int min, int max, int x) {
		int[] r = new int[x];
		for (int i = 1; i <= x; i++) {
			r[i - 1] = min + i * (max - min) / (x + 1);
		}
		return r;
	}

	public static int boundedRandom(int min, int max) {
		Random r = new Random();

		return new Random().nextInt(max - min) + min;
	}

	public static int clamp(int x, int xMin, int xMax) {
		if (x > xMax) {
			return xMax;
		}
		if (x < xMin) {
			return xMin;
		}
		return x;
	}

	public static int randomOfTwo(int x, int y) {
		boolean b = new Random().nextBoolean();
		if (b)
			return x;
		else
			return y;
	}

	public static <T> T[] concatenate(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;

		@SuppressWarnings("unchecked")
		T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}
}
