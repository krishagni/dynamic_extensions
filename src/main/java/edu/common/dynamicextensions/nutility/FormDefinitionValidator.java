package edu.common.dynamicextensions.nutility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.common.dynamicextensions.napi.FormException;

final class FormDefinitionValidator {
	private static final Set<String> MANAGED_FORM_PROPERTIES = new HashSet<>(
		Arrays.asList(
			"table", "column", "primaryKey", "parentKey", "foreignKey",
			"collectionTable", "collectionKey",
			"hierarchyTable", "hierarchyAncestorColumn", "hierarchyDescendentColumn",
			"activeCondition", "extnFkCol", "formIdCol", "sql"
		)
	);

	static void validateUserForm(Element viewElement) {
		for (String property : MANAGED_FORM_PROPERTIES) {
			NodeList nodes = viewElement.getElementsByTagName(property);
			if (nodes != null && nodes.getLength() > 0) {
				throw new FormException("Property '" + property + "' is not allowed in user forms");
			}
		}
	}

	static void validateUserForm(Map<String, Object> input) {
		for (Map.Entry<String, Object> property : input.entrySet()) {
			if (MANAGED_FORM_PROPERTIES.contains(property.getKey())) {
				throw new FormException("Property '" + property.getKey() + "' is not allowed in user forms");
			}

			Object value = property.getValue();
			if (value instanceof Map) {
				validateUserForm((Map<String, Object>) value);
			} else if (value instanceof List) {
				validateUserForm((List<?>) value);
			}
		}
	}

	private static void validateUserForm(List<?> input) {
		for (Object value : input) {
			if (value instanceof Map) {
				validateUserForm((Map<String, Object>) value);
			} else if (value instanceof List) {
				validateUserForm((List<?>) value);
			}
		}
	}

	private FormDefinitionValidator() {

	}
}
