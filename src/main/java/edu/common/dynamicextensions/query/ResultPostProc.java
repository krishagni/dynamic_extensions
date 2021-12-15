package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.util.List;

public interface ResultPostProc {
	int processResultSet(ResultSet rs, ResultPostProc defaultProc);
	
	List<ResultColumn> getResultColumns();

	RowsList getRows();
	
	void cleanup();
}
