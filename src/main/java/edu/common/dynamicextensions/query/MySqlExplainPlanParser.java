package edu.common.dynamicextensions.query;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MySqlExplainPlanParser {
	private static final ObjectMapper mapper = new ObjectMapper();

	public QueryExplainPlan parse(String explainJson) {
		try {
			QueryExplainPlan plan = new QueryExplainPlan();
			plan.rawPlanText(explainJson);
			collect(mapper.readTree(explainJson), plan);
			return plan;
		} catch (IOException e) {
			throw new QueryOptimisationException("Error parsing MySQL explain plan", e);
		}
	}

	private void collect(JsonNode json, QueryExplainPlan plan) {
		if (json == null || json.isNull()) {
			return;
		}

		if (json.isObject()) {
			collectOperationFlags(json, plan);
			if (json.has("table")) {
				plan.addNode(parseTable(json.get("table")));
			}

			Iterator<JsonNode> elements = json.elements();
			while (elements.hasNext()) {
				collect(elements.next(), plan);
			}
		} else if (json.isArray()) {
			for (JsonNode child : json) {
				collect(child, plan);
			}
		}
	}

	private void collectOperationFlags(JsonNode json, QueryExplainPlan plan) {
		JsonNode queryCost = json.path("cost_info").get("query_cost");
		if (queryCost != null && !queryCost.isNull()) {
			plan.queryCost(queryCost.asDouble());
		}

		if (json.path("using_filesort").asBoolean(false)) {
			plan.setUsingFilesort(true);
		}

		if (json.has("temporary_table")) {
			plan.setUsingTemporaryTable(true);
		}

		if (json.path("dependent").asBoolean(false)) {
			plan.setDependentSubquery(true);
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

		JsonNode possibleKeys = table.get("possible_keys");
		if (possibleKeys != null && possibleKeys.isArray()) {
			for (JsonNode possibleKey : possibleKeys) {
				node.addPossibleKey(possibleKey.asText());
			}
		}

		return node;
	}

	private String text(JsonNode json, String field) {
		JsonNode value = json.get(field);
		return value == null || value.isNull() ? null : value.asText();
	}
}
