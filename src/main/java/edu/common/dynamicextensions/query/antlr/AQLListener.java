// Generated from AQL.g4 by ANTLR 4.5.1
package edu.common.dynamicextensions.query.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AQLParser}.
 */
public interface AQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AQLParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(AQLParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(AQLParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code QueryExpr}
	 * labeled alternative in {@link AQLParser#query_expr}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpr(AQLParser.QueryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code QueryExpr}
	 * labeled alternative in {@link AQLParser#query_expr}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpr(AQLParser.QueryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectExpr}
	 * labeled alternative in {@link AQLParser#select_list}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpr(AQLParser.SelectExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectExpr}
	 * labeled alternative in {@link AQLParser#select_list}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpr(AQLParser.SelectExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectElement}
	 * labeled alternative in {@link AQLParser#select_element}.
	 * @param ctx the parse tree
	 */
	void enterSelectElement(AQLParser.SelectElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectElement}
	 * labeled alternative in {@link AQLParser#select_element}.
	 * @param ctx the parse tree
	 */
	void exitSelectElement(AQLParser.SelectElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterOrFilterExpr(AQLParser.OrFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitOrFilterExpr(AQLParser.OrFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NthChildFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterNthChildFilterExpr(AQLParser.NthChildFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NthChildFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitNthChildFilterExpr(AQLParser.NthChildFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SimpleFilter}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFilter(AQLParser.SimpleFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SimpleFilter}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFilter(AQLParser.SimpleFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterAndFilterExpr(AQLParser.AndFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitAndFilterExpr(AQLParser.AndFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PandFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterPandFilterExpr(AQLParser.PandFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PandFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitPandFilterExpr(AQLParser.PandFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterNotFilterExpr(AQLParser.NotFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitNotFilterExpr(AQLParser.NotFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParensFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterParensFilterExpr(AQLParser.ParensFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParensFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitParensFilterExpr(AQLParser.ParensFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HavingExpr}
	 * labeled alternative in {@link AQLParser#having_expr}.
	 * @param ctx the parse tree
	 */
	void enterHavingExpr(AQLParser.HavingExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HavingExpr}
	 * labeled alternative in {@link AQLParser#having_expr}.
	 * @param ctx the parse tree
	 */
	void exitHavingExpr(AQLParser.HavingExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterNotAggFilterExpr(AQLParser.NotAggFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitNotAggFilterExpr(AQLParser.NotAggFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterAndAggFilterExpr(AQLParser.AndAggFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitAndAggFilterExpr(AQLParser.AndAggFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SimpleAggFilter}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAggFilter(AQLParser.SimpleAggFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SimpleAggFilter}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAggFilter(AQLParser.SimpleAggFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParensAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterParensAggFilterExpr(AQLParser.ParensAggFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParensAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitParensAggFilterExpr(AQLParser.ParensAggFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void enterOrAggFilterExpr(AQLParser.OrAggFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 */
	void exitOrAggFilterExpr(AQLParser.OrAggFilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrderExpr}
	 * labeled alternative in {@link AQLParser#order_expr}.
	 * @param ctx the parse tree
	 */
	void enterOrderExpr(AQLParser.OrderExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrderExpr}
	 * labeled alternative in {@link AQLParser#order_expr}.
	 * @param ctx the parse tree
	 */
	void exitOrderExpr(AQLParser.OrderExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrderElement}
	 * labeled alternative in {@link AQLParser#order_element}.
	 * @param ctx the parse tree
	 */
	void enterOrderElement(AQLParser.OrderElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrderElement}
	 * labeled alternative in {@link AQLParser#order_element}.
	 * @param ctx the parse tree
	 */
	void exitOrderElement(AQLParser.OrderElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LimitExpr}
	 * labeled alternative in {@link AQLParser#limit_expr}.
	 * @param ctx the parse tree
	 */
	void enterLimitExpr(AQLParser.LimitExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LimitExpr}
	 * labeled alternative in {@link AQLParser#limit_expr}.
	 * @param ctx the parse tree
	 */
	void exitLimitExpr(AQLParser.LimitExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CrossTabExpr}
	 * labeled alternative in {@link AQLParser#crosstab_expr}.
	 * @param ctx the parse tree
	 */
	void enterCrossTabExpr(AQLParser.CrossTabExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CrossTabExpr}
	 * labeled alternative in {@link AQLParser#crosstab_expr}.
	 * @param ctx the parse tree
	 */
	void exitCrossTabExpr(AQLParser.CrossTabExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReportExpr}
	 * labeled alternative in {@link AQLParser#report_expr}.
	 * @param ctx the parse tree
	 */
	void enterReportExpr(AQLParser.ReportExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReportExpr}
	 * labeled alternative in {@link AQLParser#report_expr}.
	 * @param ctx the parse tree
	 */
	void exitReportExpr(AQLParser.ReportExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BasicFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterBasicFilter(AQLParser.BasicFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BasicFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitBasicFilter(AQLParser.BasicFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MvFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterMvFilter(AQLParser.MvFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MvFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitMvFilter(AQLParser.MvFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubQueryFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterSubQueryFilter(AQLParser.SubQueryFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubQueryFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitSubQueryFilter(AQLParser.SubQueryFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SqlFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterSqlFilter(AQLParser.SqlFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SqlFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitSqlFilter(AQLParser.SqlFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcatCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterConcatCompFilter(AQLParser.ConcatCompFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcatCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitConcatCompFilter(AQLParser.ConcatCompFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcatWsCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterConcatWsCompFilter(AQLParser.ConcatWsCompFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcatWsCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitConcatWsCompFilter(AQLParser.ConcatWsCompFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterStringCompFilter(AQLParser.StringCompFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitStringCompFilter(AQLParser.StringCompFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterUnaryFilter(AQLParser.UnaryFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitUnaryFilter(AQLParser.UnaryFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateRangeFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterDateRangeFilter(AQLParser.DateRangeFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateRangeFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitDateRangeFilter(AQLParser.DateRangeFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BetweenFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterBetweenFilter(AQLParser.BetweenFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BetweenFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitBetweenFilter(AQLParser.BetweenFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AggFilter}
	 * labeled alternative in {@link AQLParser#agg_filter}.
	 * @param ctx the parse tree
	 */
	void enterAggFilter(AQLParser.AggFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AggFilter}
	 * labeled alternative in {@link AQLParser#agg_filter}.
	 * @param ctx the parse tree
	 */
	void exitAggFilter(AQLParser.AggFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link AQLParser#literal_values}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_values(AQLParser.Literal_valuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#literal_values}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_values(AQLParser.Literal_valuesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(AQLParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(AQLParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(AQLParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(AQLParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(AQLParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(AQLParser.FloatLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(AQLParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(AQLParser.BoolLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArithExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithExpr(AQLParser.ArithExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithExpr(AQLParser.ArithExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateFmtFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterDateFmtFunc(AQLParser.DateFmtFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateFmtFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitDateFmtFunc(AQLParser.DateFmtFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MonthsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterMonthsDiffFunc(AQLParser.MonthsDiffFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MonthsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitMonthsDiffFunc(AQLParser.MonthsDiffFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RoundFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterRoundFunc(AQLParser.RoundFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RoundFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitRoundFunc(AQLParser.RoundFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateIntervalExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterDateIntervalExpr(AQLParser.DateIntervalExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateIntervalExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitDateIntervalExpr(AQLParser.DateIntervalExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcatExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterConcatExpr(AQLParser.ConcatExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcatExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitConcatExpr(AQLParser.ConcatExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Field}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterField(AQLParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Field}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitField(AQLParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurrentDateFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterCurrentDateFunc(AQLParser.CurrentDateFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurrentDateFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitCurrentDateFunc(AQLParser.CurrentDateFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AggExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterAggExpr(AQLParser.AggExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AggExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitAggExpr(AQLParser.AggExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParensArithExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterParensArithExpr(AQLParser.ParensArithExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParensArithExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitParensArithExpr(AQLParser.ParensArithExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code YearsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterYearsDiffFunc(AQLParser.YearsDiffFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code YearsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitYearsDiffFunc(AQLParser.YearsDiffFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcatWsExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterConcatWsExpr(AQLParser.ConcatWsExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcatWsExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitConcatWsExpr(AQLParser.ConcatWsExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralVal}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralVal(AQLParser.LiteralValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralVal}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralVal(AQLParser.LiteralValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MinsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterMinsDiffFunc(AQLParser.MinsDiffFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MinsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitMinsDiffFunc(AQLParser.MinsDiffFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateRangeFunc}
	 * labeled alternative in {@link AQLParser#date_range}.
	 * @param ctx the parse tree
	 */
	void enterDateRangeFunc(AQLParser.DateRangeFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateRangeFunc}
	 * labeled alternative in {@link AQLParser#date_range}.
	 * @param ctx the parse tree
	 */
	void exitDateRangeFunc(AQLParser.DateRangeFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AggFunc}
	 * labeled alternative in {@link AQLParser#agg_expr}.
	 * @param ctx the parse tree
	 */
	void enterAggFunc(AQLParser.AggFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AggFunc}
	 * labeled alternative in {@link AQLParser#agg_expr}.
	 * @param ctx the parse tree
	 */
	void exitAggFunc(AQLParser.AggFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcatFunc}
	 * labeled alternative in {@link AQLParser#concat_expr}.
	 * @param ctx the parse tree
	 */
	void enterConcatFunc(AQLParser.ConcatFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcatFunc}
	 * labeled alternative in {@link AQLParser#concat_expr}.
	 * @param ctx the parse tree
	 */
	void exitConcatFunc(AQLParser.ConcatFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcatWsFunc}
	 * labeled alternative in {@link AQLParser#concat_ws_expr}.
	 * @param ctx the parse tree
	 */
	void enterConcatWsFunc(AQLParser.ConcatWsFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcatWsFunc}
	 * labeled alternative in {@link AQLParser#concat_ws_expr}.
	 * @param ctx the parse tree
	 */
	void exitConcatWsFunc(AQLParser.ConcatWsFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link AQLParser#date_interval}.
	 * @param ctx the parse tree
	 */
	void enterDate_interval(AQLParser.Date_intervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#date_interval}.
	 * @param ctx the parse tree
	 */
	void exitDate_interval(AQLParser.Date_intervalContext ctx);
}