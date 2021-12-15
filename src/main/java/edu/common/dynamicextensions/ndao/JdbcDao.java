package edu.common.dynamicextensions.ndao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import edu.common.dynamicextensions.nutility.IdGenerator;

public class JdbcDao {
	private JdbcTemplate jdbcTemplate;
	
	public JdbcDao(DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
	}

	public void setQueryTimeout(int queryTimeout) {
		jdbcTemplate.setQueryTimeout(queryTimeout);
	}
	
	public <T> T getResultSet(final String query, final List<?> params, final ResultExtractor<T> extractor) {
		Object[] paramArray = params != null ? params.toArray() : new Object[0];
		return jdbcTemplate.query(query, paramArray, new ResultSetExtractor<T>() {
			@Override
			public T extractData(ResultSet rs) 
			throws SQLException, DataAccessException {
				return extractor.extract(rs);
			}
		});
	}
	
	public int executeUpdate(String updateSql, List<?> params) {
		return jdbcTemplate.update(updateSql, params != null ? params.toArray() : new Object[0]);
	}
	
	public Number executeUpdateAndGetKey(final String sql, final List<?> params, final String keyCol) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {				
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
			throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{keyCol});
				if (params != null) {
					int i = 1;
					for (Object param : params) {
						pstmt.setObject(i++, param);
					}
				}
				return pstmt;
			}
		}, keyHolder);
			
		return keyHolder.getKey();
	}
					
	public void executeDDL(String ddl) {
		jdbcTemplate.execute(ddl);		
		// TODO: DDL are transactions on their own. How to deal with this.		
	}
	
	public Long getNextId(String tableName) {
		return IdGenerator.getInstance().getNextId(tableName);
	}		
	
	public int[] batchUpdate(final String sql, final List<Object[]> records) {
		return jdbcTemplate.batchUpdate(sql, records);
	}
}
