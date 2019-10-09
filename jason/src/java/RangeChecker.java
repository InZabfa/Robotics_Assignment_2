
public class RangeChecker {
	private Range r1;
	private Range r2;

	public RangeChecker(Range r1, Range r2) {
		this.r1 = r1;
		this.r2 = r2;
	}

	public boolean IsInRange(float r, float g, float b) {
		return (r <= this.r2.r && r >= this.r1.r) && (g <= this.r2.g && g >= this.r1.g) && (b <= this.r2.b && b >= this.r1.b);
	}

	public static class Range {
		private float r, g, b;

		public Range(float r, float g, float b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public float R() {
			return this.r;
		}

		public float G() {
			return this.g;
		}

		public float B() {
			return this.b;
		}
	}
}
