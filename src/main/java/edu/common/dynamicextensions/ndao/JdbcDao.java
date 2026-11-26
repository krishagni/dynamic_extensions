package edu.common.dynamicextensions.ndao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceUtils;
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

	public void setFetchSize(int fetchSize) {
		if (DbSettingsFactory.isMySQL()) {
//			System.out.println("Setting the result set fetch size: " + fetchSize);
			jdbcTemplate.setFetchSize(fetchSize);
		}
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

	public void executeDDLWithoutForeignKeyChecks(String ddl) {
		jdbcTemplate.execute((ConnectionCallback<Void>) conn -> {
			int foreignKeyChecks = getForeignKeyChecks(conn);
			Exception ddlError = null;

			try {
				executeDDL(conn, "SET FOREIGN_KEY_CHECKS = 0");
				executeDDL(conn, ddl);
			} catch (SQLException | RuntimeException t) {
				ddlError = t;
				throw t;
			} finally {
				try {
					executeDDL(conn, "SET FOREIGN_KEY_CHECKS = " + foreignKeyChecks);
				} catch (SQLException | RuntimeException restoreError) {
					invalidateConnection(conn, restoreError);
					if (ddlError != null) {
						ddlError.addSuppressed(restoreError);
					} else {
						throw restoreError;
					}
				}
			}

			return null;
		});
	}

	public boolean hasMoreRowsThan(String tableName, int rowCount) {
		String query = String.format("SELECT 1 FROM %s LIMIT 1 OFFSET %d", tableName, rowCount);
		return getResultSet(query, null, ResultSet::next);
	}
	
	public Long getNextId(String tableName) {
		return IdGenerator.getInstance().getNextId(tableName);
	}		
	
	public int[] batchUpdate(final String sql, final List<Object[]> records) {
		return jdbcTemplate.batchUpdate(sql, records);
	}

	private int getForeignKeyChecks(Connection conn) throws SQLException {
		try (
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT @@FOREIGN_KEY_CHECKS")
		) {
			rs.next();
			return rs.getInt(1);
		}
	}

	private void executeDDL(Connection conn, String ddl) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(ddl);
		}
	}

	private void invalidateConnection(Connection conn, Exception restoreError) {
		Connection targetConn = DataSourceUtils.getTargetConnection(conn);
		boolean discarded = discardTomcatConnection(targetConn, restoreError);
		if (!discarded) {
			try {
				targetConn.abort(Runnable::run);
			} catch (SQLException | RuntimeException abortError) {
				restoreError.addSuppressed(abortError);
			}
		}

		try {
			targetConn.close();
		} catch (SQLException | RuntimeException closeError) {
			restoreError.addSuppressed(closeError);
		}
	}

	private boolean discardTomcatConnection(Connection conn, Exception restoreError) {
		try {
			Class<?> pooledConnectionClass = Class.forName("org.apache.tomcat.jdbc.pool.PooledConnection");
			Object pooledConnection = conn.unwrap(pooledConnectionClass);
			Method setDiscarded = pooledConnectionClass.getMethod("setDiscarded", boolean.class);
			setDiscarded.invoke(pooledConnection, true);
			return true;
		} catch (ReflectiveOperationException | SQLException | RuntimeException e) {
			restoreError.addSuppressed(e);
			return false;
		}
	}
}
