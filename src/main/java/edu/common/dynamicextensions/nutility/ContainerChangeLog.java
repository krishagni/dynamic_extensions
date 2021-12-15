package edu.common.dynamicextensions.nutility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Control;

public class ContainerChangeLog {
	private List<Control> addedCtrls = new ArrayList<>();
	
	private List<Control> deletedCtrls = new ArrayList<>();
	
	private List<Control> editedCtrls = new ArrayList<>();
	
	private Map<String, ContainerChangeLog> editedSubCtrls = new HashMap<>();

	public List<Control> getAddedCtrls() {
		return addedCtrls;
	}

	public void setAddedCtrls(List<Control> addedCtrls) {
		this.addedCtrls = addedCtrls;
	}

	public List<Control> getDeletedCtrls() {
		return deletedCtrls;
	}

	public void setDeletedCtrls(List<Control> deletedCtrls) {
		this.deletedCtrls = deletedCtrls;
	}

	public List<Control> getEditedCtrls() {
		return editedCtrls;
	}

	public void setEditedCtrls(List<Control> editedCtrls) {
		this.editedCtrls = editedCtrls;
	}
	
	public Map<String, ContainerChangeLog> getEditedSubCtrls() {
		return editedSubCtrls;
	}

	public void setEditedSubCtrls(Map<String, ContainerChangeLog> editedSubCtrls) {
		this.editedSubCtrls = editedSubCtrls;
	}

	public boolean anyChanges() {
		return !addedCtrls.isEmpty() || !editedCtrls.isEmpty() ||  !deletedCtrls.isEmpty() || !editedSubCtrls.isEmpty();
	}
}
