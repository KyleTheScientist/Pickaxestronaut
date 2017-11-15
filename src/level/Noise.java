package level;


public class Noise{

	private static double noise(int x) {

		x = (x << 13) ^ x;
		return (1.0 - ((x * (x * x * 1573 + 89221) + 1376312589) & 0x7FFFFFFF) / 1073741824.0);
	}

	private static double smoothedNoise(int x) {
		return noise(x) / 2 + noise(x - 1) / 4 + noise(x + 1) / 4;
	}

	private static double interpolate(double a, double b, double x) {
		double ft = x * Math.PI;
		double f = (1 - Math.cos(ft)) * .5;

		return a * (1 - f) + b * f;
	}

	private static double interpolatedNoise(double x) {
		int intX = (int) x;
		double rX = x - intX;

		double v1 = smoothedNoise(intX);
		double v2 = smoothedNoise(intX + 1);

		return interpolate(v1, v2, rX);
	}

	public static double perlinNoise(double x, double frequency, double persistence, int octaves) {

		double total = 0;
		octaves--;
		
		for (int i = 0; i < octaves; i++) {
			double amplitude = persistence;
			for (int j = 0; j < i; j++) {
				frequency *= frequency;
				amplitude *= amplitude;
			}
			
			total += interpolatedNoise(x * frequency) * amplitude;
		}
		return total;
	}

}
