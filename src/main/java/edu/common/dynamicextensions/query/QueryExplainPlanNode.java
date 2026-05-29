package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class QueryExplainPlanNode {
	private String tableName;

	private String accessType;

	private List<String> possibleKeys = new ArrayList<>();

	private String key;

	private long rowsExaminedPerScan;

	private long rowsProducedPerJoin;

	private double filtered;

	private String attachedCondition;

	private String usingJoinBuffer;

	private QueryExplainPlanBlock materializedFromSubquery;

	private List<QueryExplainPlanBlock> attachedSubqueries = new ArrayList<>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public List<String> getPossibleKeys() {
		return possibleKeys;
	}

	public void addPossibleKey(String possibleKey) {
		possibleKeys.add(possibleKey);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getRowsExaminedPerScan() {
		return rowsExaminedPerScan;
	}

	public void setRowsExaminedPerScan(long rowsExaminedPerScan) {
		this.rowsExaminedPerScan = rowsExaminedPerScan;
	}

	public long getRowsProducedPerJoin() {
		return rowsProducedPerJoin;
	}

	public void setRowsProducedPerJoin(long rowsProducedPerJoin) {
		this.rowsProducedPerJoin = rowsProducedPerJoin;
	}

	public double getFiltered() {
		return filtered;
	}

	public void setFiltered(double filtered) {
		this.filtered = filtered;
	}

	public String getAttachedCondition() {
		return attachedCondition;
	}

	public void setAttachedCondition(String attachedCondition) {
		this.attachedCondition = attachedCondition;
	}

	public boolean isFullTableScan() {
		return "ALL".equalsIgnoreCase(accessType);
	}

	public boolean isMissingUsableKey() {
		return key == null && !possibleKeys.isEmpty();
	}

	public String usingJoinBuffer() {
		return usingJoinBuffer;
	}

	public void usingJoinBuffer(String usingJoinBuffer) {
		this.usingJoinBuffer = usingJoinBuffer;
	}

	public boolean isUsingJoinBuffer() {
		return usingJoinBuffer != null && !usingJoinBuffer.trim().isEmpty();
	}

	public QueryExplainPlanBlock materializedFromSubquery() {
		return materializedFromSubquery;
	}

	public void materializedFromSubquery(QueryExplainPlanBlock materializedFromSubquery) {
		this.materializedFromSubquery = materializedFromSubquery;
	}

	public List<QueryExplainPlanBlock> attachedSubqueries() {
		return attachedSubqueries;
	}

	public void addAttachedSubquery(QueryExplainPlanBlock attachedSubquery) {
		attachedSubqueries.add(attachedSubquery);
	}
}
