package edu.common.dynamicextensions.domain.nui.factory;

import java.util.HashMap;
import java.util.Map;

public class ControlManager {
	private Map<String, ControlFactory> ctrlFactoryMap = new HashMap<String, ControlFactory>();
	
	private static ControlManager instance = new ControlManager();
	
	public static ControlManager getInstance() {
		return instance;
	}
	
	private ControlManager() {
		// default control factories
		registerFactory(LabelFactory.getInstance());
		registerFactory(TextFieldFactory.getInstance());
		registerFactory("stringTextField", TextFieldFactory.getInstance());
		registerFactory(TextAreaFactory.getInstance());
		registerFactory(NumberFieldFactory.getInstance());
		registerFactory(BooleanCheckboxFactory.getInstance());
		registerFactory("booleanCheckbox", BooleanCheckboxFactory.getInstance());
		registerFactory(DropdownFactory.getInstance());
		registerFactory("combobox", DropdownFactory.getInstance());
		registerFactory(DatePickerFactory.getInstance());
		registerFactory(FileUploadFactory.getInstance());
		registerFactory(SignatureFactory.getInstance());
		registerFactory(ListBoxFactory.getInstance());
		registerFactory(MultiSelectListBoxFactory.getInstance());
		registerFactory(MultiSelectCheckboxFactory.getInstance());
		registerFactory("checkbox", MultiSelectCheckboxFactory.getInstance());
		registerFactory(RadioButtonFactory.getInstance());
		registerFactory("radiobutton", RadioButtonFactory.getInstance());
		registerFactory(SubFormControlFactory.getInstance());
		registerFactory(LinkControlFactory.getInstance());
	}
	
	public void registerFactory(ControlFactory factory) {
		ctrlFactoryMap.put(factory.getType(), factory);
	}

	public void registerFactory(String type, ControlFactory factory) {
		ctrlFactoryMap.put(type, factory);
	}
	
	public ControlFactory getFactory(String type) {
		return ctrlFactoryMap.get(type);
	}
}
