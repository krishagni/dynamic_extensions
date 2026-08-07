package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.AbstractLookupControl;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.nutility.ParserUtil;

public abstract class AbstractLookupControlFactory extends AbstractControlFactory {

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		AbstractLookupControl ctrl = (AbstractLookupControl) createControl();
		super.setControlProps(ctrl, ele, row, xPos);
		ctrl.setMultiValued(ParserUtil.getBooleanValue(ele, "multiple"));
		ctrl.setCollectionTable(ParserUtil.getTextValue(ele, "collectionTable"));
		ctrl.setCollectionKey(ParserUtil.getTextValue(ele, "collectionKey"));
		ctrl.setParentKey(ParserUtil.getTextValue(ele, "parentKey"));
		return ctrl;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		AbstractLookupControl ctrl = (AbstractLookupControl) createControl();
		super.setControlProps(ctrl, props, row, xPos);
		ctrl.setMultiValued(getBool(props, "multiple", false));
		return ctrl;
	}

	protected abstract Control createControl();
}
