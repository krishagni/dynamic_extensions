package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class CurrentTimestampNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = 8732187126164817258L;

	@Override
	public DataType getType() {
		return DataType.DATE;
	}

	@Override
	public ExpressionNode copy() {
		CurrentTimestampNode copy = new CurrentTimestampNode();
		super.copy(this, copy);
		return copy;
	}

	@Override
	public String[] getFormNames() {
		return new String[0];
	}

	@Override
	public boolean isPhi() {
		return false;
	}

	@Override
	public Set<FieldNode> getFields() {
		return Collections.emptySet();
	}
}
