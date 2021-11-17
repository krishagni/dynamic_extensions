package edu.common.dynamicextensions.nutility;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import com.google.gson.Gson;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormException;

public class ContainerJsonSerializer implements ContainerSerializer {
	private Container container;
	
	private OutputStream out;
	
	private Writer writer;

	public ContainerJsonSerializer(Container container, OutputStream out) {
		this.container = container;
		this.out = out;		
	}
	
	public ContainerJsonSerializer(Container container, Writer writer) {
		this.container = container;
		this.writer = writer;		
	}
	
	@Override
	public void serialize() {
		serialize(0);
	}	

	@Override
	public void serialize(int maxPvListSize) {
		container.setMaxPvListSize(maxPvListSize);

		Map<String, Object> containerProps = container.getProps();
		try {
			containerProps.put("id", container.getId());

			String json = new Gson().toJson(containerProps);
			if (out != null) {
				out.write(json.getBytes());
			} else {
				writer.write(json);
			}			
		} catch (Exception e) {
			throw new FormException("Error writing to output stream");
		}		
	}
}
