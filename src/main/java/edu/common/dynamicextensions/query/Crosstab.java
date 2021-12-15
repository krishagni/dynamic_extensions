package edu.common.dynamicextensions.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.Util;
import edu.common.dynamicextensions.query.ast.CrosstabNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.LiteralValueNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class Crosstab implements ResultPostProc {
	private static final String NULL_STR_MARKER = "\0\0\0\0\0";
	
	private QueryExpressionNode queryExpr;

	private String timeZone;
	
	private Map<String, Row> rows = new TreeMap<>();
	
	private Set<Object> dynamicCols = new TreeSet<>();
	
	private Set<String> measureCols = new LinkedHashSet<>();
	
	private Set<String> rollupExcludeCols = new LinkedHashSet<>();

	private boolean numericMeasure;
			
	public Crosstab(QueryExpressionNode queryExpr, String timeZone) {
		this.queryExpr = queryExpr;
		this.timeZone = timeZone;
		
		CrosstabNode ctSpec = queryExpr.getCrosstabSpec();

		numericMeasure = true;
		for (int valIdx : ctSpec.getMeasureColumns()) {
			ExpressionNode column = queryExpr
					.getSelectList()
					.getElements()
					.get(Math.abs(valIdx) - 1);
			
			if (column.getType() != DataType.INTEGER && column.getType() != DataType.FLOAT) {
				numericMeasure = false;				
			}
			
			String measureCol = getMeasureColumnLabel(column);
			measureCols.add(measureCol);

			if (valIdx < 0) {
				rollupExcludeCols.add(measureCol);
			}
		}
		
		if (ctSpec.isIncludeSubTotals() && !numericMeasure) {
			throw new FormException("Sub totals can be done only on numeric measure columns");			
		}				
	}
	
	@Override
	public int processResultSet(ResultSet rs, ResultPostProc defProc) {
		try {
			int rowCount = 0;
						
			while (rs.next()) {
				addRow(rs);
				rowCount++;
			}
			
			return rowCount;			
		} catch (SQLException e) {
			throw new FormException("Error processing result set", e);
		}
	}
	
	@Override
	public List<ResultColumn> getResultColumns() {
		CrosstabNode ctSpec = queryExpr.getCrosstabSpec();
		List<ExpressionNode> selectList = queryExpr.getSelectList().getElements();
		
		List<ResultColumn> columns = new ArrayList<>();
		for (int idx : ctSpec.getRowGroupByColumns()) {
			columns.add(new ResultColumn(selectList.get(idx - 1), 0));			
		}
		
		if (ctSpec.getMeasureColumns().size() > 1) {
			ExpressionNode valueType = new LiteralValueNode(DataType.STRING);			
			valueType.setLabel("Value Type");			
			columns.add(new ResultColumn(valueType, 0));
		}
		
		ExpressionNode colExpr = selectList.get(ctSpec.getColGroupByColumn() - 1);
		String dateFormat = getDateFormat(colExpr);
		SimpleDateFormat sdf = dateFormat != null ? new SimpleDateFormat(dateFormat) : null;

		for (Object colKey : dynamicCols) {
			ExpressionNode colExprClone = colExpr.copy();
			if (sdf != null) {
				colExprClone.setLabel(sdf.format((Date)colKey));
			} else {
				colExprClone.setLabel(colKey.toString());
			}
						
			columns.add(new ResultColumn(colExprClone, 0));
		}
		
		if (numericMeasure) {
			ExpressionNode total = new LiteralValueNode(DataType.INTEGER);			
			total.setLabel("Total");
			columns.add(new ResultColumn(total, 0));			
		}
		
		return columns;		
	}
	
	@Override
	public RowsList getRows() {
		RowsList result = new RowsList();
		
		for (Row row : rows.values()) {
			for (String measureCol : measureCols) {
				if (row.calculatedRow && rollupExcludeCols.contains(measureCol)) {
					continue;
				}

				List<Object> values = new ArrayList<Object>(row.getRowKeyValues());
				BigDecimal sum = BigDecimal.ZERO;
				
				if (measureCols.size() > 1) {
					values.add(measureCol);
				}
				
				for (Object colKey : dynamicCols) {
					Map<String, Object> measureMap = row.getColValue(colKey);
					Object measure = measureMap.get(measureCol);
					values.add(measure);
					
					if (numericMeasure && measure != null) {
						sum = sum.add(getBigDecimal(measure));
					}
				}
				
				if (numericMeasure) {
					values.add(rollupExcludeCols.contains(measureCol) ? null : sum);
				} 
				
				result.add(values.toArray(new Object[0]));								
			}
		}
		
		return result;
	}
	
	@Override
	public void cleanup() {
		; // no-op
	}
		
	private void addRow(ResultSet rs) 
	throws SQLException {
		CrosstabNode ctSpec = queryExpr.getCrosstabSpec();																									
		
		List<Object> rowKeyValues = new ArrayList<Object>();
		for (int idx : ctSpec.getRowGroupByColumns()) {
			rowKeyValues.add(rs.getObject(idx));
		}
		
		Object colKeyVal = rs.getObject(ctSpec.getColGroupByColumn());
		colKeyVal = colKeyVal == null ? "" : colKeyVal;
		if (Util.isOraTimestamp(colKeyVal)) {
			colKeyVal = Util.getDateFromOraTimestamp(colKeyVal);
		}
		
		Map<String, Object> measureMap = new LinkedHashMap<String, Object>();
		for (int idx : ctSpec.getMeasureColumns()) {
			idx = Math.abs(idx);
			ExpressionNode node = queryExpr.getSelectList().getElements().get(idx - 1);
			String label = getMeasureColumnLabel(node);
			measureMap.put(label, rs.getObject(idx));
		}
		
		List<List<Object>> rowGrps = new ArrayList<List<Object>>();
		if (!numericMeasure) {
			rowGrps.add(rowKeyValues);
		} else {
			rowGrps.addAll(subtotals(ctSpec.isIncludeSubTotals(), rowKeyValues));
		}

		for (List<Object> rowGrp : rowGrps) {
			String key = getRowKey(rowGrp);
			Row row = rows.get(key);
			if (row == null) {
				row = new Row(key, rowGrp);
				rows.put(key, row);
			}
			
			row.addColValue(colKeyVal, measureMap);
		}
		
		dynamicCols.add(colKeyVal);
	}
	
	private String getRowKey(List<Object> rowKeyVals) {
		StringBuilder key = new StringBuilder("-##-");
		
		for (Object val : rowKeyVals) {
			key.append(val == null ? "" : val.toString()).append("-##-");
		}
		
		return key.toString();
	}
	
	private class Row {
		private String key;
		
		private List<Object> rowKeyValues;
		
		private Map<Object, Map<String, Object>> colKeyValueMap = new TreeMap<Object, Map<String, Object>>();
		
		private boolean calculatedRow;

		public Row(String key, List<Object> rowKeyValues) {
			this.key = key;
			this.rowKeyValues = rowKeyValues;
			this.calculatedRow = false;

			for (Object rowKey : rowKeyValues) {
				if (rowKey != null && rowKey.equals(NULL_STR_MARKER)) {
					this.calculatedRow = true;
					break;
				}
			}
		}

		public List<Object> getRowKeyValues() {
			return rowKeyValues;
		}

		public void addColValue(Object columnKey, Map<String, Object> measureMap) {
			Map<String, Object> existingMeasures = colKeyValueMap.get(columnKey);
			if (existingMeasures == null || !numericMeasure) {
				Map<String, Object> columnValueMap = new LinkedHashMap<String, Object>(measureMap);				
				if (calculatedRow) {
					for (String excludeMeasureCol : rollupExcludeCols) {
						columnValueMap.remove(excludeMeasureCol);
					}
				}

				colKeyValueMap.put(columnKey, columnValueMap);
			} else {
				for (Map.Entry<String, Object> measure : measureMap.entrySet()) {
					if (rollupExcludeCols.contains(measure.getKey())) {
						continue;
					}

					BigDecimal existingMeasure = getBigDecimal(existingMeasures.get(measure.getKey()));
					BigDecimal val = getBigDecimal(measure.getValue());
					existingMeasures.put(measure.getKey(), existingMeasure.add(val));
				}
			}
		}
		
		public Map<String, Object> getColValue(Object columnKey) {
			Map<String, Object> measureMap = colKeyValueMap.get(columnKey);
			if (measureMap == null && numericMeasure) {
				measureMap = new LinkedHashMap<String, Object>();
				for (String measureCol : measureCols) {
					measureMap.put(measureCol, BigDecimal.ZERO);
				}
			}
			
			return measureMap;
		}
	}
			
	private List<List<Object>> subtotals(boolean includeSubTotals, List<Object> input) {
		List<List<Object>> result = new ArrayList<List<Object>>();
		result.add(input);
		
		List<Object> grandTotal = new ArrayList<Object>();	
		for (int i = 0; i < input.size(); ++i) {
			grandTotal.add(NULL_STR_MARKER);
		}
		result.add(grandTotal);
		
		if (!includeSubTotals || input.size() == 1) {
			return result;
		}
		
		for (int i = 0; i < input.size(); ++i) {
			List<Object> elem = new ArrayList<Object>();
			for (int j = 0; j < input.size(); ++j) {
				if (i == j) {
					elem.add(input.get(i));
				} else {
					elem.add(NULL_STR_MARKER);
				}
			}
			result.add(elem);
		}
				
		return result;
	}
		
	private String getDateFormat(ExpressionNode expr) {
		if (expr instanceof FieldNode) {
			FieldNode f = (FieldNode)expr;
			if (f.getCtrl() instanceof DatePicker) {
				DatePicker dp = (DatePicker)f.getCtrl();
				return dp.getFormat();
			}
		}
		
		return null;		
	}
	
	private String getMeasureColumnLabel(ExpressionNode node) {
		if (node.getLabel() != null) {
			return node.getLabel();
		} else if (node instanceof FieldNode) {
			String[] captions = ((FieldNode) node).getNodeCaptions();
			return captions[captions.length - 1];
		} else {
			return "Unknown";
		}
	}
	
	private BigDecimal getBigDecimal(Object val) {
		BigDecimal ret = BigDecimal.ZERO;
		if (val != null) {
			ret = new BigDecimal(val.toString());
		}
		
		return ret;
	}	
}
