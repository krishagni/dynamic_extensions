package edu.common.dynamicextensions.nutility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;

public class IdGenerator {
	private class IdRange {
		private Long nextId;

		private Long maxId;

		public IdRange(Long nextId, Long maxId) {
			this.nextId = nextId;
			this.maxId = maxId;
		}
	};

	private Map<String, IdRange> identifiers = new HashMap<String, IdRange>();

	private Long rangeSize = 25L;

	private static final IdGenerator instance = new IdGenerator();

	private static final String SELECT_LAST_ID = "SELECT LAST_ID FROM DYEXTN_ID_SEQ WHERE TABLE_NAME = ? FOR UPDATE";

	private static final String UPDATE_LAST_ID = "UPDATE DYEXTN_ID_SEQ SET LAST_ID = ? WHERE TABLE_NAME = ?";

	private static final String INSERT_LAST_ID = "INSERT INTO DYEXTN_ID_SEQ VALUES(?, ?)";

	private static final String SELECT_MAX_ID = "SELECT MAX(IDENTIFIER) FROM %s";

	private IdGenerator() {
	}

	public static IdGenerator getInstance() {
		return instance;
	}

	public synchronized Long getNextId(String tableName) {
		try {
			IdRange idRange = identifiers.get(tableName);
			if (idRange == null || idRange.nextId > idRange.maxId) {				
				idRange = getIdSliceFromDb(tableName);
				if (idRange == null) {
					throw new FormException("Couldn't obtain ID slice DB for table "	+ tableName);
				}

				identifiers.put(tableName, idRange);
			}

			Long id = idRange.nextId;
			idRange.nextId++;
			return id;
		} catch (Exception e) {
			throw new FormException("Error obtaining next ID for "
					+ tableName, e);
		}
	}

	private IdRange getIdSliceFromDb(String tableName) {
		Transaction txn = TransactionManager.getInstance().newTxn();

		try {
			IdRange idRange = getIdSliceFromDb0(tableName);
			if (idRange == null) {
				idRange = getFreshIdSliceFromDb(tableName);
			}

			TransactionManager.getInstance().commit(txn);
			return idRange;
		} catch (Exception e) {
			TransactionManager.getInstance().rollback(txn);
			throw new FormException("Error obtaining id slice from db for table: " + tableName);
		}
	}

	private IdRange getIdSliceFromDb0(String tableName) {
		Long lastId = getLastId(tableName);
		if (lastId == null) {
			return null;
		}

		updateLastId(tableName, lastId + rangeSize);
		return new IdRange(lastId + 1, lastId + rangeSize);
	}

	private IdRange getFreshIdSliceFromDb(String tableName) {
		Long maxId = getMaxId(tableName);
		if (maxId == null) {
			maxId = 0L;
		}

		insertLastId(tableName, maxId);
		return getIdSliceFromDb0(tableName);
	}

	private Long getLastId(String tableName) {
		List<String> params = Collections.singletonList(tableName);
		return JdbcDaoFactory.getJdbcDao().getResultSet(SELECT_LAST_ID, params,
				new ResultExtractor<Long>() {
					@Override
					public Long extract(ResultSet rs) throws SQLException {
						return rs == null || !rs.next() ? null : rs.getLong(1);
					}
				});
	}

	private Long getMaxId(String tableName) {
		String sql = String.format(SELECT_MAX_ID, tableName);
		return JdbcDaoFactory.getJdbcDao().getResultSet(sql, null,
				new ResultExtractor<Long>() {
					@Override
					public Long extract(ResultSet rs) throws SQLException {
						return rs == null || !rs.next() ? null : rs.getLong(1);
					}
				});
	}

	private void insertLastId(String tableName, Long lastId) {
		updateLastId(tableName, lastId, true);
	}

	private void updateLastId(String tableName, Long lastId) {
		updateLastId(tableName, lastId, false);
	}

	private void updateLastId(String tableName, Long lastId, boolean insert) {
		if (insert) {
			List<Object> params = new ArrayList<Object>();
			params.add(tableName);
			params.add(lastId);
			JdbcDaoFactory.getJdbcDao().executeUpdate(INSERT_LAST_ID, params);
		} else {
			List<Object> params = new ArrayList<Object>();
			params.add(lastId);
			params.add(tableName);
			JdbcDaoFactory.getJdbcDao().executeUpdate(UPDATE_LAST_ID, params);
		}
	}
}