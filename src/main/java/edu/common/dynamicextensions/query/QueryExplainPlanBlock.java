package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class QueryExplainPlanBlock {
	private String type;

	private boolean usingFilesort;

	private boolean usingTemporaryTable;

	private boolean dependent;

	private Boolean cacheable;

	private double queryCost;

	private List<QueryExplainPlanNode> nodes = new ArrayList<>();

	private List<QueryExplainPlanBlock> children = new ArrayList<>();

	public QueryExplainPlanBlock(String type) {
		this.type = type;
	}

	public String type() {
		return type;
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

	public boolean isDependent() {
		return dependent;
	}

	public void setDependent(boolean dependent) {
		this.dependent = dependent;
	}

	public Boolean cacheable() {
		return cacheable;
	}

	public void cacheable(Boolean cacheable) {
		this.cacheable = cacheable;
	}

	public double queryCost() {
		return queryCost;
	}

	public void queryCost(double queryCost) {
		this.queryCost = queryCost;
	}

	public List<QueryExplainPlanNode> nodes() {
		return nodes;
	}

	public void addNode(QueryExplainPlanNode node) {
		nodes.add(node);
	}

	public List<QueryExplainPlanBlock> children() {
		return children;
	}

	public void addChild(QueryExplainPlanBlock block) {
		children.add(block);
	}

	public double getEstimatedJoinWork() {
		double work = 0.0D;
		double previousRowsProduced = 1.0D;
		for (QueryExplainPlanNode node : nodes) {
			work += node.getRowsExaminedPerScan() * previousRowsProduced;
			previousRowsProduced = Math.max(1.0D, node.getRowsProducedPerJoin());

			if (node.materializedFromSubquery() != null) {
				work += node.materializedFromSubquery().getEstimatedJoinWork();
			}

			for (QueryExplainPlanBlock attachedSubquery : node.attachedSubqueries()) {
				work += attachedSubquery.getEstimatedJoinWork();
			}
		}

		for (QueryExplainPlanBlock child : children) {
			work += child.getEstimatedJoinWork();
		}

		return work;
	}

	public double getEstimatedDependentSubqueryWork() {
		return getEstimatedDependentSubqueryWork(1.0D);
	}

	private double getEstimatedDependentSubqueryWork(double outerRows) {
		double work = 0.0D;

		for (QueryExplainPlanNode node : nodes) {
			double rowsProduced = Math.max(1.0D, node.getRowsProducedPerJoin());

			if (node.materializedFromSubquery() != null) {
				// Materialised subquery is assumed to execute once.
				// Only dependent subqueries inside it are counted here.
				work += node.materializedFromSubquery().getEstimatedDependentSubqueryWork(1.0D);
			}

			for (QueryExplainPlanBlock attachedSubquery : node.attachedSubqueries()) {
				if (attachedSubquery.isDependent()) {
					work += rowsProduced * attachedSubquery.getEstimatedJoinWork();
				}

				// Count dependent subqueries nested inside this attached subquery.
				work += attachedSubquery.getEstimatedDependentSubqueryWork(rowsProduced);
			}
		}

		for (QueryExplainPlanBlock child : children) {
			if (child.isDependent()) {
				work += outerRows * child.getEstimatedJoinWork();
			}

			work += child.getEstimatedDependentSubqueryWork(outerRows);
		}

		return work;
	}
}
