package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.nutility.ContainerParser;
import edu.common.dynamicextensions.nutility.ContainerPropsParser;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class SubFormControlFactory extends AbstractControlFactory {
	public static SubFormControlFactory getInstance() {
		return new SubFormControlFactory();
	}

	@Override
	public String getType() {		
		return "subForm";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		SubFormControl subForm = new SubFormControl();
		setControlProps(subForm, ele, row, xPos);
		
		Integer numEntries = getIntValue(ele, "maxEntries", -1);
		if (numEntries != null) {
			subForm.setNoOfEntries(numEntries);
		} 
		
		subForm.setShowAddMoreLink(getBooleanValue(ele, "showAddMoreLink"));
		subForm.setPasteButtonEnabled(getBooleanValue(ele, "pasteButtonEnabled"));
		subForm.setParentKey(getTextValue(ele, "parentKey"));
		subForm.setForeignKey(getTextValue(ele, "foreignKey"));
		subForm.setPathLink(getBooleanValue(ele, "pathLink"));
		subForm.setCustomFieldsInfo(getTextValue(ele, "customFieldsInfo"));
		subForm.setFlatten(getBooleanValue(ele, "flatten", true));
		subForm.setExtnFkColumn(getTextValue(ele, "extnFkCol"));
		subForm.setFormIdColumn(getTextValue(ele, "formIdCol", "FORM_ID"));

		ContainerParser parser = new ContainerParser(props.getProperty("pvDir"));
		Container subContainer = parser.parse(ele, false);
		subForm.setSubContainer(subContainer);
		return subForm;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		SubFormControl subForm = new SubFormControl();
		setControlProps(subForm, props, row, xPos);

		Object singleEntry = props.get("singleEntry");
		if (Boolean.TRUE.equals(singleEntry)) {
			subForm.setNoOfEntries(1);
		} else {
			subForm.setNoOfEntries(-1);
		}

		ContainerPropsParser parser = new ContainerPropsParser(props);
		subForm.setSubContainer(parser.parse());
		return subForm;
	}
}