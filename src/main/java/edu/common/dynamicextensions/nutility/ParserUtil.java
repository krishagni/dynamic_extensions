package edu.common.dynamicextensions.nutility;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.napi.FormException;

public class ParserUtil {
	public static Long getLongValue(Element element, String name) {
		String value = getTextValue(element, name);
		if (value == null || value.trim().isEmpty()) {
			return null;
		} else {
			return Long.parseLong(value);
		}
	}
	
	public static Integer getIntValue(Element element, String name, Integer defValue) {
		Integer value = getIntValue(element, name);

		if (value == null) {
			return defValue;
		}
		return value;
	}

	public static Integer getIntValue(Element element, String name) {
		String value = getTextValue(element, name);
		if (value == null || value.trim().isEmpty()) {
			return null;
		} else {
			return Integer.parseInt(value);
		}
	}
	
	public static boolean getBooleanValue(Element element, String name, boolean defVal) {
		String value = getTextValue(element, name);
		if (value == null || value.trim().isEmpty()) {
			return defVal;
		} else {
			return Boolean.parseBoolean(value);
		}
		
	}
	
	public static boolean getBooleanValue(Element element, String name) {
		return getBooleanValue(element, name, false);
	}
		
	public static String getTextValue(Element element, String name) {
		return getTextValue(element, name, null);
	}
	
	public static String getTextValue(Element element, String name, String def) {
		NodeList nodeList = element.getElementsByTagName(name);
		if (nodeList == null || nodeList.getLength() == 0) {
			return def;
		}
		
		//
		// This check will ensure element with name is direct child of input element
		//
		if (element != nodeList.item(0).getParentNode()) { 
			return def;
		}
		return getTextValue(nodeList.item(0), def);		
	}
	
	public static String getTextValue(Node node, String def) {
		if (node.getFirstChild() == null) {
			//
			// Handles scenario where-in we've elements like
			// <toolTip/> or <toolTip></toolTip>
			//
			return def;
		}
		
		if (node.getFirstChild().getNodeType() == Node.TEXT_NODE || node.getFirstChild().getNodeType() == Node.CDATA_SECTION_NODE) {
			StringBuilder text = new StringBuilder();

			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); ++i) {
				text.append(childNodes.item(i).getNodeValue().trim());
			}

			return text.toString();
		}
		
		throw new FormException("Element " + node.getNodeName() + " is not a text element");
	}
	
	public static List<PermissibleValue> getPermissibleValues(Element optionsParentEl, String pvDir) {
		List<PermissibleValue> pvs = null;
		
		Element options = (Element)optionsParentEl.getElementsByTagName("options").item(0);		
		NodeList optionFile = options.getElementsByTagName("optionsFile");
		if (optionFile != null && optionFile.getLength() == 1) { // read options from file
			String file = optionFile.item(0).getFirstChild().getNodeValue();
			pvs = getPermissibleValueFromFile(pvDir, file);
		} else { // inline options
			NodeList optionList = options.getElementsByTagName("option");
			pvs = new ArrayList<PermissibleValue>();

			for (int i = 0; i < optionList.getLength(); ++i) {
				Node option = optionList.item(i);
				Element optionEle = (Element)option;
				
				PermissibleValue pv = new PermissibleValue();
				pv.setValue(getTextValue(optionEle, "value"));
				pv.setShowWhen(getTextValue(optionEle, "showWhen"));
				pv.setNumericCode(getLongValue(optionEle, "numericCode"));
//				pv.setOptionName(pv.getValue());
				
				if (pv.getValue() != null) {
					pvs.add(pv);
				}
			}
		}
		
		return pvs;
	}
	
	public static List<PermissibleValue> getPermissibleValueFromFile(String pvDir, String optionsFile) {
		FileReader reader = null;
		CSVReader csvReader = null;		
		try {
			List<PermissibleValue> pvs = new ArrayList<PermissibleValue>();
			
			String filePath = pvDir + File.separator + optionsFile;			
			reader = new FileReader(filePath);			
			csvReader = new CSVReader(reader);
			String[] option = null;

			while ((option = csvReader.readNext()) != null) {
				PermissibleValue pv = new PermissibleValue();
				if (option[0].isEmpty()) {
					pv.setValue(null);
				} else {
					int idx = option[0].indexOf("##:");
					if (idx != -1) {
						pv.setNumericCode(Long.parseLong(option[0].substring(0, idx)));
						pv.setValue(option[0].substring(idx + 3));
					} else {
						pv.setValue(option[0]);
					}
				}

				if (option.length > 1 && option[1] != null && !option[1].isEmpty()) {
					pv.setShowWhen(option[1]);
				}
//				pv.setOptionName(pv.getValue());				
				pvs.add(pv);
			}
			return pvs;
		} catch (Exception e) {
			throw new FormException ("Error reading options file: " + optionsFile, e);
		} finally {
			IoUtil.close(csvReader);
			IoUtil.close(reader);
		}
	}	
}
