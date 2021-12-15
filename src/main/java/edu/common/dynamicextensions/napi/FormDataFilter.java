package edu.common.dynamicextensions.napi;

import edu.common.dynamicextensions.domain.nui.UserContext;

public interface FormDataFilter {
	public FormData execute(UserContext userCtx, FormData input);
}
