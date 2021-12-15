package edu.common.dynamicextensions.nutility;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.TransactionManager;

public class DeConfiguration {
	private static final DeConfiguration cfg = new DeConfiguration();

	private String fileUploadDir = System.getProperty("java.io.tmpdir");

	private String dateFormat = "MM-dd-yyyy";

	private String timeFormat = "HH:mm";

	private int maxCacheElementsInMemory = 100;

	private DeConfiguration() {

	}

	public static DeConfiguration getInstance() {
		return cfg;
	}

	public String fileUploadDir() {
		return fileUploadDir;
	}

	public DeConfiguration fileUploadDir(String fileUploadDir) {
		this.fileUploadDir = fileUploadDir;
		return this;
	}

	public String dateFormat() {
		return dateFormat;
	}

	public DeConfiguration dateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public String timeFormat() {
		return timeFormat;
	}

	public DeConfiguration timeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
		return this;
	}

	public DeConfiguration dataSource(DataSource ds, PlatformTransactionManager txnMgr) {
		JdbcDaoFactory.setDataSource(ds);
		TransactionManager.getInstance(ds, txnMgr);
		try {
			String product = ds.getConnection().getMetaData().getDatabaseProductName();
			DbSettingsFactory.init(product);
		} catch (SQLException e) {
			throw new FormException("Error while retrieving the Db type from Datasource " + e);
		}

		return this;
	}

	public int maxCacheElementsInMemory() {
		return maxCacheElementsInMemory;
	}

	public DeConfiguration maxCacheElementsInMemory(int maxCacheElementsInMemory) {
		this.maxCacheElementsInMemory = maxCacheElementsInMemory;
		return this;
	}
}