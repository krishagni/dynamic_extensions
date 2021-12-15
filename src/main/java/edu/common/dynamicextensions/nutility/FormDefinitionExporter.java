package edu.common.dynamicextensions.nutility;

import edu.common.dynamicextensions.domain.nui.Container;

public class FormDefinitionExporter {
	public void export(Container container, String dir) {
		ContainerSerializer serializer = new ContainerXmlSerializer(container, dir);	
		serializer.serialize();
	}
}
