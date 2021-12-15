package edu.common.dynamicextensions.query;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.TextArea;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.IoUtil;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.LiteralValueNode;

public class SasProgramGenerator {
	private String pvFile;
	
	private String pgmFile;
	
	private String inputCsv;
	
	public SasProgramGenerator(String pgmFile, String pvFile, String inputCsv) {
		this.pgmFile = pgmFile;
		this.pvFile = pvFile;
		this.inputCsv = inputCsv;		
	}
	
	public void generate(List<ResultColumn> columns) {
		FileWriter writer = null;
		FormatInfo formatInfo = new FormatInfo();
				
		try {
			
			generateFormatInfo(columns, formatInfo);
			
			writer = new FileWriter(pgmFile);						
			writeProgramHeader(writer);
			writeFileImportInfo(writer);
			
			writer.write(
					"\r\nDATA &library..&file1; \r\n" +
					"%let _EFIERR_ = 0; \r\n" +
					"infile &file1 dlm=',' MISSOVER DSD lrecl = 32000 firstobs = 2 ;");
			
			writer.write("\r\n/********** informat **********/\r\n");
			writer.write(formatInfo.getInformat());
			writer.write("\r\n/********** format **********/\r\n");
			writer.write(formatInfo.getFormat());
			writer.write("\r\n/********** label **********/\r\n");
			writer.write(formatInfo.getLabel());
			writer.write("\r\n/********** input **********/\r\n");
			writer.write("\r\ninput\r\n");
			writer.write(formatInfo.getInputVars());
			writer.write("\r\n ;\r\nif _ERROR_ then call symput('_EFIERR_',1); \r\n  run ;\r\n");		
			writer.flush();
		} catch (Exception e) {
			throw new FormException("Error generating SAS program", e);
		} finally {
			IoUtil.close(writer);
		}
		
		try {
			writer = new FileWriter(pvFile);
			writeNum2StrMapping(writer, formatInfo);
			writeStr2NumMapping(writer, formatInfo);			
			writer.write("\r\nrun;\r\n");
			writer.flush();			
		} catch (Exception e) {
			throw new FormException("Error writing PV mapping file", e);
		} finally {
			IoUtil.close(writer);
		}
	}
	
	private void writeProgramHeader(FileWriter writer) 
	throws IOException {
		writer.write(
				"/*Replace 'C:\\path-to-RETRIEVE-data' to reflect where you would like the file stored; */" +
				"\n\n%let stored=C:\\path-to-RETRIEVE-data;\n\n" +
				"/*Replace 'DataDir' with the name you want for your library; */" +
				"\n\n%let library=DataDir;\n\n" +
				"/*Replace 'C:\\path-to-SAS-Library\\' with where you want to locate your library; */" +
				"\n\nlibname &library 'C:\\path-to-SAS-Library\\';\n\n");						
	}
	
	private void writeFileImportInfo(FileWriter writer) 
	throws IOException {
		writer.write(
				"%let file1=S_1;\r\n\r\n" +
				"ods listing;\r\n" +
				"options linesize=256;\r\n\r\n" +		
			    "filename &file1 \"&stored\\" + inputCsv + "\";\r\n");
	}
	
	private void generateFormatInfo(List<ResultColumn> columns, FormatInfo formatInfo) {
		ResultColumnLabelFormatter formatter = new DefaultResultColLabelFormatter("_");
		Map<String, Integer> varCountMap = new HashMap<String, Integer>();
		
		for (ResultColumn column : columns) {
			String[] captions = column.getCaptions();
			String formShortName = getShortName(captions[0]);
			String desc = column.getColumnLabel(formatter).replaceAll("[\\s+-]", "_");
			
			Control ctrl = null;
			String varName = null;
			DataType type = null;
			if (column.getExpression() instanceof FieldNode) {
				ctrl = ((FieldNode)column.getExpression()).getCtrl();
				varName = getUniqueName(varCountMap, formShortName + "_" + ctrl.getUserDefinedName());
				type = (ctrl instanceof FileUploadControl) ? DataType.STRING : ctrl.getDataType();
			} else if (column.getExpression() instanceof LiteralValueNode) {
				varName = captions[captions.length - 1];
				type = column.getExpression().getType();
			}
									
			int size = 0;
			if (ctrl instanceof StringTextField) {
				size = ((StringTextField)ctrl).getMaxLength();
			} else if (ctrl instanceof TextArea) {
				size = ((TextArea)ctrl).getMaxLength();
			}
			
			switch (type) {
			    case INTEGER:
			    case FLOAT:
			    case BOOLEAN:
			    case DATE_INTERVAL:
			    	formatInfo.writeNumberVar(varName, desc);
			    	break;
			    	
			    case DATE:
			    	formatInfo.writeDateVar(varName, desc);
			    	break;
			    	
			    case STRING:
			    default:
			    	formatInfo.writeStringVar(varName, desc, size);
			    	break;
			}
			
			if (ctrl instanceof SelectControl) {
				formatInfo.addPvVar(varName, column);				
			}
		}
		
	}
	
	private void writeNum2StrMapping(Writer writer, FormatInfo formatInfo) 
	throws IOException {
		writer.write("\r\n/********** numberToStringFormat **********/\r\n");
		writer.write("proc format ; \r\n");
		
		for (PvVar pvVar : formatInfo.getPvVars()) {
			FieldNode field = (FieldNode)pvVar.column.getExpression();
			SelectControl ctrl = (SelectControl)field.getCtrl();
			
			writer.write("\r\n/* Numeric to character format for " + pvVar.varName + " */ \r\n");
			writer.write("\r\n value $" + pvVar.varName + "noToStrFmt\r\n");
			
			int count = 1;
			for (PermissibleValue pv : ctrl.getPvs()) {					
				writer.write(count + " = " + " \"" + pv.getValue() + "\"\r\n");
				++count;
			}
			
			writer.write(";\r\n");
		}
		
		writer.flush();		
	}
	
	private void writeStr2NumMapping(Writer writer, FormatInfo formatInfo)
	throws IOException {
		writer.write("\r\n/********** stringToNumberFormat **********/\r\n");
		writer.write("proc format ; \r\n");

		for (PvVar pvVar : formatInfo.getPvVars()) {
			FieldNode field = (FieldNode)pvVar.column.getExpression();
			SelectControl ctrl = (SelectControl)field.getCtrl();
			
			writer.write("\r\n/* Character to numeric format for " + pvVar.varName + " */ \r\n");
			writer.write("\r\n value $" + pvVar.varName + "strToNoFmt\r\n");
			
			int count = 1;
			for (PermissibleValue pv : ctrl.getPvs()) {					
				writer.write(" \"" + pv.getValue() + "\"" + " = " + count + "\r\n");
				++count;
			}
			
			writer.write(";\r\n");
		}
		
		writer.flush();		
	}
	
	private String getUniqueName(Map<String, Integer> countMap, String name) {
		name = name.replaceAll("[\\s+-]", "_");
		if (name.length() > 28) {
			name = name.substring(0, 28);
		}
		
		Integer count = countMap.get(name); 
		count = (count == null) ? 1 : count + 1;		
		countMap.put(name, count);
		
		return (count == 1) ? name : name + (count - 1);		
	}
	
	private String getShortName(String name) {
		name = WordUtils.initials(name, new char[] {' ', '_'});
		if (name.length() > 4) {
			name = name.substring(0, 4);
		}
		
		return name;		
	}
	
	
	private class FormatInfo {
		private StringBuilder informat = new StringBuilder();
		
		private StringBuilder format = new StringBuilder();
		
		private StringBuilder label = new StringBuilder();
		
		private StringBuilder inputVars = new StringBuilder();
		
		private List<PvVar> pvVars = new ArrayList<PvVar>();
		
		public void writeStringVar(String name, String desc, int size) {
			size = (size != 0 && size < 50) ? 50 : (size == 0 || size > 1000) ? 1000 : size;

			informat.append("informat ").append(name).append(" $").append(size).append(". ;\r\n");
			format.append("format ").append(name).append(" $").append(size).append(". ;\r\n");
			label.append("label ").append(name).append(" = '").append(desc).append("' ;\r\n");
			inputVars.append(name).append(" $\r\n");
		}
		
		public void writeNumberVar(String name, String desc) {
			informat.append("informat ").append(name).append(" best12. ;\r\n");
			format.append("format ").append(name).append(" best32. ;\r\n");
			label.append("label ").append(name).append(" = '").append(desc).append("' ;\r\n");
			inputVars.append(name).append("\r\n");
		}
		
		public void writeDateVar(String name, String desc) {
			informat.append("informat ").append(name).append(" mmddyy10. ;\r\n");
			format.append("format ").append(name).append(" mmddyy10. ;\r\n");
			label.append("label ").append(name).append(" = '").append(desc).append("' ;\r\n");
			inputVars.append(name).append("\r\n");			
		}
		
		public void addPvVar(String name, ResultColumn column) {
			pvVars.add(new PvVar(name, column));
		}
		
		public List<PvVar> getPvVars() {
			return pvVars;
		}
		
		
		public String getInformat() {
			return informat.toString();
		}
		
		public String getFormat() {
			return format.toString();
		}
		
		public String getLabel() {
			return label.toString();
		}
		
		public String getInputVars() {
			return inputVars.toString();
		}
	}	
	
	private class PvVar {
		private String varName;
		
		private ResultColumn column;
		
		public PvVar(String varName, ResultColumn column) {
			this.varName = varName;
			this.column = column;
		}
	}
}
