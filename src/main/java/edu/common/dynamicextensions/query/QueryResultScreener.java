package edu.common.dynamicextensions.query;

import java.util.List;

public interface QueryResultScreener {
	public List<ResultColumn> getScreenedResultColumns(List<ResultColumn> preScreenedResultCols);
	
	public Object[] getScreenedRowData(List<ResultColumn> preScreenedResultCols, Object[] rowData);
}
