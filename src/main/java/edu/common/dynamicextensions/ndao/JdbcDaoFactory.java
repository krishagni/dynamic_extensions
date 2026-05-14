package edu.common.dynamicextensions.ndao;

import javax.sql.DataSource;

public class JdbcDaoFactory {
	private static DataSource ds = null;
	
	public static void setDataSource(DataSource ds) {
		JdbcDaoFactory.ds = ds;
	}
	
	public static JdbcDao getJdbcDao() {
		return new JdbcDao(ds);
	}

	public static JdbcDao getJdbcDao(DataSource ds) {
		return new JdbcDao(ds != null ? ds : JdbcDaoFactory.ds);
	}
}
