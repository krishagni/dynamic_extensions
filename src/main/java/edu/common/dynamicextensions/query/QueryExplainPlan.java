package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class QueryExplainPlan {
	private String rawPlanText;

	private QueryExplainPlanBlock rootBlock;

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

	public QueryExplainPlanBlock rootBlock() {
		return rootBlock;
	}

	public void rootBlock(QueryExplainPlanBlock rootBlock) {
		this.rootBlock = rootBlock;
		refreshSummary();
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

	public double getEstimatedJoinWork() {
		return rootBlock != null ? rootBlock.getEstimatedJoinWork() : 0.0D;
	}

	public double getEstimatedDependentSubqueryWork() {
		return rootBlock != null ? rootBlock.getEstimatedDependentSubqueryWork() : 0.0D;
	}

	private void refreshSummary() {
		nodes.clear();
		usingFilesort = false;
		usingTemporaryTable = false;
		dependentSubquery = false;
		queryCost = rootBlock != null ? rootBlock.queryCost() : 0.0D;
		if (rootBlock != null) {
			collectSummary(rootBlock);
		}
	}

	private void collectSummary(QueryExplainPlanBlock block) {
		usingFilesort = usingFilesort || block.isUsingFilesort();
		usingTemporaryTable = usingTemporaryTable || block.isUsingTemporaryTable();
		dependentSubquery = dependentSubquery || block.isDependent();

		for (QueryExplainPlanNode node : block.nodes()) {
			nodes.add(node);
			if (node.materializedFromSubquery() != null) {
				collectSummary(node.materializedFromSubquery());
			}

			for (QueryExplainPlanBlock attachedSubquery : node.attachedSubqueries()) {
				collectSummary(attachedSubquery);
			}
		}

		for (QueryExplainPlanBlock child : block.children()) {
			collectSummary(child);
		}
	}
}
