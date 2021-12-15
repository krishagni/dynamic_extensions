package edu.common.dynamicextensions.ndao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultExtractor<T> {
	T extract(ResultSet rs)
	throws SQLException;
}
