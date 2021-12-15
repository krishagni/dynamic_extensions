package edu.common.dynamicextensions.query.ast;


import edu.common.dynamicextensions.query.JoinTree;

public class FilterNode implements FilterNodeMarker {
    public enum RelationalOp {
        EQ("="),
        LT("<"),
        LE("<="),
        GT(">"),
        GE(">="),
        NE("!="),
        IN("in"),
        NOT_IN("not in"),
        STARTS_WITH("starts with"),
        ENDS_WITH("ends with"),
        CONTAINS("contains"),
        ANY("any"),
        EXISTS("exists"),
        NOT_EXISTS("not exists"),
        BETWEEN("between");
        
        
        private String symbol;
        
        private RelationalOp(String symbol) {
            this.symbol = symbol;  
        }
        
        public String symbol() {
            return symbol;
        }
        
        public static RelationalOp getBySymbol(String symbol) {
            RelationalOp result = null;
            for (RelationalOp op : RelationalOp.values()) {
                if (op.symbol.equals(symbol)) {
                    result = op;
                    break;
                }
            }
            
            return result;
        }                
    }

    private String aql;
    
    private ExpressionNode lhs;
   
	private RelationalOp relOp;
    
    private ExpressionNode rhs;

    private QueryExpressionNode subQuery;

    private JoinTree subQueryJoinTree;

    private String sql;

	@Override
	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public ExpressionNode getLhs() {
		return lhs;
	}

	public void setLhs(ExpressionNode lhs) {
		this.lhs = lhs;
	}

	public RelationalOp getRelOp() {
		return relOp;
	}

	public void setRelOp(RelationalOp relOp) {
		this.relOp = relOp;
	}

	public ExpressionNode getRhs() {
		return rhs;
	}

	public void setRhs(ExpressionNode rhs) {
		this.rhs = rhs;
	}

	public QueryExpressionNode getSubQuery() {
		return subQuery;
	}

	public void setSubQuery(QueryExpressionNode subQuery) {
		this.subQuery = subQuery;
	}

	public JoinTree getSubQueryJoinTree() {
		return subQueryJoinTree;
	}

	public void setSubQueryJoinTree(JoinTree subQueryJoinTree) {
		this.subQueryJoinTree = subQueryJoinTree;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
