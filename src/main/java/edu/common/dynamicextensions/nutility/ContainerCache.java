package edu.common.dynamicextensions.nutility;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;

public class ContainerCache {
	private static final int MAX_ELEMENTS = 100;
	
	private static final ContainerCache instance = new ContainerCache();
	
	private Map<Long, Container> cacheMap = Collections.synchronizedMap(
			new LinkedHashMap<Long, Container>(MAX_ELEMENTS + 2, 0.75f, true) {
				private static final long serialVersionUID = -9164975002022907616L;
				
				@Override
				public boolean removeEldestEntry(Map.Entry<Long, Container> eldest) {
					if (size() > MAX_ELEMENTS) {
						nameToIdsMap.remove(eldest.getValue().getName());
						return true;
					} else {
						return false;
					}
				}
			});

	private Map<String, Long> nameToIdsMap = Collections.synchronizedMap(new LinkedHashMap<>());
	
	private ContainerCache() { }
	
	public static ContainerCache getInstance() {
		return instance;
	}
	
	public Container get(Long id) {
		return cacheMap.get(id);
	}

	public Container get(String name) {
		Long id = nameToIdsMap.get(name);
		return id != null ? get(id) : null;
	}
	
	public Container put(Container container) {
		if (container == null || container.getId() == null) {
			return container;
		}

		cacheMap.put(container.getId(), container);
		nameToIdsMap.put(container.getName(), container.getId());
		return container;
	}

	public void clear() {
		nameToIdsMap.clear();
		cacheMap.clear();
	}

	public boolean isEmpty() {
		return cacheMap.isEmpty();
	}

	public Container remove(Long id) {
		Container form = cacheMap.remove(id);
		if (form != null) {
			nameToIdsMap.remove(form.getName());
		}

		return form;
	}

	public Container remove(String name) {
		Long id = nameToIdsMap.remove(name);
		return id != null ? cacheMap.remove(id) : null;
	}

	public int size() {
		return cacheMap.size();
	}

	public Collection<Container> values() {
		return cacheMap.values();
	}
}
