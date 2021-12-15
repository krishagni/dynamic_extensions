package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;

public class FileUploadFactory extends AbstractControlFactory {

	public static FileUploadFactory getInstance() {
		return new FileUploadFactory();
	}
	
	@Override
	public String getType() {
		return "fileUpload";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		FileUploadControl fileControl = new FileUploadControl();
		setControlProps(fileControl, ele, row, xPos);
		return fileControl;
	}

	@Override
	public Control parseControl(Map<String, Object> props, int row, int xPos) {
		FileUploadControl fileControl = new FileUploadControl();
		setControlProps(fileControl, props, row, xPos);
		return fileControl;
	}
}
