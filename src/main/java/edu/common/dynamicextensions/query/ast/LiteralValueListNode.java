package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class LiteralValueListNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = 4108477895530979428L;
	
	private List<LiteralValueNode> literalVals = new ArrayList<LiteralValueNode>();

	@Override
	public DataType getType() {
		DataType type = null;
		if (literalVals.size() > 0) {
			type = literalVals.get(0).getType();
		}
		return type;
	}
	
	@Override
	public LiteralValueListNode copy() {
		LiteralValueListNode copy = new LiteralValueListNode();
		super.copy(this, copy);
		for (LiteralValueNode literal : literalVals) {
			copy.addLiteralVal(literal.copy());
		}
		
		return copy;
	}
	
	@Override
	public String[] getFormNames() {
		return new String[0];
	}
	
	public void addLiteralVal(LiteralValueNode literal) {
		this.literalVals.add(literal);
	}
	
	public List<LiteralValueNode> getLiteralVals() {
		return literalVals;
	}

	public int size() {
		return literalVals.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return prime * result + ((literalVals == null) ? 0 : literalVals.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		LiteralValueListNode other = (LiteralValueListNode) obj;
		if (literalVals == null && other.literalVals != null) {
			return false;
		} else if (!literalVals.equals(other.literalVals)) {
			return false;
		}
		
		return true;
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
