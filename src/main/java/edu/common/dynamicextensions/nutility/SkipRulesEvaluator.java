package edu.common.dynamicextensions.nutility;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SkipAction;
import edu.common.dynamicextensions.domain.nui.SkipCondition;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;

public class SkipRulesEvaluator {
	private Map<Control, Boolean> evaluatedTgtCtrls = new IdentityHashMap<Control, Boolean>();
	
	public void evaluate(FormData formData) {
		Container form = null;//formData.getContainer();
		evaluateSkipRules(formData, getFormRules(form, form.getSkipRules()));
		
		for (ControlValue cv : formData.getFieldValues()) {
			if (!(cv.getControl() instanceof SubFormControl)) {
				continue;
			}
			
			SubFormControl sfCtrl = (SubFormControl)cv.getControl();
			List<SkipRule> formRules = getFormRules(sfCtrl.getSubContainer(), form.getSkipRules());
				
			List<FormData> subFormDataList = (List<FormData>)cv.getValue();
			if (subFormDataList != null) {
				for (FormData subFormData : subFormDataList) {
					evaluateSkipRules(subFormData, formRules);
				}					
			}
		}
	}
	
	private void evaluateSkipRules(FormData formData, List<SkipRule> rules) {
		evaluatedTgtCtrls.clear();
		for (SkipRule rule : rules) {
			evaluateSkipRule(formData, rule);
		}		
	}
	
	public void evaluateSkipRule(FormData data, SkipRule rule) {
		boolean result = false;
		
		for (SkipCondition condition : rule.getConditions()) {
			ControlValue fieldValue = data.getFieldValue(condition.getSourceControl().getName());
			result = condition.evaluate(fieldValue);
			
			if (result && rule.getLogicalOp() == LogicalOp.OR) {
				break;
			} else if (!result && rule.getLogicalOp() == LogicalOp.AND) {
				break;
			}			
		}
		
		for (SkipAction action : rule.getActions()) {
			if (evaluatedTgtCtrls.containsKey(action.getTargetCtrl())) { 
				// one of the skip rule evaluated to true for this target control
				continue;
			}
			
			List<ControlValue> fieldValues = new ArrayList<ControlValue>();//data.getFieldValue(action.getTargetCtrl());
			if (fieldValues == null) {
				continue;
			}
				
			for (ControlValue fieldValue : fieldValues) {
				if (result) {
					action.perform(fieldValue);
					evaluatedTgtCtrls.put(action.getTargetCtrl(), true);
				} else {
					action.reset(fieldValue);
				}				
			}
		}
	}
		
	private List<SkipRule> getFormRules(Container form, List<SkipRule> rules) {
		List<SkipRule> currentFormRules = new ArrayList<SkipRule>();
		
		for (SkipRule rule : rules) {
			boolean add = true;
			for (SkipCondition cond : rule.getConditions()) {
				if (form != cond.getSourceControl().getContainer()) {					
					add = false;
					break;
				} 
			}
			
			if (add) {
				currentFormRules.add(rule);
			} 
		}
		
		return currentFormRules;		
	}	
}
