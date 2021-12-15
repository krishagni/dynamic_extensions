package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Page implements Serializable {
	private static final long serialVersionUID = 5552295077941247997L;

	private List<PageRow> rows = new ArrayList<>();

	public List<PageRow> getRows() {
		return rows;
	}

	public void setRows(List<PageRow> rows) {
		this.rows = rows;
	}

	public Map<String, Object> getProps() {
		List<List<Map<String, Object>>> rowProps = new ArrayList<>();
		for (PageRow row : rows) {
			rowProps.add(row.getProps());
		}

		return Collections.singletonMap("rows", rowProps);
	}
}
