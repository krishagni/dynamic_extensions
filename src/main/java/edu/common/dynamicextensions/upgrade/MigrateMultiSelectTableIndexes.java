package edu.common.dynamicextensions.upgrade;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.nutility.LogUtil;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

public class MigrateMultiSelectTableIndexes implements CustomTaskChange {
	private static final LogUtil logger = LogUtil.getLogger(MigrateMultiSelectTableIndexes.class);

	@Override
	public String getConfirmationMessage() {
		return "Multi-select field table indexes migrated successfully";
	}

	@Override
	public void setUp() throws SetupException {

	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {

	}

	@Override
	public ValidationErrors validate(Database database) {
		return null;
	}

	@Override
	public void execute(Database database) throws CustomChangeException {
		try {
			JdbcConnection jdbcConn = (JdbcConnection) database.getConnection();
			Connection conn = jdbcConn.getUnderlyingConnection();
			boolean oracle = jdbcConn.getDatabaseProductName().toLowerCase().contains("oracle");

			migrateTables(conn, oracle);
		} catch (Exception e) {
			throw new CustomChangeException("Error migrating multi-select field table indexes", e);
		}
	}

	private void migrateTables(Connection conn, boolean oracle) throws Exception {
		Set<String> migratedTables = new HashSet<>();
		int formsCount = 0;
		int failedFormsCount = 0;
		int tablesCount = 0;
		int migratedTablesCount = 0;
		int alreadyMigratedTablesCount = 0;
		int skippedTablesCount = 0;
		int failedTablesCount = 0;

		logger.info("Starting migration of multi-select field table indexes");
		try (
			PreparedStatement stmt = conn.prepareStatement(GET_CONTAINERS_SQL);
			ResultSet rs = stmt.executeQuery()
		) {
			while (rs.next()) {
				Long formId = rs.getLong("IDENTIFIER");
				String formName = rs.getString("NAME");
				++formsCount;

				Set<String> tableNames = getMultiSelectTables(rs, formId, formName);
				if (tableNames == null) {
					++failedFormsCount;
					continue;
				}

				for (String tableName : tableNames) {
					if (!migratedTables.add(tableName)) {
						logger.info("Skipping already migrated multi-select table: " + tableName);
						continue;
					}

					++tablesCount;
					switch (migrateTableInTxn(conn, oracle, tableName)) {
						case MIGRATED -> ++migratedTablesCount;
						case ALREADY_MIGRATED -> ++alreadyMigratedTablesCount;
						case SKIPPED -> ++skippedTablesCount;
						case FAILED -> ++failedTablesCount;
					}
				}
			}
		}

		logger.info(
			"Completed migration of multi-select field table indexes. Forms processed = " + formsCount +
			", form metadata failures = " + failedFormsCount +
			", tables discovered = " + tablesCount +
			", tables migrated = " + migratedTablesCount +
			", tables already migrated = " + alreadyMigratedTablesCount +
			", tables skipped = " + skippedTablesCount +
			", table failures = " + failedTablesCount);

		if (failedTablesCount > 0) {
			throw new CustomChangeException("Failed to migrate indexes for " + failedTablesCount + " multi-select field table(s)");
		}
	}

	private Set<String> getMultiSelectTables(ResultSet rs, Long formId, String formName) {
		Set<String> result = new HashSet<>();
		try {
			logger.info("Reading multi-select field metadata for form: " + formName + " [" + formId + "]");

			String xml = getXml(rs);
			if (xml == null || xml.trim().isEmpty()) {
				logger.warn("Skipping form with empty metadata: " + formName + " [" + formId + "]");
				return result;
			}

			Container container = Container.fromXml(xml);
			if (container == null || container.isManagedTables()) {
				return result;
			}

			for (Control control : container.getAllControls(true)) {
				if (control instanceof MultiSelectControl) {
					String tableName = ((MultiSelectControl) control).getTableName();
					if (tableName != null && !tableName.trim().isEmpty()) {
						result.add(tableName);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error reading multi-select field metadata for form: " + formName + " [" + formId + "]", e);
			return null;
		}

		return result;
	}

	private String getXml(ResultSet rs) throws SQLException {
		Blob xmlBlob = rs.getBlob("XML");
		if (xmlBlob == null) {
			return null;
		}

		int length = (int) xmlBlob.length();
		return new String(xmlBlob.getBytes(1L, length));
	}

	private MigrationStatus migrateTableInTxn(Connection conn, boolean oracle, String tableName) throws SQLException {
		boolean autoCommit = conn.getAutoCommit();
		try {
			logger.info("Starting migration of multi-select table: " + tableName);
			conn.setAutoCommit(false);
			MigrationStatus status = migrateTable(conn, oracle, tableName);
			conn.commit();
			logger.info("Completed migration of multi-select table: " + tableName + "; status = " + status);
			return status;
		} catch (Exception e) {
			rollback(conn, tableName);
			logger.error("Error migrating multi-select table: " + tableName, e);
			return MigrationStatus.FAILED;
		} finally {
			conn.setAutoCommit(autoCommit);
		}
	}

	private MigrationStatus migrateTable(Connection conn, boolean oracle, String tableName) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
		if (!tableExists(md, tableName) || !columnExists(md, tableName, "RECORD_ID") || !columnExists(md, tableName, "VALUE")) {
			logger.warn("Skipping multi-select table as it does not exist or lacks RECORD_ID/VALUE columns: " + tableName);
			return MigrationStatus.SKIPPED;
		}

		if (indexExists(md, tableName, tableName + "_UQ")) {
			logger.info("Skipping already migrated multi-select table: " + tableName);
			return MigrationStatus.ALREADY_MIGRATED;
		}

		logger.info("Cleaning invalid rows from multi-select table: " + tableName);
		removeInvalidRows(conn, md, tableName);

		logger.info("Deduplicating rows in multi-select table: " + tableName);
		deduplicateRows(conn, oracle, tableName);

		logger.info("Adding VALUE not-null constraint to multi-select table: " + tableName);
		addNotNullConstraint(conn, md, oracle, tableName);

		logger.info("Dropping old RECORD_ID index on multi-select table: " + tableName);
		dropIndex(conn, md, oracle, tableName, tableName + "_RECORD_IDX");

		logger.info("Creating unique index on (RECORD_ID, VALUE) for multi-select table: " + tableName);
		createUniqueIndex(conn, md, tableName, tableName + "_UQ");
		return MigrationStatus.MIGRATED;
	}

	private void removeInvalidRows(Connection conn, DatabaseMetaData md, String tableName) throws SQLException {
		executeUpdate(conn, "delete from " + tableName + " where VALUE is null");
		if (isStringColumn(md, tableName, "VALUE")) {
			executeUpdate(conn, "delete from " + tableName + " where trim(VALUE) = ''");
		}
	}

	private void addNotNullConstraint(Connection conn, DatabaseMetaData md, boolean oracle, String tableName)
	throws SQLException {
		if (!isNullableColumn(md, tableName, "VALUE")) {
			return;
		}

		if (oracle) {
			execute(conn, "alter table " + tableName + " modify VALUE not null");
		} else {
			execute(conn, "alter table " + tableName + " modify VALUE " + getColumnType(md, tableName, "VALUE") + " not null");
		}
	}

	private void deduplicateRows(Connection conn, boolean oracle, String tableName) throws SQLException {
		String tmpTable = "TMP_" + tableName;
		dropTable(conn, oracle, tmpTable);

		try {
			String createSql = oracle ?
				"create global temporary table " + tmpTable + " on commit preserve rows as " +
					"select distinct RECORD_ID, VALUE from " + tableName :
				"create temporary table " + tmpTable + " as select distinct RECORD_ID, VALUE from " + tableName;

			execute(conn, createSql);
			executeUpdate(conn, "delete from " + tableName);
			executeUpdate(conn, "insert into " + tableName + " (RECORD_ID, VALUE) select RECORD_ID, VALUE from " + tmpTable);
		} finally {
			dropTable(conn, oracle, tmpTable);
		}
	}

	private void dropIndex(Connection conn, DatabaseMetaData md, boolean oracle, String tableName, String indexName)
	throws SQLException {
		if (!indexExists(md, tableName, indexName)) {
			return;
		}

		execute(conn, oracle ? "drop index " + indexName : "drop index " + indexName + " on " + tableName);
	}

	private void createUniqueIndex(Connection conn, DatabaseMetaData md, String tableName, String indexName)
	throws SQLException {
		if (!indexExists(md, tableName, indexName)) {
			execute(conn, "create unique index " + indexName + " on " + tableName + "(RECORD_ID, VALUE)");
		}
	}

	private boolean tableExists(DatabaseMetaData md, String tableName) throws SQLException {
		return objectExists(name -> md.getTables(null, null, name, new String[] { "TABLE" }), tableName);
	}

	private boolean columnExists(DatabaseMetaData md, String tableName, String columnName) throws SQLException {
		return objectExists(name -> md.getColumns(null, null, tableName, name), columnName) ||
			objectExists(name -> md.getColumns(null, null, tableName.toUpperCase(), name), columnName);
	}

	private boolean indexExists(DatabaseMetaData md, String tableName, String indexName) throws SQLException {
		return objectExists(name -> md.getIndexInfo(null, null, tableName, false, false), indexName, "INDEX_NAME") ||
			objectExists(name -> md.getIndexInfo(null, null, tableName.toUpperCase(), false, false), indexName, "INDEX_NAME");
	}

	private boolean isStringColumn(DatabaseMetaData md, String tableName, String columnName) throws SQLException {
		try (ResultSet rs = md.getColumns(null, null, tableName, columnName)) {
			if (rs.next()) {
				return isStringType(rs.getInt("DATA_TYPE"));
			}
		}

		try (ResultSet rs = md.getColumns(null, null, tableName.toUpperCase(), columnName)) {
			return rs.next() && isStringType(rs.getInt("DATA_TYPE"));
		}
	}

	private boolean isStringType(int type) {
		return type == Types.CHAR || type == Types.VARCHAR || type == Types.LONGVARCHAR ||
			type == Types.NCHAR || type == Types.NVARCHAR || type == Types.LONGNVARCHAR;
	}

	private boolean isNullableColumn(DatabaseMetaData md, String tableName, String columnName) throws SQLException {
		ColumnInfo column = getColumnInfo(md, tableName, columnName);
		return column != null && column.nullable;
	}

	private String getColumnType(DatabaseMetaData md, String tableName, String columnName) throws SQLException {
		ColumnInfo column = getColumnInfo(md, tableName, columnName);
		if (column == null) {
			throw new SQLException("Column not found: " + tableName + "." + columnName);
		}

		if (column.dataType == Types.CHAR || column.dataType == Types.VARCHAR ||
			column.dataType == Types.NCHAR || column.dataType == Types.NVARCHAR) {
			return column.typeName + "(" + column.size + ")";
		} else if (column.dataType == Types.DECIMAL || column.dataType == Types.NUMERIC) {
			return column.typeName + "(" + column.size + ", " + column.scale + ")";
		}

		return column.typeName;
	}

	private ColumnInfo getColumnInfo(DatabaseMetaData md, String tableName, String columnName) throws SQLException {
		for (String table : new String[] { tableName, tableName.toUpperCase(), tableName.toLowerCase() }) {
			for (String column : new String[] { columnName, columnName.toUpperCase(), columnName.toLowerCase() }) {
				try (ResultSet rs = md.getColumns(null, null, table, column)) {
					if (rs.next()) {
						return new ColumnInfo(
							rs.getString("TYPE_NAME"),
							rs.getInt("DATA_TYPE"),
							rs.getInt("COLUMN_SIZE"),
							rs.getInt("DECIMAL_DIGITS"),
							rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
					}
				}
			}
		}

		return null;
	}

	private boolean objectExists(ResultSetSupplier supplier, String objectName) throws SQLException {
		return objectExists(supplier, objectName, null);
	}

	private boolean objectExists(ResultSetSupplier supplier, String objectName, String nameColumn) throws SQLException {
		for (String name : new String[] { objectName, objectName.toUpperCase(), objectName.toLowerCase() }) {
			try (ResultSet rs = supplier.get(name)) {
				while (rs.next()) {
					if (nameColumn == null || objectName.equalsIgnoreCase(rs.getString(nameColumn))) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private void dropTable(Connection conn, boolean oracle, String tableName) throws SQLException {
		try {
			execute(conn, "drop " + (oracle ? "table " : "temporary table ") + tableName);
		} catch (SQLException e) {
			// Ignore missing temp tables. This keeps the task rerunnable after a partial failure.
		}
	}

	private void execute(Connection conn, String sql) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		}
	}

	private void executeUpdate(Connection conn, String sql) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		}
	}

	private void rollback(Connection conn, String tableName) {
		try {
			conn.rollback();
		} catch (Exception e) {
			logger.error("Error rolling back migration of multi-select table: " + tableName, e);
		}
	}

	private interface ResultSetSupplier {
		ResultSet get(String name) throws SQLException;
	}

	private enum MigrationStatus {
		MIGRATED,

		ALREADY_MIGRATED,

		SKIPPED,

		FAILED
	}

	private static class ColumnInfo {
		private String typeName;

		private int dataType;

		private int size;

		private int scale;

		private boolean nullable;

		ColumnInfo(String typeName, int dataType, int size, int scale, boolean nullable) {
			this.typeName = typeName;
			this.dataType = dataType;
			this.size = size;
			this.scale = scale;
			this.nullable = nullable;
		}
	}

	private static final String GET_CONTAINERS_SQL =
		"select IDENTIFIER, NAME, XML from DYEXTN_CONTAINERS where DELETED_ON is null";
}
