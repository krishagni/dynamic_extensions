package edu.common.dynamicextensions.ndao;

import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.ContainerInfo;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.nutility.IdGenerator;

public class ContainerDao {	
	private static final String INSERT_CONTAINER_SQL_MYSQL = 
			"INSERT INTO DYEXTN_CONTAINERS (IDENTIFIER, NAME, CAPTION, CREATED_BY, CREATE_TIME, XML) VALUES(?, ?, ?, ?, ?, ?)";

	private static final String INSERT_CONTAINER_SQL_ORACLE = 
			"INSERT INTO DYEXTN_CONTAINERS (IDENTIFIER, NAME, CAPTION, CREATED_BY, CREATE_TIME, XML) " +
			"VALUES(?, ?, ?, ?, ?, empty_blob())";

	private static final String INSERT_CONTAINER_AUD_SQL_MYSQL =
		"INSERT INTO DYEXTN_CONTAINERS_AUD " +
			"(REV, REV_TYPE, REV_BY, REV_TIME, IDENTIFIER, NAME, CAPTION, DELETED_ON, XML) " +
		"VALUES " +
			"(default, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String INSERT_CONTAINER_AUD_SQL_ORA =
		"INSERT INTO DYEXTN_CONTAINERS_AUD " +
			"(REV, REV_TYPE, REV_BY, REV_TIME, IDENTIFIER, NAME, CAPTION, DELETED_ON, XML) " +
		"VALUES " +
			"(DYEXTN_CONTAINERS_AUD_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?, empty_blob())";

	private static final String UPDATE_CONTAINER_SQL_MYSQL = 
			"UPDATE DYEXTN_CONTAINERS SET NAME = ?, CAPTION = ?, LAST_MODIFIED_BY = ?, LAST_MODIFY_TIME = ?, XML = ? " +
			"WHERE IDENTIFIER = ? AND DELETED_ON IS NULL";
	
	private static final String UPDATE_CONTAINER_SQL_ORACLE = 
			"UPDATE DYEXTN_CONTAINERS SET NAME = ?, CAPTION = ?, LAST_MODIFIED_BY = ?, LAST_MODIFY_TIME = ?, XML = empty_blob() " +
			"WHERE IDENTIFIER = ? AND DELETED_ON IS NULL";
	
	private static final String GET_CONTAINER_XML_BY_ID_SQL = "SELECT XML, CREATED_BY, CREATE_TIME, LAST_MODIFIED_BY, LAST_MODIFY_TIME"
			+ " FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ? AND DELETED_ON IS NULL";

	private static final String GET_CONTAINER_XML_AUD_BY_REV =
		"SELECT " +
			"XML " +
		"FROM " +
			"DYEXTN_CONTAINERS_AUD " +
		"WHERE " +
			"REV = ?";
	
	private static final String GET_CONTAINER_XML_BY_NAME_SQL = "SELECT XML, CREATED_BY, CREATE_TIME, LAST_MODIFIED_BY, LAST_MODIFY_TIME"
			+ " FROM DYEXTN_CONTAINERS WHERE NAME = ? AND DELETED_ON IS NULL";
	
	private static final String GET_CONTAINER_NAME_BY_ID =  
			"SELECT NAME FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ? AND DELETED_ON IS NULL";

	private static final String GET_CONTAINER_ID_BY_NAME =
			"SELECT IDENTIFIER FROM DYEXTN_CONTAINERS WHERE NAME = ? AND DELETED_ON IS NULL";
		
	private static final String GET_CONTAINER_INFO =
			"SELECT IDENTIFIER, NAME, CAPTION, CREATED_BY, CREATE_TIME, LAST_MODIFIED_BY, LAST_MODIFY_TIME " +
			"FROM DYEXTN_CONTAINERS " +
			"WHERE DELETED_ON IS NULL";
	
	private static final String GET_CONTAINER_INFO_BY_CREATOR_SQL = 
			"SELECT IDENTIFIER, NAME, CAPTION, CREATED_BY, CREATE_TIME, LAST_MODIFIED_BY, LAST_MODIFY_TIME " +
			"FROM DYEXTN_CONTAINERS WHERE CREATED_BY = ? AND DELETED_ON IS NULL";
	
	private static final String GET_LAST_UPDATED_TIME_SQL = 
			"SELECT LAST_MODIFY_TIME FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ? AND DELETED_ON IS NULL";
	
	private static final String DELETE_CONTAINER_SQL = "DELETE FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ?";
	
	private static final String SOFT_DELETE_CONTAINER_SQL = "UPDATE DYEXTN_CONTAINERS SET DELETED_ON = ? WHERE IDENTIFIER = ?";

	private JdbcDao jdbcDao = null;
	
	public ContainerDao(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	public List<Long> getContainerIds(int numIds) 
	throws Exception {
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < numIds; ++i) {
			ids.add(IdGenerator.getInstance().getNextId("DYEXTN_CONTAINERS"));
		}
		
		return ids;
	}
	
	public void insert(UserContext userCtxt, Container c) 
	throws SQLException {
		List<Object> params = new ArrayList<Object>();	
		params.add(c.getId());
		params.add(c.getName());
		params.add(c.getCaption());
		params.add(userCtxt != null ? userCtxt.getUserId() : null);
		Timestamp createTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		params.add(createTime);
		if (DbSettingsFactory.getProduct().equals("Oracle")) {
			jdbcDao.executeUpdate(INSERT_CONTAINER_SQL_ORACLE, params);	
			updateContainerXml(GET_CONTAINER_XML_BY_ID_SQL, c.getId(), c.toXml());
		} else {
			params.add(c.toXml());
			jdbcDao.executeUpdate(INSERT_CONTAINER_SQL_MYSQL, params);	
		}

		addContainerRev(0, c, userCtxt, createTime);
	}
	
	
	public void update(UserContext userCtxt, Container c) 
	throws SQLException {		
		List<Object> params = new ArrayList<Object>();				
		params.add(c.getName());
		params.add(c.getCaption());
		params.add(userCtxt != null ? userCtxt.getUserId() : null);
		Timestamp updateTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		params.add(updateTime);
		
		if (DbSettingsFactory.getProduct().equals("Oracle")) {
			params.add(c.getId());
			jdbcDao.executeUpdate(UPDATE_CONTAINER_SQL_ORACLE, params);
			updateContainerXml(GET_CONTAINER_XML_BY_ID_SQL, c.getId(), c.toXml());
		} else {
			params.add(c.toXml());
			params.add(c.getId());
			jdbcDao.executeUpdate(UPDATE_CONTAINER_SQL_MYSQL, params);	
		}

		addContainerRev(1, c, userCtxt, updateTime);
	}
	
	public boolean delete(UserContext userCtxt, Container c, boolean softDelete) {
		Integer rowsDeleted = null;
		
		try {
			Date deleteTime = Calendar.getInstance().getTime();
			List<Object> params = new ArrayList<>();
			String sql = null;
			if (softDelete) {
				sql = SOFT_DELETE_CONTAINER_SQL;
				params.add(deleteTime);
				params.add(c.getId());
			} else {
				sql = DELETE_CONTAINER_SQL;
				params.add(c.getId());
			}
			
			rowsDeleted = jdbcDao.executeUpdate(sql, params);
			if (rowsDeleted != null && rowsDeleted > 0) {
				addContainerRev(2, c, userCtxt, deleteTime);
			}
		} catch (Exception e) {
			throw new FormException("Error deleting form", e);
		}
		
		return rowsDeleted != null && rowsDeleted == 1;
	}
	
	public Container getById(Long id) throws SQLException {
		List<Object> params = new ArrayList<Object>();
		params.add(id);

		return jdbcDao.getResultSet(GET_CONTAINER_XML_BY_ID_SQL, params, new ResultExtractor<Container>() {
			@Override
			public Container extract(ResultSet rs) throws SQLException {
				if (!rs.next()) {
					return null;
				}
								
				Blob xmlBlob = rs.getBlob("XML");
				if (xmlBlob == null) {
					return null;
				}
				
				int length = (int) xmlBlob.length();
				String xml = new String(xmlBlob.getBytes(1L, length));
						
				Container container = Container.fromXml(xml);
				container.setCreatedBy(rs.getLong("CREATED_BY"));
				container.setCreationTime(rs.getTimestamp("CREATE_TIME"));
				container.setLastUpdatedBy(rs.getLong("LAST_MODIFIED_BY"));
				container.setLastUpdatedTime(rs.getTimestamp("LAST_MODIFY_TIME"));
				return container;
			}
		});
	}
	
	public List<ContainerInfo> getContainerInfo() 
	throws SQLException {
		return jdbcDao.getResultSet(GET_CONTAINER_INFO, null, containerInfoExtractor);
	}
	
	public List<ContainerInfo> getContainerInfoByCreator(Long creatorId) 
	throws SQLException {
		List<Object> params = new ArrayList<Object>();
		params.add(creatorId);
		return jdbcDao.getResultSet(GET_CONTAINER_INFO_BY_CREATOR_SQL, params, containerInfoExtractor);
	}

	private void addContainerRev(int revType, Container container, UserContext userCtx, Date opTime) {
		List<Object> params = new ArrayList<>();
		params.add(revType);
		params.add(userCtx != null ? userCtx.getUserId() : null);
		params.add(opTime);
		params.add(container.getId());
		params.add(container.getName());
		params.add(container.getCaption());
		params.add(revType == 2 ? opTime : null);

		if (DbSettingsFactory.isOracle()) {
			Number rev = jdbcDao.executeUpdateAndGetKey(INSERT_CONTAINER_AUD_SQL_ORA, params, "REV");
			updateContainerXml(GET_CONTAINER_XML_AUD_BY_REV, rev.longValue(), container.toXml());
		} else {
			params.add(container.toXml());
			jdbcDao.executeUpdate(INSERT_CONTAINER_AUD_SQL_MYSQL, params);
		}
	}

	private void updateContainerXml(String sql, Long id, final String xml) {
		jdbcDao.getResultSet(sql + " for update", Collections.singletonList(id), new ResultExtractor<Object>() {
			@Override
			public Object extract(ResultSet rs) throws SQLException {
				rs.next();
				Blob blob = rs.getBlob("XML");
				writeToBlob(blob, xml);								
				return null;
			}
		});		
	}
	
	private void writeToBlob(Blob blob, String xml) {
		try {
			OutputStream blobOut = blob.setBinaryStream(0);		
			blobOut.write(xml.getBytes());
			blobOut.close();
		} catch (Exception e) {
			throw new FormException("Error writing blob", e);
		}
	}

//	public List<NameValueBean> listAllContainerIdAndName() throws SQLException {
//		logger.info("containerDao: listAllContainer : " + LIST_ALL_CONTAINERS);
//		return jdbcDao.getResultSet(LIST_ALL_CONTAINERS, null, new ResultExtractor<List<NameValueBean>>() {
//			@Override
//			public List<NameValueBean> extract(ResultSet rs)
//			throws SQLException {
//				List<NameValueBean> beans = new ArrayList<NameValueBean>();					
//				while (rs.next()) {
//					beans.add(new NameValueBean(rs.getString("NAME"), rs.getLong("IDENTIFIER")));
//				}
//					
//				return beans;			
//			}				
//		});
//	}
	
	public Container getByName(String name) throws SQLException {
		List<Object> params = new ArrayList<Object>();
		params.add(name);
		return jdbcDao.getResultSet(GET_CONTAINER_XML_BY_NAME_SQL, params, new ResultExtractor<Container>() {
			@Override
			public Container extract(ResultSet rs) throws SQLException {
				if (!rs.next()) {
					return null;
				}
					
				Blob xmlBlob = rs.getBlob("XML");			
				if (xmlBlob == null) {
					return null;
				}
					
				int length = (int) xmlBlob.length();
				String xml = new String(xmlBlob.getBytes(1L, length));
				Container container = Container.fromXml(xml);
				container.setCreatedBy(rs.getLong("CREATED_BY"));
				container.setCreationTime(rs.getTimestamp("CREATE_TIME"));
				container.setLastUpdatedBy(rs.getLong("LAST_MODIFIED_BY"));
				container.setLastUpdatedTime(rs.getTimestamp("LAST_MODIFY_TIME"));
				return container;
			}
		});
	}
	
	public Date getLastUpdatedTime(Long id) throws SQLException {
		return jdbcDao.getResultSet(GET_LAST_UPDATED_TIME_SQL, Collections.singletonList(id), new ResultExtractor<Date>() {
			@Override
			public Date extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getTimestamp("LAST_MODIFY_TIME") : null;
			}				
		});
	}

	public String getNameById(Long id) throws SQLException {
		return jdbcDao.getResultSet(GET_CONTAINER_NAME_BY_ID, Collections.singletonList(id), new ResultExtractor<String>() {
			@Override
			public String extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getString("NAME") : null;
			}				
		});
	}

	public Long getIdByName(String name) throws SQLException {
		return jdbcDao.getResultSet(GET_CONTAINER_ID_BY_NAME, Collections.singletonList(name), new ResultExtractor<Long>() {
			@Override
			public Long extract(ResultSet rs) throws SQLException {
				return rs.next() ? rs.getLong("IDENTIFIER") : null;
			}
		});
	}
	
	private static ResultExtractor<List<ContainerInfo>> containerInfoExtractor = new ResultExtractor<List<ContainerInfo>>() {
		@Override
		public List<ContainerInfo> extract(ResultSet rs)
		throws SQLException {
			List<ContainerInfo> result = new ArrayList<ContainerInfo>();
				
			while (rs.next()) {
				ContainerInfo containerInfo = new ContainerInfo();
				containerInfo.setId(rs.getLong("IDENTIFIER"));
				containerInfo.setName(rs.getString("NAME"));
				containerInfo.setCaption(rs.getString("CAPTION"));
				containerInfo.setCreatedBy(rs.getLong("CREATED_BY"));
				containerInfo.setCreationTime(rs.getTimestamp("CREATE_TIME"));
				containerInfo.setLastUpdatedBy(rs.getLong("LAST_MODIFIED_BY"));
				containerInfo.setLastUpdatedTime(rs.getTimestamp("LAST_MODIFY_TIME"));
					
				result.add(containerInfo);				
			}
				
			return result;
		}				
	};
}
