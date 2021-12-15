package edu.common.dynamicextensions.nutility;

import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

import edu.common.dynamicextensions.napi.FormException;

public class XmlUtil {
	
	/**
	 * Emits <element> 
	 */	
	public static void writeElementStart(Writer writer, String element) {
		String elementStart = new StringBuilder()
			.append("<").append(element).append(">").toString();
		write(writer, elementStart);
	}

	/**
	 * Emits <element attr1="value1" attr2="value2">
	 */
	public static void writeElementStart(Writer writer, String tag, Map<String, String> attrs) {
		
		StringBuilder startTag = new StringBuilder().append("<").append(tag);
		for(Entry<String, String> attr : attrs.entrySet()) {
			startTag.append(" ")
				.append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
		}
		startTag.append(">");
		write(writer,startTag.toString());
	}
	
	/**
	 * Emits </element>
	 */
	// </tag>
	//
	public static void writeElementEnd(Writer writer, String element) {
		String elementStart = new StringBuilder()
			.append("</").append(element).append(">").toString();
		write(writer, elementStart);
	}
	

	/**
	 * Emits <element>value</element>
	 */
	public static void writeElement(Writer writer, String element, Object value) {
		if(value == null) {
			return;
		}
		
		String elementStr = new StringBuilder()
			.append("<").append(element).append(">")
			.append(value)
			.append("</").append(element).append(">")
			.toString();
		
		write(writer, elementStr);
	}

	public static void writeCDataElement(Writer writer, String element, Object value) {
		if(value == null) {
			return;
		}
		
		String elementStr = new StringBuilder()
			.append("<").append(element).append(">")
			.append("<![CDATA[")
			.append(value)
			.append("]]>")
			.append("</").append(element).append(">")
			.toString();
		
		write(writer, elementStr);
	}

	/**
	 * Emits <element attr1="value1" attr2="value2">value</element>
	 */
	public static void writeElement(Writer writer, String element, Object value, Map<String, String> attrs) {
		StringBuilder elementStr = new StringBuilder();
		
		elementStr.append("<").append(element);
		for (Map.Entry<String, String> attr : attrs.entrySet()) {
			elementStr.append(" ")
				.append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
		}

		if (value != null) {
			elementStr.append(">").append(value).append("</").append(element).append(">");
		} else {
			elementStr.append("/>");
		}
		
		write(writer, elementStr.toString());
	}
	
	private static void write(Writer writer, String value) {
		try {
			writer.write(value);
			writer.write("\n");
		} catch (Exception e) {
			throw new FormException("Error writing value " + value, e);
		}
	}
}
