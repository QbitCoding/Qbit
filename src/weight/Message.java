package weight;

import java.util.Calendar;
import java.util.Date;

public class Message implements Weight {
	private long weight;
	
	private int weight0;
	private Calendar time;
	private String gps;

	@Override
	public int getWeight() {
		if(weight!=0) return (int) weight;
		weight = weight0;
		Calendar now = Calendar.getInstance();
		timeWeight(now, time);
		gpsWeight();
		return (int)weight;
	}
	private long timeWeight(Calendar now,Calendar before){
		int i = now.get(Calendar.YEAR) - before.get(Calendar.YEAR);
		if (i%5 == 0||i==1)
			weight = weight * 3 / 2;
		i = now.get(Calendar.MONTH) - before.get(Calendar.MONTH);
		if (i == 0)
			weight = weight * 3 / 2;
		i = now.get(Calendar.DATE) - before.get(Calendar.DATE);
		if (i == 0)
			weight = weight * 3 / 2;
		i = now.get(Calendar.HOUR) - before.get(Calendar.HOUR);
		if (i == 0)
			weight = weight * 3 / 2;
		i = now.get(Calendar.MINUTE) - before.get(Calendar.MINUTE);
		if (i == 0)
			weight = weight * 3 / 2;
		i = now.get(Calendar.DAY_OF_WEEK)- before.get(Calendar.DAY_OF_WEEK);
		if (i == 0)
			weight = weight * 3 / 2;
//		if(weight>Integer.MAX_VALUE) weight=Integer.MAX_VALUE;
//		return (int)weight;
		return weight;
	}

}
