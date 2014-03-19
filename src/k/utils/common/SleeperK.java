package k.utils.common;

public class SleeperK implements SleeperKI {
	private long max;
	private long min;
	private long now;

	private SleeperK(long max, long now, long min) {
		super();
		this.max = max;
		this.now = now;
		this.min = min;
	}

	@Override
	public void reset() {
		now = min;
	}

	private static final byte b1 = 3;
	private static final byte b2 = 2;

	@Override
	public long getSleepTime() {
		long cache = now * b1;
		if (cache < 0 || cache > max * b2)
			return now=max;
		return now = cache / b2;
	}

	public static SleeperKI getInstance(long max, long now, long min) {
		if (max < now || now < min || min <= 0)
			return null;
		return new SleeperK(max, now, min);
	}

}
