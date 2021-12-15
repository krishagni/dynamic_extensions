package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getTextValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.LinkControl;
import edu.common.dynamicextensions.napi.FormException;

public class LinkControlFactory extends AbstractControlFactory {
	public static LinkControlFactory getInstance() {
		return new LinkControlFactory();
	}

	@Override
	public String getType() {
		return "linkField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		LinkControl linkField = new LinkControl();
		setControlProps(linkField, ele, row, xPos);

		Map<String, Object> lfProps = new HashMap<>();
		lfProps.put("formName", getTextValue(ele, "formName", null));
		setLinkProps(linkField, lfProps);
		return linkField;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		LinkControl linkField = new LinkControl();
		setControlProps(linkField, props, row, xPos);
		setLinkProps(linkField, props);
		return linkField;
	}

	private void setLinkProps(LinkControl linkField, Map<String, Object> props) {
		String formName = (String) props.getOrDefault("formName", null);
		if (StringUtils.isBlank(formName)) {
			throw new FormException("Form name is mandatory for link field");
		}

		Container form = Container.getContainer(formName);
		if (form == null) {
			throw new FormException("Invalid form name: " + formName);
		}

		linkField.setFormName(formName);
	}
}
