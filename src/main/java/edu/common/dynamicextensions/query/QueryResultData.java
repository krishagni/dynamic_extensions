package edu.common.dynamicextensions.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.nutility.Util;
import edu.common.dynamicextensions.query.ast.AggregateNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.cachestore.LinkedEhCacheMap;

public class QueryResultData {
	private static final String ISO_DATE_TIME_FMT = "yyyy-MM-dd'T'HH:mm:ss";

	private static final String ISO_DATE_FMT = "yyyy-MM-dd";

    private ResultColumnLabelFormatter formatter = new DefaultResultColLabelFormatter("# ");
    
    private List<ResultColumn> resultColumns = null;

	private List<ResultColumn> screenedColumns = null;

	private RowsList rows = null;
    
    private ShallowWideRowGenerator rowGen = null;

    private SimpleDateFormat dateOnly = null;
            
    private SimpleDateFormat sdf = null;

    private SimpleDateFormat tsf = null;
    
    private QueryResultScreener screener = null;

	private boolean outputExpression;

    private int dbRowsCount;

    private String[] columnLabels;

	public QueryResultData(List<ResultColumn> resultColumns, String timeZone) {
		this(resultColumns, null, null, timeZone);
	}

    public QueryResultData(List<ResultColumn> resultColumns, String dateFormat, String timeFormat, String timeZone) {
		this.resultColumns = resultColumns;

        if (dateFormat != null) {
        	dateOnly = new SimpleDateFormat(dateFormat);
			sdf = new SimpleDateFormat(dateFormat);
			if (timeFormat != null) {
				tsf = new SimpleDateFormat(dateFormat + " " + timeFormat);
			}
        } else {
        	dateOnly = new SimpleDateFormat(ISO_DATE_FMT);
			sdf = new SimpleDateFormat(ISO_DATE_FMT);
			tsf = new SimpleDateFormat(ISO_DATE_TIME_FMT);
		}

        if (timeZone != null) {
        	try {
				TimeZone tz = TimeZone.getTimeZone(timeZone);
				if (sdf != null) {
					sdf.setTimeZone(tz);
				}

				if (tsf != null) {
					tsf.setTimeZone(tz);
				}
			} catch (Exception e) {
        		System.err.println("Error setting the timezone: " + timeZone);
        		e.printStackTrace();
			}
		}
    }

    public QueryResultScreener getScreener() {
        return screener;
    }

    public void setScreener(QueryResultScreener screener) {
        this.screener = screener;
    }

    public void setColumnLabelFormatter(ResultColumnLabelFormatter formatter) {
    	this.formatter = formatter;
    	this.columnLabels = null;
    }

    public Integer[] getColumnIndices(String name) {
    	List<Integer> indices = new ArrayList<>();
    	
    	int i = 0;
    	for (ResultColumn column : getResultColumns()) {
    		if (column.getExpression() instanceof FieldNode) {
    			FieldNode node = (FieldNode)column.getExpression();
    			if (node.getName().equals(name)) {
    				indices.add(i);
    			}    			
    		}
    		
    		++i;
    	}
    	
    	return indices.toArray(new Integer[0]);
    }

    //
	// At present only AQL expr and whether the expression is aggregate or not are returned
	// Eventually, everything (label, url, type) will be subsumed by this column metadata
	//
    public List<Map<String, Object>> getColumnMetadata() {
		return getResultColumns().stream().map(rs -> {
			Map<String, Object> metadata = new HashMap<>();
			metadata.put("expr", rs.getExpression().getAql());
			metadata.put("aggregate", rs.getExpression().isAggregateExpression());
			return metadata;
		}).collect(Collectors.toList());
	}

    public String[] getColumnLabels() {
		if (columnLabels != null) {
			return columnLabels;
		}

		Function<ResultColumn, String> labelFn;
		if (outputExpression) {
			labelFn = (rs) -> rs.getColumnExpr(formatter);
		} else {
			labelFn = (rs) -> rs.getColumnLabel(formatter);
		}

		columnLabels = getResultColumns().stream().map(labelFn).toArray(String[]::new);
		return columnLabels;
    }

	public String[] getColumnUrls() {
		return getResultColumns().stream().map(ResultColumn::getUrl).toArray(String[]::new);
	}

	public String[] getColumnTypes() {
		return getResultColumns().stream()
			.map(ResultColumn::getExpression)
			.map(expr -> expr != null && expr.getType() != null ? expr.getType().name() : null)
			.toArray(String[]::new);
	}
    
    public List<ResultColumn> getResultColumns() {
		if (screenedColumns != null) {
			return screenedColumns;
		}

		screenedColumns = resultColumns;
		if (screener != null) {
			screenedColumns = screener.getScreenedResultColumns(resultColumns);
		}

		return screenedColumns;
    }
    
//    public void dataSource(List<Object[]> rows) {
//		if (rows == null) {
//			if (this.rows != null) {
//				this.rows.cleanup();
//				this.rows = null;
//			}
//
//			return;
//		}
//
//		this.rows = new RowsList();
//		for (Object[] row : rows) {
//			this.rows.add(row);
//		}
//    }

    public void dataSource(RowsList rows) {
		if (this.rows != null) {
			this.rows.cleanup();
			this.rows = null;
		}

		this.rows = rows;
	}
       
    public void dataSource(ResultSet rs) {
    	if (this.rows != null) {
    		this.rows.cleanup();
    		this.rows = null;
		}

    	this.rows = new RowsList();
		Map<Integer, BigDecimal> cumulative = new HashMap<>();
    	try {
        	int columnCount = rs.getMetaData().getColumnCount();
        	if (columnCount == resultColumns.size() + 1 && DbSettingsFactory.isOracle()) {
        		columnCount--;
        	}
        	
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; ++i) {
					ExpressionNode expr = resultColumns.get(i).getExpression();
                    row[i] = rs.getObject(i + 1);
                    if (row[i] instanceof Date) {
						row[i] = rs.getTimestamp(i + 1);
                    } else if (expr instanceof AggregateNode && ((AggregateNode) expr).isCumulative()) {
						BigDecimal existing = cumulative.get(i);
						if (existing == null) {
							existing = BigDecimal.ZERO;
						}

						if (row[i] != null) {
							existing = existing.add(new BigDecimal(row[i].toString()));
						}

						row[i] = existing;
						cumulative.put(i, existing);
					}
                }
                
                if (screener != null) {
                	row = screener.getScreenedRowData(resultColumns, row);
                }

                this.rows.add(row);
            }    		
    	} catch (Exception e) {
    		if (rows != null) {
    			this.rows.cleanup();
			}

    		throw new FormException("Error traversing result set", e);
    	} finally {
    		cumulative.clear();
			cumulative = null;
		}

    	this.dbRowsCount = this.rows.size();
    }
    
    public void dataSource(ShallowWideRowGenerator rowGen) {
    	this.rowGen = rowGen;
    }

    public List<Object[]> getRows() {
		Iterator<Object[]> iter = null;
		if (rows != null) {
			iter = rowIterator(rows.iterator());
		} else if (rowGen != null) {
			iter = rowIterator(rowGen.iterator());
		}

		List<Object[]> result = new ArrayList<>();
		while (iter.hasNext()) {
			result.add(iter.next());
		}

		return result;
    }

	public boolean isOutputExpression() {
		return outputExpression;
	}

	public void setOutputExpression(boolean outputExpression) {
		this.outputExpression = outputExpression;
	}

	public int getDbRowsCount() {
		return dbRowsCount;
	}

	public void setDbRowsCount(int dbRowsCount) {
		this.dbRowsCount = dbRowsCount;
	}

	public RowsList rows() {
		return rows;
	}

	public List<String[]> getStringifiedRows() {
		List<String[]> rows = new ArrayList<String[]>();
		Iterator<String[]> iter = stringifiedRowIterator();
		while (iter.hasNext()) {
			rows.add(iter.next());
		}

		return rows;
	}
    
    public Iterator<Object[]> rowIterator() {
    	if (rows != null) {
    		return rows.iterator();
    	} else if (rowGen != null) {
    		return rowIterator(rowGen.iterator());
    	}
    	
    	return null;
    }
    
    public Iterator<String[]> stringifiedRowIterator() {
    	Iterator<Object[]> iter = null;
    	if (rows != null) {
    		iter = rowIterator(rows.iterator());
    	} else if (rowGen != null) {
    		iter = rowIterator(rowGen.iterator());
    	}
    	
    	return stringifiedRowIterator(iter);
    }
    
    public void close() {
    	if (rowGen != null) {
    		rowGen.cleanup();
    	}
    	
    	if (rows != null) {
    		rows.cleanup();
    		rows = null;
    	}
    }
    
    public String[] stringifyRow(Object[] row) {
        String[] result = new String[row.length];
        
        for(int j = 0; j < row.length; j++) {
            if (row[j] == null) {
                result[j] = null;
            } else if (row[j] instanceof Timestamp) {
            	result[j] = toDateTime(j, (Date)row[j], true);
            } else if (row[j] instanceof Date){
				result[j] = toDateTime(j, (Date)row[j], false);
            } else if (Util.isOraTimestamp(row[j])) {
				Date dateObj = Util.getDateFromOraTimestamp(row[j]);
				result[j] = toDateTime(j, dateObj, true);
			} else if (row[j] instanceof Number) {
            	BigDecimal bd = BigDecimal.valueOf(((Number) row[j]).doubleValue());
            	int scale = getResultColumns().get(j).getScale();
            	if (scale > 0) {
            		bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
				} else if (bd.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            		// Not an integer. set the scale
            		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
            		// cent-percent sure - this is an integer field
            		bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
				}

				result[j] = bd.toString();
            } else {
            	result[j] = row[j].toString();
            }
        }

        return result;
    }
    
    private Iterator<Object[]> rowIterator(final Iterator<Object[]> iter) {
    	return new Iterator<Object[]>() {
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Object[] next() {
				Object[] row = iter.next();
				if (screener != null) {
					row = screener.getScreenedRowData(resultColumns, row);
				}
				
				return row;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();				
			}    		
    	};
    }
    
    private Iterator<String[]> stringifiedRowIterator(final Iterator<Object[]> iter) {
    	return new Iterator<String[]>() {
			@Override
			public boolean hasNext() {					
				return iter.hasNext();
			}

			@Override
			public String[] next() {
				return stringifyRow(iter.next());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}    			
   		};
    }

	private String toDateTime(int columnIdx, Date input, boolean dateTime) {
		if (input == null) {
			return null;
		}

		ResultColumn col = null;
		if (columnIdx < resultColumns.size()) {
			col = resultColumns.get(columnIdx);
		}

		boolean strictlyDate = false;
		if (col != null && col.getExpression() instanceof FieldNode) {
			Control ctrl = ((FieldNode) col.getExpression()).getCtrl();
			if (ctrl instanceof DatePicker) {
				DatePicker dp = (DatePicker) ctrl;
				if (dp.getFormat() != null) {
					dateTime = dp.getFormat().contains("HH:mm");
					strictlyDate = !dateTime && dp.getFormat().endsWith("/dateOnly");
				}
			}
		}

		return dateTime ? tsf.format(input) : (strictlyDate ? dateOnly.format(input) : sdf.format(input));
	}
}
