package edu.common.dynamicextensions.query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.IoUtil;

public class QueryResultSasExporter implements QueryResultExporter {
	private QueryResultExporter csvExporter;
	
	private static final String CSV_FILE = "SearchResults.csv";
	
	private static final String SAS_PGM_FILE = "SearchResults_pgm.sas";
	
	private static final String SAS_PV_FILE = "SearchResults_pv.sas";
					
	public QueryResultSasExporter(QueryResultExporter csvExporter) {
		this.csvExporter = csvExporter;
	}

	@Override
	public QueryResponse export(String exportPath, Query query) {
		return export(exportPath, query, null);
	}
	
	@Override
	public QueryResponse export(String exportPath, Query query, QueryResultScreener screener) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(exportPath);
			return export(out, query, screener);
		} catch (Exception e) {
			throw new FormException("Error exporting SAS program for query results", e);
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
			
			data = resp.getResultData();
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
		String ioDir = getIoDir();
		try {
			new File(ioDir).mkdirs();
			exportCsv(ioDir, result);
			exportSas(ioDir, result);
			IoUtil.zipFiles(ioDir, out);
		} finally {
			IoUtil.delete(ioDir + CSV_FILE);
			IoUtil.delete(ioDir + SAS_PGM_FILE);
			IoUtil.delete(ioDir + SAS_PV_FILE);
			IoUtil.delete(ioDir);
		}		
	}
	
	private String getIoDir() {
		String name = UUID.randomUUID().toString();
		return System.getProperty("java.io.tmpdir") + File.separator + name + File.separator;
	}
	
	private void exportCsv(String ioDir, QueryResultData result) {
		String csvFile = ioDir + CSV_FILE;
		FileOutputStream csvOut = null;
		try {
			csvOut = new FileOutputStream(csvFile);
			csvExporter.export(csvOut, result);			
		} catch (Exception e) {
			throw new FormException("Error exporting CSV data", e);
		} finally {
			IoUtil.close(csvOut);
		}		
	}
	
	private void exportSas(String ioDir, QueryResultData result) {
		String pgmFile = ioDir + SAS_PGM_FILE;
		String pvFile = ioDir + SAS_PV_FILE;
		SasProgramGenerator pgmGenerator = new SasProgramGenerator(pgmFile, pvFile, CSV_FILE);
		pgmGenerator.generate(result.getResultColumns());
	}
}
