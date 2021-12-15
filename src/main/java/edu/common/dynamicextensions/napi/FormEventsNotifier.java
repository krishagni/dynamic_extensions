package edu.common.dynamicextensions.napi;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;

public class FormEventsNotifier {
	private static FormEventsNotifier instance = new FormEventsNotifier();

	private List<FormEventsListener> listeners = new ArrayList<>();

	private FormEventsNotifier() {
	}

	public static FormEventsNotifier getInstance() {
		return instance;
	}

	public void addListener(FormEventsListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeListener(FormEventsListener listener) {
		listeners.remove(listener);
	}

	public void notifyCreate(Container form) {
		listeners.forEach(listener -> listener.onCreate(form));
	}
	
	public void notifyPreUpdate(Container form) {
		listeners.forEach(listener -> listener.preUpdate(form));
	}

	public void notifyUpdate(Container form) {
		listeners.forEach(listener -> listener.onUpdate(form));
	}

	public void notifyDelete(Container form) {
		listeners.forEach(listener -> listener.onDelete(form));
	}
}