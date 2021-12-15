package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class CurrentDateNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = -503108091981431728L;

	@Override
	public DataType getType() {
		return DataType.DATE;
	}

	@Override
	public ExpressionNode copy() {
		CurrentDateNode copy = new CurrentDateNode();
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
