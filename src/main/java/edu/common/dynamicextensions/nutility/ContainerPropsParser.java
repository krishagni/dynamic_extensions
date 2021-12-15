package edu.common.dynamicextensions.nutility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.factory.ControlFactory;
import edu.common.dynamicextensions.domain.nui.factory.ControlManager;
import edu.common.dynamicextensions.napi.FormException;

public class ContainerPropsParser {
	private Map<String, Object> props;

	public ContainerPropsParser(Map<String, Object> props) {
		this.props = props;
	}

	public Container parse() {
		Container container = new Container();

		container.useAsDto();
		Number formId = (Number) props.get("id");
		if (formId != null) {
			container.setId(formId.longValue());
		}

		String name = (String) props.get("name");
		container.setName(name);
		if (name == null || name.trim().isEmpty()) {
			throw new FormException("Form name can't be null");
		}

		String caption = (String) props.get("caption");
		container.setCaption(caption);
		if (caption == null) {
			throw new FormException("Form caption can't be null");
		}

		int currentRow = 0;
		List<List<Map<String, Object>>> rows = (List<List<Map<String, Object>>>) props.get("rows");
		if (rows == null) {
			rows = new ArrayList<>();
		}

		for (List<Map<String, Object>> row : rows) {
			++currentRow;

			int xPos = 0;
			for (Map<String, Object> fieldProps : row) {
				++xPos;
				String type = (String) fieldProps.get("type");

				ControlFactory factory = ControlManager.getInstance().getFactory(type);
				if (factory == null) {
					throw new FormException("Invalid control type: " + type);
				}

				Control ctrl = factory.parseControl(fieldProps, currentRow, xPos);
				container.addControl(ctrl);
			}
		}

		return container;
	}
}
