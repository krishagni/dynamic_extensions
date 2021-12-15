package edu.common.dynamicextensions.query;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuerySpaceCache {
	private static final int MAX_ELEMENTS = 25;

	private static final QuerySpaceCache instance = new QuerySpaceCache();

	private final Map<String, QuerySpace> cacheMap = Collections.synchronizedMap(
		new LinkedHashMap<String, QuerySpace>(MAX_ELEMENTS + 2, 0.75f, true) {
			private static final long serialVersionUID = -9164975002022907616L;

			@Override
			public boolean removeEldestEntry(Map.Entry<String, QuerySpace> eldest) {
				return size() > MAX_ELEMENTS;
			}
		});

	private QuerySpaceCache() { }

	public static QuerySpaceCache getInstance() {
		return instance;
	}

	public QuerySpace get(String name) {
		return cacheMap.get(name);
	}

	public QuerySpace put(QuerySpace qs) {
		if (qs == null) {
			return null;
		}

		return cacheMap.put(qs.getName(), qs);
	}

	public QuerySpace remove(String name) {
		return cacheMap.remove(name);
	}

	public QuerySpace remove(QuerySpace qs) {
		if (qs == null) {
			return null;
		}

		return cacheMap.remove(qs.getName());
	}
}
