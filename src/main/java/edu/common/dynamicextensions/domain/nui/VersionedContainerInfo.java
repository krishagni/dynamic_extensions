package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.Date;

public class VersionedContainerInfo implements Serializable {
	private static final long serialVersionUID = -6610593654188246623L;

	private Long formId;
	
	private Long containerId;
	
	private Date activationDate;
	
	private Long createdBy;
	
	private Date creationTime;
	
	private String status;
	
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
