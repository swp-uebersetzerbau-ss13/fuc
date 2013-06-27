package swp_compiler_ss13.fuc.ir;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class WatchedList<T> {

	private Logger logger = Logger.getLogger(WatchedList.class);

	private List<T> list;

	public List<T> getInnerList() {
		return list;
	}

	public WatchedList() {
		this.list = new LinkedList<T>();
	}

	public WatchedList(List<T> list) {
		this.list = list;
	}

	public void add(T e) {
		logger.debug("add " + e);
		list.add(e);
	}

	public void addAll(Collection<? extends T> es) {
		for (T e : es) {
			logger.debug("add " + e);
		}
		list.addAll(es);
	}


}