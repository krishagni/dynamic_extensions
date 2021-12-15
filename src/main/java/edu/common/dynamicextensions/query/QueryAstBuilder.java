package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.napi.FormException;
import edu.common.dynamicextensions.query.antlr.AQLBaseVisitor;
import edu.common.dynamicextensions.query.antlr.AQLParser;
import edu.common.dynamicextensions.query.antlr.AQLParser.LiteralContext;
import edu.common.dynamicextensions.query.ast.AggregateNode;
import edu.common.dynamicextensions.query.ast.AggregateNode.AGG_FN;
import edu.common.dynamicextensions.query.ast.ArithExpressionNode;
import edu.common.dynamicextensions.query.ast.ArithExpressionNode.ArithOp;
import edu.common.dynamicextensions.query.ast.BetweenNode;
import edu.common.dynamicextensions.query.ast.ConcatNode;
import edu.common.dynamicextensions.query.ast.CrosstabNode;
import edu.common.dynamicextensions.query.ast.CurrentDateNode;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode.DiffType;
import edu.common.dynamicextensions.query.ast.DateFormatFuncNode;
import edu.common.dynamicextensions.query.ast.DateIntervalNode;
import edu.common.dynamicextensions.query.ast.DateRangeFuncNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.FilterExpressionNode;
import edu.common.dynamicextensions.query.ast.FilterNode;
import edu.common.dynamicextensions.query.ast.FilterNode.RelationalOp;
import edu.common.dynamicextensions.query.ast.FilterNodeMarker;
import edu.common.dynamicextensions.query.ast.LimitExprNode;
import edu.common.dynamicextensions.query.ast.LiteralValueListNode;
import edu.common.dynamicextensions.query.ast.LiteralValueNode;
import edu.common.dynamicextensions.query.ast.Node;
import edu.common.dynamicextensions.query.ast.OrderExprListNode;
import edu.common.dynamicextensions.query.ast.OrderExprNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.ast.ResultPostProcNode;
import edu.common.dynamicextensions.query.ast.RoundOffNode;
import edu.common.dynamicextensions.query.ast.SelectListNode;

public class QueryAstBuilder extends AQLBaseVisitor<Node> {

    public QueryAstBuilder() {
    }

    @Override
    public QueryExpressionNode visitQuery(AQLParser.QueryContext ctx) {
    	return (QueryExpressionNode) visit(ctx.query_expr());
	}

    @Override 
    public QueryExpressionNode visitQueryExpr(AQLParser.QueryExprContext ctx) {
    	QueryExpressionNode queryExpr = new QueryExpressionNode();

    	SelectListNode selectList = new SelectListNode();
    	if (ctx.select_list() != null) {
    		selectList = (SelectListNode)visit(ctx.select_list());
    	} 
    	queryExpr.setSelectList(selectList);    	
    	queryExpr.setFilterExpr((FilterExpressionNode)visit(ctx.filter_expr()));

    	if (ctx.having_expr() != null) {
    		queryExpr.setHavingExpr((FilterExpressionNode)visit(ctx.having_expr()));
		}

		if (ctx.order_expr() != null) {
			queryExpr.setOrderExpr((OrderExprListNode)visit(ctx.order_expr()));
		}

    	if (ctx.limit_expr() != null) {
    		queryExpr.setLimitExpr((LimitExprNode)visit(ctx.limit_expr()));
    	}
    	
    	if (ctx.crosstab_expr() != null) {
    		CrosstabNode crosstabSpec = (CrosstabNode)visit(ctx.crosstab_expr());
    		queryExpr.setCrosstabSpec(crosstabSpec);
    	} else if (ctx.report_expr() != null) {
    		queryExpr.setResultPostProc((ResultPostProcNode)visit(ctx.report_expr()));
    	}

    	return setAql(queryExpr, ctx);
    }    
    
    @Override
    public SelectListNode visitSelectExpr(AQLParser.SelectExprContext ctx) {
    	SelectListNode list = new SelectListNode();    	
    	list.setDistinct(ctx.DISTINCT() != null);
    	
    	for (int i = 0; i < ctx.select_element().size(); ++i) {
    		list.addElement((ExpressionNode)visit(ctx.select_element(i)));    		
    	}
    	
    	return setAql(list, ctx);
    }

	@Override
	public OrderExprListNode visitOrderExpr(AQLParser.OrderExprContext ctx) {
		List<OrderExprNode> exprs = new ArrayList<>();
		for (int i = 0; i < ctx.order_element().size(); ++i) {
			exprs.add((OrderExprNode)visit(ctx.order_element(i)));
		}

		OrderExprListNode orderBy = new OrderExprListNode();
		orderBy.setExprs(exprs);
		return setAql(orderBy, ctx);
	}

	@Override
	public OrderExprNode visitOrderElement(AQLParser.OrderElementContext ctx) {
		OrderExprNode expr = new OrderExprNode();
		expr.setExpr((ExpressionNode)visit(ctx.arith_expr()));
		if (ctx.ORD_DIR() != null) {
			expr.setDescending(ctx.ORD_DIR().getText().equals("desc"));
		}

		return setAql(expr, ctx);
	}

    @Override
    public LimitExprNode visitLimitExpr(AQLParser.LimitExprContext ctx) {
    	LimitExprNode limitExpr = new LimitExprNode();
    	if (ctx.INT().size() == 2) {
    		limitExpr.setStartAt(Integer.parseInt(ctx.INT(0).getText()));
    		limitExpr.setNumRecords(Integer.parseInt(ctx.INT(1).getText()));
    	} else {
    		limitExpr.setNumRecords(Integer.parseInt(ctx.INT(0).getText()));
    	}
    	
    	return setAql(limitExpr, ctx);
    }
    
    @Override
    public ExpressionNode visitSelectElement(AQLParser.SelectElementContext ctx) {
    	ExpressionNode expr = (ExpressionNode)visit(ctx.arith_expr());
    	if (ctx.SLITERAL() != null) {
    		String text = ctx.SLITERAL().getText();
    		expr.setLabel(text.substring(1, text.length() - 1));
    	}

    	// Purposely not setAql because it will also include label if any
    	return expr;
    }

    @Override
    public FilterExpressionNode visitAndFilterExpr(AQLParser.AndFilterExprContext ctx) {
        FilterExpressionNode node = FilterExpressionNode.andExpr(
        	(FilterNodeMarker)visit(ctx.filter_expr(0)),
        	(FilterNodeMarker)visit(ctx.filter_expr(1)));
		return setAql(node, ctx);
    }

    @Override
    public Node visitOrFilterExpr(AQLParser.OrFilterExprContext ctx) {
        FilterExpressionNode node = FilterExpressionNode.orExpr(
        	(FilterNodeMarker)visit(ctx.filter_expr(0)),
        	(FilterNodeMarker)visit(ctx.filter_expr(1)));
		return setAql(node, ctx);
    }
    
    @Override
    public Node visitPandFilterExpr(AQLParser.PandFilterExprContext ctx) {
		FilterExpressionNode node =  FilterExpressionNode.pAndExpr(
    		(FilterNodeMarker)visit(ctx.filter_expr(0)),
    		(FilterNodeMarker)visit(ctx.filter_expr(1)));
		return setAql(node, ctx);
    }
    
    @Override
    public Node visitNotFilterExpr(AQLParser.NotFilterExprContext ctx) {
        return setAql(FilterExpressionNode.notExpr((FilterNodeMarker)visit(ctx.filter_expr())), ctx);
    }

    @Override
    public FilterExpressionNode visitParensFilterExpr(AQLParser.ParensFilterExprContext ctx) {
        return setAql(FilterExpressionNode.parenExpr((FilterNodeMarker)visit(ctx.filter_expr())), ctx);
    }
    
    @Override
    public FilterExpressionNode visitNthChildFilterExpr(AQLParser.NthChildFilterExprContext ctx) { 
        return setAql(FilterExpressionNode.nthChildExpr((FilterNodeMarker)visit(ctx.filter_expr())), ctx);
    }

    @Override
    public FilterExpressionNode visitSimpleFilter(AQLParser.SimpleFilterContext ctx) {
    	return setAql(FilterExpressionNode.identity((FilterNodeMarker)visit(ctx.filter())), ctx);
    }
    
    @Override
    public FilterNode visitBasicFilter(AQLParser.BasicFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode)visit(ctx.arith_expr(0)));
    	filter.setRhs((ExpressionNode)visit(ctx.arith_expr(1)));
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.OP().getText()));
    	return setAql(filter, ctx);
    }
    
    @Override
    public FilterExpressionNode visitMvFilter(AQLParser.MvFilterContext ctx) {
		ExpressionNode lhs = (ExpressionNode)visit(ctx.arith_expr());
		RelationalOp relOp = RelationalOp.getBySymbol(ctx.MOP().getText());

    	LiteralValueListNode list = (LiteralValueListNode)visit(ctx.literal_values());
    	int numLiterals = list.size();
    	List<FilterNodeMarker> filters = new ArrayList<>();
    	int literalsLimit = 995;

    	for (int i = 0; i < numLiterals; i += literalsLimit) {
    		List<LiteralValueNode> literals = list.getLiteralVals()
				.subList(i, (i + literalsLimit) < numLiterals ? i + literalsLimit : numLiterals);

    		LiteralValueListNode subList = new LiteralValueListNode();
    		literals.forEach(literal -> subList.addLiteralVal(literal));

    		FilterNode filter = new FilterNode();
    		filter.setLhs(lhs);
			filter.setRelOp(RelationalOp.IN);
			filter.setRhs(subList);
			filters.add(filter);
		}

		FilterExpressionNode logicalExpr = or(filters);
		if (relOp == RelationalOp.NOT_IN) {
			FilterExpressionNode notExpr = new FilterExpressionNode();
			notExpr.setOperator(FilterExpressionNode.Op.NOT);
			notExpr.setOperands(Collections.singletonList(logicalExpr));
			logicalExpr = notExpr;
		}

		logicalExpr = FilterExpressionNode.parenExpr(logicalExpr);
    	return setAql(logicalExpr, ctx);
    }

    private FilterExpressionNode or(List<FilterNodeMarker> filters) {
		FilterExpressionNode result = new FilterExpressionNode();
		switch (filters.size()) {
			case 0:
			case 1:
				result.setOperator(FilterExpressionNode.Op.IDENTITY);
				result.setOperands(filters);
				break;

			case 2:
				result.setOperator(FilterExpressionNode.Op.OR);
				result.setOperands(filters);
				break;

			default:
				result.setOperator(FilterExpressionNode.Op.OR);
				result.setOperands(Arrays.asList(filters.get(0), or(filters.subList(1, filters.size()))));
				break;
		}

		return result;
	}

    @Override
    public FilterNode visitSubQueryFilter(AQLParser.SubQueryFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode)visit(ctx.arith_expr()));
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.MOP().getText()));
    	filter.setSubQuery((QueryExpressionNode) visit(ctx.query_expr()));
    	return setAql(filter, ctx);
	}

	@Override
	public FilterNode visitSqlFilter(AQLParser.SqlFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode) visit(ctx.arith_expr()));
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.MOP().getText()));
    	filter.setSql(ctx.SLITERAL().getText());
    	return setAql(filter, ctx);
	}

	@Override
	public FilterNode visitConcatCompFilter(AQLParser.ConcatCompFilterContext ctx) {
		FilterNode filter = new FilterNode();
		filter.setLhs((ExpressionNode)visit(ctx.concat_expr()));

		LiteralValueNode value = new LiteralValueNode(DataType.STRING);
		value.getValues().add(ctx.SLITERAL().getText());
		filter.setRhs(value);

		filter.setRelOp(RelationalOp.getBySymbol(ctx.SOP().getText()));
		return setAql(filter, ctx);
	}

	@Override
	public FilterNode visitConcatWsCompFilter(AQLParser.ConcatWsCompFilterContext ctx) {
		FilterNode filter = new FilterNode();
		filter.setLhs((ExpressionNode)visit(ctx.concat_ws_expr()));

		LiteralValueNode value = new LiteralValueNode(DataType.STRING);
		value.getValues().add(ctx.SLITERAL().getText());
		filter.setRhs(value);

		filter.setRelOp(RelationalOp.getBySymbol(ctx.SOP().getText()));
		return setAql(filter, ctx);
	}

    @Override
    public FilterNode visitStringCompFilter(AQLParser.StringCompFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	
    	FieldNode field = new FieldNode();
    	field.setName(ctx.FIELD().getText());    	
    	filter.setLhs(field);
    	
    	LiteralValueNode value = new LiteralValueNode(DataType.STRING);
    	value.getValues().add(ctx.SLITERAL().getText());
    	filter.setRhs(value);
    	
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.SOP().getText()));
    	return setAql(filter, ctx);
    }
    
    @Override
    public FilterNode visitUnaryFilter(AQLParser.UnaryFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode)visit(ctx.arith_expr()));
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.UOP().getText()));
    	return setAql(filter, ctx);
    }

	public FilterNode visitDateRangeFilter(AQLParser.DateRangeFilterContext ctx) {
		FilterNode filter = new FilterNode();
		filter.setLhs((ExpressionNode)visit(ctx.date_range()));
		filter.setRelOp(RelationalOp.BETWEEN);
		return setAql(filter, ctx);
	}

	@Override
	public FilterNode visitBetweenFilter(AQLParser.BetweenFilterContext ctx) {
		FilterNode filter = new FilterNode();

		BetweenNode betweenNode = new BetweenNode();
		betweenNode.setLhs((ExpressionNode)visit(ctx.arith_expr(0)));
		betweenNode.setMinNode((ExpressionNode)visit(ctx.arith_expr(1)));
		betweenNode.setMaxNode((ExpressionNode)visit(ctx.arith_expr(2)));

		filter.setRelOp(RelationalOp.BETWEEN);
		filter.setLhs(betweenNode);
		return setAql(filter, ctx);
	}

	@Override
	public FilterExpressionNode visitHavingExpr(AQLParser.HavingExprContext ctx) {
    	FilterExpressionNode node = (FilterExpressionNode) visit(ctx.agg_filter_expr());
    	return setAql(node, ctx);
	}

	@Override
	public FilterExpressionNode visitAndAggFilterExpr(AQLParser.AndAggFilterExprContext ctx) {
		FilterExpressionNode node = FilterExpressionNode.andExpr(
			(FilterNodeMarker)visit(ctx.agg_filter_expr(0)),
			(FilterNodeMarker)visit(ctx.agg_filter_expr(1)));
		return setAql(node, ctx);
	}

	@Override
	public Node visitOrAggFilterExpr(AQLParser.OrAggFilterExprContext ctx) {
		FilterExpressionNode node = FilterExpressionNode.orExpr(
			(FilterNodeMarker)visit(ctx.agg_filter_expr(0)),
			(FilterNodeMarker)visit(ctx.agg_filter_expr(1)));
		return setAql(node, ctx);
	}

	@Override
	public Node visitNotAggFilterExpr(AQLParser.NotAggFilterExprContext ctx) {
		return setAql(FilterExpressionNode.notExpr((FilterNodeMarker)visit(ctx.agg_filter_expr())), ctx);
	}

	@Override
	public FilterExpressionNode visitParensAggFilterExpr(AQLParser.ParensAggFilterExprContext ctx) {
		return setAql(FilterExpressionNode.parenExpr((FilterNodeMarker)visit(ctx.agg_filter_expr())), ctx);
	}

	@Override
	public FilterExpressionNode visitSimpleAggFilter(AQLParser.SimpleAggFilterContext ctx) {
		return setAql(FilterExpressionNode.identity((FilterNodeMarker)visit(ctx.agg_filter())), ctx);
	}

	@Override
	public FilterNode visitAggFilter(AQLParser.AggFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode)visit(ctx.agg_expr()));
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.OP().getText()));

    	LiteralValueNode rhs = null;
    	if (ctx.INT() != null) {
    		rhs = new LiteralValueNode(DataType.INTEGER);
			rhs.getValues().add(Long.parseLong(ctx.INT().getText()));
		} else {
    		rhs = new LiteralValueNode(DataType.FLOAT);
			rhs.getValues().add(Double.parseDouble(ctx.FLOAT().getText()));
		}

    	filter.setRhs(rhs);
    	return setAql(filter, ctx);
	}
    
    public LiteralValueListNode visitLiteral_values(AQLParser.Literal_valuesContext ctx) {
    	LiteralValueListNode literals = new LiteralValueListNode();
    	for (LiteralContext literalCtx : ctx.literal()) {
    		literals.addLiteralVal((LiteralValueNode)visit(literalCtx));
    	}
    	
    	return setAql(literals, ctx);
    }

    @Override
    public ArithExpressionNode visitArithExpr(AQLParser.ArithExprContext ctx) {
    	ExpressionNode loperand = (ExpressionNode)visit(ctx.arith_expr(0));
    	ExpressionNode roperand = (ExpressionNode)visit(ctx.arith_expr(1));
    	ArithOp op = ArithOp.getBySymbol(ctx.ARITH_OP().getText());
    	
    	ArithExpressionNode expr = new ArithExpressionNode();
    	expr.setLeftOperand(loperand);
    	expr.setRightOperand(roperand);
    	expr.setOp(op);
    	return setAql(expr, ctx);
    }

    @Override 
    public ArithExpressionNode visitDateIntervalExpr(AQLParser.DateIntervalExprContext ctx) {
    	ExpressionNode loperand = (ExpressionNode)visit(ctx.arith_expr());
    	ExpressionNode roperand = (ExpressionNode)visit(ctx.date_interval());    	
    	ArithOp op = ArithOp.getBySymbol(ctx.ARITH_OP().getText());
    	    	
    	ArithExpressionNode expr = new ArithExpressionNode();
    	expr.setLeftOperand(loperand);
    	expr.setRightOperand(roperand);
    	expr.setOp(op);    	
    	return setAql(expr, ctx);
    }

    @Override 
    public ExpressionNode visitParensArithExpr(AQLParser.ParensArithExprContext ctx) { 
    	return setAql((ExpressionNode)visit(ctx.arith_expr()), ctx);
    }

    @Override
    public DateFormatFuncNode visitDateFmtFunc(AQLParser.DateFmtFuncContext ctx) {
		DateFormatFuncNode expr = new DateFormatFuncNode();
		expr.setDateExpr((ExpressionNode) visit(ctx.arith_expr()));

		String formatText = ctx.SLITERAL().getText();
		expr.setFormat(formatText.substring(1, formatText.length() - 1)); // remove quotes
		return setAql(expr, ctx);
	}
    
    @Override 
    public DateDiffFuncNode visitMonthsDiffFunc(AQLParser.MonthsDiffFuncContext ctx) { 
    	ExpressionNode leftOperand = (ExpressionNode)visit(ctx.arith_expr(0));
    	ExpressionNode rightOperand = (ExpressionNode)visit(ctx.arith_expr(1));
    	return setAql(getDateDiffFuncNode(DiffType.MONTH, leftOperand, rightOperand), ctx);
    }

    @Override 
    public DateDiffFuncNode visitYearsDiffFunc(AQLParser.YearsDiffFuncContext ctx) {
    	ExpressionNode leftOperand = (ExpressionNode)visit(ctx.arith_expr(0));
    	ExpressionNode rightOperand = (ExpressionNode)visit(ctx.arith_expr(1));
    	return setAql(getDateDiffFuncNode(DiffType.YEAR, leftOperand, rightOperand), ctx);
    }

	@Override
	public DateDiffFuncNode visitMinsDiffFunc(AQLParser.MinsDiffFuncContext ctx) {
		ExpressionNode leftOperand = (ExpressionNode)visit(ctx.arith_expr(0));
		ExpressionNode rightOperand = (ExpressionNode)visit(ctx.arith_expr(1));
		return setAql(getDateDiffFuncNode(DiffType.MINUTES, leftOperand, rightOperand), ctx);
	}
    
    @Override
    public CurrentDateNode visitCurrentDateFunc(AQLParser.CurrentDateFuncContext ctx) {
    	return setAql(new CurrentDateNode(), ctx);
    }
    
    @Override
    public AggregateNode visitAggExpr(AQLParser.AggExprContext ctx) {
    	return setAql((AggregateNode)visit(ctx.agg_expr()), ctx);
    }

	@Override
	public DateRangeFuncNode visitDateRangeFunc(AQLParser.DateRangeFuncContext ctx) {
		DateRangeFuncNode func = new DateRangeFuncNode();
		func.setDateExpr((ExpressionNode)visit(ctx.arith_expr()));
		func.setRangeType(DateRangeFuncNode.RangeType.from(ctx.ID().getText()));

		if (ctx.INT() != null) {
			func.setRange(Integer.parseInt(ctx.INT().getText()));
		}

		return setAql(func, ctx);
	}

    @Override
    public AggregateNode visitAggFunc(AQLParser.AggFuncContext ctx) {
    	AggregateNode countNode = new AggregateNode();
    	if (ctx.COUNT() != null) {
			countNode.setAggFn(AGG_FN.COUNT);
		} else if (ctx.CCOUNT() != null) {
			countNode.setAggFn(AGG_FN.CCOUNT);
    	} else if (ctx.SUM() != null) {
			countNode.setAggFn(AGG_FN.SUM);
		} else if (ctx.CSUM() != null) {
			countNode.setAggFn(AGG_FN.CSUM);
    	} else if (ctx.MIN() != null) {
    		countNode.setAggFn(AGG_FN.MIN);
    	} else if (ctx.MAX() != null) {
    		countNode.setAggFn(AGG_FN.MAX);
    	} else if (ctx.AVG() != null) {
    		countNode.setAggFn(AGG_FN.AVG);
    	} else {
    		throw new FormException("Unknown aggregate function");
    	}
    	    	
    	if (ctx.DISTINCT() != null) {
    		countNode.setDistinct(true);
    	}
    	
    	FieldNode field = new FieldNode();
    	field.setName(ctx.FIELD().getText());
    	countNode.setField(field);    	
    	return setAql(countNode, ctx);
    }

	@Override
	public ConcatNode visitConcatExpr(AQLParser.ConcatExprContext ctx) {
		return setAql((ConcatNode)visit(ctx.concat_expr()), ctx);
	}

	@Override
	public ConcatNode visitConcatFunc(AQLParser.ConcatFuncContext ctx) {
		ConcatNode concatNode = new ConcatNode();
		for (int i = 0; i < ctx.arith_expr().size(); ++i) {
			concatNode.addArg((ExpressionNode)visit(ctx.arith_expr(i)));
		}

		return setAql(concatNode, ctx);
	}

	@Override
	public ConcatNode visitConcatWsExpr(AQLParser.ConcatWsExprContext ctx) {
		return setAql((ConcatNode)visit(ctx.concat_ws_expr()), ctx);
	}

	@Override
	public ConcatNode visitConcatWsFunc(AQLParser.ConcatWsFuncContext ctx) {
		ConcatNode concatNode = new ConcatNode();
		concatNode.setSeparator(ctx.SLITERAL().getText());
		for (int i = 0; i < ctx.arith_expr().size(); ++i) {
			concatNode.addArg((ExpressionNode)visit(ctx.arith_expr(i)));
		}

		return setAql(concatNode, ctx);
	}
    
    public RoundOffNode visitRoundFunc(AQLParser.RoundFuncContext ctx) {
    	RoundOffNode node = new RoundOffNode();
    	node.setExprNode((ExpressionNode)visit(ctx.arith_expr()));
    	node.setNoOfDigitsAfterDecimal(Integer.parseInt(ctx.INT().getText()));
    	return setAql(node, ctx);
    }

    @Override
    public FieldNode visitField(AQLParser.FieldContext ctx) {
    	FieldNode field = new FieldNode();
    	field.setName(ctx.FIELD().getText());
    	return setAql(field, ctx);
    }    
        
    @Override
    public LiteralValueNode visitStringLiteral(AQLParser.StringLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.STRING);
    	value.getValues().add(ctx.SLITERAL().getText());
    	return setAql(value, ctx);
    }
    
    @Override
    public LiteralValueNode visitIntLiteral(AQLParser.IntLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.INTEGER);
    	value.getValues().add(Long.parseLong(ctx.INT().getText()));
    	return setAql(value, ctx);
    }

    @Override
    public LiteralValueNode visitFloatLiteral(AQLParser.FloatLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.FLOAT);
    	value.getValues().add(Double.parseDouble(ctx.FLOAT().getText()));
    	return setAql(value, ctx);
    }
    
    @Override
    public LiteralValueNode visitBoolLiteral(AQLParser.BoolLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.BOOLEAN);
    	value.getValues().add(Boolean.parseBoolean(ctx.BOOL().getText()));
    	return setAql(value, ctx);
    }
    
    @Override 
    public DateIntervalNode visitDate_interval(AQLParser.Date_intervalContext ctx) {
    	DateIntervalNode di = new DateIntervalNode();
    	
    	di.setDays(diPart(ctx.DAY()));
    	di.setMonths(diPart(ctx.MONTH()));
    	di.setYears(diPart(ctx.YEAR()));
    	return setAql(di, ctx);
    }
    
    @Override
    public CrosstabNode visitCrossTabExpr(AQLParser.CrossTabExprContext ctx) {
    	CrosstabNode crosstabSpec = new CrosstabNode();
    	crosstabSpec.setName(ctx.CROSSTAB().getText());
    	
    	List<Integer> rowCols = new ArrayList<Integer>();
    	for (Token rowIdx : ctx.row) {
    		rowCols.add(getInt(rowIdx));
    	}    	
    	crosstabSpec.setRowGroupByColumns(rowCols);
    	
    	crosstabSpec.setColGroupByColumn(getInt(ctx.col));

    	List<Integer> valCols = new ArrayList<Integer>();
    	for (Token valIdx : ctx.value) {
    		valCols.add(getInt(valIdx));
    	}
    	crosstabSpec.setMeasureColumns(valCols);
    	if (ctx.BOOL() != null) {
    		crosstabSpec.setIncludeSubTotals(Boolean.parseBoolean(ctx.BOOL().getText()));
    	}
    	
    	return setAql(crosstabSpec, ctx);
    }
    
    @Override
    public ResultPostProcNode visitReportExpr(AQLParser.ReportExprContext ctx) {
    	List<String> args = new ArrayList<>();
    	if (ctx.SLITERAL() != null) {
    		for (TerminalNode tn : ctx.SLITERAL()) {
    			String text = tn.getText();
    			args.add(text.substring(1, text.length() - 1).trim()); // remove quotes
    		}
    	}
    	
    	ResultPostProcNode postProc = new ResultPostProcNode();
    	postProc.setName(ctx.ID().getText());
    	postProc.setArgs(args);
    	return setAql(postProc, ctx);
    }
    
    private DateDiffFuncNode getDateDiffFuncNode(DiffType diffType, ExpressionNode leftOperand, ExpressionNode rightOperand) {
    	DateDiffFuncNode diff = new DateDiffFuncNode();
    	diff.setDiffType(diffType);    	
    	diff.setLeftOperand(leftOperand);
    	diff.setRightOperand(rightOperand);
    	return diff;
    }
    
    private int diPart(TerminalNode term) {
    	int result = 0;
    	
    	if (term != null && term.getText() != null && !term.getText().isEmpty()) {
    		String text = term.getText().substring(0, term.getText().length() - 1);
    		result = Integer.parseInt(text);
    	}
    	
    	return result;
    }
    
//    private int getInt(TerminalNode node) {
//    	return Integer.parseInt(node.getText());
//    }
    
    private int getInt(Token token) {
    	return Integer.parseInt(token.getText());
    }

    private <T extends Node> T setAql(T node, ParseTree tree) {
		node.setAql(getAql(tree));
		return node;
	}

	private String getAql(ParseTree tree) {
		StringBuilder aql = new StringBuilder();
		for (int i = 0; i < tree.getChildCount(); ++i) {
			if (tree.getChild(i) instanceof TerminalNode) {
				aql.append(((TerminalNode) tree.getChild(i)).getSymbol().getText()).append(" ");
			} else {
				aql.append(getAql(tree.getChild(i)));
			}
		}

		return aql.toString().trim();
	}
}
