package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.common.dynamicextensions.domain.nui.DataType;

public class ConcatNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = -285272789497508681L;

	private String separator;

	private List<ExpressionNode> args = new ArrayList<>();

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public List<ExpressionNode> getArgs() {
		return args;
	}

	public void setArgs(List<ExpressionNode> args) {
		this.args = args;
	}

	public void addArg(ExpressionNode arg) {
		args.add(arg);
	}

	@Override
	public DataType getType() {
		return DataType.STRING;
	}

	@Override
	public ExpressionNode copy() {
		ConcatNode copy = new ConcatNode();
		super.copy(this, copy);
		copy.setSeparator(getSeparator());
		copy.setArgs(getArgs().stream().map(arg -> arg.copy()).collect(Collectors.toList()));
		return copy;
	}

	@Override
	public String[] getFormNames() {
		Set<String> formNames = new HashSet<>();
		for (ExpressionNode arg : getArgs()) {
			for (String formName : arg.getFormNames()) {
				formNames.add(formName);
			}
		}

		return formNames.toArray(new String[0]);
	}

	@Override
	public boolean isPhi() {
		ExpressionNode phiArg = getArgs().stream().filter(arg -> arg.isPhi()).findFirst().orElse(null);
		return phiArg != null;
	}

	//
	// Checks whether any of its args is made of pure PHI field.
	//
	public boolean hasPhiField() {
		for (ExpressionNode en : getArgs()) {
			if (en instanceof FieldNode && en.isPhi()) {
				return true;
			} else if (en instanceof ConcatNode) {
				boolean hasPhiField = ((ConcatNode) en).hasPhiField();
				if (hasPhiField) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public Set<FieldNode> getFields() {
		Set<FieldNode> fields = new HashSet<>();
		for (ExpressionNode arg : getArgs()) {
			fields.addAll(arg.getFields());
		}

		return fields;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (ExpressionNode arg : getArgs()) {
			result = prime * result + arg.hashCode();
		}

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		ConcatNode other = (ConcatNode) obj;
		if (getArgs().size() != other.getArgs().size()) {
			return false;
		}

		for (int i = 0; i < getArgs().size(); ++i) {
			if (!getArgs().get(i).equals(other.getArgs().get(i))) {
				return false;
			}
		}

		return true;
	}
}