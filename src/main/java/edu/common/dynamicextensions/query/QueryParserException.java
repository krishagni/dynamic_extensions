package edu.common.dynamicextensions.query;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class QueryParserException extends RuntimeException {
	private static final long serialVersionUID = 6564470605442099072L;
	
	private String message;
	
	public QueryParserException(ParseCancellationException e) {
		if (e.getCause() instanceof RecognitionException) {
			RecognitionException recExp = (RecognitionException)e.getCause();

			String input = "";
			if (recExp instanceof NoViableAltException) {
				NoViableAltException ex = (NoViableAltException) recExp;
				if (ex.getStartToken() != null) {
					input += " " + ex.getStartToken().getText() + " : ";
				}
			}

			Token token = recExp.getOffendingToken();
			input += token.getText();
			message = new StringBuilder()
			  .append("Recognition exception at input: ").append(input)
			  .append(". Position: ").append(token.getLine()).append(":").append(token.getCharPositionInLine())
			  .toString();
		} else if (e.getCause() != null) {
			message = e.getCause().getMessage();
		} else {
			message = "Unknown parse error";
		}				
	}
	
	public String getMessage() {
		return message;
	}
}
