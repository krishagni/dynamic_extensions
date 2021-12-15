package edu.common.dynamicextensions.nutility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.nfunk.jep.JEP;

import edu.common.dynamicextensions.napi.FormException;

public class FormulaParser {
	private JEP parser = null;

	public FormulaParser()  {
		parser = new JEP();
		parser.setAllowUndeclared(true);
		parser.setAllowAssignment(true);
		parser.addStandardFunctions();
		parser.addStandardConstants();
	}

	public JEP getParser() {
		return parser;
	}

	public void setParser(JEP parser) {
		this.parser = parser;
	}

	public boolean parseExpression(String expr) {
		parser.parseExpression(expr); // Parse the expression
		if (parser.hasError()) {
			throw new FormException(parser.getErrorInfo());
		}

		return true;
	}

	public void setVariableValue(String operand, Object value) {
		parser.setVarValue(operand,value);
	}

	public BigDecimal evaluateExpression() {
		return new BigDecimal(parser.getValue()).stripTrailingZeros();
	}

	public boolean validateExpression(String formulaExpression) {
		return parseExpression(formulaExpression);
	}

	public List<String> getSymbols() {
		List<String> symbols = new ArrayList <>();

		Enumeration enumeration = parser.getSymbolTable().keys();
		while (enumeration.hasMoreElements()) {
			String element = (String) enumeration.nextElement();
			if (!("pi".equals(element)) && !("e".equals(element))) {
				symbols.add(element);
			}
		}

		return symbols;
	}
}