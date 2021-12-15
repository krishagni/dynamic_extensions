package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.LookupControl;

public class FieldNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = -1438504214260687216L;

	private Control ctrl;
	
	private String name;
	
	private String tabAlias;
	
	private String[] nodeCaptions = new String[0];
	
	public Control getCtrl() {
		return ctrl;
	}

	public void setCtrl(Control ctrl) {
		this.ctrl = ctrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTabAlias() {
		return tabAlias;
	}

	public void setTabAlias(String tabAlias) {
		this.tabAlias = tabAlias;
	}

	public String[] getNodeCaptions() {
		return nodeCaptions;
	}

	public void setNodeCaptions(String[] nodeCaptions) {
		this.nodeCaptions = nodeCaptions;
	}

	@Override
	public DataType getType() {
		DataType type = ctrl != null ? ctrl.getDataType() : null;
		if (ctrl instanceof FileUploadControl) {
			type = DataType.STRING;
		} else if (ctrl instanceof LookupControl) {
			type = ((LookupControl)ctrl).getValueType();
		}
		
		return type;
	}

	@Override
	public FieldNode copy() {
		FieldNode copy = new FieldNode();
		super.copy(this, copy);
		copy.setCtrl(ctrl);
		copy.setName(name);
		copy.setTabAlias(tabAlias);
		
		String[] copyNodeCaptions = new String[nodeCaptions.length];
		for (int i = 0; i < nodeCaptions.length; ++i) {
			copyNodeCaptions[i] = nodeCaptions[i];
		}

		return copy;
	}	
	
	@Override
	public String[] getFormNames() {
		String[] fieldNameParts = name.split("\\.");
		return new String[] {fieldNameParts[0]};
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ctrl == null) ? 0 : ctrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(nodeCaptions);
		result = prime * result	+ ((tabAlias == null) ? 0 : tabAlias.hashCode());
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
		
		FieldNode other = (FieldNode) obj;
		if (ctrl == null && other.ctrl != null) {
			return false;
		} else if (ctrl != null && !ctrl.equals(other.ctrl)) {
			return false;
		}
		
		if (name == null && other.name != null) {
			return false;
		} else if (name != null && !name.equals(other.name)) {
			return false;
		}
		
		if (!Arrays.equals(nodeCaptions, other.nodeCaptions)) {
			return false;
		}
		
		if (tabAlias == null && other.tabAlias != null) {
			return false;
		} else if (tabAlias != null && !tabAlias.equals(other.tabAlias)) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isPhi() {
		return ctrl.isPhi();
	}

	@Override
	public Set<FieldNode> getFields() {
		return Collections.singleton(this);
	}
}