package weight;

import java.util.Collection;
import java.util.Random;

public abstract class WeightKA implements Weight {
	public Weight getOne(Collection<Weight> c){
		if(c==null) return null;
		long weight =0L;
		for(Weight w:c){
			weight+=w.getWeight();
		}
		Random r = new Random(weight);
		long index = r.nextLong();
		for(Weight w:c){
			index-=w.getWeight();
			if(index<=0) return w;
		}
		return null;
	}
	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
