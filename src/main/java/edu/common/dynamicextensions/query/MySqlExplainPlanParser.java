package edu.common.dynamicextensions.query;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MySqlExplainPlanParser {
	private static final ObjectMapper mapper = new ObjectMapper();

	public QueryExplainPlan parse(String explainJson) {
		try {
			JsonNode root = mapper.readTree(explainJson);

			QueryExplainPlan plan = new QueryExplainPlan();
			plan.rawPlanText(explainJson);
			plan.rootBlock(parseQueryBlock(root.path("query_block"), "query_block"));
			return plan;
		} catch (IOException e) {
			throw new QueryRiskAssessmentException("Error parsing MySQL explain plan", e);
		}
	}

	private QueryExplainPlanBlock parseQueryBlock(JsonNode queryBlock, String type) {
		QueryExplainPlanBlock block = new QueryExplainPlanBlock(type);
		collectOperationFlags(queryBlock, block);
		boolean hasDirectSource = hasDirectSource(queryBlock);

		if (queryBlock.has("nested_loop")) {
			for (JsonNode item : queryBlock.get("nested_loop")) {
				parseQueryBlockItem(item, block);
			}
		}

		if (queryBlock.has("table")) {
			block.addNode(parseTable(queryBlock.get("table")));
		}

		if (queryBlock.has("union_result")) {
			block.addChild(parseUnionResult(queryBlock.get("union_result")));
		}

		parseOperation(queryBlock, "ordering_operation", block, !hasDirectSource);
		parseOperation(queryBlock, "grouping_operation", block, !hasDirectSource);
		parseOperation(queryBlock, "duplicates_removal", block, !hasDirectSource);
		return block;
	}

	private void parseQueryBlockItem(JsonNode item, QueryExplainPlanBlock block) {
		if (item.has("table")) {
			block.addNode(parseTable(item.get("table")));
		}

		if (item.has("query_block")) {
			block.addChild(parseQueryBlock(item.get("query_block"), "query_block"));
		}

		if (item.has("union_result")) {
			block.addChild(parseUnionResult(item.get("union_result")));
		}

		boolean hasDirectSource = hasDirectSource(item);
		parseOperation(item, "ordering_operation", block, !hasDirectSource);
		parseOperation(item, "grouping_operation", block, !hasDirectSource);
		parseOperation(item, "duplicates_removal", block, !hasDirectSource);
	}

	private void parseOperation(JsonNode parent, String name, QueryExplainPlanBlock block, boolean parseSource) {
		JsonNode operation = parent.get(name);
		if (operation == null || operation.isNull()) {
			return;
		}

		QueryExplainPlanBlock operationBlock = new QueryExplainPlanBlock(name);
		collectOperationFlags(operation, operationBlock);
		if (!parseSource) {
			block.addChild(operationBlock);
			return;
		}

		if (operation.has("nested_loop")) {
			for (JsonNode item : operation.get("nested_loop")) {
				parseQueryBlockItem(item, operationBlock);
			}
		}

		if (operation.has("query_block")) {
			operationBlock.addChild(parseQueryBlock(operation.get("query_block"), "query_block"));
		}

		if (operation.has("table")) {
			operationBlock.addNode(parseTable(operation.get("table")));
		}

		boolean hasDirectSource = hasDirectSource(operation);
		parseOperation(operation, "ordering_operation", operationBlock, !hasDirectSource);
		parseOperation(operation, "grouping_operation", operationBlock, !hasDirectSource);
		parseOperation(operation, "duplicates_removal", operationBlock, !hasDirectSource);

		block.addChild(operationBlock);
	}

	private boolean hasDirectSource(JsonNode json) {
		return json.has("nested_loop") || json.has("table") || json.has("union_result");
	}

	private QueryExplainPlanBlock parseUnionResult(JsonNode unionResult) {
		QueryExplainPlanBlock block = new QueryExplainPlanBlock("union_result");
		collectOperationFlags(unionResult, block);

		JsonNode specifications = unionResult.get("query_specifications");
		if (specifications != null && specifications.isArray()) {
			for (JsonNode specification : specifications) {
				if (specification.has("query_block")) {
					block.addChild(parseQueryBlock(specification.get("query_block"), "query_specification"));
				}
			}
		}

		return block;
	}

	private void collectOperationFlags(JsonNode json, QueryExplainPlanBlock block) {
		if (json == null || json.isNull()) {
			return;
		}

		JsonNode queryCost = json.path("cost_info").get("query_cost");
		if (queryCost != null && !queryCost.isNull()) {
			block.queryCost(queryCost.asDouble());
		}

		if (json.path("using_filesort").asBoolean(false)) {
			block.setUsingFilesort(true);
		}

		if (json.path("using_temporary_table").asBoolean(false) || json.has("temporary_table")) {
			block.setUsingTemporaryTable(true);
		}

		if (json.path("dependent").asBoolean(false)) {
			block.setDependent(true);
		}

		JsonNode cacheable = json.get("cacheable");
		if (cacheable != null && !cacheable.isNull()) {
			block.cacheable(cacheable.asBoolean());
		}
	}

	private QueryExplainPlanNode parseTable(JsonNode table) {
		QueryExplainPlanNode node = new QueryExplainPlanNode();
		node.setTableName(text(table, "table_name"));
		node.setAccessType(text(table, "access_type"));
		node.setKey(text(table, "key"));
		node.setRowsExaminedPerScan(table.path("rows_examined_per_scan").asLong(0L));
		node.setRowsProducedPerJoin(table.path("rows_produced_per_join").asLong(0L));
		node.setFiltered(table.path("filtered").asDouble(100.0D));
		node.setAttachedCondition(text(table, "attached_condition"));
		node.usingJoinBuffer(text(table, "using_join_buffer"));

		JsonNode possibleKeys = table.get("possible_keys");
		if (possibleKeys != null && possibleKeys.isArray()) {
			for (JsonNode possibleKey : possibleKeys) {
				node.addPossibleKey(possibleKey.asText());
			}
		}

		parseMaterializedSubquery(table, node);
		parseAttachedSubqueries(table, node);
		return node;
	}

	private void parseMaterializedSubquery(JsonNode table, QueryExplainPlanNode node) {
		JsonNode materialized = table.get("materialized_from_subquery");
		if (materialized == null || materialized.isNull()) {
			return;
		}

		QueryExplainPlanBlock block = new QueryExplainPlanBlock("materialized_from_subquery");
		collectOperationFlags(materialized, block);
		if (materialized.has("query_block")) {
			block.addChild(parseQueryBlock(materialized.get("query_block"), "query_block"));
		}

		node.materializedFromSubquery(block);
	}

	private void parseAttachedSubqueries(JsonNode table, QueryExplainPlanNode node) {
		JsonNode attachedSubqueries = table.get("attached_subqueries");
		if (attachedSubqueries == null || !attachedSubqueries.isArray()) {
			return;
		}

		for (JsonNode attachedSubquery : attachedSubqueries) {
			QueryExplainPlanBlock block = new QueryExplainPlanBlock("attached_subquery");
			collectOperationFlags(attachedSubquery, block);
			if (attachedSubquery.has("query_block")) {
				block.addChild(parseQueryBlock(attachedSubquery.get("query_block"), "query_block"));
			}

			node.addAttachedSubquery(block);
		}
	}

	private String text(JsonNode json, String field) {
		JsonNode value = json.get(field);
		return value == null || value.isNull() ? null : value.asText();
	}
}
