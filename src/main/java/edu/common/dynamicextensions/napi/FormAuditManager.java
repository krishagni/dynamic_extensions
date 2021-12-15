package edu.common.dynamicextensions.napi;

import java.util.List;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.JdbcDao;

public interface FormAuditManager {

	List<ControlValue> getDirtyFields(FormData prevData, FormData currData);

	void audit(UserContext userCtxt, Container form, List<ControlValue> dirtyFields, String operation, Long recordId);

	void audit(UserContext userCtxt, Container form, List<ControlValue> dirtyFields, String operation, Long recordId, JdbcDao jdbcDao);
}
