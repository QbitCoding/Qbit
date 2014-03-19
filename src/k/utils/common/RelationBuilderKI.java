/**
 * 
 */
package k.utils.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.Transformer;

/**
 * @author Qbit
 * 2014-02-20
 */
public interface RelationBuilderKI<P, C> {
	interface RelationKI<P, C> {
	}
	/**
	 * 增加一段关系,其中child的产生依赖于所有的parent
	 * 一般来说child可以为null但是parent不应该为null,但是后面KA也未作检查
	 * @param parent
	 * @param child
	 * @return
	 */
	boolean add(P parent, C child);
	/**
	 * 得到所有的children
	 * @return
	 */
	Collection<C> getAllChildren();
	/**
	 * 得到这些panents的所有child
	 * @param parents
	 * @return
	 */
	Collection<C> getAllChildren(P... parents);
	/**
	 * 结束一段关系
	 * @param parent
	 * @param child
	 * @return
	 */
	boolean remove(P parent, C child);
	/**
	 * 结束掉object作为parent的所有关系,真是一个不称职的parent
	 * @param object
	 * @return
	 */
	int removeParent(P object);
	/**
	 * 移出一个不称职的child
	 * @param child
	 * @return
	 */
	int removeChild(C child);
	/**
	 * 得到child的直接parent
	 * @param child
	 * @return
	 */
	Collection<P> getParent(C child);
	/**
	 * 得到parent的直接child
	 * @param parent
	 * @return
	 */
	Collection<C> getChild(P parent);
	/**
	 * 得到child的parent以及parent的parent以及...
	 * @param child
	 * @return
	 */
	Collection<P> getParents(C child);
	/**
	 * 得到parent的child,以及child的child,以及...
	 * @param parent
	 * @return
	 */
	Collection<C> getChildren(P parent);
	/**
	 * 得到真正意义的children,也就是没有作为parent的child
	 * @return
	 */
	Collection<C> getChildren();
	/**
	 * 得到不会是child的parent
	 * @return
	 */
	Collection<P> getParents();

	 abstract class RelationBuilderKA<E> implements
			RelationBuilderKI<E, E> {
		@SuppressWarnings("hiding")
		class Relation<E> implements RelationKI<E, E> {
			private E parent;
			private E child;

			Relation(E parent, E child) {
				this.parent = parent;
				this.child = child;
			}

			@Override
			public boolean equals(Object arg0) {
				if (arg0 == null)
					return false;
				else if (arg0 instanceof Relation) {
					@SuppressWarnings("rawtypes")
					Relation r = (Relation) arg0;
					return parent.equals(r.parent) && child.equals(r.child);
				} else {
					return false;
				}
			}

			@Override
			public int hashCode() {
				return parent.hashCode() * child.hashCode();
			}

			@Override
			public String toString() {
				return "parent:" + parent.toString() + "&child:"
						+ child.toString();
			}

		}

		final private Transformer<Relation<E>, E> selectP = new Transformer<Relation<E>, E>() {
			@Override
			public E transform(Relation<E> e) {
				return e.parent;
			}
		};
		final private Transformer<Relation<E>, E> selectC = new Transformer<Relation<E>, E>() {
			public E transform(Relation<E> e) {
				return e.child;
			}
		};
		private Collection<Relation<E>> relationKS = getRelationCollection();

		abstract protected Collection<Relation<E>> getRelationCollection();

		abstract protected Collection<E> getCollection();
		
		public boolean add(E parent, E child) {
			Relation<E> relation = new Relation<E>(parent, child);
			if (relationKS.contains(relation))
				return false;
			return relationKS.add(relation);
		}

		public boolean remove(E parent, E child) {
			return relationKS.remove(new Relation<E>(parent, child));
		}

		public int remove(E obj) {
			int counter = 0;
			counter += removeParent(obj);
			counter += removeChild(obj);
			return counter;
		}

		/*
		 * 帮助方法,抽取removeParent和removeChild的共同部分
		 */
		private int remove(Object obj,
				Transformer<? super Relation<E>, E> selecter) {
			int counter = 0;
			for (Relation<E> r : relationKS) {
				if (selecter.transform(r).equals(obj)) {
					relationKS.remove(r);
					counter++;
				}
			}
			return counter;
		}

		/**
		 * 删掉所有parent作为P
		 * 
		 * @param parent
		 * @return
		 */
		public int removeParent(E obj) {
			return remove(obj, selectP);
		}

		public int removeChild(E obj) {
			return remove(obj, selectC);
		}

		private Collection<E> get(E e, Transformer<Relation<E>, E> selectkey,
				Transformer<Relation<E>, E> selectvalue) {
			Collection<E> collection = getCollection();
			for (Relation<E> r : relationKS) {
				if (selectkey.transform(r).equals(e)){
					E ee=selectvalue.transform(r);
					if(ee!=null);//去除那些独立关系
					collection.add(e);
				}
				
			}
			return collection;
		}

		private Collection<E> gets(E obj, Transformer<Relation<E>, E> getp,
				Transformer<Relation<E>, E> getc, boolean addpro) {
			Collection<E> direct = get(obj, getp, getc);
			if (direct == null)
				return null;
			Collection<E> undirect = getCollection();
			for (E e : direct) {
				Collection<E> sons = gets(e, getp, getc,addpro);
				if(addpro) undirect.add(e);
				if (sons != null) {
					for (E son : sons) {
						if (son.equals(e))
							throw new RuntimeException();
						if (!undirect.contains(son))
							undirect.add(son);
					}
					if(!addpro)undirect.add(e);
				}
			}
			return undirect;
		}

		public Collection<E> getChildren(E parent) {
			return gets(parent, selectP, selectC,true);
		}

		public Collection<E> getParents(E child) {
			return gets(child, selectC, selectP,false);
		}

		public Collection<E> getChild(E obj) {
			return get(obj, selectP, selectC);
		}

		public Collection<E> getParent(E obj) {
			return get(obj, selectC, selectP);
		}

		@Override
		public Collection<E> getAllChildren(E...parents) {
			Set<E> allset=new HashSet<E>();
			for(E parent:parents){
				allset.addAll(getChildren(parent));
			}
			return orderby(allset);
		}
		private TreeSet<E> orderby(Collection<E> input){
			TreeSet<E> ts=new TreeSet<E>(new Comparator<E>() {
				@Override
				public int compare(E o1, E o2) {
					return getRangKM().get(o1)-getRangKM().get(o2);
				}
			});
			ts.addAll(input);
			return ts;
		}
		/**
		 * 用于确定层次关系,用于初始化rangKM和rangKL;
		 */
		private void getRang(){
			rangKM=new HashMap<E,Integer>();
			rangKL=new ArrayList<Set<E>>();
			Collection<E> children = new HashSet<E>();
			for(Relation<E> r:relationKS){
				E e=selectC.transform(r);
				if(!children.contains(e))
				children.add(selectC.transform(r));
			}
			for(Relation<E> r:relationKS){
				E e = selectP.transform(r);
				if(!children.contains(e)){
					rangKM.put(e, 0);
				}
			}
			Set<E> swap=new HashSet<E>();
			int index=0;
			do{
				rangKL.add(swap);
				index++;
				swap=new HashSet<E>();
				nextchild:
				for(E e:children){
					for(E parent:getParent(e)){
						if(rangKM.get(parent)==null)
							continue nextchild;
					}
					swap.add(e);
				}
				for(E e:swap){
					rangKM.put(e, index);
					children.remove(e);
				}
			}while(swap.size()!=0);
		}
		/**
		 * 注意rangKL的第一个set用于存放不是child的parent,可能为空,依赖于具体实现;
		 */
		private List<Set<E>> rangKL;
		private List<Set<E>> getRangKL(){
			if(rangKL==null) getRang();
			return rangKL;
		}
		private Map<E,Integer> rangKM;
		private Map<E,Integer> getRangKM(){
			if(rangKM==null) getRang();
			return rangKM;
		}
		/**
		 * getParents和getChildren的帮助方法
		 * @param getp
		 * @param getc
		 * @return
		 */
		private Collection<E> gets(Transformer<Relation<E>, E> getp,
				Transformer<Relation<E>, E> getc){
			Collection<E> p = getCollection();
//			for()
			return null;
		}
		@Override
		public Collection<E> getParents() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<E> getChildren() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<E> getAllChildren() {
			Collection<E> c = getCollection();
			List<Set<E>>list=getRangKL();
			for(int i=1;i<list.size();i++){
				c.addAll(list.get(i));
			}
			return c;
		}
		
		
	}
	/**
	 * 通过该类得到的Collection都经过了排序
	 * @author Qbit
	 *
	 * @param <E>
	 */
	class PowerRelationBuilder<E> extends RelationBuilderKA<E> {
		@Override
		protected Collection<Relation<E>> getRelationCollection() {
			return new ArrayList<Relation<E>>();
		}

		@Override
		protected Collection<E> getCollection() {
			return new ArrayList<E>();
		}

		
	}
	/**
	 * 通过该类得到的Collection未排序,但速度应该快些,未测试
	 * @author Qbit
	 *
	 * @param <E>
	 */
	class QuickRelationBuilder<E> extends RelationBuilderKA<E> {

		@Override
		protected Collection<Relation<E>> getRelationCollection() {
			return new HashSet<Relation<E>>();
		}

		@Override
		protected Collection<E> getCollection() {
			return new HashSet<E>();
		}


	}
	/**
	 * 正如名字所示,该类使用了cache,但可能bug多
	 * @author Qbit
	 *
	 * @param <E>
	 */
	class CachedRelationBuilder<E> extends RelationBuilderKA<E> {
		private RelationBuilderKA<E> rbKA;

		public CachedRelationBuilder(RelationBuilderKA<E> instance) {
			this.rbKA = instance;
			System.out.println(instance);
			System.out.println("----");
		}

		@Override
		protected Collection<Relation<E>> getRelationCollection() {
			return rbKA.getRelationCollection();
		}

		@Override
		protected Collection<E> getCollection() {
			return rbKA.getCollection();
		}

		private Map<E, Collection<E>> childKM = new HashMap<E, Collection<E>>();
		private Map<E, Collection<E>> parentKM = new HashMap<E, Collection<E>>();
		private Map<E, Collection<E>> childrenKM = new HashMap<E, Collection<E>>();
		private Map<E, Collection<E>> parentsKM = new HashMap<E, Collection<E>>();
		private boolean isBuild = false;

		private void init() {
			childKM = new HashMap<E, Collection<E>>();
			parentKM = new HashMap<E, Collection<E>>();
			childrenKM = new HashMap<E, Collection<E>>();
			parentsKM = new HashMap<E, Collection<E>>();
			orderlist=new ArrayList<E>();
		}

		@Override
		public Collection<E> getChildren(E parent) {
			Collection<E> c = childrenKM.get(parent);
			if (c != null)
				return c;
			c = rbKA.getChildren(parent);
			childrenKM.put(parent, c);
			isBuild = true;
			return c;
		}

		@Override
		public Collection<E> getParents(E child) {
			Collection<E> c = parentsKM.get(child);
			if (c != null)
				return c;
			c = rbKA.getParents(child);
			parentsKM.put(child, c);
			isBuild = true;
			return c;
		}

		@Override
		public Collection<E> getChild(E obj) {
			Collection<E> c = childKM.get(obj);
			if (c != null)
				return c;
			c = rbKA.getChild(obj);
			childKM.put(obj, c);
			isBuild = true;
			return c;
		}

		@Override
		public Collection<E> getParent(E obj) {
			Collection<E> c = parentKM.get(obj);
			if (c != null)
				return c;
			c = rbKA.getParent(obj);
			parentKM.put(obj, c);
			isBuild = true;
			return c;
		}

		@Override
		public boolean add(E parent, E child) {
			if (isBuild) {
				init();
				isBuild = false;
			}
			return rbKA.add(parent, child);
		}

		@Override
		public boolean remove(E parent, E child) {
			if (isBuild) {
				init();
				isBuild = false;
			}
			return rbKA.remove(parent, child);
		}

		@Override
		public int remove(E obj) {
			if (isBuild) {
				init();
				isBuild = false;
			}
			return rbKA.remove(obj);
		}

		@Override
		public int removeParent(E obj) {
			if (isBuild) {
				init();
				isBuild = false;
			}
			return rbKA.removeParent(obj);
		}

		@Override
		public int removeChild(E obj) {
			if (isBuild) {
				init();
				isBuild = false;
			}
			return rbKA.removeChild(obj);
		}
		private List<E> orderlist=new ArrayList<E>();
		@Override
		public Collection<E> getChildren() {
			if(orderlist!=null) return orderlist;
			isBuild=true;
			return rbKA.getChildren();
		}

	}

}
