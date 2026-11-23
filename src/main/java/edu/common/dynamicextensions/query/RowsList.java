package edu.common.dynamicextensions.query;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import edu.common.dynamicextensions.nutility.DeConfiguration;
import edu.common.dynamicextensions.nutility.LogUtil;
import edu.common.dynamicextensions.query.store.ResultStore;
import edu.common.dynamicextensions.query.store.SpillableResultStore;

public class RowsList {
	private static final LogUtil logger = LogUtil.getLogger(RowsList.class);

	private static final AtomicInteger openCount = new AtomicInteger(0);

	private ResultStore<Object[]> rows;

	public RowsList() {
		this(DeConfiguration.getInstance().maxResultStoreValuesInMemory());
	}

	public RowsList(int maxValuesInMemory) {
		this(new SpillableResultStore<>(maxValuesInMemory));
	}

	public RowsList(ResultStore<Object[]> rows) {
		this.rows = rows;
		logger.info("Create: Open rows list: " + openCount.incrementAndGet());
	}

	public void add(Object[] row) {
		rows.add(row);
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

		try {
			rows.close();
			rows = null;
			logger.info("Destruct: Open rows list: " + openCount.decrementAndGet());
		} catch (Exception e) {
			logger.error("Error closing/destructing open rows list", e);
		}
	}
}
