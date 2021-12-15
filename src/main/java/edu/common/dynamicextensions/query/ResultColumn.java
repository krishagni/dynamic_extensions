package edu.common.dynamicextensions.query;

import java.io.Serializable;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.query.ast.ConcatNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;

public class ResultColumn implements Serializable {
	private static final long serialVersionUID = 9037400363981158491L;

	private ExpressionNode columnExpr;
	
	private int instance;
	
	private Object value;

	private transient boolean firstColumnOfShallowForm;
	
	public ResultColumn(ExpressionNode columnExpr, int instance) {
		this.columnExpr = columnExpr;
		this.instance = instance;
	}
	
	public ResultColumn(ExpressionNode columnExpr, Object value) {
		this.columnExpr = columnExpr;
		this.value = value;
	}
	
	public ExpressionNode getExpression() {
		return columnExpr;
	}
	
	public int getInstance() {
		return instance;
	}
	
	public void setInstance(int instance) {
		this.instance = instance;
	}
	
	public Object getValue() {
		return value;
	}

	public boolean isFirstColumnOfShallowForm() {
		return firstColumnOfShallowForm;
	}

	public void setFirstColumnOfShallowForm(boolean firstColumnOfShallowForm) {
		this.firstColumnOfShallowForm = firstColumnOfShallowForm;
	}

	public String getColumnLabel() {
		return getColumnLabel(null);
	}
	
	public String getColumnLabel(ResultColumnLabelFormatter formatter) {
		String[] captions;
		if (columnExpr.getLabel() != null) {
			captions = new String[] {columnExpr.getLabel()};
		} else if (!(columnExpr instanceof FieldNode)) {
			captions = new String[] {"Column"};
		} else {
			captions = ((FieldNode)columnExpr).getNodeCaptions();
		}
		
		return format(formatter, captions, instance);		
	}

	public String getColumnExpr() { return getColumnExpr(null); }

	public String getColumnExpr(ResultColumnLabelFormatter formatter) {
		if (columnExpr.getLabel() != null && columnExpr.getLabel().startsWith("$")) {
			return format(formatter, new String[] { columnExpr.getLabel() }, instance);
		}

		String[] labels = {columnExpr.getAql()};
		return format(formatter, labels, instance);
	}

	public String getUrl() {
		String url = null;
		if (columnExpr instanceof FieldNode) {
			Control ctrl = ((FieldNode)columnExpr).getCtrl();
			if (ctrl instanceof StringTextField && ((StringTextField) ctrl).isUrl()) {
				url = "true";
			} else if (ctrl.getRecordUrl() != null) {
				url = ctrl.getRecordUrl();
			}
		}

		return url;
	}
	
	public String[] getCaptions() {
		if (!(columnExpr instanceof FieldNode)) {
			return new String[] {"Column - " + instance};
		}
		
		return ((FieldNode)columnExpr).getNodeCaptions();
	}
	
	public boolean isPhi() {
		return columnExpr.isPhi();
	}

	public boolean isSimpleExpr() {
		return isSimpleExpr(columnExpr);
	}

	public int getScale() {
		int precision = 0;
		for (FieldNode field : columnExpr.getFields()) {
			if (field.getCtrl() instanceof NumberField) {
				NumberField numberField = (NumberField)field.getCtrl();
				if (numberField.getNoOfDigitsAfterDecimal() > precision) {
					precision = numberField.getNoOfDigitsAfterDecimal();
				}
			}
		}

		return precision;
	}
	
	public String toString() {
		return getColumnLabel();
	}

	private boolean isSimpleExpr(ExpressionNode expr) {
		if (expr instanceof FieldNode) {
			return true;
		} else if (expr instanceof ConcatNode) {
			ExpressionNode field = ((ConcatNode) expr).getArgs().stream()
				.filter(arg -> isSimpleExpr(arg))
				.findFirst().orElse(null);
			return field != null;
		} else {
			return false;
		}
	}
	
	private String format(ResultColumnLabelFormatter formatter, String[] captions, int instance) {
		if (formatter != null) {
			return formatter.format(captions, instance);
		} 
		
		StringBuilder result = new StringBuilder();
		for (String caption : captions) {
			result.append(caption).append("#");
		}
		
		result.append(instance);
		return result.toString();
	}
}
