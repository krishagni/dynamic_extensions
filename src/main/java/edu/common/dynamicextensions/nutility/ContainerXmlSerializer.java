package edu.common.dynamicextensions.nutility;

import static edu.common.dynamicextensions.nutility.XmlUtil.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVWriter;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DisableAction;
import edu.common.dynamicextensions.domain.nui.EnableAction;
import edu.common.dynamicextensions.domain.nui.HideAction;
import edu.common.dynamicextensions.domain.nui.Layout;
import edu.common.dynamicextensions.domain.nui.Page;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.PageField;
import edu.common.dynamicextensions.domain.nui.PageRow;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.ShowAction;
import edu.common.dynamicextensions.domain.nui.ShowPvAction;
import edu.common.dynamicextensions.domain.nui.SkipAction;
import edu.common.dynamicextensions.domain.nui.SkipCondition;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.FormException;

public class ContainerXmlSerializer implements ContainerSerializer  {
	private static Map<Class<?>, String> actionNameMap = initializeSkipActionNameMap();;
	
	private static final String[] PV_CSV_HEADER = {"Value", "Numeric Code", "Concept Code", "Definition Source"};
	
	private Container container;
	
	private Writer writer;
	
	private Properties props = new Properties();

	public ContainerXmlSerializer(Container container, Writer writer, Properties props) {
		this.container = container;
		this.writer = writer;
		this.props = props;		
	}
	
	public ContainerXmlSerializer(Container container, String outDir) {
		this.container = container;
		
		createOpDirectory(outDir);		
		props.put("outDir", outDir);
		
		try {
			String xmlFile = new StringBuilder()
				.append(outDir).append(File.separator)
				.append(container.getName()).append(".xml")
				.toString();

			writer = new BufferedWriter(new FileWriter(xmlFile));
		} catch (IOException e) {
			throw new FormException("Error creating output file writer", e);
		}
	}

	public void serialize() {
		serialize(0);
	}

	public void serialize(int maxPvListSize) {
		container.setMaxPvListSize(maxPvListSize);

		try {
			emitContainerStart();
									
			emitViewStart();
			serializeView(container);			
			emitViewEnd();

			emitDeletedControlsStart();
			serializeDeletedFields();
			emitDeletedControlsEnd();

			emitSkipRulesStart();
			serializeSkipRules(container.getSkipRules());
			emitSkipRulesEnd();

			serializeLayouts(container.getLayouts());
	
			emitContainerEnd();
			
			writer.flush();
		} catch(IOException e) {
			throw new FormException("Error serializing container", e);
		} finally {
			IoUtil.close(writer);
		}
	}

	public void serializeView() {
		serializeView(container);
	}
	
	public void serializeView(Container container) {		
		emitContainerProps(container);
		
		Collection<List<Control>> rows = container.getControlsGroupedByRow();		
		for (List<Control> rowCtrls : rows) {
			if (rowCtrls.size() == 1 && rowCtrls.get(0) instanceof PageBreak) {
				rowCtrls.get(0).serializeToXml(writer, props);				
			} else {
				emitStartRow();

				for (Control ctrl : rowCtrls) {
					ctrl.serializeToXml(writer, props);
				}
				
				emitEndRow();				
			}			
		}
	}

	public static void writePvValues(Writer writer, List<PermissibleValue> pvs, String fileName, Properties props) {
		if(pvs.size() > 10) {
			writePvsToFile(writer, pvs, fileName, props);
		} else {
			for (PermissibleValue pv : pvs){
				writeElementStart(writer, "option");

				writeElement(writer, "numericCode", pv.getNumericCode());
				writeCDataElement(writer, "value", pv.getValue());
				writeCDataElement(writer, "showWhen", pv.getShowWhen());

				writeElementEnd(writer,"option");

			}
		}
	}

	
	private void createOpDirectory(String outDir) {
		File dirFile = new File(outDir);
		
		if(!dirFile.isDirectory()) {
			if (!dirFile.mkdirs()) {
				throw new FormException("Failed to create output directory: " + outDir);
			}
		}
	}


	private static Map<Class<?>, String> initializeSkipActionNameMap() {
		Map<Class<?>, String> actionNameMap = new HashMap<Class<?>, String>();
		actionNameMap.put(HideAction.class,     "hide");
		actionNameMap.put(ShowAction.class,     "show");
		actionNameMap.put(ShowPvAction.class,   "showPv");
		actionNameMap.put(EnableAction.class,   "enable");
		actionNameMap.put(DisableAction.class,  "disable");
		
		return actionNameMap;
	}

	private static void writePvsToFile(Writer writer, List<PermissibleValue> pvs, String filename, Properties props) {
		String outDir = props.getProperty("outDir");
		writeElement(writer, "optionsFile", createCsvFile(pvs, filename, outDir));
	}

	private void emitContainerStart() {
		writeElementStart(writer, "form");
	}
	
	private void emitContainerProps(Container container) {
		writeElement(writer, "name", 	container.getName());
		writeCDataElement(writer, "caption", container.getCaption());
	}

	private void emitContainerEnd() {
		writeElementEnd(writer, "form");
	}
	
	private void emitViewStart() {
		writeElementStart(writer, "view");
	}

	private void emitViewEnd() {
		 writeElementEnd(writer, "view");
	}
	
	private void emitStartRow() {
		writeElementStart(writer, "row");
	}
	
	private void emitEndRow() {
		writeElementEnd(writer, "row");
	}

	private void emitDeletedControlsStart() {
		writeElementStart(writer, "deletedFields");
	}

	private void emitDeletedControlsEnd() {
		writeElementEnd(writer, "deletedFields");
	}

	private void serializeDeletedFields() {
		serializeDeletedFields(container, "");
	}

	private void serializeDeletedFields(Container container, String prefix) {
		if (StringUtils.isNotBlank(prefix)) {
			prefix += ".";
		}

		for (Control deletedField : container.getDeletedCtrls()) {
			String udn = prefix + deletedField.getUserDefinedName();

			writeElement(writer, "field", null, Collections.singletonMap("udn", udn));
			if (deletedField instanceof SubFormControl) {
				serializeDeletedFields(((SubFormControl) deletedField).getSubContainer(), udn);
			}
		}

		for (Control field : container.getOrderedControlList()) {
			if (field instanceof SubFormControl) {
				serializeDeletedFields(((SubFormControl) field).getSubContainer(), prefix + field.getUserDefinedName());
			}
		}
	}

	private void emitSkipRulesStart() {
		writeElementStart(writer, "skipRules");
	}
	
	private void emitSkipRulesEnd() {
		writeElementEnd(writer, "skipRules");
	}
	
	private void serializeSkipRules(List<SkipRule> skipRules) {		
		for (SkipRule skipRule : skipRules) {
			writeElementStart(writer, "skipRule");
			
			String logicalOp = getLogicalOp(skipRule.getLogicalOp());
			writeElementStart(writer, logicalOp);
			for (SkipCondition condition : skipRule.getConditions()) {
				writeCondition(condition);  
			}			
			writeElementEnd(writer, logicalOp);
			
			
			writeElementStart(writer, "actions");
			for (SkipAction action : skipRule.getActions()) {
				String actionName = actionNameMap.get(action.getClass());
				String field = container.getControlCanonicalName(action.getTargetCtrl());
				
				Map<String, String> actionAttrs = Collections.singletonMap("field", field);
				if (action instanceof ShowPvAction) {
					ShowPvAction pvAction = (ShowPvAction)action;
					writeElementStart(writer, actionName, actionAttrs);
					
					writeElementStart(writer, "options");
					writePvValues(writer, pvAction.getListOfPvs(), field.concat("-skipLogic"), props);					
					writeElementEnd(writer, "options");
					
					writeElementEnd(writer, actionName);					
				} else {
					writeElement(writer, actionName, null, actionAttrs);
				}
			}
						
			writeElementEnd(writer, "actions");
			writeElementEnd(writer, "skipRule");
		}		
	}

	private void writeCondition(SkipCondition condition) {
		String fieldName = container.getControlCanonicalName(condition.getSourceControl());
		if (fieldName == null) {
			throw new FormException("Invalid field in skip condition. Invalid container state");
		}
		
		Map<String,String> attrs = new HashMap<String, String>();
		attrs.put("field",  fieldName);
		attrs.put("op",     condition.getRelationalOp().name());
		attrs.put("value",  condition.getValue());		
		writeElement(writer, "condition", null, attrs);
	}

	private String getLogicalOp(LogicalOp logicalOp) {
		String logicalOpStr = "oneOf";
		if(logicalOp.equals(LogicalOp.AND)){
			logicalOpStr = "all";
		}
		
		return logicalOpStr;
	}

	private void serializeLayouts(List<Layout> layouts) {
		if (layouts == null || layouts.isEmpty()) {
			return;
		}

		writeElementStart(writer, "layouts");
		layouts.forEach(this::serializeLayout);
		writeElementEnd(writer, "layouts");
	}

	private void serializeLayout(Layout layout) {
		if (layout.getPages() == null || layout.getPages().isEmpty()) {
			return;
		}

		writeElementStart(writer, "layout");
		layout.getPages().forEach(this::serializePage);
		writeElementEnd(writer, "layout");
	}

	private void serializePage(Page page) {
		if (page.getRows() == null || page.getRows().isEmpty()) {
			return;
		}

		writeElementStart(writer, "page");
		page.getRows().forEach(this::serializeRow);
		writeElementEnd(writer, "page");
	}

	private void serializeRow(PageRow row) {
		if (row.getFields() == null || row.getFields().isEmpty()) {
			return;
		}

		writeElementStart(writer, "pageRow");
		row.getFields().forEach(this::serializeField);
		writeElementEnd(writer, "pageRow");
	}

	private void serializeField(PageField field) {
		writeElement(writer, "pageField", null, Collections.singletonMap("name", field.getCtrl().getName()));
	}

	private static String createCsvFile(
			List<PermissibleValue> permissibleValues, 
			String fileName, String outDir) {
		
		StringBuilder csvFile = null;
		CSVWriter csvWriter = null;
		try {
			StringBuilder pvDir = new StringBuilder().append(outDir).append(File.separator).append("pvs");
			File pvDirFile = new File(pvDir.toString());
			
			if (!pvDirFile.exists() && !pvDirFile.mkdirs()) {
				throw new FormException("Unable to create pv directory");
			}
			
			csvFile =  new StringBuilder(pvDir).append(File.separator).append(fileName).append(".csv");
			csvWriter = new CSVWriter(new FileWriter(csvFile.toString()));

			//csvWriter.writeNext(PV_CSV_HEADER);
			
			for(PermissibleValue pv : permissibleValues) {
				String val = pv.getValue() != null ? pv.getValue() : "";
				if (pv.getNumericCode() != null) {
					val = pv.getNumericCode().toString() + "##:" + val;
				}

				String showWhen = pv.getShowWhen() != null ? pv.getShowWhen() : "";
				String[] pvDetails = {val, showWhen};
				csvWriter.writeNext(pvDetails);
			}
			
			csvWriter.flush();
		} catch(IOException e){
			throw new FormException("Error occured while creating .csv file" + csvFile.toString());
		} finally {
			IoUtil.close(csvWriter);
		}
		
		return new StringBuilder().append(fileName).append(".csv").toString();
	}
}