package edu.common.dynamicextensions.ndao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.VersionedContainerInfo;
import edu.common.dynamicextensions.napi.FormException;

public class VersionedContainerDao {
	private JdbcDao jdbcDao;
	
	public VersionedContainerDao(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	public String getFormName(Long formId) {
		List<Long> params = Collections.singletonList(formId);
		return jdbcDao.getResultSet(GET_FORM_NAME_BY_FORM_ID_SQL, params, new ResultExtractor<String>() {
			@Override
			public String extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getString("FORM_NAME") : null;
			}				
		});
	}
	
	public Long getFormId(String formName) {
		List<String> params = Collections.singletonList(formName);
		return jdbcDao.getResultSet(GET_FORM_ID_BY_FORM_NAME_SQL, params, new ResultExtractor<Long>() {
			@Override
			public Long extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getLong("IDENTIFIER") : null;
			}			
		});
	}
	
	public Long getFormIdByDraftContainerId(Long draftContainerId) {
		List<Long> params = Collections.singletonList(draftContainerId);
		return jdbcDao.getResultSet(GET_FORM_ID_BY_DRAFT_CONTAINER_ID_SQL, params, new ResultExtractor<Long>() {
			@Override
			public Long extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getLong("IDENTIFIER") : null;
			}				
		});
	}
	
	
	public VersionedContainerInfo getDraftContainerInfo(final Long formId) {
		return jdbcDao.getResultSet(GET_DRAFT_CONTAINER_INFO_SQL, Collections.singletonList(formId), 
				new ResultExtractor<VersionedContainerInfo>() {
					@Override
					public VersionedContainerInfo extract(ResultSet rs)
					throws SQLException {
						return rs.next() ? getInfo(formId, "draft", rs) : null;
					}			
				});
	}
	
	public List<VersionedContainerInfo> getPublishedContainersInfo(final Long formId) {
		return jdbcDao.getResultSet(GET_PUBLISHED_CONTAINER_INFO_SQL, Collections.singletonList(formId),
				new ResultExtractor<List<VersionedContainerInfo>>() {
					@Override
					public List<VersionedContainerInfo> extract(ResultSet rs)
					throws SQLException {
						List<VersionedContainerInfo> result = new ArrayList<VersionedContainerInfo>();
						while (rs.next()) {
							result.add(getInfo(formId, "published", rs));
						}							
						return result;
					}				
				});
	}
	
	public List<VersionedContainerInfo> getPublishedContainersInfo(String formName) {
		return jdbcDao.getResultSet(GET_PUBLISHED_CONTAINER_INFO_BY_FORM_NAME_SQL, Collections.singletonList(formName),
				new ResultExtractor<List<VersionedContainerInfo>>() {
					@Override
					public List<VersionedContainerInfo> extract(ResultSet rs)
					throws SQLException {
						List<VersionedContainerInfo> result = new ArrayList<VersionedContainerInfo>();
						while (rs.next()) {
							result.add(getInfo(rs.getLong("IDENTIFIER"), "published", rs));
						}
							
						return result;
					}				
				});			
	}
	
	public void insertVersionedContainerInfo(VersionedContainerInfo info) {
		
		try {
			List<Object> params = new ArrayList<Object>();

			params.add(info.getFormId());
			params.add(info.getContainerId());
			params.add(info.getActivationDate());
			params.add(info.getCreatedBy());
			params.add(info.getCreationTime());
			params.add(info.getStatus());
			
			jdbcDao.executeUpdate(INSERT_VERSIONED_CONTAINER_INFO_SQL, params);
		
		} catch (Exception e) {
			throw new FormException("Error inserting versioned container info", e);
		}
	}
	
	private VersionedContainerInfo getInfo(Long formId, String status, ResultSet rs) 
	throws SQLException {
		VersionedContainerInfo info = new VersionedContainerInfo();
		info.setFormId(formId);
		info.setContainerId(rs.getLong("CONTAINER_ID"));
		info.setActivationDate(rs.getDate("ACTIVATION_DATE"));
		info.setCreatedBy(rs.getLong("CREATED_BY"));
		info.setCreationTime(rs.getDate("CREATE_TIME"));		
		info.setStatus(status);
		return info;		
	}
		
	public Long insertFormInfo(String formName) {
		try {
			Long formId = jdbcDao.getResultSet(GET_NEXT_ID_SQL, null, new ResultExtractor<Long>() {
				@Override
				public Long extract(ResultSet rs) throws SQLException {
					return rs.next() ? rs.getLong(1) : null;
				}				
			});
		
			List<Object> params = new ArrayList<Object>();
			params.add(formId);
			params.add(formName);			
			jdbcDao.executeUpdate(INSERT_FORM_INFO_SQL, params);
			
			return formId;
		} catch (Exception e) {
			throw new FormException("Error inserting versioned container info", e);
		}
	}
	
	private final static String GET_DRAFT_CONTAINER_INFO_SQL = 
			"SELECT CONTAINER_ID, ACTIVATION_DATE, CREATED_BY, CREATE_TIME " +
			"FROM DYEXTN_VERSIONED_CONTAINERS " +
			"WHERE IDENTIFIER = ? AND STATUS = 'draft'";
	
	private final static String GET_PUBLISHED_CONTAINER_INFO_SQL =
			"SELECT CONTAINER_ID, ACTIVATION_DATE, CREATED_BY, CREATE_TIME " +
			"FROM DYEXTN_VERSIONED_CONTAINERS " +
			"WHERE IDENTIFIER = ? AND STATUS = 'published' " +
			"ORDER BY ACTIVATION_DATE";
	
	private final static String GET_PUBLISHED_CONTAINER_INFO_BY_FORM_NAME_SQL =
			"SELECT VC.IDENTIFIER, CONTAINER_ID, ACTIVATION_DATE, CREATED_BY, CREATE_TIME " +
			"FROM DYEXTN_VERSIONED_CONTAINERS VC " +
			"INNER JOIN DYEXTN_FORMS DF ON VC.IDENTIFIER = DF.IDENTIFIER " +
			"WHERE FORM_NAME = ? AND STATUS = 'published' " +
			"ORDER BY ACTIVATION_DATE";
	
	private final static String INSERT_VERSIONED_CONTAINER_INFO_SQL = 
			"INSERT INTO DYEXTN_VERSIONED_CONTAINERS (IDENTIFIER, CONTAINER_ID, ACTIVATION_DATE, CREATED_BY, CREATE_TIME, STATUS) " +
			"VALUES (?, ?, ?, ?, ?, ?)";
	
	private final static String GET_FORM_ID_BY_DRAFT_CONTAINER_ID_SQL =
			"SELECT IDENTIFIER FROM DYEXTN_VERSIONED_CONTAINERS WHERE CONTAINER_ID = ? AND STATUS = 'draft'";
	
	private final static String INSERT_FORM_INFO_SQL = 
			"INSERT INTO DYEXTN_FORMS (IDENTIFIER, FORM_NAME) VALUES (?, ?)";
	
	private final static String GET_FORM_ID_BY_FORM_NAME_SQL =
			"SELECT IDENTIFIER FROM DYEXTN_FORMS WHERE FORM_NAME = ? ";
	
	private final static String GET_FORM_NAME_BY_FORM_ID_SQL =
			"SELECT FORM_NAME FROM DYEXTN_FORMS WHERE IDENTIFIER = ?";
	
	private final static String GET_NEXT_ID_SQL = 
			"SELECT DYEXTN_FORMS_SEQ.NEXTVAL FROM DUAL";

}