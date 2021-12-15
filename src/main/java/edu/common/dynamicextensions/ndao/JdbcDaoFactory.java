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
}
