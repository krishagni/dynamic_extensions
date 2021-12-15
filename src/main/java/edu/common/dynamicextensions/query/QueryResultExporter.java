package edu.common.dynamicextensions.query;

import java.io.OutputStream;


public interface QueryResultExporter {
	public QueryResponse export(String exportPath, Query query);
	
	public QueryResponse export(String exportPath, Query query, QueryResultScreener screener);
	
	public QueryResponse export(OutputStream out, Query query);
	
	public QueryResponse export(OutputStream out, Query query, QueryResultScreener screener);
	
	public void export(OutputStream out, QueryResultData result);
}
