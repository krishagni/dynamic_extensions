package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;

public interface ControlFactory {
	String getType();
	
	Control parseControl(Element ele, int row, int xPos, Properties props);

	Control parseControl(Map<String, Object> props, int row, int xPos);
}
