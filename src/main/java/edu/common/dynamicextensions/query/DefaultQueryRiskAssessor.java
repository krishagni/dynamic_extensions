package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.nutility.LogUtil;

public class DefaultQueryRiskAssessor implements QueryRiskAssessor {
	private static final LogUtil logger = LogUtil.getLogger(DefaultQueryRiskAssessor.class);

	private MySqlExplainPlanParser mysqlExplainPlanParser = new MySqlExplainPlanParser();

	@Override
	public QueryRiskAssessmentResult assess(QueryRiskAssessmentRequest request) {
		if (request.config() == null || !request.config().isEnabled() || !DbSettingsFactory.isMySQL()) {
			return QueryRiskAssessmentResult.ok(request.sql());
		}

		QueryExplainPlan plan = explain(request.sql(), request);
		List<String> rejectReasons = getRejectReasons(plan, request.config());

		if (rejectReasons.isEmpty() || request.config().isObserveOnly()) {
			if (!rejectReasons.isEmpty()) {
				logger.warn("Query risk assessor observe-only rejection: " + StringUtils.join(rejectReasons, "; "));
			}

			return QueryRiskAssessmentResult.ok(request.sql());
		}

		return QueryRiskAssessmentResult.reject(request.sql(), plan, StringUtils.join(rejectReasons, "; "));
	}

	private QueryExplainPlan explain(String sql, QueryRiskAssessmentRequest request) {
		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao(request.dataSource());
		String explainJson = jdbcDao.getResultSet("explain format=json " + sql, null, rs -> {
			if (!rs.next()) {
				throw new QueryRiskAssessmentException("MySQL did not return an explain plan");
			}

			return rs.getString(1);
		});

		return mysqlExplainPlanParser.parse(explainJson);
	}

	private List<String> getRejectReasons(QueryExplainPlan plan, QueryRiskAssessmentConfig config) {
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

		if (config.maxEstimatedJoinWork() > 0 && plan.getEstimatedJoinWork() > config.maxEstimatedJoinWork()) {
			reasons.add(
				"estimated join work " + plan.getEstimatedJoinWork() +
				" exceeds configured limit " + config.maxEstimatedJoinWork());
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

		addSortOrTempRejectReasons(plan, config, reasons);

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

		double dependentQueryWork = plan.getEstimatedDependentSubqueryWork();
		if (plan.isDependentSubquery() &&
			config.maxDependentSubqueryRows() > 0 &&
			dependentQueryWork > config.maxDependentSubqueryRows()) {
			reasons.add(
				"query uses dependent subquery with estimated repeated work " +
				dependentQueryWork +
				", exceeding configured limit " + config.maxDependentSubqueryRows());
		}

		return reasons;
	}

	private void addSortOrTempRejectReasons(QueryExplainPlan plan, QueryRiskAssessmentConfig config, List<String> reasons) {
		if (!plan.isUsingFilesort() && !plan.isUsingTemporaryTable()) {
			return;
		}

		if (config.maxFullScanRowsForSortOrTemp() <= 0) {
			return;
		}

		for (QueryExplainPlanNode node : plan.getNodes()) {
			if (node.isFullTableScan() && node.getRowsExaminedPerScan() > config.maxFullScanRowsForSortOrTemp()) {
				reasons.add(
					"table " + node.getTableName() + " uses full table scan over " +
					node.getRowsExaminedPerScan() +
					" rows while query uses filesort/temporary table, exceeding configured limit " +
					config.maxFullScanRowsForSortOrTemp());
			}
		}
	}

	private void addNodeRejectReasons(QueryExplainPlanNode node, QueryRiskAssessmentConfig config, List<String> reasons) {
		if (config.maxRowsExaminedPerScan() > 0 && node.getRowsExaminedPerScan() > config.maxRowsExaminedPerScan()) {
			reasons.add(
				"table " + node.getTableName() + " examines " + node.getRowsExaminedPerScan() +
				" rows per scan, exceeding configured limit " + config.maxRowsExaminedPerScan());
		}

		if (node.isFullTableScan() &&
			config.largeTableRows() > 0 &&
			config.minFilteredPercentForFullScan() > 0 &&
			node.getRowsExaminedPerScan() > config.largeTableRows() &&
			node.getFiltered() >= config.minFilteredPercentForFullScan()) {
			reasons.add(
				"table " + node.getTableName() + " uses full table scan over " + node.getRowsExaminedPerScan() +
				" rows with low selectivity " + node.getFiltered());
		}

		if (node.isMissingUsableKey() &&
			config.largeTableRows() > 0 &&
			StringUtils.isNotBlank(node.getAttachedCondition()) &&
			node.getRowsExaminedPerScan() > config.largeTableRows()) {
			reasons.add(
				"table " + node.getTableName() + " has possible keys but MySQL did not choose one for condition " +
				node.getAttachedCondition());
		}

		if (node.isUsingJoinBuffer() &&
			config.maxJoinBufferRows() > 0 &&
			node.getRowsProducedPerJoin() > config.maxJoinBufferRows()) {
			reasons.add(
				"table " + node.getTableName() + " uses join buffer/hash join (" + node.usingJoinBuffer() +
				") over estimated " + node.getRowsProducedPerJoin() +
				" intermediate rows, exceeding configured limit " + config.maxJoinBufferRows());
		}
	}
}
