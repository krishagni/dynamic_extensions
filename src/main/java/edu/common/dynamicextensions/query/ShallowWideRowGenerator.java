package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.LookupControl;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.cachestore.LinkedEhCacheMap;

public class ShallowWideRowGenerator {
    private LinkedEhCacheMap<String, WideRowNode> wideRows = new LinkedEhCacheMap<String, WideRowNode>();
    
    private Map<String, String[]> tabJoinPath = new HashMap<String, String[]>();
    
    private Map<String, Integer> aliasRowCountMap = new HashMap<String, Integer>();
       
    private Map<String, List<ExpressionNode>> tabFieldsMap = new HashMap<String, List<ExpressionNode>>();
   
    private Map<String, WideRowMode> tabWideRowMode = new HashMap<String, WideRowMode>();
    
    private String rootTabAlias = null;
    
    private String lastRootId = null;
    
    private QueryExpressionNode queryExpr;
    
    private JoinTree queryJoinTree;
    
    private WideRowMode mode;
        
    public ShallowWideRowGenerator(JoinTree queryJoinTree, QueryExpressionNode queryExpr) {
    	this(queryJoinTree, queryExpr, WideRowMode.DEEP);
    }
    
    public ShallowWideRowGenerator(JoinTree queryJoinTree, QueryExpressionNode queryExpr, WideRowMode mode) {
        this.queryExpr = queryExpr;
        this.queryJoinTree = queryJoinTree;
        this.rootTabAlias = queryJoinTree.getAlias();
        this.mode = mode;
        initTableJoinPath(queryJoinTree);    	
    }
    
    
        
    public void start() {
        wideRows.clear();
        aliasRowCountMap.clear();
        aliasRowCountMap.put(rootTabAlias, 1);    
        tabFieldsMap = getTabFieldsMap();
        lastRootId = null;
    }
    
    public int processResultSet(ResultSet rs) {
    	int rowCount = 0;
        try {        	
            while (rs.next()) {
            	++rowCount;
            	
                Map<String, String> tabAliasIdMap = getTabAliasIdMap(rs);
                Map<String, List<ResultColumn>> tabAliasColValuesMap = getTabAliasColumnValuesMap(rs);
                
                String rootId = tabAliasIdMap.get(rootTabAlias);
                WideRowNode rootTabRow = wideRows.get(rootId);
                if (lastRootId == null || !lastRootId.equals(rootId)) {
                    assert(rootTabRow == null);
                    rootTabRow = new WideRowNode(rootTabAlias, rootId);
                    rootTabRow.setColumns(tabAliasColValuesMap.get(rootTabAlias));
                    wideRows.put(rootId, rootTabRow);
                    if (lastRootId != null) {
                    	mergeCounts(wideRows.get(lastRootId));
                    }                    
                }
                
                for (Map.Entry<String, String> tabAliasId : tabAliasIdMap.entrySet()) {
                    if (tabAliasId.getKey().equals(rootTabAlias)) {
                        continue;
                    }
                    
                    String[] joinNodes = tabJoinPath.get(tabAliasId.getKey());
                    WideRowNode wideRow = rootTabRow;
                    for (int j = 1; j < joinNodes.length; ++j) {
                        Map<String, WideRowNode> childTabRows = wideRow.getChildrenRows(joinNodes[j]);
                        if (childTabRows == null) {
                            childTabRows = wideRow.initChildrenRows(joinNodes[j]);
                        }
                        
                        String id = "-1";
                        if (tabAliasIdMap.containsKey(joinNodes[j])) {
                        	id = tabAliasIdMap.get(joinNodes[j]);
                        	id = joinNodes[j] + ":" + (id != null ? id : "null");
                        }
                        
                        WideRowNode childRow = childTabRows.get(id);
                        if (childRow == null) {
                            childRow = new WideRowNode(joinNodes[j], id);
                            childRow.setColumns(tabAliasColValuesMap.get(joinNodes[j]));
                            childTabRows.put(id, childRow);
                        }
                        
                        wideRow = childRow;
                    }
                }
                
                lastRootId = rootId;
            }
        } catch (Exception e) {
            throw new FormException("Error processing result for generating wide rows", e);
        }
        
        return rowCount;
    }
    
    public void end() {
    	if (lastRootId != null) {
    		mergeCounts(wideRows.get(lastRootId));
    	}        
    }
    
    public void cleanup() {
    	wideRows.destroy();
    }
    
    public List<ResultColumn> getResultColumns() {
    	List<ResultColumn> columns = null;
    	
    	Iterator<WideRowNode> iter = wideRows.iterator();
    	if (iter != null && iter.hasNext()) {
    		List<List<ResultColumn>> resultCols = iter.next().flatten(aliasRowCountMap, tabWideRowMode, tabFieldsMap);
    		columns = resultCols.iterator().next();
    	} else {
    		columns = getTabColumns(aliasRowCountMap, tabFieldsMap);
    	}

        return columns;    	
    }
    
    public Iterator<Object[]> iterator() {
    	return new Iterator<Object[]>() {
    		private Iterator<WideRowNode> iterator = wideRows.iterator();
    		
    		private Iterator<Object[]> subRowIter = null;
    		
    		private WideRowNode next = null;
    		
    		{
    			ensureNext();
    		}
    		
			@Override
			public boolean hasNext() {			
				return subRowIter != null;
			}

			@Override
			public Object[] next() {
				Object[] row = null;
				if (subRowIter != null) {
					row = subRowIter.next();
					ensureNext();
				}
				
				return row;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();				
			}    		
			
			private void ensureNext() {
				if (subRowIter != null && subRowIter.hasNext()) {
					return;
				}
								
				next = iterator.hasNext() ? iterator.next() : null;
				if (next != null) {
					List<Object[]> rows = flattenedRows(next);
					subRowIter = rows.iterator();
				} else {
					subRowIter = null;
				}				
			}
    	};
    }
    
    private List<Object[]> flattenedRows(WideRowNode wideRow) {
    	List<Object[]> result = new ArrayList<Object[]>();
    	
        List<List<ResultColumn>> rows = wideRow.flatten(aliasRowCountMap, tabWideRowMode, tabFieldsMap);        
        for (List<ResultColumn> row : rows) {            
            Object[] values = new Object[row.size()];
            int i = 0;
            for (ResultColumn col : row) {
            	values[i++] = col.getValue();
            }
            
            result.add(values);
        }
        
        return result;    	
    }
        
    private void mergeCounts(WideRowNode wideRow) {
    	if (wideRow.childrenRowsMap == null) {
    		return;
    	}
    	
    	for (Map.Entry<String, Map<String, WideRowNode>> childTable : wideRow.childrenRowsMap.entrySet()) {
    		Integer count = childTable.getValue().size();
    		Integer actual = aliasRowCountMap.get(childTable.getKey());
    		if (actual == null || actual < count) {
    			aliasRowCountMap.put(childTable.getKey(), count);
    		}
    		
    		for (WideRowNode childTableRow : childTable.getValue().values()) {
    			mergeCounts(childTableRow);
    		}
    	}
    }
        
    private void initTableJoinPath(JoinTree queryJoinTree) {
        tabJoinPath.clear();
        initTableJoinPath(queryJoinTree, new ArrayList<String>());        
    }
    
    private void initTableJoinPath(JoinTree joinTree, List<String> path) {
		if (tabJoinPath.containsKey(joinTree.getAlias())) {
			return;
		}

        String tabAlias = joinTree.getAlias();
        path = new ArrayList<>(path);
        path.add(tabAlias);
        
        tabJoinPath.put(tabAlias, path.toArray(new String[0]));
        
        if (joinTree.isMultiSelect()) {
        	tabWideRowMode.put(tabAlias, WideRowMode.DEEP);
        } else if (mode == WideRowMode.DEEP && (joinTree.isSubForm() || joinTree.isNonTopLevelExtensionForm())) {
        	tabWideRowMode.put(tabAlias, WideRowMode.DEEP);
        } else {
        	tabWideRowMode.put(tabAlias, WideRowMode.SHALLOW);
        }
        
        for (JoinTree childTree : joinTree.getChildren()) {
			if (childTree.getLinkedTrees().size() != 0) {
				continue;
			}

            initTableJoinPath(childTree, path);
        }

        //
		// TODO: added for link control
		//
        for (JoinTree linkedFrom : joinTree.getLinkedFromTrees()) {
			initTableJoinPath(linkedFrom, path);

			for (JoinTree linkedTo : linkedFrom.getLinkedTrees().values()) {
				initTableJoinPath(linkedTo, path);
			}

			if (linkedFrom.getParent() != null) {
				initTableJoinPath(linkedFrom.getParent(), path);
			}
		}
    }
    
    private Map<String, String> getTabAliasIdMap(ResultSet rs) 
    throws Exception {
        Map<String, String> tabAliasIdMap = new LinkedHashMap<String, String>();
        
        int cols = 0;
        List<ExpressionNode> selectElements = queryExpr.getSelectList().getElements();        
        for (ExpressionNode element : selectElements) {
            ++cols;
            
            if (!(element instanceof FieldNode)) {
                continue;
            }
            
            FieldNode field = (FieldNode)element;
            if (field.getCtrl() instanceof MultiSelectControl || field.getCtrl() instanceof LookupControl) {
                tabAliasIdMap.put(field.getTabAlias(), rs.getString(cols));
            }         
        }
        
        int numCols = rs.getMetaData().getColumnCount();
        if (queryExpr.getLimitExpr() != null && DbSettingsFactory.isOracle()) {
        	numCols--;        	
        }
        
        for (int i = selectElements.size() + 1; i <= numCols; i += 2) {
            tabAliasIdMap.put(rs.getString(i), rs.getString(i + 1));
        }
        
        return tabAliasIdMap;
    }
        
    private Map<String, List<ResultColumn>> getTabAliasColumnValuesMap(ResultSet rs) 
    throws Exception {
        Map<String, List<ResultColumn>> aliasColumnValuesMap = new HashMap<String, List<ResultColumn>>(); 
                
        int col = 0;
        List<ExpressionNode> selectElements = queryExpr.getSelectList().getElements();      
        for (ExpressionNode element : selectElements) {
            col++;
            
            String[] aliasPk = WideRowUtil.getTabAliasPk(queryJoinTree, element);
            String alias = aliasPk == null ? "literal" : aliasPk[0];
            List<ResultColumn> columns = aliasColumnValuesMap.get(alias);
            if (columns == null) {
                columns = new ArrayList<ResultColumn>();
                aliasColumnValuesMap.put(alias, columns);
            }
            
            Object obj = rs.getObject(col);
            if (obj instanceof Date) {
            	obj = rs.getTimestamp(col);
            }            
            columns.add(new ResultColumn(element, obj));         
        }
        
        return aliasColumnValuesMap;
    }
    
    private Map<String, List<ExpressionNode>> getTabFieldsMap() {
        Map<String, List<ExpressionNode>> tabFieldsMap = new LinkedHashMap<String, List<ExpressionNode>>();
        
        int i = -1;
        for (ExpressionNode exprNode : queryExpr.getSelectList().getElements()) {
            String[] aliasPk = WideRowUtil.getTabAliasPk(queryJoinTree, exprNode);
            String tabAlias = aliasPk == null ? "literal" : aliasPk[0];

            List<ExpressionNode> fields = tabFieldsMap.get(tabAlias);
            if (fields == null) {
                fields = new ArrayList<ExpressionNode>();
                tabFieldsMap.put(tabAlias, fields);
                ++i;
            }
            
            if (fields.isEmpty()) {
            	exprNode.setPos(i);
            } else if (i != fields.get(0).getPos()){
            	exprNode.setPos(++i);            	
            } else {
            	exprNode.setPos(i);
            }
            fields.add(exprNode);
        }
        
        return tabFieldsMap;        
    }
        
    private List<ResultColumn> getTabColumns(Map<String, Integer> maxCount, Map<String, List<ExpressionNode>> tabFieldsMap) {
        List<ResultColumn> resultColumns = new ArrayList<ResultColumn>();
                    
        for (Map.Entry<String, List<ExpressionNode>> tabFields : tabFieldsMap.entrySet()) {
        	String alias = tabFields.getKey();
        	Integer count = null;
        	
        	switch (tabWideRowMode.get(alias)) {
        		case DEEP:
        			count = maxCount.get(alias);
        			break;
        			
        		case SHALLOW:
        		case OFF:
        			count = 1;
        			break;
        	}
        	
        	if (count == null || count == 0) {
        		count = 1;
        	}
        	
        	List<ExpressionNode> fields = tabFieldsMap.get(alias);
        	for (int i = 0; i < count; ++i) {
        		for (ExpressionNode field : fields) {
        			resultColumns.add(new ResultColumn(field, i));
        		}
        	}
        }
                    
        return resultColumns;
    }

}