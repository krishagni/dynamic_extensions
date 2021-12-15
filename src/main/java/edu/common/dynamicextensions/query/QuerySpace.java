package edu.common.dynamicextensions.query;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.ContainerParser;
import edu.common.dynamicextensions.nutility.XmlUtil;

public class QuerySpace {
	private String name;

	private Map<String, Container> forms;

	private PathConfig pathConfig;

	private String rootForm;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Container> getForms() {
		return forms;
	}

	public void setForms(Map<String, Container> forms) {
		this.forms = forms;
	}

	public PathConfig getPathConfig() {
		return pathConfig;
	}

	public void setPathConfig(PathConfig pathConfig) {
		this.pathConfig = pathConfig;
	}

	public String getRootForm() {
		return rootForm;
	}

	public void setRootForm(String rootForm) {
		this.rootForm = rootForm;
	}

	public Container getForm(String name) {
		return forms != null ? forms.get(name) : null;
	}

	public static QuerySpace fromJson(String json) {
		try {
			QuerySpace qs = new QuerySpace();

			Map<String, Object> qsMap = new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {});
			qs.setName((String) qsMap.get("name"));
			qs.setRootForm((String) qsMap.get("rootForm"));

			List<Container> forms = getForms((List<Map<String, Object>>) qsMap.get("forms"));
			qs.setForms(forms.stream().collect(Collectors.toMap(Container::getName, form -> form)));
			qs.setPathConfig(getPathConfig((List<Map<String, Object>>) qsMap.get("paths")));
			return qs;
		} catch (JsonProcessingException jpe) {
			throw new FormException("Invalid query space JSON: " + jpe.getMessage(), jpe);
		} catch (Exception e) {
			throw new FormException("Unknown server error: " + e.getMessage(), e);
		}
	}

	private static List<Container> getForms(List<Map<String, Object>> formsList) {
		if (formsList == null) {
			return Collections.emptyList();
		}

		return formsList.stream().map(QuerySpace::getForm).collect(Collectors.toList());
	}

	private static Container getForm(Map<String, Object> formMap) {
		try {
			StringWriter writer = new StringWriter();

			XmlUtil.writeElementStart(writer, "form");
			XmlUtil.writeElementStart(writer, "view");
			writeFormMetadata(formMap, writer);
			XmlUtil.writeElementEnd(writer, "view");
			XmlUtil.writeElementEnd(writer, "form");

			return new ContainerParser(new ByteArrayInputStream(writer.toString().getBytes())).parse();
		} catch (Exception e) {
			throw new FormException("Error creating form: " + e.getMessage(), e);
		}
	}

	private static void writeFormMetadata(Map<String, Object> formMd, StringWriter writer) {
		String name = (String) formMd.get("name");
		if (StringUtils.isBlank(name)) {
			throw new FormException("Form or sub-form name cannot be blank");
		}

		String caption = (String) formMd.get("caption");
		if (StringUtils.isBlank(caption)) {
			caption = name;
		}

		String dbTable = (String) formMd.get("table");
		if (StringUtils.isBlank(dbTable)) {
			throw new FormException("Form or sub-form data table name cannot be blank. Form: " + name);
		}

		XmlUtil.writeElement(writer, "name", name);
		XmlUtil.writeElement(writer, "udn", name);
		XmlUtil.writeCDataElement(writer, "caption", caption);
		XmlUtil.writeElement(writer, "table", dbTable);
		XmlUtil.writeElement(writer, "primaryKey", formMd.get("primaryKey"));
		XmlUtil.writeElement(writer, "parentKey", formMd.get("parentKey"));
		XmlUtil.writeElement(writer, "foreignKey", formMd.get("foreignKey"));

		List<Map<String, Object>> fields = (List<Map<String, Object>>) formMd.get("fields");
		if (fields == null || fields.isEmpty()) {
			return;
		}

		XmlUtil.writeElementStart(writer, "row");
		for (Map<String, Object> fieldMd : fields) {
			String type = (String) fieldMd.get("type");
			if (StringUtils.isBlank(type)) {
				throw new FormException("One or more fields do not have type specified. Form: " + name);
			}

			XmlUtil.writeElementStart(writer, type);
			if (type.equals("subForm")) {
				writeFormMetadata(fieldMd, writer);
			} else {
				for (Map.Entry<String, Object> fieldProp : fieldMd.entrySet()) {
					if (fieldProp.getKey().equals("caption")) {
						XmlUtil.writeCDataElement(writer, "caption", fieldProp.getValue());
					} else {
						XmlUtil.writeElement(writer, fieldProp.getKey(), fieldProp.getValue());
						if (fieldProp.getKey().equals("name")) {
							XmlUtil.writeElement(writer, "udn", fieldProp.getValue());
						}
					}
				}
			}
			XmlUtil.writeElementEnd(writer, type);
		}

		XmlUtil.writeElementEnd(writer, "row");
	}

	private static PathConfig getPathConfig(List<Map<String, Object>> pathsList) {
		if (pathsList == null) {
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		return PathConfig.from(
			pathsList.stream()
				.map(pathMd -> (Path) mapper.convertValue(pathMd, new TypeReference<Path>() { }))
				.collect(Collectors.toList())
		);
	}
}