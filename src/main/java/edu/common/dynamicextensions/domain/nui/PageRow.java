package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageRow implements Serializable {

	private static final long serialVersionUID = 3962265194245394936L;

	private List<PageField> fields;

	public List<PageField> getFields() {
		return fields;
	}

	public void setFields(List<PageField> fields) {
		this.fields = fields;
	}

	public List<Map<String, Object>> getProps() {
		List<Map<String, Object>> fieldProps = new ArrayList<>();
		for (PageField field : fields) {
			fieldProps.add(field.getProps());
		}

		return fieldProps;
	}
}
