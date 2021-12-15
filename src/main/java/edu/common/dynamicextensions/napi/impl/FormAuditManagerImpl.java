/**
 * 
 */
package edu.common.dynamicextensions.napi.impl;

import java.io.IOException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.common.dynamicextensions.domain.FormAuditEvent;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.FormAuditManager;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;

public class FormAuditManagerImpl implements FormAuditManager {
	private static ObjectMapper mapper = new ObjectMapper();

	@Override
	public List<ControlValue> getDirtyFields(FormData previous, FormData current) {
		Map<String, ControlValue> currValueMap = current.getFieldValues().stream()
			.collect(Collectors.toMap(cv -> cv.getControl().getUserDefinedName(), cv -> cv));
		if (previous == null) {
			return new ArrayList<>(currValueMap.values());
		}

		List<ControlValue> dirtyFields = new ArrayList<>();
		for (ControlValue prevCv : previous.getFieldValues()) {
			ControlValue currCv = currValueMap.remove(prevCv.getControl().getUserDefinedName());
			if (currCv == null) {
				currCv = new ControlValue(prevCv.getControl(), null);
			}

			if (isModified(prevCv, currCv)) {
				dirtyFields.add(currCv);
			}
		}

		for (ControlValue currCv : currValueMap.values()) {
			Object value = currCv.getValue();

			if (value == null ||
				(value instanceof List && ((List<Object>)value).isEmpty()) ||
				(value.getClass().isArray() && ((Object[])value).length == 0)) {
				continue;
			}

			dirtyFields.add(currCv);
		}

		return dirtyFields;
	}

	@Override
	public void audit(UserContext userCtxt, Container form, List<ControlValue> dirtyFields, String operation, Long recordId) {
		try {
			audit(userCtxt, form, dirtyFields, operation, recordId, JdbcDaoFactory.getJdbcDao());
		} catch (Exception e) {
			throw new FormException("Error saving form audit data", e);
		} 
	}

	@Override
	public void audit(UserContext userCtxt, Container form, List<ControlValue> dirtyFields, String operation, Long recordId, JdbcDao jdbcDao) {
		FormAuditEvent formAuditEvent = new FormAuditEvent();
		formAuditEvent.setFormId(form.getId());
		formAuditEvent.setFormName(form.getName());
		formAuditEvent.setRecordId(recordId);
		formAuditEvent.setFormData(getAuditDataJson(userCtxt, form, recordId, dirtyFields));
		formAuditEvent.setIpAddress(userCtxt.getIpAddress());
		formAuditEvent.setUserId(userCtxt.getUserId());
		formAuditEvent.setOperation(operation != null ? operation : "");
	
		persist(formAuditEvent, jdbcDao);
	}

	private boolean isModified(ControlValue prevValue, ControlValue currValue) {
		if (prevValue == currValue) {
			return false;
		}

		if (prevValue == null || currValue == null) {
			return true;
		}

		if (prevValue.getControl() instanceof SubFormControl) {
			SubFormControl sfCtrl = (SubFormControl) prevValue.getControl();
			if (sfCtrl.isOneToOne()) {
				Map<String, Object> prevSfd = null;
				if (prevValue.getValue() != null) {
					prevSfd = ((FormData) prevValue.getValue()).getFieldNameValueMap(false);
				}

				Map<String, Object> currSfd = null;
				if (currValue.getValue() != null) {
					currSfd = ((FormData) currValue.getValue()).getFieldNameValueMap(false);
				}

				return !mapEquals(prevSfd, currSfd);
			} else {
				List<Map<String, Object>> prevSfdList = null;
				if (prevValue.getValue() != null) {
					prevSfdList = ((List<FormData>)prevValue.getValue()).stream()
						.map(fd -> fd.getFieldNameValueMap(false)).collect(Collectors.toList());
				}

				List<Map<String, Object>> currSfdList = null;
				if (currValue.getValue() != null) {
					currSfdList = ((List<FormData>)currValue.getValue()).stream()
						.map(fd -> fd.getFieldNameValueMap(false)).collect(Collectors.toList());
				}

				return !listMapEquals(prevSfdList, currSfdList);
			}
		} else if (prevValue.getControl() instanceof FileUploadControl) {
			FileControlValue prevFcv = (FileControlValue) prevValue.getValue();
			FileControlValue currFcv = (FileControlValue) currValue.getValue();
			return (prevFcv != currFcv) &&
				(
					prevFcv == null ||
					currFcv == null ||
					!Objects.equals(prevFcv.getContentType(), currFcv.getContentType()) ||
					!Objects.equals(prevFcv.getFileId(), currFcv.getFileId()) ||
					!Objects.equals(prevFcv.getFilename(), currFcv.getFilename())
				);
		} else if (prevValue.getControl() instanceof MultiSelectControl) {
			return !arrayEquals((Object[]) prevValue.getValue(), (Object[]) currValue.getValue());
		} else {
			return !valueEquals(prevValue.getValue(), currValue.getControl().toString(currValue.getValue()));
		}
	}

	private boolean valueEquals(Object value1, Object value2) {
		if (value1 == value2) {
			return true;
		}

		if (value1 == null) {
			return StringUtils.isBlank(value2.toString());
		} else if (value2 == null) {
			return StringUtils.isBlank(value1.toString());
		} else {
			return value1.toString().equals(value2.toString());
		}
	}

	private boolean arrayEquals(Object[] array1, Object[] array2) {
		if (array1 == array2) {
			return true;
		}

		if (array1 == null || array2 == null || array1.length != array2.length) {
			return false;
		}

		return listEquals(Arrays.asList(array1), Arrays.asList(array2));
	}

	private boolean listEquals(List<Object> list1, List<Object> list2) {
		if (list1 == list2) {
			return true;
		}

		if (list1 == null || list2 == null || list1.size() != list2.size()) {
			return false;
		}

		for (Object l1e : list1) {
			boolean matches = false;
			for (Object l2e : list2) {
				if (valueEquals(l1e, l2e)) {
					matches = true;
					break;
				}
			}

			if (!matches) {
				return false;
			}
		}

		return true;
	}

	private boolean mapEquals(Map<String, Object> map1, Map<String, Object> map2) {
		if (map1 == map2) {
			return true;
		}

		if (map1 == null || map2 == null || map1.size() != map2.size() || !map1.keySet().containsAll(map2.keySet())) {
			return false;
		}

		for (Map.Entry<String, Object> map1Kv : map1.entrySet()) {
			String prop = map1Kv.getKey();
			if (prop.equals("appData") || prop.equals("id") || prop.equals("containerId")) {
				continue;
			}

			Object map1Value = map1Kv.getValue();
			Object map2Value = map2.get(map1Kv.getKey());

			if (map1Value == map2Value) {
				continue;
			}

			if (map1Value == null || map2Value == null || !map1Value.getClass().equals(map2Value.getClass())) {
				return false;
			}

			if (map1Value instanceof List) {
				if (!listEquals((List<Object>) map1Value, (List<Object>) map2Value)) {
					return false;
				}
			} else if (map1Value.getClass().isArray()) {
				if (!arrayEquals((Object[]) map1Value, (Object[]) map2Value)) {
					return false;
				}
			} else if (map1Value instanceof Map) {
				if (!mapEquals((Map<String, Object>) map1Value, (Map<String, Object>) map2Value)) {
					return false;
				}
			} else if (!valueEquals(map1Value, map2Value)) {
				return false;
			}
		}

		return true;
	}

	private boolean listMapEquals(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
		if (list1 == list2) {
			return true;
		}

		if (list1 == null || list2 == null || list1.size() != list2.size()) {
			return false;
		}

		for (Map<String, Object> list1Map : list1) {
			boolean someMatch = false;

			Iterator<Map<String, Object>> list2Iter = list2.iterator();
			while (list2Iter.hasNext()) {
				Map<String, Object> list2Map = list2Iter.next();
				if (mapEquals(list1Map, list2Map)) {
					list2Iter.remove();
					someMatch = true;
					break;
				}
			}

			if (!someMatch) {
				return false;
			}
		}

		return true;
	}


	private String getAuditDataJson(UserContext userCtxt, Container form, Long recordId, List<ControlValue> fields) {
		try {
			Map<String, Object> data = new HashMap<>();
			data.put("name", form.getName());
			data.put("user", userCtxt != null ? userCtxt.getUserName() : "no-user");
			data.put("ipAddress", userCtxt != null ? userCtxt.getIpAddress() : "no-ip");
			data.put("recordId", recordId);
			data.put("data", getFieldSets(form, fields));

			return mapper.writeValueAsString(data);
		} catch (Exception e) {
			throw new FormException("Error generating audit event data JSON: " + e.getMessage(), e);
		}
	}

	private Map<String, Object> getFieldSets(Container form, Collection<ControlValue> fields) {
		Map<String, Object> result = new HashMap<>();
		result.put("formId", form.getId());
		result.put("formName", form.getName());
		result.put("dbTable", form.getDbTableName());

		List<Map<String, Object>> fieldProps = new ArrayList<>();
		for (ControlValue fieldValue : fields) {
			if (fieldValue == null) {
				continue;
			}

			Control ctrl = fieldValue.getControl();
			Object val = fieldValue.getValue();

			if (ctrl instanceof Label || ctrl instanceof PageBreak) {
				continue;
			}

			Map<String, Object> field = new HashMap<>();
			field.put("controlName", ctrl.getName());

			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl) ctrl;
				List<FormData> subFormsData = (List<FormData>) val;
				if (subFormsData == null) {
					continue;
				}

				List<Map<String, Object>> subRecords = new ArrayList<>();
				for (FormData subFormData : subFormsData) {
					subRecords.add(getFieldSets(sfCtrl.getSubContainer(), subFormData.getFieldValues()));
				}

				field.put("subRecords", subRecords);
			} else {
				if (ctrl instanceof MultiSelectControl) {
					MultiSelectControl msCtrl = (MultiSelectControl) ctrl;
					field.put("multiple", true);
					field.put("dbTable", msCtrl.getTableName());
				}

				field.put("dbColumn", ctrl.getDbColumnName());
				field.put("value", val);
			}

			fieldProps.add(field);
		}

		result.put("fields", fieldProps);
		return result;
	}

	private void persist(FormAuditEvent formAuditEvent, JdbcDao jdbcDao) {
		try {
			Long auditId = insertAndRetrieveAuditEvent(formAuditEvent, jdbcDao);
			formAuditEvent.setIdentifier(auditId);

			if (DbSettingsFactory.isOracle()) {
				insertFormAuditEventData(formAuditEvent, jdbcDao);
			}
		} catch (Exception e) {
			throw new FormException("Failed to persist audit data", e);
		}
	}


	private Long insertAndRetrieveAuditEvent(FormAuditEvent auditEvent, JdbcDao jdbcDao) 
	throws Exception {
		List<Object> params = new ArrayList<>();
		params.add(auditEvent.getIpAddress());
		params.add(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		params.add(auditEvent.getUserId());
		params.add(auditEvent.getOperation());
		params.add(auditEvent.getFormId());
		params.add(auditEvent.getFormName());
		params.add(auditEvent.getRecordId());

		String sql = INSERT_AUDIT_EVENT_SQL_ORA;
		if (DbSettingsFactory.isMySQL()) {
			sql = INSERT_AUDIT_EVENT_SQL_MSQL;
			params.add(auditEvent.getFormData());
		}

		Number key = jdbcDao.executeUpdateAndGetKey(sql, params, "IDENTIFIER");
		return key != null ? key.longValue() : null;
	}

	private void insertFormAuditEventData(FormAuditEvent formAuditEvent, JdbcDao jdbcDao)
	throws Exception {
		List<Object> params = new ArrayList<>();
		params.add(formAuditEvent.getIdentifier());
		
		Clob clob = jdbcDao.getResultSet(GET_AUDIT_DATA_BY_ID_SQL, params, (rs) -> {
			rs.next();
			return rs.getClob("FORM_DATA");
		});

		writeToClob(clob, formAuditEvent.getFormData());
	}
	
	private void writeToClob(Clob clob, String auditData) throws IOException {
		Writer clobOut = null;
		try {
			clobOut = clob.setCharacterStream(0);	
			clobOut.write(auditData);
		} catch (Exception e) {
			throw new FormException("Error writing clob", e);
		} finally {
			IOUtils.closeQuietly(clobOut);
		}
	}

	private static final String INSERT_AUDIT_EVENT_SQL_ORA =
			"INSERT INTO " +
			"  DYEXTN_AUDIT_EVENTS(IDENTIFIER, IP_ADDRESS, EVENT_TIMESTAMP, USER_ID, EVENT_TYPE, FORM_ID, FORM_NAME, RECORD_ID, FORM_DATA) " +
			"VALUES" +
			"  (DYEXTN_AUDIT_EVENTS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, empty_clob())";

	private static final String INSERT_AUDIT_EVENT_SQL_MSQL =
			"INSERT INTO " +
			"  DYEXTN_AUDIT_EVENTS(IDENTIFIER, IP_ADDRESS, EVENT_TIMESTAMP, USER_ID, EVENT_TYPE, FORM_ID, FORM_NAME, RECORD_ID, FORM_DATA) " +
			"VALUES" +
			"  (default, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String GET_AUDIT_DATA_BY_ID_SQL =
			"SELECT " +
			"  FORM_DATA " +
			"FROM " +
			"  DYEXTN_AUDIT_EVENTS " +
			"WHERE " +
			"  IDENTIFIER = ? " +
			"FOR UPDATE";
}
