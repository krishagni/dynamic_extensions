package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getBooleanValue;
import static edu.common.dynamicextensions.nutility.ParserUtil.getTextValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvVersion;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.ParserUtil;

public abstract class AbstractControlFactory implements ControlFactory {
	
	public void setControlProps(Control ctrl, Element ctrlEle, int currentRow, int xPos) {	
		String name = getTextValue(ctrlEle, "name");
		String userDefName = getTextValue(ctrlEle, "udn");
	
		if (name == null) {
			throw new FormException("Control name can't be null. Type = " + ctrlEle.getNodeName());
		}
		
		if (userDefName == null) {
			throw new FormException("User defined name of control can't be null. Type = " + ctrlEle.getNodeName());
		}
		
		ctrl.setName(name);
		ctrl.setUserDefinedName(userDefName);
		ctrl.setCaption(getTextValue(ctrlEle, "caption"));
		ctrl.setCustomLabel(getTextValue(ctrlEle, "customLabel"));
		ctrl.setToolTip(getTextValue(ctrlEle, "toolTip", ""));
		ctrl.setPhi(getBooleanValue(ctrlEle, "phi"));
		ctrl.setMandatory(getBooleanValue(ctrlEle, "mandatory"));
		ctrl.setUnique(getBooleanValue(ctrlEle, "unique"));
		ctrl.setShowInGrid(getBooleanValue(ctrlEle, "showInGrid"));
		ctrl.setShowLabel(getBooleanValue(ctrlEle, "showLabel", true));
		ctrl.setSequenceNumber(currentRow);
		ctrl.setConceptCode(getTextValue(ctrlEle, "conceptCode", null));
		ctrl.setxPos(xPos);
		ctrl.setRecordUrl(getTextValue(ctrlEle, "recordUrl", null));
		ctrl.setShowWhenExpr(getTextValue(ctrlEle, "showWhen", null));
		ctrl.setHidden(getBooleanValue(ctrlEle, "hidden", false));
		
		String dbColumn = getTextValue(ctrlEle, "column");
		if (dbColumn != null && !dbColumn.trim().isEmpty()) {
			ctrl.setDbColumnName(dbColumn.trim());
		}

		if (StringUtils.isNotBlank(ctrl.getShowWhenExpr())) {
			ctrl.setMandatory(false);
		}
	} 	
	
	public void setSelectProps(SelectControl selectControl, Element ctrlEle, int currentRow, int xPos, String pvDir) {		
		setControlProps(selectControl, ctrlEle, currentRow, xPos);		
			
		PvDataSource pvDataSource = new PvDataSource();
		pvDataSource.setDataType(DataType.STRING); // TODO: Need to read data type of options as well
		selectControl.setPvDataSource(pvDataSource);
		
		String sql = getSql(ctrlEle);
		if (sql == null) {
			List<PermissibleValue> permissibleValues = ParserUtil.getPermissibleValues(ctrlEle, pvDir);
			PvVersion pvVersion = new PvVersion();
			pvVersion.setPermissibleValues(permissibleValues);

			String defVal = getTextValue(ctrlEle, "defaultValue");
			if (defVal != null) {
				PermissibleValue pv = new PermissibleValue();
				pv.setOptionName(defVal);
				pv.setValue(defVal);
				pvVersion.setDefaultValue(pv);
			}
						
			List<PvVersion> pvVersions = new ArrayList<>();
			pvVersions.add(pvVersion);			
			pvDataSource.setPvVersions(pvVersions);
		} else {
			pvDataSource.setSql(sql);
		}		
	}

	public void setControlProps(Control ctrl, Map<String, Object> props, int currentRow, int xPos) {
		String name = (String) props.get("name");
		String udn  = (String) props.get("udn");

		if (name == null || name.trim().isEmpty()) {
			throw new FormException("Control name can't be null or empty. Type = " + ctrl.getCtrlType());
		}

		if (udn == null || udn.trim().isEmpty()) {
			throw new FormException("User defined name of control can't be null or empty. Type = " + ctrl.getCtrlType());
		}

		ctrl.setName(name);
		ctrl.setUserDefinedName(udn);
		ctrl.setCaption((String) props.get("caption"));
		ctrl.setCustomLabel((String) props.get("customLabel"));
		ctrl.setToolTip((String) props.get("toolTip"));
		ctrl.setPhi(getBool(props, "phi"));
		ctrl.setMandatory(getBool(props, "mandatory"));
		ctrl.setUnique(getBool(props, "unique"));
		ctrl.setShowInGrid(getBool(props, "showInGrid"));
		ctrl.setShowLabel(getBool(props, "showLabel", true));
		ctrl.setSequenceNumber(currentRow);
		ctrl.setConceptCode((String) props.get("conceptCode"));
		ctrl.setxPos(xPos);
		ctrl.setRecordUrl((String) props.get("recordUrl"));
		ctrl.setShowWhenExpr((String) props.get("showWhen"));
		ctrl.setHidden(getBool(props, "hidden", false));

		String dbColumn = (String) props.get("column");
		if (dbColumn != null && !dbColumn.trim().isEmpty()) {
			ctrl.setDbColumnName(dbColumn.trim());
		}

		if (StringUtils.isNotBlank(ctrl.getShowWhenExpr())) {
			ctrl.setMandatory(false);
		}
	}

	public void setSelectProps(SelectControl selectControl, Map<String, Object> props, int currentRow, int xPos) {
		setControlProps(selectControl, props, currentRow, xPos);

		PvDataSource pvDataSource = new PvDataSource();
		pvDataSource.setDataType(DataType.STRING); // TODO: Need to read data type of options as well
		selectControl.setPvDataSource(pvDataSource);

		String ordering = (String) props.getOrDefault("pvOrdering", "NONE");
		try {
			pvDataSource.setOrdering(PvDataSource.Ordering.valueOf(ordering));
		} catch (IllegalArgumentException iae) {
			throw new FormException("Invalid sort order: " + ordering);
		}

		List<Map<String, Object>> values = (List<Map<String, Object>>) props.get("pvs");
		if (values == null) {
			values = Collections.emptyList();
		}

		List<PermissibleValue> permissibleValues = values.stream()
			.filter((value) -> StringUtils.isNotBlank((String) value.get("value")))
			.map((value) -> {
				PermissibleValue pv = new PermissibleValue();
				pv.setOptionName((String) value.get("value"));
				pv.setValue((String) value.get("value"));
				return pv;
			})
			.collect(Collectors.toList());

		PvVersion pvVersion = new PvVersion();
		pvVersion.setPermissibleValues(permissibleValues);

		Object defVal = props.get("defaultValue");
		if (defVal instanceof Map) {
			Map<String, String> defValMap = (Map<String, String>) defVal;
			if (StringUtils.isNotBlank(defValMap.get("value"))) {
				PermissibleValue pv = new PermissibleValue();
				pv.setOptionName(defValMap.get("value"));
				pv.setValue(defValMap.get("value"));
				pvVersion.setDefaultValue(pv);
			} else {
				pvVersion.setDefaultValue(null);
			}
		} else if (defVal instanceof String) {
			PermissibleValue pv = new PermissibleValue();
			pv.setOptionName((String) defVal);
			pv.setValue((String) defVal);
			pvVersion.setDefaultValue(pv);
		} else {
			pvVersion.setDefaultValue(null);
		}

		List<PvVersion> pvVersions = new ArrayList<>();
		pvVersions.add(pvVersion);
		pvDataSource.setPvVersions(pvVersions);


	}

	private String getSql(Element optionsParentEl) {
		Element options = (Element)optionsParentEl.getElementsByTagName("options").item(0);
		NodeList sqlNode = options.getElementsByTagName("sql");
		return (sqlNode != null && sqlNode.getLength() == 1) ? 
				sqlNode.item(0).getFirstChild().getNodeValue() : null;  
	}

	protected boolean getBool(Map<String, Object> props, String propName) {
		return getBool(props, propName, null);
	}

	protected boolean getBool(Map<String, Object> props, String propName, Boolean defValue) {
		Object value = props.getOrDefault(propName, defValue);
		if (value == null) {
			return false;
		} else if (value instanceof String) {
			return ((String) value).equalsIgnoreCase("true");
		} else {
			return (Boolean) value;
		}
	}

	protected Integer getInt(Map<String, Object> props, String propName) {
		return getInt(props, propName, null);
	}

	protected Integer getInt(Map<String, Object> props, String propName, Integer defValue) {
		Object value = props.getOrDefault(propName, defValue);
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			if (StringUtils.isBlank((String) value)) {
				return null;
			}

			return Integer.parseInt((String) value);
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		throw new FormException("Unknown integer type/value: " + value.getClass() + " (" + value.toString() + ")");
	}
}