package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryRiskAssessmentMetrics {
	private double queryCost;

	private long totalRowsExamined;

	private long maxRowsProducedPerJoin;

	private double estimatedJoinWork;

	private double estimatedDependentSubqueryWork;

	private int tableCount;

	private boolean usingFilesort;

	private boolean usingTemporaryTable;

	private boolean dependentSubquery;

	private List<TableMetrics> tables = new ArrayList<>();

	public static QueryRiskAssessmentMetrics from(QueryExplainPlan plan) {
		QueryRiskAssessmentMetrics metrics = new QueryRiskAssessmentMetrics();
		if (plan == null) {
			return metrics;
		}

		metrics.queryCost = plan.queryCost();
		metrics.totalRowsExamined = plan.getTotalRowsExamined();
		metrics.maxRowsProducedPerJoin = plan.getMaxRowsProducedPerJoin();
		metrics.estimatedJoinWork = plan.getEstimatedJoinWork();
		metrics.estimatedDependentSubqueryWork = plan.getEstimatedDependentSubqueryWork();
		metrics.tableCount = plan.getNodes().size();
		metrics.usingFilesort = plan.isUsingFilesort();
		metrics.usingTemporaryTable = plan.isUsingTemporaryTable();
		metrics.dependentSubquery = plan.isDependentSubquery();

		for (QueryExplainPlanNode node : plan.getNodes()) {
			metrics.tables.add(TableMetrics.from(node));
		}

		return metrics;
	}

	public double queryCost() {
		return queryCost;
	}

	public long totalRowsExamined() {
		return totalRowsExamined;
	}

	public long maxRowsProducedPerJoin() {
		return maxRowsProducedPerJoin;
	}

	public double estimatedJoinWork() {
		return estimatedJoinWork;
	}

	public double estimatedDependentSubqueryWork() {
		return estimatedDependentSubqueryWork;
	}

	public int tableCount() {
		return tableCount;
	}

	public boolean isUsingFilesort() {
		return usingFilesort;
	}

	public boolean isUsingTemporaryTable() {
		return usingTemporaryTable;
	}

	public boolean isDependentSubquery() {
		return dependentSubquery;
	}

	public List<TableMetrics> tables() {
		return Collections.unmodifiableList(tables);
	}

	public static class TableMetrics {
		private String tableName;

		private String accessType;

		private List<String> possibleKeys = new ArrayList<>();

		private String key;

		private long rowsExaminedPerScan;

		private long rowsProducedPerJoin;

		private double filtered;

		private String attachedCondition;

		private String usingJoinBuffer;

		private boolean fullTableScan;

		private boolean missingUsableKey;

		public static TableMetrics from(QueryExplainPlanNode node) {
			TableMetrics metrics = new TableMetrics();
			metrics.tableName = node.getTableName();
			metrics.accessType = node.getAccessType();
			metrics.possibleKeys.addAll(node.getPossibleKeys());
			metrics.key = node.getKey();
			metrics.rowsExaminedPerScan = node.getRowsExaminedPerScan();
			metrics.rowsProducedPerJoin = node.getRowsProducedPerJoin();
			metrics.filtered = node.getFiltered();
			metrics.attachedCondition = node.getAttachedCondition();
			metrics.usingJoinBuffer = node.usingJoinBuffer();
			metrics.fullTableScan = node.isFullTableScan();
			metrics.missingUsableKey = node.isMissingUsableKey();
			return metrics;
		}

		public String tableName() {
			return tableName;
		}

		public String accessType() {
			return accessType;
		}

		public List<String> possibleKeys() {
			return Collections.unmodifiableList(possibleKeys);
		}

		public String key() {
			return key;
		}

		public long rowsExaminedPerScan() {
			return rowsExaminedPerScan;
		}

		public long rowsProducedPerJoin() {
			return rowsProducedPerJoin;
		}

		public double filtered() {
			return filtered;
		}

		public String attachedCondition() {
			return attachedCondition;
		}

		public String usingJoinBuffer() {
			return usingJoinBuffer;
		}

		public boolean isFullTableScan() {
			return fullTableScan;
		}

		public boolean isMissingUsableKey() {
			return missingUsableKey;
		}

		public boolean isUsingJoinBuffer() {
			return usingJoinBuffer != null && !usingJoinBuffer.trim().isEmpty();
		}
	}
}
