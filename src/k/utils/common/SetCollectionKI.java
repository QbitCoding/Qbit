package k.utils.common;

import java.util.Collection;
/**
 * 放入改Collection的元素不会重复
 * @author Qibt
 *
 * @param <K>
 */
public interface SetCollectionKI<K> extends Collection<K>,QbitKI {
	Collection<K> getCollection();
}
