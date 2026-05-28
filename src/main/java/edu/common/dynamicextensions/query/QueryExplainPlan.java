package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class QueryExplainPlan {
	private String rawPlanText;

	private List<QueryExplainPlanNode> nodes = new ArrayList<>();

	private boolean usingFilesort;

	private boolean usingTemporaryTable;

	private boolean dependentSubquery;

	private double queryCost;

	public String rawPlanText() {
		return rawPlanText;
	}

	public void rawPlanText(String rawPlanText) {
		this.rawPlanText = rawPlanText;
	}

	public List<QueryExplainPlanNode> getNodes() {
		return nodes;
	}

	public void addNode(QueryExplainPlanNode node) {
		nodes.add(node);
	}

	public boolean isUsingFilesort() {
		return usingFilesort;
	}

	public void setUsingFilesort(boolean usingFilesort) {
		this.usingFilesort = usingFilesort;
	}

	public boolean isUsingTemporaryTable() {
		return usingTemporaryTable;
	}

	public void setUsingTemporaryTable(boolean usingTemporaryTable) {
		this.usingTemporaryTable = usingTemporaryTable;
	}

	public long getTotalRowsExamined() {
		long rows = 0L;
		for (QueryExplainPlanNode node : nodes) {
			rows += node.getRowsExaminedPerScan();
		}

		return rows;
	}

	public boolean isDependentSubquery() {
		return dependentSubquery;
	}

	public void setDependentSubquery(boolean dependentSubquery) {
		this.dependentSubquery = dependentSubquery;
	}

	public long getMaxRowsProducedPerJoin() {
		long rows = 0L;
		for (QueryExplainPlanNode node : nodes) {
			rows = Math.max(rows, node.getRowsProducedPerJoin());
		}

		return rows;
	}

	public double queryCost() {
		return queryCost;
	}

	public void queryCost(double queryCost) {
		this.queryCost = queryCost;
	}
}
