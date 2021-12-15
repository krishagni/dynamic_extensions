package edu.common.dynamicextensions.napi;

import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.JdbcDao;

//
// Persistence layer interface
//
public interface FormDataManager {
	FormData getFormData(Long containerId, Long recordId);
	
	FormData getFormData(Container container, Long recordId);

	List<FormData> getFormData(Long containerId, List<Long> recordIds);

	List<FormData> getFormData(Container container, List<Long> recordIds);
	
	List<FormData> getSummaryData(Long containerId, List<Long> recordIds);
	
	List<FormData> getSummaryData(Container container, List<Long> recordIds);
	
	Long saveOrUpdateFormData(UserContext userCtxt, FormData formData);
	
	Long saveOrUpdateFormData(UserContext userCtxt, FormData formData, JdbcDao jdbcDao);

	void deleteFormData(UserContext userCtxt, Long containerId, Long recordId);
	
	void anonymize(UserContext userCtxt, Container form, Long recordId);

	FileControlValue getFileControlValue(Long formId, Long recordId, String ctrlName);

	FileControlValue getFileControlValue(Long formId, String ctrlName, String fileId);
	
	FormDataFilterManager getFilterMgr();

	List<Long> getRecordIds(Container container, String ctrlName, Object value, boolean useUdn);
}
