package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.SkipCondition.RelationalOp;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.napi.FormException;

public class SkipRuleBuilder implements Serializable {
	private static final long serialVersionUID = 7311929396358174753L;

	private Container container;
	
	private Container condContainer;
	
	private SkipRule skipRule;
		
	public SkipRuleBuilder(Container container) {
		this.container = container;
		this.skipRule = new SkipRule();
	}
	
	public WhenBuilder when() {
		return new WhenBuilder();
	}
	
	public ActionBuilder perform() {
		return new ActionBuilder();
	}
	
	public SkipRule get() {
		if (skipRule.getConditions().isEmpty()) {
			throw new FormException("No conditions specified for skip rule");
		}
		
		if (skipRule.getActions().isEmpty()) {
			throw new FormException("No actions specified for skip rule");
		}
		
		return skipRule;
	}
	
	public class WhenBuilder {
		public ConditionBuilder anyOf() {
			skipRule.setLogicalOp(LogicalOp.OR);
			return new ConditionBuilder();
		}
		
		public ConditionBuilder allOf() {
			skipRule.setLogicalOp(LogicalOp.AND);
			return new ConditionBuilder();
		}
	}
	
	public class ConditionBuilder {				
		public ConditionBuilder eq(String fieldName, String value) {
			addCondition(fieldName, RelationalOp.EQ, value);
			return this;
		}
		
		public ConditionBuilder gt(String fieldName, String value) {
			addCondition(fieldName, RelationalOp.GT, value);
			return this;
		}
		
		public ConditionBuilder ge(String fieldName, String value) {
			addCondition(fieldName, RelationalOp.GE, value);
			return this;
		}		
		
		public ConditionBuilder lt(String fieldName, String value) {
			addCondition(fieldName, RelationalOp.LT, value);
			return this;
		}
		
		public ConditionBuilder le(String fieldName, String value) {
			addCondition(fieldName, RelationalOp.LE, value);
			return this;
		}		
		
		public SkipRuleBuilder then() {
			return SkipRuleBuilder.this;
		}
				
		private void addCondition(String fieldName, RelationalOp op, String value) {
			Control ctrl = container.getControl(fieldName, "\\.");
			
			if (ctrl == null) {
				throw new FormException("Invalid field name: " + fieldName);
			}
						
			if (condContainer != null && condContainer != ctrl.getContainer()) {
				throw new FormException("All conditions should be made up of fields from same form");
			}
			
			condContainer = ctrl.getContainer();
			
			SkipCondition condition = new SkipCondition();
			condition.setSourceControl(ctrl);
			condition.setRelationalOp(op);
			condition.setValue(value);
			
			skipRule.getConditions().add(condition);						
		}		
	}
	
	public class ActionBuilder {
		public ActionBuilder show(String fieldName) {
			addAction("show", fieldName);
			return this;
		}
		
		public ActionBuilder showAll(String ... fieldNames) {
			if (fieldNames != null) {
				for (String fieldName : fieldNames) {
					addAction("show", fieldName);
				}
			}
			
			return this;
		}

		public ActionBuilder hide(String fieldName) {
			addAction("hide", fieldName);
			return this;
		}
		
		public ActionBuilder hideAll(String ... fieldNames) {
			if (fieldNames != null) {
				for (String fieldName : fieldNames) {
					addAction("hide", fieldName);
				}
			}
			
			return this;
		}
		
		public ActionBuilder enable(String fieldName) {
			addAction("enable", fieldName);
			return this;
		}
		
		public ActionBuilder enableAll(String ... fieldNames) {
			if (fieldNames != null) {
				for (String fieldName : fieldNames) {
					addAction("enable", fieldName);
				}
			}
			
			return this;
		}
		
		public ActionBuilder disable(String fieldName) {
			addAction("disable", fieldName);
			return this;
		}
		
		public ActionBuilder disableAll(String ... fieldNames) {
			if (fieldNames != null) {
				for (String fieldName : fieldNames) {
					addAction("disable", fieldName);
				}
			}
			
			return this;
		}
		
		public ActionBuilder subsetPv(String fieldName, List<PermissibleValue> pvs, PermissibleValue defVal) {
			addSubsetPvAction(fieldName, pvs, defVal);
			return this;
		}
		
		public SkipRuleBuilder then() {
			return SkipRuleBuilder.this;
		}
		
		private void addAction(String actionName, String fieldName) {
			Control tgtCtrl = container.getControl(fieldName, "\\.");
			if (tgtCtrl == null) {
				throw new FormException("Invalid field name: " + fieldName);
			}
			
			SkipAction action = null;
			if (actionName.equals("show")) {
				action = new ShowAction();
			} else if (actionName.equals("hide")) {
				action = new HideAction();
			} else if (actionName.equals("enable")) {
				action = new EnableAction();
			} else if (actionName.equals("disable")) {
				action = new DisableAction();
			}
			
			action.setTargetCtrl(tgtCtrl);
			skipRule.getActions().add(action);
		}
		
		private void addSubsetPvAction(String fieldName, List<PermissibleValue> pvs, PermissibleValue defVal) {
			Control tgtCtrl = container.getControl(fieldName, "\\.");
			if (tgtCtrl == null) {
				throw new FormException("Invalid field name: " + fieldName);
			}
			
			ShowPvAction pvAction = new ShowPvAction();
			pvAction.setListOfPvs(pvs);
			pvAction.setDefaultPv(defVal);
			pvAction.setTargetCtrl(tgtCtrl);
			
			skipRule.getActions().add(pvAction);
		}
	}
}
