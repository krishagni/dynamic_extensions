package edu.common.dynamicextensions.query;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryOptimisationConfig {
	private static final ObjectMapper mapper = new ObjectMapper();

	private boolean enabled = true;

	private boolean observeOnly;

	private boolean rewriteInnerJoins = true;

	private long largeTableRows = 1000000L;

	private long maxRowsExaminedPerScan = 1000000L;

	private long maxRowsProducedPerJoin = 5000000L;

	private long maxTotalRowsExamined = 10000000L;

	private long maxSortRows = 1000000L;

	private long maxTempTableRows = 1000000L;

	private long maxDependentSubqueryRows = 100000L;

	private int maxJoinTables = 30;

	private double maxQueryCost = -1.0D;

	private double minFilteredPercentForFullScan = 10.0D;

	public static QueryOptimisationConfig defaultConfig() {
		return new QueryOptimisationConfig();
	}

	public static QueryOptimisationConfig fromJson(File file) {
		try {
			return fromMap(mapper.readValue(file, new TypeReference<Map<String, Object>>() {}));
		} catch (IOException e) {
			throw new QueryOptimisationException("Error reading query optimisation config: " + file.getAbsolutePath(), e);
		}
	}

	public static QueryOptimisationConfig fromJson(InputStream input) {
		try {
			return fromMap(mapper.readValue(input, new TypeReference<Map<String, Object>>() {}));
		} catch (IOException e) {
			throw new QueryOptimisationException("Error reading query optimisation config", e);
		}
	}

	public static QueryOptimisationConfig fromJson(String json) {
		try {
			return fromMap(mapper.readValue(json, new TypeReference<Map<String, Object>>() {}));
		} catch (IOException e) {
			throw new QueryOptimisationException("Error reading query optimisation config", e);
		}
	}

	public static QueryOptimisationConfig fromMap(Map<String, Object> values) {
		QueryOptimisationConfig config = defaultConfig();
		if (values == null) {
			return config;
		}

		if (values.containsKey("enabled")) {
			config.enabled(booleanValue(values, "enabled"));
		}

		if (values.containsKey("observeOnly")) {
			config.observeOnly(booleanValue(values, "observeOnly"));
		}

		if (values.containsKey("rewriteInnerJoins")) {
			config.rewriteInnerJoins(booleanValue(values, "rewriteInnerJoins"));
		}

		if (values.containsKey("largeTableRows")) {
			config.largeTableRows(longValue(values, "largeTableRows"));
		}

		if (values.containsKey("maxRowsExaminedPerScan")) {
			config.maxRowsExaminedPerScan(longValue(values, "maxRowsExaminedPerScan"));
		}

		if (values.containsKey("maxRowsProducedPerJoin")) {
			config.maxRowsProducedPerJoin(longValue(values, "maxRowsProducedPerJoin"));
		}

		if (values.containsKey("maxTotalRowsExamined")) {
			config.maxTotalRowsExamined(longValue(values, "maxTotalRowsExamined"));
		}

		if (values.containsKey("maxSortRows")) {
			config.maxSortRows(longValue(values, "maxSortRows"));
		}

		if (values.containsKey("maxTempTableRows")) {
			config.maxTempTableRows(longValue(values, "maxTempTableRows"));
		}

		if (values.containsKey("maxDependentSubqueryRows")) {
			config.maxDependentSubqueryRows(longValue(values, "maxDependentSubqueryRows"));
		}

		if (values.containsKey("maxJoinTables")) {
			config.maxJoinTables(intValue(values, "maxJoinTables"));
		}

		if (values.containsKey("maxQueryCost")) {
			config.maxQueryCost(doubleValue(values, "maxQueryCost"));
		}

		if (values.containsKey("minFilteredPercentForFullScan")) {
			config.minFilteredPercentForFullScan(doubleValue(values, "minFilteredPercentForFullScan"));
		}

		return config;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public QueryOptimisationConfig enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public boolean isObserveOnly() {
		return observeOnly;
	}

	public QueryOptimisationConfig observeOnly(boolean observeOnly) {
		this.observeOnly = observeOnly;
		return this;
	}

	public boolean isRewriteInnerJoins() {
		return rewriteInnerJoins;
	}

	public QueryOptimisationConfig rewriteInnerJoins(boolean rewriteInnerJoins) {
		this.rewriteInnerJoins = rewriteInnerJoins;
		return this;
	}

	public long largeTableRows() {
		return largeTableRows;
	}

	public QueryOptimisationConfig largeTableRows(long largeTableRows) {
		this.largeTableRows = largeTableRows;
		return this;
	}

	public long maxRowsExaminedPerScan() {
		return maxRowsExaminedPerScan;
	}

	public QueryOptimisationConfig maxRowsExaminedPerScan(long maxRowsExaminedPerScan) {
		this.maxRowsExaminedPerScan = maxRowsExaminedPerScan;
		return this;
	}

	public long maxRowsProducedPerJoin() {
		return maxRowsProducedPerJoin;
	}

	public QueryOptimisationConfig maxRowsProducedPerJoin(long maxRowsProducedPerJoin) {
		this.maxRowsProducedPerJoin = maxRowsProducedPerJoin;
		return this;
	}

	public long maxTotalRowsExamined() {
		return maxTotalRowsExamined;
	}

	public QueryOptimisationConfig maxTotalRowsExamined(long maxTotalRowsExamined) {
		this.maxTotalRowsExamined = maxTotalRowsExamined;
		return this;
	}

	public long maxSortRows() {
		return maxSortRows;
	}

	public QueryOptimisationConfig maxSortRows(long maxSortRows) {
		this.maxSortRows = maxSortRows;
		return this;
	}

	public long maxTempTableRows() {
		return maxTempTableRows;
	}

	public QueryOptimisationConfig maxTempTableRows(long maxTempTableRows) {
		this.maxTempTableRows = maxTempTableRows;
		return this;
	}

	public long maxDependentSubqueryRows() {
		return maxDependentSubqueryRows;
	}

	public QueryOptimisationConfig maxDependentSubqueryRows(long maxDependentSubqueryRows) {
		this.maxDependentSubqueryRows = maxDependentSubqueryRows;
		return this;
	}

	public int maxJoinTables() {
		return maxJoinTables;
	}

	public QueryOptimisationConfig maxJoinTables(int maxJoinTables) {
		this.maxJoinTables = maxJoinTables;
		return this;
	}

	public double maxQueryCost() {
		return maxQueryCost;
	}

	public QueryOptimisationConfig maxQueryCost(double maxQueryCost) {
		this.maxQueryCost = maxQueryCost;
		return this;
	}

	public double minFilteredPercentForFullScan() {
		return minFilteredPercentForFullScan;
	}

	public QueryOptimisationConfig minFilteredPercentForFullScan(double minFilteredPercentForFullScan) {
		this.minFilteredPercentForFullScan = minFilteredPercentForFullScan;
		return this;
	}

	private static boolean booleanValue(Map<String, Object> values, String name) {
		Object value = values.get(name);
		if (value instanceof Boolean) {
			return (Boolean)value;
		} else if (value instanceof String) {
			return Boolean.parseBoolean((String)value);
		}

		throw new QueryOptimisationException("Invalid boolean value for query optimisation config: " + name);
	}

	private static long longValue(Map<String, Object> values, String name) {
		Object value = values.get(name);
		if (value instanceof Number) {
			return ((Number)value).longValue();
		} else if (value instanceof String) {
			return Long.parseLong((String)value);
		}

		throw new QueryOptimisationException("Invalid long value for query optimisation config: " + name);
	}

	private static int intValue(Map<String, Object> values, String name) {
		Object value = values.get(name);
		if (value instanceof Number) {
			return ((Number)value).intValue();
		} else if (value instanceof String) {
			return Integer.parseInt((String)value);
		}

		throw new QueryOptimisationException("Invalid integer value for query optimisation config: " + name);
	}

	private static double doubleValue(Map<String, Object> values, String name) {
		Object value = values.get(name);
		if (value instanceof Number) {
			return ((Number)value).doubleValue();
		} else if (value instanceof String) {
			return Double.parseDouble((String)value);
		}

		throw new QueryOptimisationException("Invalid double value for query optimisation config: " + name);
	}
}
