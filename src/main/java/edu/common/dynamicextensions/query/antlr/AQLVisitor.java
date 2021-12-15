// Generated from AQL.g4 by ANTLR 4.5.1
package edu.common.dynamicextensions.query.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AQLParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(AQLParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QueryExpr}
	 * labeled alternative in {@link AQLParser#query_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpr(AQLParser.QueryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelectExpr}
	 * labeled alternative in {@link AQLParser#select_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpr(AQLParser.SelectExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelectElement}
	 * labeled alternative in {@link AQLParser#select_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectElement(AQLParser.SelectElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrFilterExpr(AQLParser.OrFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NthChildFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNthChildFilterExpr(AQLParser.NthChildFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleFilter}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleFilter(AQLParser.SimpleFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndFilterExpr(AQLParser.AndFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PandFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPandFilterExpr(AQLParser.PandFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotFilterExpr(AQLParser.NotFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParensFilterExpr}
	 * labeled alternative in {@link AQLParser#filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensFilterExpr(AQLParser.ParensFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code HavingExpr}
	 * labeled alternative in {@link AQLParser#having_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingExpr(AQLParser.HavingExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotAggFilterExpr(AQLParser.NotAggFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndAggFilterExpr(AQLParser.AndAggFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleAggFilter}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleAggFilter(AQLParser.SimpleAggFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParensAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensAggFilterExpr(AQLParser.ParensAggFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrAggFilterExpr}
	 * labeled alternative in {@link AQLParser#agg_filter_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrAggFilterExpr(AQLParser.OrAggFilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrderExpr}
	 * labeled alternative in {@link AQLParser#order_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderExpr(AQLParser.OrderExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrderElement}
	 * labeled alternative in {@link AQLParser#order_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderElement(AQLParser.OrderElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LimitExpr}
	 * labeled alternative in {@link AQLParser#limit_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitExpr(AQLParser.LimitExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CrossTabExpr}
	 * labeled alternative in {@link AQLParser#crosstab_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrossTabExpr(AQLParser.CrossTabExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReportExpr}
	 * labeled alternative in {@link AQLParser#report_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReportExpr(AQLParser.ReportExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BasicFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicFilter(AQLParser.BasicFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MvFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMvFilter(AQLParser.MvFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubQueryFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubQueryFilter(AQLParser.SubQueryFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SqlFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlFilter(AQLParser.SqlFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConcatCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatCompFilter(AQLParser.ConcatCompFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConcatWsCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatWsCompFilter(AQLParser.ConcatWsCompFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringCompFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringCompFilter(AQLParser.StringCompFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryFilter(AQLParser.UnaryFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateRangeFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateRangeFilter(AQLParser.DateRangeFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BetweenFilter}
	 * labeled alternative in {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetweenFilter(AQLParser.BetweenFilterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AggFilter}
	 * labeled alternative in {@link AQLParser#agg_filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggFilter(AQLParser.AggFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link AQLParser#literal_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral_values(AQLParser.Literal_valuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(AQLParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(AQLParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatLiteral(AQLParser.FloatLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link AQLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(AQLParser.BoolLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArithExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithExpr(AQLParser.ArithExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateFmtFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateFmtFunc(AQLParser.DateFmtFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MonthsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonthsDiffFunc(AQLParser.MonthsDiffFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RoundFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoundFunc(AQLParser.RoundFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateIntervalExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateIntervalExpr(AQLParser.DateIntervalExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConcatExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatExpr(AQLParser.ConcatExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Field}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(AQLParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CurrentDateFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentDateFunc(AQLParser.CurrentDateFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AggExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggExpr(AQLParser.AggExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParensArithExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensArithExpr(AQLParser.ParensArithExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code YearsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYearsDiffFunc(AQLParser.YearsDiffFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConcatWsExpr}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatWsExpr(AQLParser.ConcatWsExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralVal}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralVal(AQLParser.LiteralValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MinsDiffFunc}
	 * labeled alternative in {@link AQLParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinsDiffFunc(AQLParser.MinsDiffFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DateRangeFunc}
	 * labeled alternative in {@link AQLParser#date_range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateRangeFunc(AQLParser.DateRangeFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AggFunc}
	 * labeled alternative in {@link AQLParser#agg_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggFunc(AQLParser.AggFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConcatFunc}
	 * labeled alternative in {@link AQLParser#concat_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatFunc(AQLParser.ConcatFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConcatWsFunc}
	 * labeled alternative in {@link AQLParser#concat_ws_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatWsFunc(AQLParser.ConcatWsFuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link AQLParser#date_interval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate_interval(AQLParser.Date_intervalContext ctx);
}