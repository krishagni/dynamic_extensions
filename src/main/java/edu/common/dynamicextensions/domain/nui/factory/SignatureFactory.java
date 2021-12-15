package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SignatureControl;

public class SignatureFactory extends AbstractControlFactory {

	public static SignatureFactory getInstance() {
		return new SignatureFactory();
	}

	@Override
	public String getType() {
		return "signature";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		SignatureControl control = new SignatureControl();
		setControlProps(control, ele, row, xPos);
		return control;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		SignatureControl control = new SignatureControl();
		setControlProps(control, props, row, xPos);
		return control;
	}
}
