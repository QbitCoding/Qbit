package k.utils.common;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import k.utils.common.RelationBuilderKI.RelationBuilderKA;
/**
 * 
 * @author Qbit
 *
 * @param <E>
 * 2014-03-03
 */
public class JobManager<E,F> implements Callable<List<E>>{
	private List<E> orderlist;
	private RelationBuilderKA<E> rb;
	private Map<E,Callable<F>> jobmap;
	private Deque<E> deque=new ArrayDeque<E>();
	private int doingmax;
	/**
	 * 用于存放已经完成的工作
	 */
	private Set<E> doingKS=new HashSet<E>();
	/**
	 * 用于存放正在执行的工作
	 */
	private Set<E> doneKS=new HashSet<E>();
	private ExecutorService pool = Executors.newCachedThreadPool();
	private CallbacKI<F> callbacker;
	@Override
	public List<E> call() throws Exception {
		deque.addAll(rb.getParents());
		for(int i=0;i<doingmax;i++){
			dojob(deque.pollFirst());
		}
		return orderlist;
	}
	private void dojob(E name) throws Exception {
		if(doneKS.contains(name)||doingKS.contains(name)||name==null) return;
		doingKS.add(name);
		pool.submit(jobmap.get(name)).get();
		orderlist.add(name);
		callbacker.callback(null);
		doneKS.add(name);
		doingKS.remove(name);
		for(E next:rb.getChild(name)){
			if(doneKS.contains(rb.getParent(next))) deque.add(next);
		}
		while(doingKS.size()<doingmax)
		dojob(deque.pollFirst());
	}
	private JobManager(RelationBuilderKA<E> rb,
			Map<E, Callable<F>> jobmap, int doingmax,CallbacKI<F> callbacker,List<E> orderlist) {
		super();
		this.rb = rb;
		this.jobmap = jobmap;
		if(doingmax>0)
		this.doingmax = doingmax;
		else this.doingmax=Integer.MAX_VALUE;
		this.callbacker=callbacker;
		this.orderlist=orderlist;
	}
	/**
	 * 
	 * @param rb 存放着任务之间的相互依存关系
	 * @param jobmap 存放对应要做的事情
	 * @param doingmax 最大执行的任务数,如果为0则无限制
	 * @param callbacker 每完成一个任务就会调用一次callbacker的callback
	 * @param orderlist 用于存放已经完成的任务
	 * @return
	 */
	public static <E,F> JobManager<E,F> newInstance(RelationBuilderKA<E> rb,
	Map<E, Callable<F>> jobmap, int doingmax,CallbacKI<F> callbacker,List<E> orderlist){
		return new JobManager<E,F>(rb, jobmap, doingmax, callbacker,orderlist);
	}
}
