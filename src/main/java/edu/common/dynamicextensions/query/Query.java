package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.nutility.LogUtil;
import edu.common.dynamicextensions.query.ast.ConcatNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class Query {
	private static final LogUtil logger = LogUtil.getLogger(Query.class);
    
    private JoinTree queryJoinTree;

    private QueryExpressionNode queryExpr;

    private WideRowMode wideRowMode;
    
    private boolean ic;

	private boolean outputIsoDateTime;

	private boolean outputExpression;

    private String dateFormat = "MM-dd-yyyy";

    private String timeFormat = "HH:mm";

    private String timeZone;
    
    private boolean vcEnabled;
    
    private ResultPostProc resultPostProc;

	private PathConfig pathConfig;

	private Map<String, String> autoJoinParams;

	private QuerySpace qs;

	private int timeoutInSeconds = -1;
        
    public static Query createQuery() {
        return new Query();
    }
    
    private Query() {
    }
            
    public Query wideRowMode(WideRowMode mode) {
        this.wideRowMode = mode;
        return this;
    }
    
    public Query ic(boolean ic) {
    	this.ic = ic;
    	return this;
    }

    public Query outputIsoDateTime(boolean outputIsoDateTime) {
		this.outputIsoDateTime = outputIsoDateTime;
		return this;
	}

	public Query outputExpression(boolean outputExpression) {
		this.outputExpression = outputExpression;
		return this;
	}

    public Query dateFormat(String dateFormat) {
    	this.dateFormat = dateFormat;
    	return this;
    }

    public Query timeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

    public Query timeZone(String timeZone) {
    	this.timeZone = timeZone;
    	return this;
	}
    
    public Query enableVersionedForms(boolean vcEnabled) {
    	this.vcEnabled = vcEnabled;
    	return this;
    }

    public Query pathConfig(PathConfig pathConfig) {
		this.pathConfig = pathConfig;
		return this;
	}

	public Query autoJoinParams(Map<String, String> autoJoinParams) {
		this.autoJoinParams = autoJoinParams;
		return this;
	}

	public Query querySpace(QuerySpace qs) {
    	this.qs = qs;
    	if (qs != null) {
    		this.pathConfig = qs.getPathConfig();
		}
    	return this;
	}

	public Query timeout(int timeout) {
		this.timeoutInSeconds = timeout;
		return this;
	}

    public void compile(String rootFormName, String query) {
        compile(rootFormName, query, null);
    }
    
    public void compile(String rootFormName, String query, String restriction) {
        QueryCompiler compiler = new QueryCompiler(qs != null ? qs.getRootForm() : rootFormName, query, restriction);
        compiler.enabledVersionedForms(vcEnabled).pathConfig(pathConfig).querySpace(qs).compile();
        queryExpr     = compiler.getQueryExpr();
        queryJoinTree = compiler.getQueryJoinTree();
        
        if (queryExpr.hasResultPostProc()) {
        	String procName = queryExpr.getResultPostProcName();
        	ResultPostProcFactory factory = ResultPostProcManager.getInstance().getFactory(procName);
			if (factory != null) {
				resultPostProc = factory.create(queryExpr, timeZone);
			}
        }        
    }

    public boolean isAggregateQuery() {
        if (queryExpr == null || queryExpr.getSelectList() == null) {
            return false;
        }

        return queryExpr.getSelectList().hasAggregateExpr();
    }

    public boolean isPhiResult(boolean onlyFields) {
        if (queryExpr == null || queryExpr.getSelectList() == null) {
            return false;
        }

        for (ExpressionNode element : queryExpr.getSelectList().getElements()) {
            if (!element.isPhi()) {
                continue;
            }

            if (!onlyFields || element instanceof FieldNode || (element instanceof ConcatNode && ((ConcatNode) element).hasPhiField())) {
				return true;
			}
        }

        return false;
    }

    public String getResultProcessorName() {
        return queryExpr != null && queryExpr.hasResultPostProc() ? queryExpr.getResultPostProcName() : null;
    }

    public long getCount() {
        QueryGenerator gen = new QueryGenerator(false, ic, dateFormat, timeFormat);
        gen.setAutoJoinParams(autoJoinParams);
        String countSql = gen.getCountSql(queryExpr, queryJoinTree);

        long t1 = System.currentTimeMillis();
		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
		jdbcDao.setQueryTimeout(timeoutInSeconds);
        long count = jdbcDao.getResultSet(countSql, null, new ResultExtractor<Long>() {
        	@Override
        	public Long extract(ResultSet rs) throws SQLException {
        		return rs.next() ? rs.getLong(1) : -1L;
        	}
        });
        
        long t2 = System.currentTimeMillis();
        logger.debug("Count SQL: " + countSql + "; Query Exec Time: " + (t2 - t1));
        return count;
    }

    public QueryResponse getData() {
        return getData(0, 0);
    }

    public QueryResponse getData(int start, int numRows) {
    	final boolean wideRowSupport = isWideRowSupportEnabled();
    	
        final String dataSql = getDataSql(wideRowSupport, start, numRows);        
        final long t1 = System.currentTimeMillis();

		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
		jdbcDao.setQueryTimeout(timeoutInSeconds);
		jdbcDao.setFetchSize(Integer.MIN_VALUE);

        return jdbcDao.getResultSet(dataSql, null, rs -> {
	        long t2 = System.currentTimeMillis();
	        QueryResultData resultData = null;
	        boolean cleanupResultData = true;

			try {
				if (wideRowSupport) {
					resultData = getWideRowData(rs);
				} else if (resultPostProc != null) {
					resultData = getProcessedData(rs);
				} else {
					resultData = getQueryResultData(rs);
				}

				resultData.setOutputExpression(!(resultPostProc instanceof Crosstab) && outputExpression);
				long t3 = System.currentTimeMillis();
				logger.debug("Data SQL: " + dataSql + "; Query Exec Time: " + (t2 - t1) + "; Result Prep Time: " + (t3 - t2));

				QueryResponse resp = new QueryResponse();
				resp.setSql(dataSql);
				resp.setResultData(resultData);
				resp.setExecutionTime(t2 - t1);
				resp.setPostExecutionTime(t3 - t2);

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(t1);
				resp.setTimeOfExecution(cal.getTime());
				cleanupResultData = false;
				return resp;
			} finally {
				if (cleanupResultData && resultData != null) {
					resultData.close();
				}
			}
        });
    }

    public String getDataSql() {
        return getDataSql(isWideRowSupportEnabled(), 0, 0);
    }
    
    public String getDataSql(boolean wideRows, int start, int numRows) {
        QueryGenerator gen = new QueryGenerator(wideRows, ic, dateFormat, timeFormat);
        gen.setAutoJoinParams(autoJoinParams);
        return gen.getDataSql(queryExpr, queryJoinTree, start, numRows);        
    }

	private boolean isWideRowSupportEnabled() {
		return
			(wideRowMode != WideRowMode.OFF) &&
			!queryExpr.isAggregateQuery() &&
			!queryExpr.hasResultPostProc();
	}

    private QueryResultData getWideRowData(ResultSet rs) {
        ShallowWideRowGenerator wideRowGenerator = new ShallowWideRowGenerator(queryJoinTree, queryExpr, wideRowMode);
	    boolean cleanupRowGenerator = true;

		try {
			wideRowGenerator.start();
			int dbRowsCount = wideRowGenerator.processResultSet(rs);
			wideRowGenerator.end();

			QueryResultData qrd = getQueryResultData(wideRowGenerator.getResultColumns());
			qrd.setDbRowsCount(dbRowsCount);
			qrd.dataSource(wideRowGenerator);
			cleanupRowGenerator = false;
			return qrd;
		} finally {
			if (cleanupRowGenerator) {
				wideRowGenerator.cleanup();
			}
		}
    }
    
    private QueryResultData getProcessedData(ResultSet rs) {
    	DefaultResultPostProc defProc = new DefaultResultPostProc();
	    QueryResultData qrd = null;
	    boolean cleanupPostProc = true;

    	try {
			int dbRowsCount = resultPostProc.processResultSet(rs, defProc);
		    qrd = getQueryResultData(resultPostProc.getResultColumns());
			qrd.setDbRowsCount(dbRowsCount);
			qrd.dataSource(resultPostProc.getRows());
		    cleanupPostProc = false;
			return qrd;
		} finally {
    		defProc.cleanup();
			if (cleanupPostProc) {
				try {
					resultPostProc.cleanup();
				} finally {
					if (qrd != null) {
						qrd.close();
					}
				}
			}
		}
    }

    private QueryResultData getQueryResultData(ResultSet rs) {
        QueryResultData queryResult = getQueryResultData(getResultColumns(queryExpr));
        queryResult.dataSource(rs);
        return queryResult;
    }

    private QueryResultData getQueryResultData(List<ResultColumn> columns) {
		if (outputIsoDateTime) {
			return new QueryResultData(columns, timeZone);
		} else {
			return new QueryResultData(columns, dateFormat, timeFormat, timeZone);
		}
	}
            
    private List<ResultColumn> getResultColumns(QueryExpressionNode queryExpr) {
		return queryExpr.getSelectList().getElements().stream()
			.map(node -> new ResultColumn(node, 0))
			.collect(Collectors.toList());
    }

    private class DefaultResultPostProc implements ResultPostProc {

    	private QueryResultData qrd;

		@Override
		public int processResultSet(ResultSet rs, ResultPostProc defaultProc) {
			qrd = getQueryResultData(rs);
			return qrd.getDbRowsCount();
		}

		@Override
		public List<ResultColumn> getResultColumns() {
			return qrd.getResultColumns();
		}

		@Override
		public RowsList getRows() {
			return qrd.rows();
		}

		@Override
		public void cleanup() {
			try {
				if (qrd != null) {
					qrd.close();
					qrd = null;
				}
			} catch (Exception e) {
				logger.error("Error cleaning the default result post processor", e);
			}
		}
	}
}
