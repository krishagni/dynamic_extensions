package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.nutility.LogUtil;

public class DefaultQueryOptimiser implements QueryOptimiser {
	private static final LogUtil logger = LogUtil.getLogger(DefaultQueryOptimiser.class);

	private MySqlExplainPlanParser mysqlExplainPlanParser = new MySqlExplainPlanParser();

	@Override
	public QueryOptimisationResult optimise(QueryOptimisationRequest request) {
		if (request.config() == null || !request.config().isEnabled() || !DbSettingsFactory.isMySQL()) {
			return QueryOptimisationResult.ok(request.sql());
		}

		QueryExplainPlan plan = explain(request.sql(), request);
		List<String> rejectReasons = getRejectReasons(plan, request.config());
		if (StringUtils.isNotBlank(request.originalSql()) && !StringUtils.equals(request.originalSql(), request.sql())) {
			return optimiseRewrittenSql(request, plan, rejectReasons);
		}

		if (rejectReasons.isEmpty() || request.config().isObserveOnly()) {
			if (!rejectReasons.isEmpty()) {
				logger.warn("Query optimiser observe-only rejection: " + StringUtils.join(rejectReasons, "; "));
			}

			return QueryOptimisationResult.ok(request.sql());
		}

		return QueryOptimisationResult.reject(request.sql(), plan, StringUtils.join(rejectReasons, "; "));
	}

	private QueryOptimisationResult optimiseRewrittenSql(
		QueryOptimisationRequest request, QueryExplainPlan candidatePlan, List<String> candidateRejectReasons) {
		QueryExplainPlan originalPlan = explain(request.originalSql(), request);
		List<String> originalRejectReasons = getRejectReasons(originalPlan, request.config());

		if (candidateRejectReasons.isEmpty() && (isBetter(candidatePlan, originalPlan) || !originalRejectReasons.isEmpty())) {
			return QueryOptimisationResult.ok(request.sql());
		}

		if (originalRejectReasons.isEmpty()) {
			return QueryOptimisationResult.ok(request.originalSql());
		}

		if (request.config().isObserveOnly()) {
			logger.warn("Query optimiser observe-only rejection: " + StringUtils.join(candidateRejectReasons, "; "));
			return QueryOptimisationResult.ok(request.originalSql());
		}

		return QueryOptimisationResult.reject(request.sql(), candidatePlan, StringUtils.join(candidateRejectReasons, "; "));
	}

	private boolean isBetter(QueryExplainPlan candidatePlan, QueryExplainPlan originalPlan) {
		return
			candidatePlan.getTotalRowsExamined() < originalPlan.getTotalRowsExamined() ||
			candidatePlan.getMaxRowsProducedPerJoin() < originalPlan.getMaxRowsProducedPerJoin() ||
			(originalPlan.queryCost() > 0 && candidatePlan.queryCost() > 0 && candidatePlan.queryCost() < originalPlan.queryCost());
	}

	private QueryExplainPlan explain(String sql, QueryOptimisationRequest request) {
		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao(request.dataSource());
		String explainJson = jdbcDao.getResultSet("explain format=json " + sql, null, rs -> {
			if (!rs.next()) {
				throw new QueryOptimisationException("MySQL did not return an explain plan");
			}

			return rs.getString(1);
		});

		return mysqlExplainPlanParser.parse(explainJson);
	}

	private List<String> getRejectReasons(QueryExplainPlan plan, QueryOptimisationConfig config) {
		List<String> reasons = new ArrayList<>();

		if (config.maxTotalRowsExamined() > 0 && plan.getTotalRowsExamined() > config.maxTotalRowsExamined()) {
			reasons.add(
				"estimated rows examined " + plan.getTotalRowsExamined() +
				" exceeds configured limit " + config.maxTotalRowsExamined());
		}

		if (config.maxQueryCost() > 0 && plan.queryCost() > config.maxQueryCost()) {
			reasons.add(
				"estimated query cost " + plan.queryCost() +
				" exceeds configured limit " + config.maxQueryCost());
		}

		if (config.maxRowsProducedPerJoin() > 0 && plan.getMaxRowsProducedPerJoin() > config.maxRowsProducedPerJoin()) {
			reasons.add(
				"estimated rows produced per join " + plan.getMaxRowsProducedPerJoin() +
				" exceeds configured limit " + config.maxRowsProducedPerJoin());
		}

		if (config.maxJoinTables() > 0 && plan.getNodes().size() > config.maxJoinTables()) {
			reasons.add(
				"query joins " + plan.getNodes().size() +
				" tables, exceeding configured limit " + config.maxJoinTables());
		}

		for (QueryExplainPlanNode node : plan.getNodes()) {
			addNodeRejectReasons(node, config, reasons);
		}

		if (plan.isUsingFilesort() && config.maxSortRows() > 0 && plan.getMaxRowsProducedPerJoin() > config.maxSortRows()) {
			reasons.add(
				"query uses filesort over estimated " + plan.getMaxRowsProducedPerJoin() +
				" rows, exceeding configured limit " + config.maxSortRows());
		}

		if (plan.isUsingTemporaryTable() && config.maxTempTableRows() > 0 && plan.getMaxRowsProducedPerJoin() > config.maxTempTableRows()) {
			reasons.add(
				"query uses temporary table over estimated " + plan.getMaxRowsProducedPerJoin() +
				" rows, exceeding configured limit " + config.maxTempTableRows());
		}

		if (plan.isDependentSubquery() &&
			config.maxDependentSubqueryRows() > 0 &&
			plan.getMaxRowsProducedPerJoin() > config.maxDependentSubqueryRows()) {
			reasons.add(
				"query uses dependent subquery over estimated " + plan.getMaxRowsProducedPerJoin() +
				" rows, exceeding configured limit " + config.maxDependentSubqueryRows());
		}

		return reasons;
	}

	private void addNodeRejectReasons(QueryExplainPlanNode node, QueryOptimisationConfig config, List<String> reasons) {
		if (config.maxRowsExaminedPerScan() > 0 && node.getRowsExaminedPerScan() > config.maxRowsExaminedPerScan()) {
			reasons.add(
				"table " + node.getTableName() + " examines " + node.getRowsExaminedPerScan() +
				" rows per scan, exceeding configured limit " + config.maxRowsExaminedPerScan());
		}

		if (node.isFullTableScan() &&
			node.getRowsExaminedPerScan() > config.largeTableRows() &&
			node.getFiltered() >= config.minFilteredPercentForFullScan()) {
			reasons.add(
				"table " + node.getTableName() + " uses full table scan over " + node.getRowsExaminedPerScan() +
				" rows with low selectivity");
		}

		if (node.isMissingUsableKey() &&
			StringUtils.isNotBlank(node.getAttachedCondition()) &&
			node.getRowsExaminedPerScan() > config.largeTableRows()) {
			reasons.add(
				"table " + node.getTableName() + " has possible keys but MySQL did not choose one for condition " +
				node.getAttachedCondition());
		}
	}
}
