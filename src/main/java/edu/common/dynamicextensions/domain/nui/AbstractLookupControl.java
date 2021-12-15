package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Writer;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;

public abstract class AbstractLookupControl extends Control implements LookupControl {
	private static final long serialVersionUID = 1L;
	
	private static final String LU_KEY_COLUMN = "IDENTIFIER";
	
	private static final String LU_VALUE_COLUMN = "NAME";
	
	private static final String IS_KEY_EXISTS_SQL = "select count(*) from %s where %s = ?";
	
	private static final String GET_KEY_BY_ALT_KEY = "select %s from %s where %s = ?";

	private boolean multiValued;

	private String collectionTable;

	private String collectionKey;

	private String parentKey;
	
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

	public abstract void getProps(Map<String, Object> props);
	
	public abstract String getTableName();		
	
	public abstract String getAltKeyColumn();

	@Override
	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
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
		this.collectionKey = collectionKey;
	}

	protected void serializeToXml(String field, Writer writer, Properties props) {
		writeElementStart(writer, field);
		super.serializeToXml(writer, props);
		writeElementEnd(writer, field);						
	}	

	@Override
	public ValidationStatus validate(Object value) {
		boolean empty = value == null || value.toString().trim().isEmpty();
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
						
		if (empty) {
			return ValidationStatus.OK;
		}
	
		if (!isValid(value)) {
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

		Long id = fromString(value.toString());
		if (id == null) {
			return null;
		}

		return getColumnValue(id);
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
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				query, 
				Collections.singletonList(value),
				new ResultExtractor<Long>() {
					@Override
					public Long extract(ResultSet rs) throws SQLException {
						return rs.next() ? rs.getLong(1) : null;						
					}
				});
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