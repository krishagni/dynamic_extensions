// Generated from AQL.g4 by ANTLR 4.5.1
package edu.common.dynamicextensions.query.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AQLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, WS=3, SELECT=4, WHERE=5, NTHCHILD=6, BETWEEN=7, DATE_FMT=8, 
		MTHS_BTWN=9, YRS_BTWN=10, CURR_DATE=11, MINS_BTWN=12, DATE_RANGE=13, COUNT=14, 
		CCOUNT=15, SUM=16, CSUM=17, MIN=18, MAX=19, AVG=20, DISTINCT=21, HAVING=22, 
		ORDER=23, ORD_DIR=24, LIMIT=25, CROSSTAB=26, CONCAT=27, CONCAT_WS=28, 
		OR=29, AND=30, PAND=31, NOT=32, ROUND=33, SQL=34, LP=35, RP=36, MOP=37, 
		SOP=38, UOP=39, BOOL=40, OP=41, INT=42, FLOAT=43, YEAR=44, MONTH=45, DAY=46, 
		DIGIT=47, ID=48, FIELD=49, SLITERAL=50, ESC=51, ARITH_OP=52, ERROR=53, 
		QUOTE=54;
	public static final int
		RULE_query = 0, RULE_query_expr = 1, RULE_select_list = 2, RULE_select_element = 3, 
		RULE_filter_expr = 4, RULE_having_expr = 5, RULE_agg_filter_expr = 6, 
		RULE_order_expr = 7, RULE_order_element = 8, RULE_limit_expr = 9, RULE_crosstab_expr = 10, 
		RULE_report_expr = 11, RULE_filter = 12, RULE_agg_filter = 13, RULE_literal_values = 14, 
		RULE_literal = 15, RULE_arith_expr = 16, RULE_date_range = 17, RULE_agg_expr = 18, 
		RULE_concat_expr = 19, RULE_concat_ws_expr = 20, RULE_date_interval = 21;
	public static final String[] ruleNames = {
		"query", "query_expr", "select_list", "select_element", "filter_expr", 
		"having_expr", "agg_filter_expr", "order_expr", "order_element", "limit_expr", 
		"crosstab_expr", "report_expr", "filter", "agg_filter", "literal_values", 
		"literal", "arith_expr", "date_range", "agg_expr", "concat_expr", "concat_ws_expr", 
		"date_interval"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'as'", null, "'select'", "'where'", "'nthchild'", "'between'", 
		"'date_format'", "'months_between'", "'years_between'", "'current_date'", 
		"'minutes_between'", "'date_range'", "'count'", "'c_count'", "'sum'", 
		"'c_sum'", "'min'", "'max'", "'avg'", "'distinct'", "'having'", "'order by'", 
		null, "'limit'", "'crosstab'", "'concat'", "'concat_ws'", "'or'", "'and'", 
		"'pand'", "'not'", "'round'", "'sql'", "'('", "')'", null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "'\"'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "WS", "SELECT", "WHERE", "NTHCHILD", "BETWEEN", "DATE_FMT", 
		"MTHS_BTWN", "YRS_BTWN", "CURR_DATE", "MINS_BTWN", "DATE_RANGE", "COUNT", 
		"CCOUNT", "SUM", "CSUM", "MIN", "MAX", "AVG", "DISTINCT", "HAVING", "ORDER", 
		"ORD_DIR", "LIMIT", "CROSSTAB", "CONCAT", "CONCAT_WS", "OR", "AND", "PAND", 
		"NOT", "ROUND", "SQL", "LP", "RP", "MOP", "SOP", "UOP", "BOOL", "OP", 
		"INT", "FLOAT", "YEAR", "MONTH", "DAY", "DIGIT", "ID", "FIELD", "SLITERAL", 
		"ESC", "ARITH_OP", "ERROR", "QUOTE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AQL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AQLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class QueryContext extends ParserRuleContext {
		public Query_exprContext query_expr() {
			return getRuleContext(Query_exprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(AQLParser.EOF, 0); }
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			query_expr();
			setState(45);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Query_exprContext extends ParserRuleContext {
		public Query_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query_expr; }
	 
		public Query_exprContext() { }
		public void copyFrom(Query_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class QueryExprContext extends Query_exprContext {
		public Filter_exprContext filter_expr() {
			return getRuleContext(Filter_exprContext.class,0);
		}
		public TerminalNode SELECT() { return getToken(AQLParser.SELECT, 0); }
		public Select_listContext select_list() {
			return getRuleContext(Select_listContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(AQLParser.WHERE, 0); }
		public Having_exprContext having_expr() {
			return getRuleContext(Having_exprContext.class,0);
		}
		public Order_exprContext order_expr() {
			return getRuleContext(Order_exprContext.class,0);
		}
		public Limit_exprContext limit_expr() {
			return getRuleContext(Limit_exprContext.class,0);
		}
		public Crosstab_exprContext crosstab_expr() {
			return getRuleContext(Crosstab_exprContext.class,0);
		}
		public Report_exprContext report_expr() {
			return getRuleContext(Report_exprContext.class,0);
		}
		public QueryExprContext(Query_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterQueryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitQueryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitQueryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Query_exprContext query_expr() throws RecognitionException {
		Query_exprContext _localctx = new Query_exprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_query_expr);
		int _la;
		try {
			_localctx = new QueryExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(47);
				match(SELECT);
				setState(48);
				select_list();
				setState(49);
				match(WHERE);
				}
			}

			setState(53);
			filter_expr(0);
			setState(55);
			_la = _input.LA(1);
			if (_la==HAVING) {
				{
				setState(54);
				having_expr();
				}
			}

			setState(58);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(57);
				order_expr();
				}
			}

			setState(61);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(60);
				limit_expr();
				}
			}

			setState(65);
			switch (_input.LA(1)) {
			case CROSSTAB:
				{
				setState(63);
				crosstab_expr();
				}
				break;
			case ID:
				{
				setState(64);
				report_expr();
				}
				break;
			case EOF:
			case RP:
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_listContext extends ParserRuleContext {
		public Select_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_list; }
	 
		public Select_listContext() { }
		public void copyFrom(Select_listContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SelectExprContext extends Select_listContext {
		public List<Select_elementContext> select_element() {
			return getRuleContexts(Select_elementContext.class);
		}
		public Select_elementContext select_element(int i) {
			return getRuleContext(Select_elementContext.class,i);
		}
		public TerminalNode DISTINCT() { return getToken(AQLParser.DISTINCT, 0); }
		public SelectExprContext(Select_listContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterSelectExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitSelectExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitSelectExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_listContext select_list() throws RecognitionException {
		Select_listContext _localctx = new Select_listContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_select_list);
		int _la;
		try {
			_localctx = new SelectExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			_la = _input.LA(1);
			if (_la==DISTINCT) {
				{
				setState(67);
				match(DISTINCT);
				}
			}

			setState(70);
			select_element();
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(71);
				match(T__0);
				setState(72);
				select_element();
				}
				}
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_elementContext extends ParserRuleContext {
		public Select_elementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_element; }
	 
		public Select_elementContext() { }
		public void copyFrom(Select_elementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SelectElementContext extends Select_elementContext {
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public SelectElementContext(Select_elementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterSelectElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitSelectElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitSelectElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_elementContext select_element() throws RecognitionException {
		Select_elementContext _localctx = new Select_elementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_select_element);
		int _la;
		try {
			_localctx = new SelectElementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			arith_expr(0);
			setState(81);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(79);
				match(T__1);
				setState(80);
				match(SLITERAL);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Filter_exprContext extends ParserRuleContext {
		public Filter_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter_expr; }
	 
		public Filter_exprContext() { }
		public void copyFrom(Filter_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class OrFilterExprContext extends Filter_exprContext {
		public List<Filter_exprContext> filter_expr() {
			return getRuleContexts(Filter_exprContext.class);
		}
		public Filter_exprContext filter_expr(int i) {
			return getRuleContext(Filter_exprContext.class,i);
		}
		public TerminalNode OR() { return getToken(AQLParser.OR, 0); }
		public OrFilterExprContext(Filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterOrFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitOrFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitOrFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NthChildFilterExprContext extends Filter_exprContext {
		public TerminalNode NTHCHILD() { return getToken(AQLParser.NTHCHILD, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Filter_exprContext filter_expr() {
			return getRuleContext(Filter_exprContext.class,0);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public NthChildFilterExprContext(Filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterNthChildFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitNthChildFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitNthChildFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleFilterContext extends Filter_exprContext {
		public FilterContext filter() {
			return getRuleContext(FilterContext.class,0);
		}
		public SimpleFilterContext(Filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterSimpleFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitSimpleFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitSimpleFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndFilterExprContext extends Filter_exprContext {
		public List<Filter_exprContext> filter_expr() {
			return getRuleContexts(Filter_exprContext.class);
		}
		public Filter_exprContext filter_expr(int i) {
			return getRuleContext(Filter_exprContext.class,i);
		}
		public TerminalNode AND() { return getToken(AQLParser.AND, 0); }
		public AndFilterExprContext(Filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterAndFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitAndFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitAndFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PandFilterExprContext extends Filter_exprContext {
		public List<Filter_exprContext> filter_expr() {
			return getRuleContexts(Filter_exprContext.class);
		}
		public Filter_exprContext filter_expr(int i) {
			return getRuleContext(Filter_exprContext.class,i);
		}
		public TerminalNode PAND() { return getToken(AQLParser.PAND, 0); }
		public PandFilterExprContext(Filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterPandFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitPandFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitPandFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotFilterExprContext extends Filter_exprContext {
		public TerminalNode NOT() { return getToken(AQLParser.NOT, 0); }
		public Filter_exprContext filter_expr() {
			return getRuleContext(Filter_exprContext.class,0);
		}
		public NotFilterExprContext(Filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterNotFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitNotFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitNotFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensFilterExprContext extends Filter_exprContext {
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Filter_exprContext filter_expr() {
			return getRuleContext(Filter_exprContext.class,0);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public ParensFilterExprContext(Filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterParensFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitParensFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitParensFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Filter_exprContext filter_expr() throws RecognitionException {
		return filter_expr(0);
	}

	private Filter_exprContext filter_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Filter_exprContext _localctx = new Filter_exprContext(_ctx, _parentState);
		Filter_exprContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_filter_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				_localctx = new NotFilterExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(84);
				match(NOT);
				setState(85);
				filter_expr(2);
				}
				break;
			case 2:
				{
				_localctx = new ParensFilterExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(86);
				match(LP);
				setState(87);
				filter_expr(0);
				setState(88);
				match(RP);
				}
				break;
			case 3:
				{
				_localctx = new NthChildFilterExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(90);
				match(NTHCHILD);
				setState(91);
				match(LP);
				setState(92);
				filter_expr(0);
				setState(93);
				match(RP);
				}
				break;
			case 4:
				{
				_localctx = new SimpleFilterContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(95);
				filter();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(109);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(107);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						_localctx = new AndFilterExprContext(new Filter_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_filter_expr);
						setState(98);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(99);
						match(AND);
						setState(100);
						filter_expr(8);
						}
						break;
					case 2:
						{
						_localctx = new OrFilterExprContext(new Filter_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_filter_expr);
						setState(101);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(102);
						match(OR);
						setState(103);
						filter_expr(7);
						}
						break;
					case 3:
						{
						_localctx = new PandFilterExprContext(new Filter_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_filter_expr);
						setState(104);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(105);
						match(PAND);
						setState(106);
						filter_expr(6);
						}
						break;
					}
					} 
				}
				setState(111);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Having_exprContext extends ParserRuleContext {
		public Having_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_having_expr; }
	 
		public Having_exprContext() { }
		public void copyFrom(Having_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class HavingExprContext extends Having_exprContext {
		public TerminalNode HAVING() { return getToken(AQLParser.HAVING, 0); }
		public Agg_filter_exprContext agg_filter_expr() {
			return getRuleContext(Agg_filter_exprContext.class,0);
		}
		public HavingExprContext(Having_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterHavingExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitHavingExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitHavingExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Having_exprContext having_expr() throws RecognitionException {
		Having_exprContext _localctx = new Having_exprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_having_expr);
		try {
			_localctx = new HavingExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(HAVING);
			setState(113);
			agg_filter_expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Agg_filter_exprContext extends ParserRuleContext {
		public Agg_filter_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_agg_filter_expr; }
	 
		public Agg_filter_exprContext() { }
		public void copyFrom(Agg_filter_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NotAggFilterExprContext extends Agg_filter_exprContext {
		public TerminalNode NOT() { return getToken(AQLParser.NOT, 0); }
		public Agg_filter_exprContext agg_filter_expr() {
			return getRuleContext(Agg_filter_exprContext.class,0);
		}
		public NotAggFilterExprContext(Agg_filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterNotAggFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitNotAggFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitNotAggFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndAggFilterExprContext extends Agg_filter_exprContext {
		public List<Agg_filter_exprContext> agg_filter_expr() {
			return getRuleContexts(Agg_filter_exprContext.class);
		}
		public Agg_filter_exprContext agg_filter_expr(int i) {
			return getRuleContext(Agg_filter_exprContext.class,i);
		}
		public TerminalNode AND() { return getToken(AQLParser.AND, 0); }
		public AndAggFilterExprContext(Agg_filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterAndAggFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitAndAggFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitAndAggFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleAggFilterContext extends Agg_filter_exprContext {
		public Agg_filterContext agg_filter() {
			return getRuleContext(Agg_filterContext.class,0);
		}
		public SimpleAggFilterContext(Agg_filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterSimpleAggFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitSimpleAggFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitSimpleAggFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensAggFilterExprContext extends Agg_filter_exprContext {
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Agg_filter_exprContext agg_filter_expr() {
			return getRuleContext(Agg_filter_exprContext.class,0);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public ParensAggFilterExprContext(Agg_filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterParensAggFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitParensAggFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitParensAggFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrAggFilterExprContext extends Agg_filter_exprContext {
		public List<Agg_filter_exprContext> agg_filter_expr() {
			return getRuleContexts(Agg_filter_exprContext.class);
		}
		public Agg_filter_exprContext agg_filter_expr(int i) {
			return getRuleContext(Agg_filter_exprContext.class,i);
		}
		public TerminalNode OR() { return getToken(AQLParser.OR, 0); }
		public OrAggFilterExprContext(Agg_filter_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterOrAggFilterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitOrAggFilterExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitOrAggFilterExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Agg_filter_exprContext agg_filter_expr() throws RecognitionException {
		return agg_filter_expr(0);
	}

	private Agg_filter_exprContext agg_filter_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Agg_filter_exprContext _localctx = new Agg_filter_exprContext(_ctx, _parentState);
		Agg_filter_exprContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_agg_filter_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			switch (_input.LA(1)) {
			case NOT:
				{
				_localctx = new NotAggFilterExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(116);
				match(NOT);
				setState(117);
				agg_filter_expr(2);
				}
				break;
			case LP:
				{
				_localctx = new ParensAggFilterExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(118);
				match(LP);
				setState(119);
				agg_filter_expr(0);
				setState(120);
				match(RP);
				}
				break;
			case COUNT:
			case CCOUNT:
			case SUM:
			case CSUM:
			case MIN:
			case MAX:
			case AVG:
				{
				_localctx = new SimpleAggFilterContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(122);
				agg_filter();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(133);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(131);
					switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
					case 1:
						{
						_localctx = new AndAggFilterExprContext(new Agg_filter_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_agg_filter_expr);
						setState(125);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(126);
						match(AND);
						setState(127);
						agg_filter_expr(6);
						}
						break;
					case 2:
						{
						_localctx = new OrAggFilterExprContext(new Agg_filter_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_agg_filter_expr);
						setState(128);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(129);
						match(OR);
						setState(130);
						agg_filter_expr(5);
						}
						break;
					}
					} 
				}
				setState(135);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Order_exprContext extends ParserRuleContext {
		public Order_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_order_expr; }
	 
		public Order_exprContext() { }
		public void copyFrom(Order_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class OrderExprContext extends Order_exprContext {
		public TerminalNode ORDER() { return getToken(AQLParser.ORDER, 0); }
		public List<Order_elementContext> order_element() {
			return getRuleContexts(Order_elementContext.class);
		}
		public Order_elementContext order_element(int i) {
			return getRuleContext(Order_elementContext.class,i);
		}
		public OrderExprContext(Order_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterOrderExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitOrderExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitOrderExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Order_exprContext order_expr() throws RecognitionException {
		Order_exprContext _localctx = new Order_exprContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_order_expr);
		int _la;
		try {
			_localctx = new OrderExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(ORDER);
			setState(137);
			order_element();
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(138);
				match(T__0);
				setState(139);
				order_element();
				}
				}
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Order_elementContext extends ParserRuleContext {
		public Order_elementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_order_element; }
	 
		public Order_elementContext() { }
		public void copyFrom(Order_elementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class OrderElementContext extends Order_elementContext {
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode ORD_DIR() { return getToken(AQLParser.ORD_DIR, 0); }
		public OrderElementContext(Order_elementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterOrderElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitOrderElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitOrderElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Order_elementContext order_element() throws RecognitionException {
		Order_elementContext _localctx = new Order_elementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_order_element);
		int _la;
		try {
			_localctx = new OrderElementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			arith_expr(0);
			setState(147);
			_la = _input.LA(1);
			if (_la==ORD_DIR) {
				{
				setState(146);
				match(ORD_DIR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Limit_exprContext extends ParserRuleContext {
		public Limit_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limit_expr; }
	 
		public Limit_exprContext() { }
		public void copyFrom(Limit_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class LimitExprContext extends Limit_exprContext {
		public TerminalNode LIMIT() { return getToken(AQLParser.LIMIT, 0); }
		public List<TerminalNode> INT() { return getTokens(AQLParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(AQLParser.INT, i);
		}
		public LimitExprContext(Limit_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterLimitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitLimitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitLimitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Limit_exprContext limit_expr() throws RecognitionException {
		Limit_exprContext _localctx = new Limit_exprContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_limit_expr);
		int _la;
		try {
			_localctx = new LimitExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(LIMIT);
			setState(150);
			match(INT);
			setState(153);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(151);
				match(T__0);
				setState(152);
				match(INT);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Crosstab_exprContext extends ParserRuleContext {
		public Crosstab_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_crosstab_expr; }
	 
		public Crosstab_exprContext() { }
		public void copyFrom(Crosstab_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class CrossTabExprContext extends Crosstab_exprContext {
		public Token INT;
		public List<Token> row = new ArrayList<Token>();
		public Token col;
		public List<Token> value = new ArrayList<Token>();
		public TerminalNode CROSSTAB() { return getToken(AQLParser.CROSSTAB, 0); }
		public List<TerminalNode> LP() { return getTokens(AQLParser.LP); }
		public TerminalNode LP(int i) {
			return getToken(AQLParser.LP, i);
		}
		public List<TerminalNode> RP() { return getTokens(AQLParser.RP); }
		public TerminalNode RP(int i) {
			return getToken(AQLParser.RP, i);
		}
		public List<TerminalNode> INT() { return getTokens(AQLParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(AQLParser.INT, i);
		}
		public TerminalNode BOOL() { return getToken(AQLParser.BOOL, 0); }
		public CrossTabExprContext(Crosstab_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterCrossTabExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitCrossTabExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitCrossTabExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Crosstab_exprContext crosstab_expr() throws RecognitionException {
		Crosstab_exprContext _localctx = new Crosstab_exprContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_crosstab_expr);
		int _la;
		try {
			_localctx = new CrossTabExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(CROSSTAB);
			setState(156);
			match(LP);
			setState(157);
			match(LP);
			setState(158);
			((CrossTabExprContext)_localctx).INT = match(INT);
			((CrossTabExprContext)_localctx).row.add(((CrossTabExprContext)_localctx).INT);
			setState(163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(159);
				match(T__0);
				setState(160);
				((CrossTabExprContext)_localctx).INT = match(INT);
				((CrossTabExprContext)_localctx).row.add(((CrossTabExprContext)_localctx).INT);
				}
				}
				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(166);
			match(RP);
			setState(167);
			match(T__0);
			setState(168);
			((CrossTabExprContext)_localctx).col = match(INT);
			setState(169);
			match(T__0);
			setState(170);
			match(LP);
			setState(171);
			((CrossTabExprContext)_localctx).INT = match(INT);
			((CrossTabExprContext)_localctx).value.add(((CrossTabExprContext)_localctx).INT);
			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(172);
				match(T__0);
				setState(173);
				((CrossTabExprContext)_localctx).INT = match(INT);
				((CrossTabExprContext)_localctx).value.add(((CrossTabExprContext)_localctx).INT);
				}
				}
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(179);
			match(RP);
			setState(182);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(180);
				match(T__0);
				setState(181);
				match(BOOL);
				}
			}

			setState(184);
			match(RP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Report_exprContext extends ParserRuleContext {
		public Report_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_report_expr; }
	 
		public Report_exprContext() { }
		public void copyFrom(Report_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ReportExprContext extends Report_exprContext {
		public TerminalNode ID() { return getToken(AQLParser.ID, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public List<TerminalNode> SLITERAL() { return getTokens(AQLParser.SLITERAL); }
		public TerminalNode SLITERAL(int i) {
			return getToken(AQLParser.SLITERAL, i);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public ReportExprContext(Report_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterReportExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitReportExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitReportExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Report_exprContext report_expr() throws RecognitionException {
		Report_exprContext _localctx = new Report_exprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_report_expr);
		int _la;
		try {
			_localctx = new ReportExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(ID);
			setState(197);
			_la = _input.LA(1);
			if (_la==LP) {
				{
				setState(187);
				match(LP);
				setState(188);
				match(SLITERAL);
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__0) {
					{
					{
					setState(189);
					match(T__0);
					setState(190);
					match(SLITERAL);
					}
					}
					setState(195);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(196);
				match(RP);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterContext extends ParserRuleContext {
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
	 
		public FilterContext() { }
		public void copyFrom(FilterContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BetweenFilterContext extends FilterContext {
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode BETWEEN() { return getToken(AQLParser.BETWEEN, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public BetweenFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterBetweenFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitBetweenFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitBetweenFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SqlFilterContext extends FilterContext {
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode MOP() { return getToken(AQLParser.MOP, 0); }
		public TerminalNode SQL() { return getToken(AQLParser.SQL, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public SqlFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterSqlFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitSqlFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitSqlFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringCompFilterContext extends FilterContext {
		public TerminalNode FIELD() { return getToken(AQLParser.FIELD, 0); }
		public TerminalNode SOP() { return getToken(AQLParser.SOP, 0); }
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public StringCompFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterStringCompFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitStringCompFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitStringCompFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MvFilterContext extends FilterContext {
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode MOP() { return getToken(AQLParser.MOP, 0); }
		public Literal_valuesContext literal_values() {
			return getRuleContext(Literal_valuesContext.class,0);
		}
		public MvFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterMvFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitMvFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitMvFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryFilterContext extends FilterContext {
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode UOP() { return getToken(AQLParser.UOP, 0); }
		public UnaryFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterUnaryFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitUnaryFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitUnaryFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DateRangeFilterContext extends FilterContext {
		public Date_rangeContext date_range() {
			return getRuleContext(Date_rangeContext.class,0);
		}
		public DateRangeFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterDateRangeFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitDateRangeFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitDateRangeFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConcatWsCompFilterContext extends FilterContext {
		public Concat_ws_exprContext concat_ws_expr() {
			return getRuleContext(Concat_ws_exprContext.class,0);
		}
		public TerminalNode SOP() { return getToken(AQLParser.SOP, 0); }
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public ConcatWsCompFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterConcatWsCompFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitConcatWsCompFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitConcatWsCompFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubQueryFilterContext extends FilterContext {
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode MOP() { return getToken(AQLParser.MOP, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Query_exprContext query_expr() {
			return getRuleContext(Query_exprContext.class,0);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public SubQueryFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterSubQueryFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitSubQueryFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitSubQueryFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConcatCompFilterContext extends FilterContext {
		public Concat_exprContext concat_expr() {
			return getRuleContext(Concat_exprContext.class,0);
		}
		public TerminalNode SOP() { return getToken(AQLParser.SOP, 0); }
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public ConcatCompFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterConcatCompFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitConcatCompFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitConcatCompFilter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BasicFilterContext extends FilterContext {
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode OP() { return getToken(AQLParser.OP, 0); }
		public BasicFilterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterBasicFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitBasicFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitBasicFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_filter);
		try {
			setState(243);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				_localctx = new BasicFilterContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(199);
				arith_expr(0);
				setState(200);
				match(OP);
				setState(201);
				arith_expr(0);
				}
				break;
			case 2:
				_localctx = new MvFilterContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(203);
				arith_expr(0);
				setState(204);
				match(MOP);
				setState(205);
				literal_values();
				}
				break;
			case 3:
				_localctx = new SubQueryFilterContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(207);
				arith_expr(0);
				setState(208);
				match(MOP);
				setState(209);
				match(LP);
				setState(210);
				query_expr();
				setState(211);
				match(RP);
				}
				break;
			case 4:
				_localctx = new SqlFilterContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(213);
				arith_expr(0);
				setState(214);
				match(MOP);
				setState(215);
				match(SQL);
				setState(216);
				match(LP);
				setState(217);
				match(SLITERAL);
				setState(218);
				match(RP);
				}
				break;
			case 5:
				_localctx = new ConcatCompFilterContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(220);
				concat_expr();
				setState(221);
				match(SOP);
				setState(222);
				match(SLITERAL);
				}
				break;
			case 6:
				_localctx = new ConcatWsCompFilterContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(224);
				concat_ws_expr();
				setState(225);
				match(SOP);
				setState(226);
				match(SLITERAL);
				}
				break;
			case 7:
				_localctx = new StringCompFilterContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(228);
				match(FIELD);
				setState(229);
				match(SOP);
				setState(230);
				match(SLITERAL);
				}
				break;
			case 8:
				_localctx = new UnaryFilterContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(231);
				arith_expr(0);
				setState(232);
				match(UOP);
				}
				break;
			case 9:
				_localctx = new DateRangeFilterContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(234);
				date_range();
				}
				break;
			case 10:
				_localctx = new BetweenFilterContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(235);
				arith_expr(0);
				setState(236);
				match(BETWEEN);
				setState(237);
				match(LP);
				setState(238);
				arith_expr(0);
				setState(239);
				match(T__0);
				setState(240);
				arith_expr(0);
				setState(241);
				match(RP);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Agg_filterContext extends ParserRuleContext {
		public Agg_filterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_agg_filter; }
	 
		public Agg_filterContext() { }
		public void copyFrom(Agg_filterContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AggFilterContext extends Agg_filterContext {
		public Agg_exprContext agg_expr() {
			return getRuleContext(Agg_exprContext.class,0);
		}
		public TerminalNode OP() { return getToken(AQLParser.OP, 0); }
		public TerminalNode INT() { return getToken(AQLParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(AQLParser.FLOAT, 0); }
		public AggFilterContext(Agg_filterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterAggFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitAggFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitAggFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Agg_filterContext agg_filter() throws RecognitionException {
		Agg_filterContext _localctx = new Agg_filterContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_agg_filter);
		int _la;
		try {
			_localctx = new AggFilterContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			agg_expr();
			setState(246);
			match(OP);
			setState(247);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==FLOAT) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Literal_valuesContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public Literal_valuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal_values; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterLiteral_values(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitLiteral_values(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitLiteral_values(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Literal_valuesContext literal_values() throws RecognitionException {
		Literal_valuesContext _localctx = new Literal_valuesContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_literal_values);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(LP);
			setState(250);
			literal();
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(251);
				match(T__0);
				setState(252);
				literal();
				}
				}
				setState(257);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(258);
			match(RP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StringLiteralContext extends LiteralContext {
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolLiteralContext extends LiteralContext {
		public TerminalNode BOOL() { return getToken(AQLParser.BOOL, 0); }
		public BoolLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterBoolLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitBoolLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitBoolLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FloatLiteralContext extends LiteralContext {
		public TerminalNode FLOAT() { return getToken(AQLParser.FLOAT, 0); }
		public FloatLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterFloatLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitFloatLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitFloatLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntLiteralContext extends LiteralContext {
		public TerminalNode INT() { return getToken(AQLParser.INT, 0); }
		public IntLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterIntLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitIntLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitIntLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_literal);
		try {
			setState(264);
			switch (_input.LA(1)) {
			case SLITERAL:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(260);
				match(SLITERAL);
				}
				break;
			case INT:
				_localctx = new IntLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(261);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new FloatLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(262);
				match(FLOAT);
				}
				break;
			case BOOL:
				_localctx = new BoolLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(263);
				match(BOOL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arith_exprContext extends ParserRuleContext {
		public Arith_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arith_expr; }
	 
		public Arith_exprContext() { }
		public void copyFrom(Arith_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArithExprContext extends Arith_exprContext {
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode ARITH_OP() { return getToken(AQLParser.ARITH_OP, 0); }
		public ArithExprContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterArithExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitArithExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitArithExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DateFmtFuncContext extends Arith_exprContext {
		public TerminalNode DATE_FMT() { return getToken(AQLParser.DATE_FMT, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public DateFmtFuncContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterDateFmtFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitDateFmtFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitDateFmtFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MonthsDiffFuncContext extends Arith_exprContext {
		public TerminalNode MTHS_BTWN() { return getToken(AQLParser.MTHS_BTWN, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public MonthsDiffFuncContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterMonthsDiffFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitMonthsDiffFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitMonthsDiffFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RoundFuncContext extends Arith_exprContext {
		public TerminalNode ROUND() { return getToken(AQLParser.ROUND, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode INT() { return getToken(AQLParser.INT, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public RoundFuncContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterRoundFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitRoundFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitRoundFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DateIntervalExprContext extends Arith_exprContext {
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode ARITH_OP() { return getToken(AQLParser.ARITH_OP, 0); }
		public Date_intervalContext date_interval() {
			return getRuleContext(Date_intervalContext.class,0);
		}
		public DateIntervalExprContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterDateIntervalExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitDateIntervalExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitDateIntervalExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConcatExprContext extends Arith_exprContext {
		public Concat_exprContext concat_expr() {
			return getRuleContext(Concat_exprContext.class,0);
		}
		public ConcatExprContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterConcatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitConcatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitConcatExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FieldContext extends Arith_exprContext {
		public TerminalNode FIELD() { return getToken(AQLParser.FIELD, 0); }
		public FieldContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitField(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CurrentDateFuncContext extends Arith_exprContext {
		public TerminalNode CURR_DATE() { return getToken(AQLParser.CURR_DATE, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public CurrentDateFuncContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterCurrentDateFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitCurrentDateFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitCurrentDateFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AggExprContext extends Arith_exprContext {
		public Agg_exprContext agg_expr() {
			return getRuleContext(Agg_exprContext.class,0);
		}
		public AggExprContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterAggExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitAggExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitAggExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensArithExprContext extends Arith_exprContext {
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public ParensArithExprContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterParensArithExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitParensArithExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitParensArithExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class YearsDiffFuncContext extends Arith_exprContext {
		public TerminalNode YRS_BTWN() { return getToken(AQLParser.YRS_BTWN, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public YearsDiffFuncContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterYearsDiffFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitYearsDiffFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitYearsDiffFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConcatWsExprContext extends Arith_exprContext {
		public Concat_ws_exprContext concat_ws_expr() {
			return getRuleContext(Concat_ws_exprContext.class,0);
		}
		public ConcatWsExprContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterConcatWsExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitConcatWsExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitConcatWsExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralValContext extends Arith_exprContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralValContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterLiteralVal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitLiteralVal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitLiteralVal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MinsDiffFuncContext extends Arith_exprContext {
		public TerminalNode MINS_BTWN() { return getToken(AQLParser.MINS_BTWN, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public MinsDiffFuncContext(Arith_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterMinsDiffFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitMinsDiffFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitMinsDiffFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Arith_exprContext arith_expr() throws RecognitionException {
		return arith_expr(0);
	}

	private Arith_exprContext arith_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Arith_exprContext _localctx = new Arith_exprContext(_ctx, _parentState);
		Arith_exprContext _prevctx = _localctx;
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_arith_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(314);
			switch (_input.LA(1)) {
			case LP:
				{
				_localctx = new ParensArithExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(267);
				match(LP);
				setState(268);
				arith_expr(0);
				setState(269);
				match(RP);
				}
				break;
			case DATE_FMT:
				{
				_localctx = new DateFmtFuncContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(271);
				match(DATE_FMT);
				setState(272);
				match(LP);
				setState(273);
				arith_expr(0);
				setState(274);
				match(T__0);
				setState(275);
				match(SLITERAL);
				setState(276);
				match(RP);
				}
				break;
			case MTHS_BTWN:
				{
				_localctx = new MonthsDiffFuncContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(278);
				match(MTHS_BTWN);
				setState(279);
				match(LP);
				setState(280);
				arith_expr(0);
				setState(281);
				match(T__0);
				setState(282);
				arith_expr(0);
				setState(283);
				match(RP);
				}
				break;
			case YRS_BTWN:
				{
				_localctx = new YearsDiffFuncContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(285);
				match(YRS_BTWN);
				setState(286);
				match(LP);
				setState(287);
				arith_expr(0);
				setState(288);
				match(T__0);
				setState(289);
				arith_expr(0);
				setState(290);
				match(RP);
				}
				break;
			case MINS_BTWN:
				{
				_localctx = new MinsDiffFuncContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(292);
				match(MINS_BTWN);
				setState(293);
				match(LP);
				setState(294);
				arith_expr(0);
				setState(295);
				match(T__0);
				setState(296);
				arith_expr(0);
				setState(297);
				match(RP);
				}
				break;
			case CURR_DATE:
				{
				_localctx = new CurrentDateFuncContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(299);
				match(CURR_DATE);
				setState(300);
				match(LP);
				setState(301);
				match(RP);
				}
				break;
			case COUNT:
			case CCOUNT:
			case SUM:
			case CSUM:
			case MIN:
			case MAX:
			case AVG:
				{
				_localctx = new AggExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(302);
				agg_expr();
				}
				break;
			case CONCAT:
				{
				_localctx = new ConcatExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(303);
				concat_expr();
				}
				break;
			case CONCAT_WS:
				{
				_localctx = new ConcatWsExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(304);
				concat_ws_expr();
				}
				break;
			case ROUND:
				{
				_localctx = new RoundFuncContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(305);
				match(ROUND);
				setState(306);
				match(LP);
				setState(307);
				arith_expr(0);
				setState(308);
				match(T__0);
				setState(309);
				match(INT);
				setState(310);
				match(RP);
				}
				break;
			case FIELD:
				{
				_localctx = new FieldContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(312);
				match(FIELD);
				}
				break;
			case BOOL:
			case INT:
			case FLOAT:
			case SLITERAL:
				{
				_localctx = new LiteralValContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(313);
				literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(324);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(322);
					switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
					case 1:
						{
						_localctx = new ArithExprContext(new Arith_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_arith_expr);
						setState(316);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(317);
						match(ARITH_OP);
						setState(318);
						arith_expr(15);
						}
						break;
					case 2:
						{
						_localctx = new DateIntervalExprContext(new Arith_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_arith_expr);
						setState(319);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(320);
						match(ARITH_OP);
						setState(321);
						date_interval();
						}
						break;
					}
					} 
				}
				setState(326);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Date_rangeContext extends ParserRuleContext {
		public Date_rangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_date_range; }
	 
		public Date_rangeContext() { }
		public void copyFrom(Date_rangeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DateRangeFuncContext extends Date_rangeContext {
		public TerminalNode DATE_RANGE() { return getToken(AQLParser.DATE_RANGE, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public TerminalNode ID() { return getToken(AQLParser.ID, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public TerminalNode INT() { return getToken(AQLParser.INT, 0); }
		public DateRangeFuncContext(Date_rangeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterDateRangeFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitDateRangeFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitDateRangeFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Date_rangeContext date_range() throws RecognitionException {
		Date_rangeContext _localctx = new Date_rangeContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_date_range);
		int _la;
		try {
			_localctx = new DateRangeFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			match(DATE_RANGE);
			setState(328);
			match(LP);
			setState(329);
			arith_expr(0);
			setState(330);
			match(T__0);
			setState(331);
			match(ID);
			setState(334);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(332);
				match(T__0);
				setState(333);
				match(INT);
				}
			}

			setState(336);
			match(RP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Agg_exprContext extends ParserRuleContext {
		public Agg_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_agg_expr; }
	 
		public Agg_exprContext() { }
		public void copyFrom(Agg_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AggFuncContext extends Agg_exprContext {
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public TerminalNode FIELD() { return getToken(AQLParser.FIELD, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public TerminalNode COUNT() { return getToken(AQLParser.COUNT, 0); }
		public TerminalNode CCOUNT() { return getToken(AQLParser.CCOUNT, 0); }
		public TerminalNode SUM() { return getToken(AQLParser.SUM, 0); }
		public TerminalNode CSUM() { return getToken(AQLParser.CSUM, 0); }
		public TerminalNode MIN() { return getToken(AQLParser.MIN, 0); }
		public TerminalNode MAX() { return getToken(AQLParser.MAX, 0); }
		public TerminalNode AVG() { return getToken(AQLParser.AVG, 0); }
		public TerminalNode DISTINCT() { return getToken(AQLParser.DISTINCT, 0); }
		public AggFuncContext(Agg_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterAggFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitAggFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitAggFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Agg_exprContext agg_expr() throws RecognitionException {
		Agg_exprContext _localctx = new Agg_exprContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_agg_expr);
		int _la;
		try {
			_localctx = new AggFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << COUNT) | (1L << CCOUNT) | (1L << SUM) | (1L << CSUM) | (1L << MIN) | (1L << MAX) | (1L << AVG))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(339);
			match(LP);
			setState(341);
			_la = _input.LA(1);
			if (_la==DISTINCT) {
				{
				setState(340);
				match(DISTINCT);
				}
			}

			setState(343);
			match(FIELD);
			setState(344);
			match(RP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Concat_exprContext extends ParserRuleContext {
		public Concat_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_concat_expr; }
	 
		public Concat_exprContext() { }
		public void copyFrom(Concat_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ConcatFuncContext extends Concat_exprContext {
		public TerminalNode CONCAT() { return getToken(AQLParser.CONCAT, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public ConcatFuncContext(Concat_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterConcatFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitConcatFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitConcatFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Concat_exprContext concat_expr() throws RecognitionException {
		Concat_exprContext _localctx = new Concat_exprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_concat_expr);
		int _la;
		try {
			_localctx = new ConcatFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(346);
			match(CONCAT);
			setState(347);
			match(LP);
			setState(348);
			arith_expr(0);
			setState(349);
			match(T__0);
			setState(350);
			arith_expr(0);
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(351);
				match(T__0);
				setState(352);
				arith_expr(0);
				}
				}
				setState(357);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(358);
			match(RP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Concat_ws_exprContext extends ParserRuleContext {
		public Concat_ws_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_concat_ws_expr; }
	 
		public Concat_ws_exprContext() { }
		public void copyFrom(Concat_ws_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ConcatWsFuncContext extends Concat_ws_exprContext {
		public TerminalNode CONCAT_WS() { return getToken(AQLParser.CONCAT_WS, 0); }
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public ConcatWsFuncContext(Concat_ws_exprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterConcatWsFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitConcatWsFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitConcatWsFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Concat_ws_exprContext concat_ws_expr() throws RecognitionException {
		Concat_ws_exprContext _localctx = new Concat_ws_exprContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_concat_ws_expr);
		int _la;
		try {
			_localctx = new ConcatWsFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			match(CONCAT_WS);
			setState(361);
			match(LP);
			setState(362);
			match(SLITERAL);
			setState(363);
			match(T__0);
			setState(364);
			arith_expr(0);
			setState(369);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(365);
				match(T__0);
				setState(366);
				arith_expr(0);
				}
				}
				setState(371);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(372);
			match(RP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Date_intervalContext extends ParserRuleContext {
		public TerminalNode YEAR() { return getToken(AQLParser.YEAR, 0); }
		public TerminalNode MONTH() { return getToken(AQLParser.MONTH, 0); }
		public TerminalNode DAY() { return getToken(AQLParser.DAY, 0); }
		public Date_intervalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_date_interval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterDate_interval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitDate_interval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitDate_interval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Date_intervalContext date_interval() throws RecognitionException {
		Date_intervalContext _localctx = new Date_intervalContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_date_interval);
		int _la;
		try {
			setState(395);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(374);
				match(YEAR);
				setState(376);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(375);
					match(MONTH);
					}
					break;
				}
				setState(379);
				switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
				case 1:
					{
					setState(378);
					match(DAY);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(382);
				_la = _input.LA(1);
				if (_la==YEAR) {
					{
					setState(381);
					match(YEAR);
					}
				}

				setState(384);
				match(MONTH);
				setState(386);
				switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
				case 1:
					{
					setState(385);
					match(DAY);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(389);
				_la = _input.LA(1);
				if (_la==YEAR) {
					{
					setState(388);
					match(YEAR);
					}
				}

				setState(392);
				_la = _input.LA(1);
				if (_la==MONTH) {
					{
					setState(391);
					match(MONTH);
					}
				}

				setState(394);
				match(DAY);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 4:
			return filter_expr_sempred((Filter_exprContext)_localctx, predIndex);
		case 6:
			return agg_filter_expr_sempred((Agg_filter_exprContext)_localctx, predIndex);
		case 16:
			return arith_expr_sempred((Arith_exprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean filter_expr_sempred(Filter_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean agg_filter_expr_sempred(Agg_filter_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean arith_expr_sempred(Arith_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 14);
		case 6:
			return precpred(_ctx, 13);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\38\u0190\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\5\3\66\n\3\3\3\3\3\5\3:\n\3\3\3\5\3=\n\3\3\3\5\3@\n\3\3\3\3\3"+
		"\5\3D\n\3\3\4\5\4G\n\4\3\4\3\4\3\4\7\4L\n\4\f\4\16\4O\13\4\3\5\3\5\3\5"+
		"\5\5T\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6c\n\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6n\n\6\f\6\16\6q\13\6\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b~\n\b\3\b\3\b\3\b\3\b\3\b\3\b\7"+
		"\b\u0086\n\b\f\b\16\b\u0089\13\b\3\t\3\t\3\t\3\t\7\t\u008f\n\t\f\t\16"+
		"\t\u0092\13\t\3\n\3\n\5\n\u0096\n\n\3\13\3\13\3\13\3\13\5\13\u009c\n\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\7\f\u00a4\n\f\f\f\16\f\u00a7\13\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\7\f\u00b1\n\f\f\f\16\f\u00b4\13\f\3\f\3\f\3\f\5\f"+
		"\u00b9\n\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\7\r\u00c2\n\r\f\r\16\r\u00c5\13"+
		"\r\3\r\5\r\u00c8\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00f6\n\16\3\17\3\17\3\17\3\17\3\20"+
		"\3\20\3\20\3\20\7\20\u0100\n\20\f\20\16\20\u0103\13\20\3\20\3\20\3\21"+
		"\3\21\3\21\3\21\5\21\u010b\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u013d"+
		"\n\22\3\22\3\22\3\22\3\22\3\22\3\22\7\22\u0145\n\22\f\22\16\22\u0148\13"+
		"\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u0151\n\23\3\23\3\23\3\24"+
		"\3\24\3\24\5\24\u0158\n\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\7\25\u0164\n\25\f\25\16\25\u0167\13\25\3\25\3\25\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\7\26\u0172\n\26\f\26\16\26\u0175\13\26\3\26\3\26"+
		"\3\27\3\27\5\27\u017b\n\27\3\27\5\27\u017e\n\27\3\27\5\27\u0181\n\27\3"+
		"\27\3\27\5\27\u0185\n\27\3\27\5\27\u0188\n\27\3\27\5\27\u018b\n\27\3\27"+
		"\5\27\u018e\n\27\3\27\2\5\n\16\"\30\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,\2\4\3\2,-\3\2\20\26\u01ba\2.\3\2\2\2\4\65\3\2\2\2\6F\3\2\2"+
		"\2\bP\3\2\2\2\nb\3\2\2\2\fr\3\2\2\2\16}\3\2\2\2\20\u008a\3\2\2\2\22\u0093"+
		"\3\2\2\2\24\u0097\3\2\2\2\26\u009d\3\2\2\2\30\u00bc\3\2\2\2\32\u00f5\3"+
		"\2\2\2\34\u00f7\3\2\2\2\36\u00fb\3\2\2\2 \u010a\3\2\2\2\"\u013c\3\2\2"+
		"\2$\u0149\3\2\2\2&\u0154\3\2\2\2(\u015c\3\2\2\2*\u016a\3\2\2\2,\u018d"+
		"\3\2\2\2./\5\4\3\2/\60\7\2\2\3\60\3\3\2\2\2\61\62\7\6\2\2\62\63\5\6\4"+
		"\2\63\64\7\7\2\2\64\66\3\2\2\2\65\61\3\2\2\2\65\66\3\2\2\2\66\67\3\2\2"+
		"\2\679\5\n\6\28:\5\f\7\298\3\2\2\29:\3\2\2\2:<\3\2\2\2;=\5\20\t\2<;\3"+
		"\2\2\2<=\3\2\2\2=?\3\2\2\2>@\5\24\13\2?>\3\2\2\2?@\3\2\2\2@C\3\2\2\2A"+
		"D\5\26\f\2BD\5\30\r\2CA\3\2\2\2CB\3\2\2\2CD\3\2\2\2D\5\3\2\2\2EG\7\27"+
		"\2\2FE\3\2\2\2FG\3\2\2\2GH\3\2\2\2HM\5\b\5\2IJ\7\3\2\2JL\5\b\5\2KI\3\2"+
		"\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2N\7\3\2\2\2OM\3\2\2\2PS\5\"\22\2QR\7"+
		"\4\2\2RT\7\64\2\2SQ\3\2\2\2ST\3\2\2\2T\t\3\2\2\2UV\b\6\1\2VW\7\"\2\2W"+
		"c\5\n\6\4XY\7%\2\2YZ\5\n\6\2Z[\7&\2\2[c\3\2\2\2\\]\7\b\2\2]^\7%\2\2^_"+
		"\5\n\6\2_`\7&\2\2`c\3\2\2\2ac\5\32\16\2bU\3\2\2\2bX\3\2\2\2b\\\3\2\2\2"+
		"ba\3\2\2\2co\3\2\2\2de\f\t\2\2ef\7 \2\2fn\5\n\6\ngh\f\b\2\2hi\7\37\2\2"+
		"in\5\n\6\tjk\f\7\2\2kl\7!\2\2ln\5\n\6\bmd\3\2\2\2mg\3\2\2\2mj\3\2\2\2"+
		"nq\3\2\2\2om\3\2\2\2op\3\2\2\2p\13\3\2\2\2qo\3\2\2\2rs\7\30\2\2st\5\16"+
		"\b\2t\r\3\2\2\2uv\b\b\1\2vw\7\"\2\2w~\5\16\b\4xy\7%\2\2yz\5\16\b\2z{\7"+
		"&\2\2{~\3\2\2\2|~\5\34\17\2}u\3\2\2\2}x\3\2\2\2}|\3\2\2\2~\u0087\3\2\2"+
		"\2\177\u0080\f\7\2\2\u0080\u0081\7 \2\2\u0081\u0086\5\16\b\b\u0082\u0083"+
		"\f\6\2\2\u0083\u0084\7\37\2\2\u0084\u0086\5\16\b\7\u0085\177\3\2\2\2\u0085"+
		"\u0082\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2"+
		"\2\2\u0088\17\3\2\2\2\u0089\u0087\3\2\2\2\u008a\u008b\7\31\2\2\u008b\u0090"+
		"\5\22\n\2\u008c\u008d\7\3\2\2\u008d\u008f\5\22\n\2\u008e\u008c\3\2\2\2"+
		"\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\21"+
		"\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0095\5\"\22\2\u0094\u0096\7\32\2\2"+
		"\u0095\u0094\3\2\2\2\u0095\u0096\3\2\2\2\u0096\23\3\2\2\2\u0097\u0098"+
		"\7\33\2\2\u0098\u009b\7,\2\2\u0099\u009a\7\3\2\2\u009a\u009c\7,\2\2\u009b"+
		"\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\25\3\2\2\2\u009d\u009e\7\34\2"+
		"\2\u009e\u009f\7%\2\2\u009f\u00a0\7%\2\2\u00a0\u00a5\7,\2\2\u00a1\u00a2"+
		"\7\3\2\2\u00a2\u00a4\7,\2\2\u00a3\u00a1\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5"+
		"\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7\u00a5\3\2"+
		"\2\2\u00a8\u00a9\7&\2\2\u00a9\u00aa\7\3\2\2\u00aa\u00ab\7,\2\2\u00ab\u00ac"+
		"\7\3\2\2\u00ac\u00ad\7%\2\2\u00ad\u00b2\7,\2\2\u00ae\u00af\7\3\2\2\u00af"+
		"\u00b1\7,\2\2\u00b0\u00ae\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2"+
		"\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b5\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5"+
		"\u00b8\7&\2\2\u00b6\u00b7\7\3\2\2\u00b7\u00b9\7*\2\2\u00b8\u00b6\3\2\2"+
		"\2\u00b8\u00b9\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bb\7&\2\2\u00bb\27"+
		"\3\2\2\2\u00bc\u00c7\7\62\2\2\u00bd\u00be\7%\2\2\u00be\u00c3\7\64\2\2"+
		"\u00bf\u00c0\7\3\2\2\u00c0\u00c2\7\64\2\2\u00c1\u00bf\3\2\2\2\u00c2\u00c5"+
		"\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c6\3\2\2\2\u00c5"+
		"\u00c3\3\2\2\2\u00c6\u00c8\7&\2\2\u00c7\u00bd\3\2\2\2\u00c7\u00c8\3\2"+
		"\2\2\u00c8\31\3\2\2\2\u00c9\u00ca\5\"\22\2\u00ca\u00cb\7+\2\2\u00cb\u00cc"+
		"\5\"\22\2\u00cc\u00f6\3\2\2\2\u00cd\u00ce\5\"\22\2\u00ce\u00cf\7\'\2\2"+
		"\u00cf\u00d0\5\36\20\2\u00d0\u00f6\3\2\2\2\u00d1\u00d2\5\"\22\2\u00d2"+
		"\u00d3\7\'\2\2\u00d3\u00d4\7%\2\2\u00d4\u00d5\5\4\3\2\u00d5\u00d6\7&\2"+
		"\2\u00d6\u00f6\3\2\2\2\u00d7\u00d8\5\"\22\2\u00d8\u00d9\7\'\2\2\u00d9"+
		"\u00da\7$\2\2\u00da\u00db\7%\2\2\u00db\u00dc\7\64\2\2\u00dc\u00dd\7&\2"+
		"\2\u00dd\u00f6\3\2\2\2\u00de\u00df\5(\25\2\u00df\u00e0\7(\2\2\u00e0\u00e1"+
		"\7\64\2\2\u00e1\u00f6\3\2\2\2\u00e2\u00e3\5*\26\2\u00e3\u00e4\7(\2\2\u00e4"+
		"\u00e5\7\64\2\2\u00e5\u00f6\3\2\2\2\u00e6\u00e7\7\63\2\2\u00e7\u00e8\7"+
		"(\2\2\u00e8\u00f6\7\64\2\2\u00e9\u00ea\5\"\22\2\u00ea\u00eb\7)\2\2\u00eb"+
		"\u00f6\3\2\2\2\u00ec\u00f6\5$\23\2\u00ed\u00ee\5\"\22\2\u00ee\u00ef\7"+
		"\t\2\2\u00ef\u00f0\7%\2\2\u00f0\u00f1\5\"\22\2\u00f1\u00f2\7\3\2\2\u00f2"+
		"\u00f3\5\"\22\2\u00f3\u00f4\7&\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00c9\3\2"+
		"\2\2\u00f5\u00cd\3\2\2\2\u00f5\u00d1\3\2\2\2\u00f5\u00d7\3\2\2\2\u00f5"+
		"\u00de\3\2\2\2\u00f5\u00e2\3\2\2\2\u00f5\u00e6\3\2\2\2\u00f5\u00e9\3\2"+
		"\2\2\u00f5\u00ec\3\2\2\2\u00f5\u00ed\3\2\2\2\u00f6\33\3\2\2\2\u00f7\u00f8"+
		"\5&\24\2\u00f8\u00f9\7+\2\2\u00f9\u00fa\t\2\2\2\u00fa\35\3\2\2\2\u00fb"+
		"\u00fc\7%\2\2\u00fc\u0101\5 \21\2\u00fd\u00fe\7\3\2\2\u00fe\u0100\5 \21"+
		"\2\u00ff\u00fd\3\2\2\2\u0100\u0103\3\2\2\2\u0101\u00ff\3\2\2\2\u0101\u0102"+
		"\3\2\2\2\u0102\u0104\3\2\2\2\u0103\u0101\3\2\2\2\u0104\u0105\7&\2\2\u0105"+
		"\37\3\2\2\2\u0106\u010b\7\64\2\2\u0107\u010b\7,\2\2\u0108\u010b\7-\2\2"+
		"\u0109\u010b\7*\2\2\u010a\u0106\3\2\2\2\u010a\u0107\3\2\2\2\u010a\u0108"+
		"\3\2\2\2\u010a\u0109\3\2\2\2\u010b!\3\2\2\2\u010c\u010d\b\22\1\2\u010d"+
		"\u010e\7%\2\2\u010e\u010f\5\"\22\2\u010f\u0110\7&\2\2\u0110\u013d\3\2"+
		"\2\2\u0111\u0112\7\n\2\2\u0112\u0113\7%\2\2\u0113\u0114\5\"\22\2\u0114"+
		"\u0115\7\3\2\2\u0115\u0116\7\64\2\2\u0116\u0117\7&\2\2\u0117\u013d\3\2"+
		"\2\2\u0118\u0119\7\13\2\2\u0119\u011a\7%\2\2\u011a\u011b\5\"\22\2\u011b"+
		"\u011c\7\3\2\2\u011c\u011d\5\"\22\2\u011d\u011e\7&\2\2\u011e\u013d\3\2"+
		"\2\2\u011f\u0120\7\f\2\2\u0120\u0121\7%\2\2\u0121\u0122\5\"\22\2\u0122"+
		"\u0123\7\3\2\2\u0123\u0124\5\"\22\2\u0124\u0125\7&\2\2\u0125\u013d\3\2"+
		"\2\2\u0126\u0127\7\16\2\2\u0127\u0128\7%\2\2\u0128\u0129\5\"\22\2\u0129"+
		"\u012a\7\3\2\2\u012a\u012b\5\"\22\2\u012b\u012c\7&\2\2\u012c\u013d\3\2"+
		"\2\2\u012d\u012e\7\r\2\2\u012e\u012f\7%\2\2\u012f\u013d\7&\2\2\u0130\u013d"+
		"\5&\24\2\u0131\u013d\5(\25\2\u0132\u013d\5*\26\2\u0133\u0134\7#\2\2\u0134"+
		"\u0135\7%\2\2\u0135\u0136\5\"\22\2\u0136\u0137\7\3\2\2\u0137\u0138\7,"+
		"\2\2\u0138\u0139\7&\2\2\u0139\u013d\3\2\2\2\u013a\u013d\7\63\2\2\u013b"+
		"\u013d\5 \21\2\u013c\u010c\3\2\2\2\u013c\u0111\3\2\2\2\u013c\u0118\3\2"+
		"\2\2\u013c\u011f\3\2\2\2\u013c\u0126\3\2\2\2\u013c\u012d\3\2\2\2\u013c"+
		"\u0130\3\2\2\2\u013c\u0131\3\2\2\2\u013c\u0132\3\2\2\2\u013c\u0133\3\2"+
		"\2\2\u013c\u013a\3\2\2\2\u013c\u013b\3\2\2\2\u013d\u0146\3\2\2\2\u013e"+
		"\u013f\f\20\2\2\u013f\u0140\7\66\2\2\u0140\u0145\5\"\22\21\u0141\u0142"+
		"\f\17\2\2\u0142\u0143\7\66\2\2\u0143\u0145\5,\27\2\u0144\u013e\3\2\2\2"+
		"\u0144\u0141\3\2\2\2\u0145\u0148\3\2\2\2\u0146\u0144\3\2\2\2\u0146\u0147"+
		"\3\2\2\2\u0147#\3\2\2\2\u0148\u0146\3\2\2\2\u0149\u014a\7\17\2\2\u014a"+
		"\u014b\7%\2\2\u014b\u014c\5\"\22\2\u014c\u014d\7\3\2\2\u014d\u0150\7\62"+
		"\2\2\u014e\u014f\7\3\2\2\u014f\u0151\7,\2\2\u0150\u014e\3\2\2\2\u0150"+
		"\u0151\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0153\7&\2\2\u0153%\3\2\2\2\u0154"+
		"\u0155\t\3\2\2\u0155\u0157\7%\2\2\u0156\u0158\7\27\2\2\u0157\u0156\3\2"+
		"\2\2\u0157\u0158\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u015a\7\63\2\2\u015a"+
		"\u015b\7&\2\2\u015b\'\3\2\2\2\u015c\u015d\7\35\2\2\u015d\u015e\7%\2\2"+
		"\u015e\u015f\5\"\22\2\u015f\u0160\7\3\2\2\u0160\u0165\5\"\22\2\u0161\u0162"+
		"\7\3\2\2\u0162\u0164\5\"\22\2\u0163\u0161\3\2\2\2\u0164\u0167\3\2\2\2"+
		"\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0168\3\2\2\2\u0167\u0165"+
		"\3\2\2\2\u0168\u0169\7&\2\2\u0169)\3\2\2\2\u016a\u016b\7\36\2\2\u016b"+
		"\u016c\7%\2\2\u016c\u016d\7\64\2\2\u016d\u016e\7\3\2\2\u016e\u0173\5\""+
		"\22\2\u016f\u0170\7\3\2\2\u0170\u0172\5\"\22\2\u0171\u016f\3\2\2\2\u0172"+
		"\u0175\3\2\2\2\u0173\u0171\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0176\3\2"+
		"\2\2\u0175\u0173\3\2\2\2\u0176\u0177\7&\2\2\u0177+\3\2\2\2\u0178\u017a"+
		"\7.\2\2\u0179\u017b\7/\2\2\u017a\u0179\3\2\2\2\u017a\u017b\3\2\2\2\u017b"+
		"\u017d\3\2\2\2\u017c\u017e\7\60\2\2\u017d\u017c\3\2\2\2\u017d\u017e\3"+
		"\2\2\2\u017e\u018e\3\2\2\2\u017f\u0181\7.\2\2\u0180\u017f\3\2\2\2\u0180"+
		"\u0181\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0184\7/\2\2\u0183\u0185\7\60"+
		"\2\2\u0184\u0183\3\2\2\2\u0184\u0185\3\2\2\2\u0185\u018e\3\2\2\2\u0186"+
		"\u0188\7.\2\2\u0187\u0186\3\2\2\2\u0187\u0188\3\2\2\2\u0188\u018a\3\2"+
		"\2\2\u0189\u018b\7/\2\2\u018a\u0189\3\2\2\2\u018a\u018b\3\2\2\2\u018b"+
		"\u018c\3\2\2\2\u018c\u018e\7\60\2\2\u018d\u0178\3\2\2\2\u018d\u0180\3"+
		"\2\2\2\u018d\u0187\3\2\2\2\u018e-\3\2\2\2)\659<?CFMSbmo}\u0085\u0087\u0090"+
		"\u0095\u009b\u00a5\u00b2\u00b8\u00c3\u00c7\u00f5\u0101\u010a\u013c\u0144"+
		"\u0146\u0150\u0157\u0165\u0173\u017a\u017d\u0180\u0184\u0187\u018a\u018d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}