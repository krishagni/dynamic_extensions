package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.AbstractLookupControl;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.nutility.ParserUtil;

public abstract class AbstractLookupControlFactory extends AbstractControlFactory {

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		Control ctrl = createControl();
		super.setControlProps(ctrl, ele, row, xPos);

		if (ctrl instanceof AbstractLookupControl) {
			AbstractLookupControl luCtrl = (AbstractLookupControl) ctrl;
			luCtrl.setMultiValued(ParserUtil.getBooleanValue(ele, "multiple"));
			luCtrl.setCollectionTable(ParserUtil.getTextValue(ele, "collectionTable"));
			luCtrl.setCollectionKey(ParserUtil.getTextValue(ele, "collectionKey"));
			luCtrl.setParentKey(ParserUtil.getTextValue(ele, "parentKey"));
		}


		return ctrl;
	}

	protected abstract Control createControl();
}
