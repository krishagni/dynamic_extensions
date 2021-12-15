package edu.common.dynamicextensions.napi;

import java.util.Date;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;

public interface VersionedContainer {
	public String getFormName(Long formId);
	
	public Long getContainerId(Long formId);
	
	public Long getContainerId(Long formId, Date activationDate);
	
	public String getContainerName(Long formId);
	
	public String getContainerName(Long formId, Date activationDate);
	
	public Container getContainer(Long formId);
	
	public Container getContainer(Long formId, Date activationDate);
	
	public Container getContainer(String formName);
	
	public Container getContainer(String formName, Date activationDate);
	
	public Long getDraftContainerId(Long formId);
	
	public Container getDraftContainer(Long formId);
	
	public Long saveAsDraft(UserContext usrCtx, Long draftContainerId);
	
	public void publishRetrospective(UserContext usrCtx, Long formId);
	
	public void publishProspective(UserContext usrCtx, Long formId, Date activationDate);
	
	public boolean isChangedSinceLastPublish(Long formId);
}