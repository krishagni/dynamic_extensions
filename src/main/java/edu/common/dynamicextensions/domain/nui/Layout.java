package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Layout implements Serializable {

	private static final long serialVersionUID = 9161084273291032435L;

	private String name;

	private List<Page> pages;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public Map<String, Object> getProps() {
		List<Map<String, Object>> pageProps = new ArrayList<>();
		if (pages != null) {
			for (Page page : pages) {
				pageProps.add(page.getProps());
			}
		}

		Map<String, Object> props = new HashMap<>();
		props.put("name", name);
		props.put("pages", pageProps);
		return props;
	}
}
