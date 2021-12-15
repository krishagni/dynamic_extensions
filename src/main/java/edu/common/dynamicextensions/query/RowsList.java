package edu.common.dynamicextensions.query;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.query.cachestore.LinkedEhCacheMap;

public class RowsList {
	private static final Logger logger = Logger.getLogger(RowsList.class);

	private static final AtomicInteger openCount = new AtomicInteger(0);

	private LinkedEhCacheMap<Integer, Object[]> rows = new LinkedEhCacheMap<>();

	public RowsList() {
		logger.info("Create: Open rows list: " + openCount.incrementAndGet());
	}

	public void add(Object[] row) {
		rows.put(rows.size(), row);
	}

	public int size() {
		return rows.size();
	}

	public Iterator<Object[]> iterator() {
		return rows.iterator();
	}

	public void cleanup() {
		if (rows == null) {
			return;
		}

		rows.destroy();
		rows = null;
		logger.info("Destruct: Open rows list: " + openCount.decrementAndGet());
	}
}
