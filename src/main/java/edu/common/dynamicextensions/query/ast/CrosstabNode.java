package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CrosstabNode implements Node, Serializable {
	private String aql;

	private String name;
	
	private List<Integer> rowGroupByColumns = new ArrayList<Integer>();
	
	private int colGroupByColumn = -1;
	
	private List<Integer> measureColumns = new ArrayList<Integer>();
	
	private boolean includeSubTotals = false;

	@Override
	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getRowGroupByColumns() {
		return rowGroupByColumns;
	}

	public void setRowGroupByColumns(List<Integer> rowGroupByColumns) {
		this.rowGroupByColumns = rowGroupByColumns;
	}

	public int getColGroupByColumn() {
		return colGroupByColumn;
	}

	public void setColGroupByColumn(int colGroupByColumn) {
		this.colGroupByColumn = colGroupByColumn;
	}

	public List<Integer> getMeasureColumns() {
		return measureColumns;
	}

	public void setMeasureColumns(List<Integer> measureColumns) {
		this.measureColumns = measureColumns;
	}

	public boolean isIncludeSubTotals() {
		return includeSubTotals;
	}

	public void setIncludeSubTotals(boolean includeSubTotals) {
		this.includeSubTotals = includeSubTotals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * 1 + colGroupByColumn;
		result = prime * result + measureColumns.hashCode();
		result = prime * result	+ rowGroupByColumns.hashCode();
		result = prime * result + (includeSubTotals ? 1 : 0); 
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) { 
			return false;
		}

		CrosstabNode other = (CrosstabNode) obj;
		if (colGroupByColumn != other.colGroupByColumn) {
			return false;
		}
		
		if (measureColumns == null) {
			if (other.measureColumns != null) {
				return false;
			}
		} else if (!measureColumns.equals(other.measureColumns)){
			return false;
		}
		
		if (rowGroupByColumns == null) {
			if (other.rowGroupByColumns != null) {
				return false;
			}
		} else if (!rowGroupByColumns.equals(other.rowGroupByColumns)) {
			return false;
		}
		
		return includeSubTotals != other.includeSubTotals;
	}
}
