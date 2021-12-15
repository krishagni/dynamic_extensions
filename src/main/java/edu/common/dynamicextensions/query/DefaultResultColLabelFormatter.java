package edu.common.dynamicextensions.query;

import java.util.HashMap;
import java.util.Map;

public class DefaultResultColLabelFormatter implements ResultColumnLabelFormatter {
	private String separator = "# ";

	private String instanceSeparator = " - ";

	private Map<String, Integer> instanceCntMap = new HashMap<String, Integer>();
	
	public DefaultResultColLabelFormatter(String separator) {
		this(separator, " - ");
	}

	public DefaultResultColLabelFormatter(String separator, String instanceSeparator) {
		this.separator = separator;
		this.instanceSeparator = instanceSeparator;
	}

	@Override
	public String format(String[] nodeCaptions, int instance) {
		StringBuilder heading = new StringBuilder();

		for (int j = 0; j < nodeCaptions.length - 1; ++j) {
			heading.append(nodeCaptions[j]).append(separator);
		}		
		heading.append(nodeCaptions[nodeCaptions.length - 1]);

		String headingStr = heading.toString();
		Integer instanceCnt = instanceCntMap.get(headingStr);
		if (instanceCnt == null) {
			instanceCnt = 0;
		}
		
		String result = "";
		if (nodeCaptions.length > 0) {
			result = nodeCaptions[0];
		}
		
		if (nodeCaptions.length > 1 && nodeCaptions[1].startsWith("$$_") && nodeCaptions[1].endsWith("_$$")) {
			result += separator + nodeCaptions[2];
		}
		
		if (nodeCaptions.length > 1) {
			result += separator + nodeCaptions[nodeCaptions.length - 1];
		}
				
		if (instanceCnt > 0) {
			result += instanceSeparator + instanceCnt;
		}

		instanceCntMap.put(headingStr, instanceCnt + 1);
		return result;
	}
}
