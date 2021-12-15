
package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeCDataElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;

import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.nui.ValidationRuleNames;

public abstract class Control implements Comparable<Control>, Serializable {
	private static final long serialVersionUID = -1079013546686251896L;

	public enum LabelPosition {
		LEFT_SIDE, TOP
	};

	private Long id;

	private String name;
	
	private String userDefinedName;

	private String caption;

	private String customLabel;

	private LabelPosition labelPosition = LabelPosition.LEFT_SIDE;

	private String toolTip = StringUtils.EMPTY;

	private String dbColumnName;

	private boolean phi;

	private int sequenceNumber;

	private int xPos;

	private boolean showLabel = true;

	private boolean skipLogicSourceControl;

	private boolean calculatedSourceControl;
	
	private boolean skipLogicTargetControl;
	
	private boolean showInGrid;

	private String recordUrl;

	private String showWhenExpr;

	private boolean hidden;

	private String conceptCode;

	private String conceptPreferredName;

	private String conceptDefinitionSource;

	private String conceptDefinition;

	private Set<ValidationRule> validationRules = new HashSet<>();

	private Container container;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserDefinedName() {
		return userDefinedName;
	}

	public void setUserDefinedName(String userDefinedName) {
		this.userDefinedName = userDefinedName;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCustomLabel() {
		return customLabel;
	}

	public void setCustomLabel(String customLabel) {
		this.customLabel = customLabel;
	}

	public LabelPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(LabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public boolean isPhi() {
		return phi;
	}

	public void setPhi(boolean phi) {
		this.phi = phi;
	}

	public boolean isMandatory() {
		return isRuleEnabled(ValidationRuleNames.REQUIRED);
	}

	public void setMandatory(boolean mandatory) {
		enableRule(mandatory, ValidationRuleNames.REQUIRED);
	}

	public boolean isUnique() {
		return isRuleEnabled(ValidationRuleNames.UNIQUE);
	}

	public void setUnique(boolean unique) {
		enableRule(unique, ValidationRuleNames.UNIQUE);
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public boolean showLabel() {
		return showLabel;
	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public boolean isSkipLogicSourceControl() {
		return skipLogicSourceControl;
	}

	public void setSkipLogicSourceControl(boolean skipLogicSourceControl) {
		this.skipLogicSourceControl = skipLogicSourceControl;
	}

	public boolean isCalculatedSourceControl() {
		return calculatedSourceControl;
	}

	public void setCalculatedSourceControl(boolean calculatedSourceControl) {
		this.calculatedSourceControl = calculatedSourceControl;
	}
	
	public boolean isSkipLogicTargetControl() {
		return skipLogicTargetControl;
	}

	public void setSkipLogicTargetControl(boolean skipLogicTargetControl) {
		this.skipLogicTargetControl = skipLogicTargetControl;
	}
	
	public boolean showInGrid() {
		return showInGrid;
	}

	public void setShowInGrid(boolean showInGrid) {
		this.showInGrid = showInGrid;
	}

	public String getRecordUrl() {
		return recordUrl;
	}

	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}

	public String getShowWhenExpr() {
		return showWhenExpr;
	}

	public void setShowWhenExpr(String showWhenExpr) {
		this.showWhenExpr = showWhenExpr;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getDbColumnName() {
		return dbColumnName;
	}

	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}

	public String getConceptPreferredName() {
		return conceptPreferredName;
	}

	public void setConceptPreferredName(String conceptPreferredName) {
		this.conceptPreferredName = conceptPreferredName;
	}

	public String getConceptDefinitionSource() {
		return conceptDefinitionSource;
	}

	public void setConceptDefinitionSource(String conceptDefinitionSource) {
		this.conceptDefinitionSource = conceptDefinitionSource;
	}

	public String getConceptDefinition() {
		return conceptDefinition;
	}

	public void setConceptDefinition(String conceptDefinition) {
		this.conceptDefinition = conceptDefinition;
	}

	public Set<ValidationRule> getValidationRules() {
		return validationRules;
	}
	
	public void setValidationRules(Set<ValidationRule> validationRules) {
		this.validationRules = validationRules;
	}
	
	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public void addValidationRule(String ruleName, Map<String, String> ruleParams) {
		ValidationRule reqRule = getValidationRule(ruleName);
		
		if (reqRule == null) {
			reqRule = new ValidationRule();
			reqRule.setName(ruleName);
			validationRules.add(reqRule);
		}

		if (ruleParams != null) {
			reqRule.getParams().putAll(ruleParams);
		}
	}
	
	public ValidationRule getValidationRule(String ruleName) {
		ValidationRule result = null;
		
		for (ValidationRule rule : validationRules) {
			if (rule.getName().equals(ruleName)) {
				result = rule;
				break;
			}
		}
		
		return result;
	}
	
	public String getValidationRuleParam(String ruleName, String param) {
		ValidationRule rule = getValidationRule(ruleName);
		String result = null;
		
		if (rule != null && rule.getParams() != null) {
			result = rule.getParams().get(param);
		}
		
		return result;
	}
	
	public void removeValidationRule(String ruleName) {
		Iterator<ValidationRule> ruleIterator = validationRules.iterator();
		
		while (ruleIterator.hasNext()) {
			if (ruleIterator.next().getName().equals(ruleName)) {
				ruleIterator.remove();
				break;
			}
		}
	}

	public abstract DataType getDataType();
	
	public abstract List<ColumnDef> getColumnDefs();

	public abstract <T> T fromString(String value);

	public String toDisplayValue(Object value) {
		return toString(value);
	}

	public String toString(Object value) {
		if (value == null) {
			return null;
		}

		return value.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((userDefinedName == null) ? 0 : userDefinedName.hashCode());
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result	+ ((customLabel == null) ? 0 : customLabel.hashCode());
		result = prime * result	+ ((labelPosition == null) ? 0 : labelPosition.hashCode());
		result = prime * result + ((toolTip == null) ? 0 : toolTip.hashCode());
		result = prime * result	+ ((dbColumnName == null) ? 0 : dbColumnName.hashCode());
		result = prime * result + (phi ? 1231 : 1237);
		result = prime * result + sequenceNumber;
		result = prime * result + xPos;
		result = prime * result + (showLabel ? 1231 : 1237);
		result = prime * result + (skipLogicSourceControl ? 1231 : 1237);
		result = prime * result + (calculatedSourceControl ? 1231 : 1237);
		result = prime * result + (skipLogicTargetControl ? 1231 : 1237);
		result = prime * result + (showInGrid ? 1231 : 1237);
		result = prime * result + ((conceptCode == null) ? 0 : conceptCode.hashCode());
		result = prime * result	+ ((conceptPreferredName == null) ? 0 : conceptPreferredName.hashCode());
		result = prime * result	+ ((conceptDefinitionSource == null) ? 0 : conceptDefinitionSource.hashCode());
		result = prime * result	+ ((conceptDefinition == null) ? 0 : conceptDefinition.hashCode());		
		result = prime * result	+ ((validationRules == null) ? 0 : validationRules.hashCode());		
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		
		if (getClass() != other.getClass()) {
			return false;
		}
		
		if (this == other) {
			return true;
		}
		
		Control ctrl = (Control)other;
		if (!StringUtils.equals(name, ctrl.name) ||
			!StringUtils.equals(userDefinedName, ctrl.userDefinedName) ||
			!StringUtils.equals(caption, ctrl.caption) ||
			!StringUtils.equals(customLabel, ctrl.customLabel) ||
			labelPosition != ctrl.labelPosition ||
			!StringUtils.equals(toolTip, ctrl.toolTip) ||
			!StringUtils.equals(dbColumnName, ctrl.dbColumnName) ||
			phi != ctrl.phi ||
			sequenceNumber != ctrl.sequenceNumber ||
			xPos != ctrl.xPos ||
			showLabel != ctrl.showLabel ||
			skipLogicSourceControl != ctrl.skipLogicSourceControl ||
			calculatedSourceControl != ctrl.calculatedSourceControl ||
			skipLogicTargetControl != ctrl.skipLogicTargetControl ||
			showInGrid != ctrl.showInGrid ||
			!StringUtils.equals(conceptCode, ctrl.conceptCode) ||
			!StringUtils.equals(conceptPreferredName, ctrl.conceptPreferredName) ||
			!StringUtils.equals(conceptDefinitionSource, ctrl.conceptDefinitionSource) ||
			!StringUtils.equals(conceptDefinition, ctrl.conceptDefinition)) {
			return false;
		}
		
		if (validationRules == null && ctrl.validationRules == null) {
			return true;
		}
		
		if (validationRules.size() != ctrl.validationRules.size()) {
			return false;
		}
		
		return validationRules.containsAll(ctrl.validationRules);
	}	
	
	@Override
	public int compareTo(Control o) {
		int result;
		if (this.getSequenceNumber() < o.sequenceNumber) {
			result = -1;
		} else if (this.sequenceNumber == o.sequenceNumber) {
			if (this.xPos < o.xPos) {
				result = -1;
			} else {
				result = 1;
			}
		} else {
			result = 1;
		}

		return result;
	}

	public boolean isDynamic() {
		return isSkipLogicTargetControl();
	}

	public ValidationStatus validate(Object value) {
		return ValidationStatus.OK;
	}
	
	public Set<String> getAllConceptCodes() {
		if (conceptCode != null && !conceptCode.isEmpty()) {
			return Collections.singleton(conceptCode);
		}
		return Collections.emptySet();
	}
	
	public Map<String, Object> getProps() {
		Map<String, Object> ctrlProps = new HashMap<>();
		ctrlProps.put("name", getName());
		ctrlProps.put("udn", getUserDefinedName());
		ctrlProps.put("caption", getCaption());
		ctrlProps.put("type", getCtrlType());
		ctrlProps.put("customLabel", getCustomLabel());
		ctrlProps.put("labelPosition", getLabelPosition());
		ctrlProps.put("toolTip", getToolTip());
		ctrlProps.put("skipLogicSourceControl", isSkipLogicSourceControl());
		ctrlProps.put("skipLogicTargetControl", isSkipLogicTargetControl());
		ctrlProps.put("calculatedSourceControl", isCalculatedSourceControl());
		ctrlProps.put("conceptCode", getConceptCode());
		ctrlProps.put("conceptPreferredName", getConceptPreferredName());
		ctrlProps.put("conceptDefinitionSource", getConceptDefinitionSource());
		ctrlProps.put("conceptDefinition", getConceptDefinition());
		ctrlProps.put("validationRules", getValidationRules());
		ctrlProps.put("showWhen", getShowWhenExpr());
		ctrlProps.put("hidden", isHidden());
		ctrlProps.put("phi", isPhi());
		ctrlProps.put("mandatory", isMandatory());
		ctrlProps.put("showInGrid", showInGrid());
		
		getProps(ctrlProps);
		return ctrlProps;
	}

	public abstract String getCtrlType();
		
	public abstract void getProps(Map<String, Object> props);
	
	public void serializeToXml(Writer writer, Properties props) {
		writeElement(writer, "name",        getName());
		writeElement(writer, "udn",         getUserDefinedName());
		writeCDataElement(writer, "caption",     getCaption());
		writeCDataElement(writer, "customLabel", getCustomLabel());
		writeElement(writer, "phi",         isPhi());
		writeElement(writer, "mandatory",   isMandatory());
		writeCDataElement(writer, "toolTip",     getToolTip());
		writeElement(writer, "showLabel",   showLabel());
		writeElement(writer, "showInGrid",  showInGrid());
		writeCDataElement(writer, "showWhen", getShowWhenExpr());
		writeElement(writer, "hidden", isHidden());
	}

	private boolean isRuleEnabled(String ruleName) {
		return (getValidationRule(ruleName) != null);
	}

	private void enableRule(boolean enable, String ruleName) {
		if (enable) {
			addValidationRule(ruleName, null);
		} else {
			removeValidationRule(ruleName);
		}
	}
}
