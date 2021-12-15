package edu.common.dynamicextensions.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.query.ast.ExpressionNode;

public class WideRowNode implements Serializable {

    private static final long serialVersionUID = 9096578807871606291L;

    private String alias;
    
    private String id;
    
    private List<ResultColumn> columns;
            
    public Map<String, Map<String, WideRowNode>> childrenRowsMap = 
            new LinkedHashMap<String, Map<String, WideRowNode>>();
    
    public WideRowNode(String alias, String id) {
        this.alias = alias;
        this.id = id;
    }
    
    public List<ResultColumn> getColumns() {
        return columns;
    }
    
    public void setColumns(List<ResultColumn> columns) {
        this.columns = columns;
    }
    
    public Map<String, WideRowNode> getChildrenRows(String alias) {
        return childrenRowsMap.get(alias);
    }
    
    public Map<String, WideRowNode> initChildrenRows(String alias) {
        Map<String, WideRowNode> childrenRows = new LinkedHashMap<>();
        childrenRowsMap.put(alias, childrenRows);
        return childrenRows;
    }
    
    public List<List<ResultColumn>> flatten(
    		Map<String, Integer> maxRowCntMap, 
    		Map<String, WideRowMode> tabWideRowMode, 
    		Map<String, List<ExpressionNode>> tabFieldsMap) {
    	
    	List<ResultColumn> currentRow = new ArrayList<>();
    	if (this.columns != null) {
    		currentRow.addAll(this.columns);
    		currentRow.sort(getPosBasedComparator());
    	}
    	
    	List<List<ResultColumn>> rows = new ArrayList<>();
    	rows.add(currentRow);
    	
    	for (Map.Entry<String, Map<String, WideRowNode>> childTabRows : childrenRowsMap.entrySet()) {
    		List<List<ResultColumn>> currentRows = new ArrayList<>();
    		
    		WideRowMode mode = tabWideRowMode.get(childTabRows.getKey());
    		if (mode == WideRowMode.SHALLOW) {
    			for (List<ResultColumn> existingRow : rows) {
    				if (!existingRow.isEmpty()) {
						existingRow.get(0).setFirstColumnOfShallowForm(true);
					}


        			for (Map.Entry<String, WideRowNode> childRow : childTabRows.getValue().entrySet()) {
        				List<List<ResultColumn>> flattenedChildRows = childRow.getValue().flatten(maxRowCntMap, tabWideRowMode, tabFieldsMap);
        				for (List<ResultColumn> flattenedChildRow : flattenedChildRows) {
        					//
        					// mark the first field of shallow form
							// this is very useful in doing the merge sort of columns order in the final output
							//
							if (!flattenedChildRow.isEmpty()) {
								flattenedChildRow.get(0).setFirstColumnOfShallowForm(true);
							}
        					currentRows.add(mergeShallowWideRowColumns(existingRow, flattenedChildRow));
        				}
        			}
        			
        			if (!childTabRows.getValue().isEmpty()) {
        				continue;
        			}
        			            		
            		List<ResultColumn> row = new ArrayList<>(existingRow);
            		List<ExpressionNode> tabFields = tabFieldsMap.get(childTabRows.getKey());
            		addEmptyChildRow(row, tabFields);
            		currentRows.add(row);
    			}
    		} else if (mode == WideRowMode.DEEP) { 
    			for (List<ResultColumn> existingRow : rows) {
        			List<ResultColumn> row = new ArrayList<>(existingRow);
        			WideRowNode childNode = null;
        			int instance = 0;
        			int childRowPos = -1, insertIdx = -1;
        			
        			for (Map.Entry<String, WideRowNode> childRow : childTabRows.getValue().entrySet()) {
        				childNode = childRow.getValue();
        				List<List<ResultColumn>> flattenedChildRows = childNode.flatten(maxRowCntMap, tabWideRowMode, tabFieldsMap);
        				        				
        				for (List<ResultColumn> flattenedChildRow : flattenedChildRows) {
        					if (childRowPos == -1) {
        						childRowPos = getFirstElementPos(flattenedChildRow);
        					}

        					if (insertIdx == -1) {
        						insertIdx = getIndexToInsert(row, childRowPos);
        					}
     
        					addChildRow(row, flattenedChildRow, insertIdx, instance);        					        					
        				}   
        				
        				++instance;
        			}
        			
        			int rowCount = childTabRows.getValue().size();
        			Integer maxCount = maxRowCntMap.get(childTabRows.getKey());
        			if (maxCount == null) {
        				maxCount = 0;
        			} else if (maxCount == 0) {
        				maxCount = 1;
        			}
        			
        			if (rowCount == maxCount) {
        				currentRows.add(row);
        				continue;
        			}
        			 
        			if (childNode == null) {
        				throw new FormException("Unexpected scenario: child node is null");
        			}
        			
        			// addEmptyChildRows(tabFieldsMap, maxRowCntMap, childNode.alias, childNode.getAliases(false), row, rowCount, maxCount);
					addEmptyChildRows(childTabRows.getValue(), maxRowCntMap, tabWideRowMode, tabFieldsMap, row, rowCount, maxCount);
        			currentRows.add(row);
    			}
    		}
    		
    		rows = currentRows;
    	}
    	
    	return rows;
    }

	private void addEmptyChildRows(
		Map<String, WideRowNode> tabRow,
		Map<String, Integer> maxRowCntMap,
		Map<String, WideRowMode> tabWideRowMode,
		Map<String, List<ExpressionNode>> tabFieldsMap,
		List<ResultColumn> parentRow,
		int from, int to) {

		WideRowNode node = tabRow.values().iterator().next();
		List<List<ResultColumn>> rows = node.flatten(maxRowCntMap, tabWideRowMode, tabFieldsMap);

		List<ResultColumn> childRows = new ArrayList<>();
		for (ResultColumn column : rows.get(0)) {
			childRows.add(new ResultColumn(column.getExpression(), 0));
		}

		addChildRows(parentRow, childRows, from, to);
	}

	//
	// TODO: Apr 02, 2020: Remove this block of code after COVID-19 crisis is resolved!
	// TODO: Apr 02, 2020: On a more serious note, remove this after couple of releases (7.0, 7.1, 7.2 etc)
	//

	// TODO: START
//	private Set<String> getAliases(boolean incThisNodeAlias) {
//		Set<String> aliases = new HashSet<>();
//		if (incThisNodeAlias) {
//			aliases.add(alias);
//		}
//
//		for (Map<String, WideRowNode> childrenRows : childrenRowsMap.values()) {
//			for (Map.Entry<String, WideRowNode> wideRow : childrenRows.entrySet()) {
//				if (wideRow.getValue() != null) {
//					aliases.addAll(wideRow.getValue().getAliases(true));
//					break;
//				}
//			}
//		}
//
//		return aliases;
//    }
//
//    private void addEmptyChildRows(
//    		Map<String, List<ExpressionNode>> tabFieldsMap,
//    		Map<String, Integer> maxRowCntMap,
//    		String alias, Set<String> childAliases,
//    		List<ResultColumn> parentRow,
//    		int from, int to) {
//
//		List<ResultColumn> childColumns = new ArrayList<ResultColumn>();
//		for (String childAlias : childAliases) {
//			List<ExpressionNode> fields = tabFieldsMap.get(childAlias);
//			if (fields == null) {
//				continue;
//			}
//
//			Integer maxRowCnt = maxRowCntMap.get(childAlias);
//			if (maxRowCnt == null) {
//				maxRowCnt = 0;
//			}
//
//			addChildRows(childColumns, getResultColumns(fields), 0, maxRowCnt);
//		}
//
//		List<ResultColumn> mainColumns = new ArrayList<ResultColumn>();
//		List<ExpressionNode> fields = tabFieldsMap.get(alias);
//		if (fields != null) {
//			mainColumns = getResultColumns(fields);
//		}
//
//		if (!childColumns.isEmpty()) {
//			addChildRow(mainColumns, childColumns);
//		}
//
//		addChildRows(parentRow, mainColumns, from, to);
//	}
	// TODO: END
    
	private Comparator<ResultColumn> getPosBasedComparator() {
		return new Comparator<ResultColumn>() {
			@Override
			public int compare(ResultColumn col0, ResultColumn col1) {
				return col0.getExpression().getPos() - col1.getExpression().getPos();
			}
		};
	}
	
	private int getFirstElementPos(List<ResultColumn> columns) {
		return columns.get(0).getExpression().getPos();
	}

	private int getIndexToInsert(List<ResultColumn> columns, int pos) {
		int idx = 0;
		for (ResultColumn rc : columns) {
			ExpressionNode expr = rc.getExpression();
			if ((expr.getLabel() == null || !expr.getLabel().startsWith("$")) && expr.getPos() >= pos) {
				break;
			}
			
			++idx;
		}
		
		return idx;
	}

	private void addEmptyChildRow(List<ResultColumn> parentRow, List<ExpressionNode> tabFields) {
		if (tabFields == null || tabFields.isEmpty()) {
			return;
		}
		
		List<ResultColumn> childRow = getResultColumns(tabFields);
		addChildRows(parentRow, childRow, 0, 1);
	}
		
	private void addChildRow(List<ResultColumn> parentRow, List<ResultColumn> childRow) {
		int childPos = getFirstElementPos(childRow);
		int insertIdx = getIndexToInsert(parentRow, childPos);
		addChildRow(parentRow, childRow, insertIdx);		
	}
	
	private void addChildRow(List<ResultColumn> parentRow, List<ResultColumn> childRow, int index) {
		addChildRows(parentRow, childRow, index, 0, 1);
	}
	
	private void addChildRow(List<ResultColumn> parentRow, List<ResultColumn> childRow, int index, int instance) {
		parentRow.addAll(index + instance * childRow.size(), childRow);
	}
	
	private void addChildRows(List<ResultColumn> parentRow, List<ResultColumn> childRow, int fromInstance, int toInstance) {
		int childRowPos = getFirstElementPos(childRow);
		int insertIdx = getIndexToInsert(parentRow, childRowPos);            		
		addChildRows(parentRow, childRow, insertIdx, fromInstance, toInstance);				
	}
	
	private void addChildRows(List<ResultColumn> parentRow, List<ResultColumn> childRow, int index, int fromInstance, int toInstance) {
		for (int i = fromInstance; i < toInstance; ++i) {
			addChildRow(parentRow, childRow, index, i);
		}		
	}
	
	private List<ResultColumn> getResultColumns(List<ExpressionNode> tabFields) {
		List<ResultColumn> columns = new ArrayList<ResultColumn>();
		for (ExpressionNode fieldExpr : tabFields) {
			columns.add(new ResultColumn(fieldExpr, 0));
		}
		
		Collections.sort(columns, getPosBasedComparator());
		return columns;		
	}

	private List<ResultColumn> mergeShallowWideRowColumns(List<ResultColumn> list1, List<ResultColumn> list2) {
		List<ResultColumn> result = new ArrayList<>();

		int i = 0, j = 0;
		while (i < list1.size() && j < list2.size()) {
			ResultColumn x = list1.get(i);
			ResultColumn y = list2.get(j);

			if (x.getExpression().getPos() <= y.getExpression().getPos()) {
				result.add(x);
				++i;

				while (i < list1.size() && !list1.get(i).isFirstColumnOfShallowForm()) {
					result.add(list1.get(i));
					++i;
				}
			} else {
				result.add(y);
				++j;

				while (j < list2.size() && !list2.get(j).isFirstColumnOfShallowForm()) {
					result.add(list2.get(j));
					++j;
				}
			}
		}

		if (i < list1.size()) {
			result.addAll(list1.subList(i, list1.size()));
		} else if (j < list2.size() ){
			result.addAll(list2.subList(j, list2.size()));
		}

		return result;
	}
}
