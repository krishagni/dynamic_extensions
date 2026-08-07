package edu.common.dynamicextensions.domain.nui;

import java.io.Writer;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.nutility.XmlUtil;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

public abstract class AbstractLookupControl extends Control implements LookupControl {
	private static final long serialVersionUID = 1L;

	private static final String COLLECTION_VALUE_COLUMN = "VALUE";
	
	private static final String LU_KEY_COLUMN = "IDENTIFIER";
	
	private static final String LU_VALUE_COLUMN = "NAME";
	
	private static final String IS_KEY_EXISTS_SQL = "select count(*) from %s where %s = ?";
	
	private static final String GET_KEY_BY_ALT_KEY = "select %s from %s where %s = ?";

	private boolean multiValued;

	private String collectionTable;

	private String collectionKey = "RECORD_ID";

	private String parentKey = "IDENTIFIER";
	
	@Override
	public DataType getDataType() {
		return DataType.INTEGER;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		ColumnDef def = ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getIntegerColType());
		def.setRefTable(getTableName());
		def.setRefColumn(getLookupKey());
		return Collections.singletonList(def);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		
		try {
			return new BigDecimal(value).longValueExact();
		} catch (Exception e) {
			return getKeyByAltKey(value);
		}
	}

	@Override
	public final void getProps(Map<String, Object> props) {
		props.put("multiple", isMultiValued());
		getLookupProps(props);
	}

	protected abstract void getLookupProps(Map<String, Object> props);
	
	public abstract String getTableName();		
	
	public abstract String getAltKeyColumn();

	@Override
	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = StringUtils.defaultIfBlank(parentKey, "IDENTIFIER");
	}

	@Override
	public String getLookupKey() {
		return LU_KEY_COLUMN;
	}

	@Override
	public String getValueColumn() {
		return LU_VALUE_COLUMN;
	}

	@Override
	public DataType getValueType() {
		return DataType.STRING;
	}

	@Override
	public abstract Properties getPvSourceProps();

	@Override
	public boolean isMultiValued() {
		return multiValued;
	}

	public void setMultiValued(boolean multiValued) {
		this.multiValued = multiValued;
	}

	@Override
	public String getCollectionTable() {
		return collectionTable;
	}

	public void setCollectionTable(String collectionTable) {
		this.collectionTable = collectionTable;
	}

	@Override
	public String getCollectionKey() {
		return collectionKey;
	}

	public void setCollectionKey(String collectionKey) {
		this.collectionKey = StringUtils.defaultIfBlank(collectionKey, "RECORD_ID");
	}

	@Override
	public String getCollectionValueColumn() {
		return getContainer() != null && getContainer().isManagedTables()
			? getDbColumnName()
			: COLLECTION_VALUE_COLUMN;
	}

	@Override
	public List<ColumnDef> getCollectionColumnDefs() {
		List<ColumnDef> columns = new ArrayList<>();
		ColumnDef valueColumn = ColumnDef.get(getCollectionValueColumn(), ColumnTypeHelper.getIntegerColType() + " NOT NULL");
		valueColumn.setRefTable(getTableName());
		valueColumn.setRefColumn(getLookupKey());

		columns.add(valueColumn);
		columns.add(ColumnDef.get(getCollectionKey(), ColumnTypeHelper.getIntegerColType()));
		return columns;
	}

	protected void serializeToXml(String field, Writer writer, Properties props) {
		writeElementStart(writer, field);
		super.serializeToXml(writer, props);
		serializeLookupProps(writer);
		writeElementEnd(writer, field);						
	}

	protected void serializeLookupProps(Writer writer) {
		XmlUtil.writeElement(writer, "multiple", multiValued);
	}

	@Override
	public ValidationStatus validate(Object value) {
		if (multiValued) {
			Collection<?> values;
			if (value == null) {
				values = Collections.emptyList();
			} else if (value instanceof Collection) {
				values = (Collection<?>) value;
			} else if (value.getClass().isArray()) {
				values = Arrays.asList((Object[]) value);
			} else {
				return ValidationStatus.INVALID_VALUE;
			}

			if (isMandatory() && values.stream().allMatch(this::isEmptyValue)) {
				return ValidationStatus.NULL_OR_EMPTY;
			}

			for (Object element : new LinkedHashSet<>(values)) {
				if (!isEmptyValue(element) && !isValid(element)) {
					return ValidationStatus.INVALID_VALUE;
				}
			}

			return ValidationStatus.OK;
		}

		boolean empty = isEmptyValue(value);
		if (!empty) {
			Long id = fromString(value.toString());
			if (id == null) {
				return ValidationStatus.INVALID_VALUE;
			}
			
			empty = id.equals(-1L);
		}
		
		if (isMandatory() && empty) {
			return ValidationStatus.NULL_OR_EMPTY;
		}
						
		if (!empty && !isValid(value)) {
			return ValidationStatus.INVALID_VALUE;
		}
		
		return ValidationStatus.OK;
	}

	@Override
	public String getCodeColumn() {
		return null;
	}

	@Override
	public String toDisplayValue(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Collection<?> values) {
			return values.stream()
				.map(this::toDisplayValue)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(", "));
		} else if (value instanceof Object[] values) {
			return Arrays.stream(values)
				.map(this::toDisplayValue)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(", "));
		}

		Long id = fromString(value.toString());
		return id != null ? getColumnValue(id) : null;
	}

	public Object getValue(Object input) {
		if (input instanceof Map) {
			Map<String, Object> fields = (Map<String, Object>) input;
			if (fields.get("id") != null) {
				return fields.get("id").toString();
			} else if (fields.get("name") != null) {
				return fields.get("name").toString();
			}
		}

		return input;
	}

	private boolean isEmptyValue(Object value) {
		return value == null || value.toString().trim().isEmpty() || "-1".equals(value.toString());
	}

	private boolean isValid(Object value) {
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				String.format(IS_KEY_EXISTS_SQL, getTableName(), getLookupKey()), 
				Collections.singletonList(fromString(value.toString())), 
				new ResultExtractor<Boolean>() {
					@Override
					public Boolean extract(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getLong(1) > 0;
					}
				});
	}	
	
	private Long getKeyByAltKey(String value) {
		String query = String.format(GET_KEY_BY_ALT_KEY, getLookupKey(), getTableName(), getAltKeyColumn());
		Long result = JdbcDaoFactory.getJdbcDao().getResultSet(
				query, 
				Collections.singletonList(value),
				rs -> rs.next() ? rs.getLong(1) : null);

		if (result == null && !getAltKeyColumn().equalsIgnoreCase(getValueColumn())) {
			query = String.format(GET_KEY_BY_ALT_KEY, getLookupKey(), getTableName(), getValueColumn());
			result = JdbcDaoFactory.getJdbcDao().getResultSet(
				query,
				Collections.singletonList(value),
				rs -> rs.next() ? rs.getLong(1) : null
			);
		}
		return result;
	}

	private String getColumnValue(Long id) {
		String query = String.format(GET_KEY_BY_ALT_KEY, getValueColumn(),  getTableName(), getLookupKey());
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				query,
				Collections.singletonList(id),
				new ResultExtractor<String>() {
					@Override
					public String extract(ResultSet rs) throws SQLException {
						rs.next();
						return rs.getString(1);
					}
				});
	}
}
