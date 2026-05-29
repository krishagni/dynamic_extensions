package edu.common.dynamicextensions.query;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryRiskAssessmentConfig {
	private static final ObjectMapper mapper = new ObjectMapper();

	private boolean enabled = true;

	private boolean observeOnly;

	private long largeTableRows = 1000000L;

	private long maxRowsExaminedPerScan = 1000000L;

	private long maxRowsProducedPerJoin = 5000000L;

	private long maxTotalRowsExamined = 10000000L;

	private long maxSortRows = 1000000L;

	private long maxTempTableRows = 1000000L;

	private long maxDependentSubqueryRows = 100000L;

	private int maxJoinTables = 30;

	private double maxQueryCost = -1.0D;

	private double maxEstimatedJoinWork = -1.0D;

	private long maxJoinBufferRows = 100000L;

	private double minFilteredPercentForFullScan = 10.0D;

	public static QueryRiskAssessmentConfig defaultConfig() {
		return new QueryRiskAssessmentConfig();
	}

	public static QueryRiskAssessmentConfig fromJson(File file) {
		try {
			return fromMap(mapper.readValue(file, new TypeReference<Map<String, Object>>() {}));
		} catch (IOException e) {
			throw new QueryRiskAssessmentException("Error reading query risk assessment config: " + file.getAbsolutePath(), e);
		}
	}

	public static QueryRiskAssessmentConfig fromJson(InputStream input) {
		try {
			return fromMap(mapper.readValue(input, new TypeReference<Map<String, Object>>() {}));
		} catch (IOException e) {
			throw new QueryRiskAssessmentException("Error reading query risk assessment config", e);
		}
	}

	public static QueryRiskAssessmentConfig fromJson(String json) {
		try {
			return fromMap(mapper.readValue(json, new TypeReference<Map<String, Object>>() {}));
		} catch (IOException e) {
			throw new QueryRiskAssessmentException("Error reading query risk assessment config", e);
		}
	}

	public static QueryRiskAssessmentConfig fromMap(Map<String, Object> values) {
		QueryRiskAssessmentConfig config = defaultConfig();
		if (values == null) {
			return config;
		}

		if (values.containsKey("enabled")) {
			config.enabled(booleanValue(values, "enabled"));
		}

		if (values.containsKey("observeOnly")) {
			config.observeOnly(booleanValue(values, "observeOnly"));
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

		if (values.containsKey("maxEstimatedJoinWork")) {
			config.maxEstimatedJoinWork(doubleValue(values, "maxEstimatedJoinWork"));
		}

		if (values.containsKey("maxJoinBufferRows")) {
			config.maxJoinBufferRows(longValue(values, "maxJoinBufferRows"));
		}

		if (values.containsKey("minFilteredPercentForFullScan")) {
			config.minFilteredPercentForFullScan(doubleValue(values, "minFilteredPercentForFullScan"));
		}

		return config;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public QueryRiskAssessmentConfig enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public boolean isObserveOnly() {
		return observeOnly;
	}

	public QueryRiskAssessmentConfig observeOnly(boolean observeOnly) {
		this.observeOnly = observeOnly;
		return this;
	}

	public long largeTableRows() {
		return largeTableRows;
	}

	public QueryRiskAssessmentConfig largeTableRows(long largeTableRows) {
		this.largeTableRows = largeTableRows;
		return this;
	}

	public long maxRowsExaminedPerScan() {
		return maxRowsExaminedPerScan;
	}

	public QueryRiskAssessmentConfig maxRowsExaminedPerScan(long maxRowsExaminedPerScan) {
		this.maxRowsExaminedPerScan = maxRowsExaminedPerScan;
		return this;
	}

	public long maxRowsProducedPerJoin() {
		return maxRowsProducedPerJoin;
	}

	public QueryRiskAssessmentConfig maxRowsProducedPerJoin(long maxRowsProducedPerJoin) {
		this.maxRowsProducedPerJoin = maxRowsProducedPerJoin;
		return this;
	}

	public long maxTotalRowsExamined() {
		return maxTotalRowsExamined;
	}

	public QueryRiskAssessmentConfig maxTotalRowsExamined(long maxTotalRowsExamined) {
		this.maxTotalRowsExamined = maxTotalRowsExamined;
		return this;
	}

	public long maxSortRows() {
		return maxSortRows;
	}

	public QueryRiskAssessmentConfig maxSortRows(long maxSortRows) {
		this.maxSortRows = maxSortRows;
		return this;
	}

	public long maxTempTableRows() {
		return maxTempTableRows;
	}

	public QueryRiskAssessmentConfig maxTempTableRows(long maxTempTableRows) {
		this.maxTempTableRows = maxTempTableRows;
		return this;
	}

	public long maxDependentSubqueryRows() {
		return maxDependentSubqueryRows;
	}

	public QueryRiskAssessmentConfig maxDependentSubqueryRows(long maxDependentSubqueryRows) {
		this.maxDependentSubqueryRows = maxDependentSubqueryRows;
		return this;
	}

	public int maxJoinTables() {
		return maxJoinTables;
	}

	public QueryRiskAssessmentConfig maxJoinTables(int maxJoinTables) {
		this.maxJoinTables = maxJoinTables;
		return this;
	}

	public double maxQueryCost() {
		return maxQueryCost;
	}

	public QueryRiskAssessmentConfig maxQueryCost(double maxQueryCost) {
		this.maxQueryCost = maxQueryCost;
		return this;
	}

	public double maxEstimatedJoinWork() {
		return maxEstimatedJoinWork;
	}

	public QueryRiskAssessmentConfig maxEstimatedJoinWork(double maxEstimatedJoinWork) {
		this.maxEstimatedJoinWork = maxEstimatedJoinWork;
		return this;
	}

	public long maxJoinBufferRows() {
		return maxJoinBufferRows;
	}

	public QueryRiskAssessmentConfig maxJoinBufferRows(long maxJoinBufferRows) {
		this.maxJoinBufferRows = maxJoinBufferRows;
		return this;
	}

	public double minFilteredPercentForFullScan() {
		return minFilteredPercentForFullScan;
	}

	public QueryRiskAssessmentConfig minFilteredPercentForFullScan(double minFilteredPercentForFullScan) {
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

		throw new QueryRiskAssessmentException("Invalid boolean value for query risk assessment config: " + name);
	}

	private static long longValue(Map<String, Object> values, String name) {
		Object value = values.get(name);
		if (value instanceof Number) {
			return ((Number)value).longValue();
		} else if (value instanceof String) {
			return Long.parseLong((String)value);
		}

		throw new QueryRiskAssessmentException("Invalid long value for query risk assessment config: " + name);
	}

	private static int intValue(Map<String, Object> values, String name) {
		Object value = values.get(name);
		if (value instanceof Number) {
			return ((Number)value).intValue();
		} else if (value instanceof String) {
			return Integer.parseInt((String)value);
		}

		throw new QueryRiskAssessmentException("Invalid integer value for query risk assessment config: " + name);
	}

	private static double doubleValue(Map<String, Object> values, String name) {
		Object value = values.get(name);
		if (value instanceof Number) {
			return ((Number)value).doubleValue();
		} else if (value instanceof String) {
			return Double.parseDouble((String)value);
		}

		throw new QueryRiskAssessmentException("Invalid double value for query risk assessment config: " + name);
	}
}
