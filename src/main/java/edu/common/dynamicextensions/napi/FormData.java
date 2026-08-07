package edu.common.dynamicextensions.napi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.common.dynamicextensions.domain.nui.AbstractLookupControl;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.ValidationErrors;
import edu.common.dynamicextensions.domain.nui.ValidationStatus;

public class FormData {
	private Container container;
	
	private Long recordId;

	private Long parentRecordId;
	
	private Map<String, Object> appData = new HashMap<>();
	
	private Map<String, ControlValue> fieldValues = new LinkedHashMap<>();
	
	private FormData parentFormData;

	private int revision;

	public FormData(Container container) {
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getParentRecordId() {
		return parentRecordId;
	}

	public void setParentRecordId(Long parentRecordId) {
		this.parentRecordId = parentRecordId;
	}

	public Map<String, Object> getAppData() {
		return appData;
	}

	public void setAppData(Map<String, Object> appData) {
		this.appData = appData;
	}
	
	public Collection<ControlValue> getFieldValues() {
		return fieldValues.values();
	}

	public void setFieldValues(Map<String, ControlValue> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	public void addFieldValue(ControlValue controlValue) {
		fieldValues.put(controlValue.getControl().getName(), controlValue);
	}
	
	public ControlValue getFieldValue(String name) {
		return fieldValues.get(name);
	}
	
	public Collection<ControlValue> getOrderedFieldValues() {
		List<ControlValue> controlValues = new ArrayList<ControlValue>();
		for (Control ctrl : getContainer().getOrderedControlList()) {
			ControlValue ctrlValue = fieldValues.get(ctrl.getName());
			if (ctrlValue == null) {
				ctrlValue = new ControlValue(ctrl, null);
			}

			controlValues.add(ctrlValue);
		}
		
		return controlValues;
	}

	public boolean isUsingUdn() {
		return isUsingUdn(appData);
	}

	public FormData getRootFormData() {
		FormData result = this;
		while (result.parentFormData != null) {
			result = result.parentFormData;
		}
		
		return result;		
	}

	public int getRevision() {
		return revision;
	}

	public void incrementRevision() {
		++revision;
	}

	public String toJson() {
		return toJson(false);
	}
	
	public String toJson(boolean includeUdn) {
		return new Gson().toJson(getFieldNameValueMap(includeUdn));
	}
	
	public static FormData fromJson(String json) {
		return fromJson(json, null);
	}
	
	@SuppressWarnings("unchecked")
	public static FormData fromJson(String json, Long containerId) {
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> valueMap = new Gson().fromJson(json, type);
		return fromValueMap(containerId, valueMap);
	}

	public static List<FormData> fromValueMap(Long containerId, List<Map<String, Object>> valueMapList) {
		if (valueMapList.isEmpty()) {
			return Collections.<FormData>emptyList();
		}

		Container container = getContainer(containerId, valueMapList.get(0));
		List<FormData> formDataList = new ArrayList<FormData>();

		for (Map<String, Object> valueMap : valueMapList) {
			formDataList.add(prepareFormData(container, valueMap));
		}

		return formDataList;
	}
	
	public static FormData fromValueMap(Long containerId, Map<String, Object> valueMap) {
		Container container = getContainer(containerId, valueMap);
		return prepareFormData(container, valueMap);
	}
		
	public static FormData getFormData(Container container, Map<String, Object> valueMap) {
		return getFormData(container, valueMap, false, null);
	}
	
	@SuppressWarnings("unchecked")
	public static FormData getFormData(Container container, Map<String, Object> valueMap, boolean useUdn, FormData parent) {
		FormData formData = new FormData(container);
		formData.parentFormData = parent;
		
		Map<String, Object> appData = (Map<String, Object>)valueMap.get("appData");
		formData.setAppData(appData);
				
		Number recordId = (Number)valueMap.get("id");
		if (recordId != null) {
			formData.setRecordId(recordId.longValue());
		}
		
		for (Map.Entry<String, Object> fieldValue : valueMap.entrySet()) {
			if (fieldValue.getKey().equals("id")) {
				continue;
			}
						
			Control ctrl = null;
			if (useUdn) {
				ctrl = container.getControlByUdn(fieldValue.getKey());
			} else {
				ctrl = container.getControl(fieldValue.getKey());
			}

			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				if (sfCtrl.isOneToOne()) {
					Map<String, Object> subValueMap = (Map<String, Object>)fieldValue.getValue();
					FormData subFormData = getFormData(sfCtrl.getSubContainer(), subValueMap, useUdn, formData);
					formData.addFieldValue(new ControlValue(ctrl, subFormData));					
				} else {
					List<?> subValueMapList = (List<Map<String, Object>>)fieldValue.getValue();
					List<FormData> subFormData = new ArrayList<>();
					if (subValueMapList != null) {
						for (Object element : subValueMapList) {
							Map<String, Object> subValueMap = null;
							if (element instanceof List<?>) {
								subValueMap = new HashMap<>();
								for (Object field : ((List) element)) {
									Map<String, Object> fieldMap = (Map<String, Object>) field;
									subValueMap.put((String) (useUdn ? fieldMap.get("udn") : fieldMap.get("name")), fieldMap.get("value"));
								}
							} else if (element instanceof Map<?,?>) {
								subValueMap = (Map<String, Object>) element;
							}

							if (subValueMap != null) {
								subFormData.add(getFormData(sfCtrl.getSubContainer(), subValueMap, useUdn, formData));
							}
						}
					}
					
					formData.addFieldValue(new ControlValue(ctrl, subFormData));					
				}
			} else if (ctrl instanceof MultiSelectControl || (ctrl instanceof AbstractLookupControl luCtrl && luCtrl.isMultiValued())) {
				Object input = fieldValue.getValue();
				Collection<?> values = null;
				if (input == null) {
				} else if (input instanceof Collection) {
					values = (Collection<?>) input;
				} else if (input.getClass().isArray()) {
					values = Arrays.asList((Object[]) input);
				}

				AbstractLookupControl luCtrl = ctrl instanceof  AbstractLookupControl ? (AbstractLookupControl) ctrl : null;
				String[] result = null;
				if (values != null) {
					result = values.stream()
						.map(value -> luCtrl != null ? luCtrl.getValue(value) : value)
						.filter(Objects::nonNull)
						.map(Object::toString)
						.toArray(String[]::new);
				}

				formData.addFieldValue(new ControlValue(ctrl, result));
			} else if (ctrl instanceof FileUploadControl) {
				FileControlValue fcv = null;
				if (fieldValue.getValue() instanceof Map) {
					fcv = FileControlValue.fromValueMap((Map<String, String>) fieldValue.getValue());
				}

				formData.addFieldValue(new ControlValue(ctrl, fcv));
			} else if (ctrl instanceof AbstractLookupControl) {
				formData.addFieldValue(new ControlValue(ctrl, ((AbstractLookupControl) ctrl).getValue(fieldValue.getValue())));
			} else if (ctrl != null){
				formData.addFieldValue(new ControlValue(ctrl, fieldValue.getValue()));
			}			
		}
		
		return formData;
	}
						
	public Map<String, Object> getFieldNameValueMap(boolean includeUdn) {
		return getFieldNameValueMap(includeUdn, false);
	}

	public Map<String, Object> getFieldNameValueMap(boolean includeUdn, boolean includeUiValue) {
		Map<String, Object> props = new HashMap<>();
		props.put("appData", getAppData());
		props.put("containerId", container.getId());
		props.put("id", recordId);
		
		for (ControlValue fieldValue : getFieldValues()) {
			String name = fieldValue.getControl().getName();
			if (includeUdn) {
				name = fieldValue.getControl().getUserDefinedName();
			}
			
			Object value = fieldValue.getValue();			
			if (value instanceof FileControlValue fcv) {
				props.put(name, fcv.toValueMap());
			} else if (value instanceof List) {
				List<FormData> formDataList = (List<FormData>)value;
				
				List<Map<String, Object>> sfData = new ArrayList<>();
				for (FormData formData : formDataList) {
					sfData.add(formData.getFieldNameValueMap(includeUdn, includeUiValue));
				}
				
				props.put(name, sfData);
			} else if (value instanceof FormData) {
				props.put(name, ((FormData) value).getFieldNameValueMap(includeUdn, includeUiValue));
			} else if (value != null && value.getClass().isArray()) {
				if (includeUiValue && fieldValue.getUiValue() != null && fieldValue.getUiValue().getClass().isArray()) {
					props.put(name, fieldValue.getUiValue());
				} else {
					props.put(name, value);
				}
			} else {
				if (includeUiValue && fieldValue.getUiValue() != null) {
					props.put(name, fieldValue.getUiValue());
				} else {
					props.put(name, fieldValue.getControl().toString(value));
				}
			}			
		}
		
		return props;
	}

	public Map<String, Object> getFieldValueMap() {
		Map<String, Object> props = new HashMap<>();
		props.put("appData", getAppData());
		props.put("containerId", container.getId());
		props.put("caption", container.getCaption());
		props.put("name", container.getName());
		props.put("id", recordId);

		List<Map<String, Object>> fields = new ArrayList<>();
		props.put("fields", fields);

		for (ControlValue fieldValue : getOrderedFieldValues()) {
			Map<String, Object> fieldData = new HashMap<>();
			fields.add(fieldData);

			Control ctrl = fieldValue.getControl();
			fieldData.put("name", ctrl.getName());
			fieldData.put("udn", ctrl.getUserDefinedName());
			fieldData.put("caption", ctrl.getCaption());
			fieldData.put("type", ctrl.getCtrlType());
			if (StringUtils.isNotBlank(ctrl.getShowWhenExpr())) {
				fieldData.put("showWhen", ctrl.getShowWhenExpr());
			}

			if (ctrl instanceof DatePicker) {
				fieldData.put("format", ((DatePicker) ctrl).getFormat());
			}

			if (ctrl.getRecordUrl() != null && ctrl.getRecordUrl().length() > 0) {
				fieldData.put("url", ctrl.getRecordUrl());
			}

			Object value = fieldValue.getValue();
			if (value instanceof FileControlValue fcv) {
				value = fcv.toValueMap();
			} else if (value instanceof List) {
				List<FormData> formDataList = (List<FormData>)value;

				List<Map<String, Object>> sfData = new ArrayList<>();
				for (FormData formData : formDataList) {
					sfData.add(formData.getFieldValueMap());
				}

				value = sfData;
			} else if (value instanceof FormData fd) {
				value = fd.getFieldValueMap();
			}

			if (value != null) {
				fieldData.put("value", value);
				if (fieldValue.getUiValue() != null) {
					fieldData.put("displayValue", fieldValue.getUiValue());
				}

				if (fieldValue.getCodedValue() != null) {
					fieldData.put("codedValue", fieldValue.getCodedValue());
				}
			}
		}

		return props;
	}

	public void validate() {
		validate(isUsingUdn());
	}
	
	public void validate(boolean useUdn) {
		ValidationErrors errors = new ValidationErrors();
		
		Map<String, Control> mandatory = new HashMap<String, Control>();
		for (Control ctrl : container.getControls()) {
			if (ctrl.isMandatory()) {
				mandatory.put(ctrl.getName(), ctrl);
			}
			
//			if (ctrl instanceof SubFormControl) {
//				mandatory.put(ctrl.getName(), ctrl);
//			}
		}
		
		for (ControlValue ctrlValue : fieldValues.values()) {
			Control ctrl = ctrlValue.getControl();
			mandatory.remove(ctrl.getName());
			
			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				
				List<FormData> subFormData = null;
				if (sfCtrl.isOneToOne()) {
					subFormData = Collections.singletonList((FormData)ctrlValue.getValue());
				} else {
					subFormData = (List<FormData>)ctrlValue.getValue();
				}
								
				if (subFormData == null || subFormData.isEmpty()) {
					subFormData = Collections.emptyList();
				}
				
				try {
					for(FormData sf : subFormData) {
						sf.validate(useUdn);
					}
				} catch (ValidationErrors e) {
					errors.addErrors(e.getErrors());
				}				
			} else {
				ValidationStatus status = ctrl.validate(ctrlValue.getValue());
				if (status != ValidationStatus.OK) {
					String formName = useUdn ? ctrl.getContainer().getName() : ctrl.getContainer().getCaption();
					String field = useUdn ? ctrl.getUserDefinedName() : ctrl.getCaption();
					errors.addError(formName + "." + field, status);
				}
			}
		}
		
		for (Control ctrl : mandatory.values()) {
			String formName = useUdn ? ctrl.getContainer().getName() : ctrl.getContainer().getCaption();
			String field = useUdn ? ctrl.getUserDefinedName() : ctrl.getCaption();
			errors.addError(formName + "." + field, ValidationStatus.NULL_OR_EMPTY);
		}
		
		errors.throwIfErrors();
	}

	public void maskPhiFieldValues() {
		for (ControlValue cv : getFieldValues()) {
			if (cv.getControl() instanceof SubFormControl) {
				SubFormControl sf = (SubFormControl) cv.getControl();
				List<FormData> sfDataList = new ArrayList<FormData>();
				if (sf.isOneToOne()) {
					sfDataList.add((FormData)cv.getValue());
				} else {
					sfDataList.addAll((List<FormData>)cv.getValue());
				}

				for (FormData sfData : sfDataList) {
					sfData.maskPhiFieldValues();
				}
			} else if (cv.getControl().isPhi()) {
				cv.setValue("###");
			}
		}
	}
	
	private static boolean isUsingUdn(Map<String, Object> appData) {
		if (appData == null) {
			return false;
		}
		
		Object val = appData.get("useUdn");
		if (val instanceof String) {
			return ((String) val).trim().equals("true");
		} else if (val instanceof Boolean) {
			return ((Boolean) val).booleanValue();
		} else if (val instanceof Number) {
			return ((Number) val).intValue() == 1;
		}
		
		return false;
	}

	private static Container getContainer(Long containerId, Map<String, Object> valueMap) {
		if (valueMap.get("containerId") == null && containerId == null) {
			throw new FormException("Input doesn't have mandatory property: containerId");
		}

		if (containerId == null) {
			containerId = ((Number)valueMap.get("containerId")).longValue();
		}

		Container container = Container.getContainer(containerId);
		if (container == null) {
			throw new FormException("Input specifies invalid container id: " + containerId);
		}

		return container;
	}

	private static FormData prepareFormData(Container container, Map<String, Object> valueMap) {
		valueMap.remove("containerId");

		Map<String, Object> appData = (Map<String, Object>)valueMap.get("appData");
		boolean useUdn = isUsingUdn(appData);

		FormData formData = getFormData(container, valueMap, useUdn, null);
		if (valueMap.get("recordId") != null) {
			formData.setRecordId(((Number)valueMap.get("recordId")).longValue());
		}

		return formData;
	}
}
