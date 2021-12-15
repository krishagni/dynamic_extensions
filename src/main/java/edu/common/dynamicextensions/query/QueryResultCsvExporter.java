package edu.common.dynamicextensions.query;	

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.IoUtil;
import au.com.bytecode.opencsv.CSVWriter;

public class QueryResultCsvExporter implements QueryResultExporter {
	private static final String NULL_STR_MARKER = "\0\0\0\0\0";
	
	private char fieldSeparator = CSVWriter.DEFAULT_SEPARATOR;

	public QueryResultCsvExporter() {
	}
	
	public QueryResultCsvExporter(char fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}
	
	public char getFieldSeparator() {
		return fieldSeparator;
	}

	public void setFieldSeparator(char fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	@Override
	public QueryResponse export(String exportPath, Query query) {
		return export(exportPath, query, null);
	}
	
	@Override
	public QueryResponse export(String exportPath, Query query, QueryResultScreener screener) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(exportPath);
			return export(out, query, screener);
		} catch (Exception e) {
			throw new FormException("Error exporting CSV file", e);
		} finally {
			IoUtil.close(out);
		}
	}
	

	@Override
	public QueryResponse export(OutputStream out, Query query) {
		return export(out, query, null);
	}
	
	@Override
	public QueryResponse export(OutputStream out, Query query, QueryResultScreener screener) {		
		QueryResultData data = null;
		try {
			QueryResponse resp = query.getData();
			
			data =  resp.getResultData();
			data.setScreener(screener);
			data.setColumnLabelFormatter(new DefaultResultColLabelFormatter("_"));
			export(out, data);
			return resp;
		} finally {
			if (data != null) {
				data.close();
			}
		}
	}
	

	@Override
	public void export(OutputStream out, QueryResultData result) {
		CSVWriter csvWriter = null;

		try {
			csvWriter = getCsvWriter(out);			
			csvWriter.writeNext(result.getColumnLabels());

			Iterator<String[]> iterator = result.stringifiedRowIterator();
			while (iterator.hasNext()) {
				String[] row = iterator.next();
				for (int i = 0; i < row.length; ++i) {
					if (row[i] != null && row[i].equals(NULL_STR_MARKER)) {
						row[i] = "All";
					}					
				}
				
				csvWriter.writeNext(row);
			}			
		} catch (Exception e) {
			throw new FormException("Error writing query result data to CSV file", e);
		} finally {
			IoUtil.close(csvWriter);
		}		
	}
	
	private CSVWriter getCsvWriter(OutputStream out) {
		return new CSVWriter(new OutputStreamWriter(out), getFieldSeparator());
	}
}