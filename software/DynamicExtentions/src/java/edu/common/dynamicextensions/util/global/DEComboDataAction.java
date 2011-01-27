
package edu.common.dynamicextensions.util.global;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.action.BaseDynamicExtensionsAction;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * @author kunal_kamble
 * This action is called in the auto complete dropdown combo box
 *
 */
public class DEComboDataAction extends BaseDynamicExtensionsAction
{

	/** The Constant CONTROL_ID. */
	private static final String CONTROL_ID = "controlId";

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String limit = request.getParameter("limit");
		String query = request.getParameter("query");
		String start = request.getParameter("start");

		String controlIdStr = request.getParameter(CONTROL_ID);

		// Create a map out of control Id Str
		Map<String, String> keyMap = extractRequestParameter(controlIdStr);

		String controlId = keyMap.get("controlId");;
		String containerId = keyMap.get("containerIdentifier");
		String[] sourceValues = {};

		String[] sourceHtmlComponentValues = null;
		if (sourceValues.length > 1)
		{
			sourceHtmlComponentValues = sourceValues[1].split("~");
		}
		List<String> sourceControlValues = new ArrayList<String>();
		if (sourceHtmlComponentValues != null)
		{
			for (String sourceValue : sourceHtmlComponentValues)
			{
				if (sourceValue != null && sourceValue.length() > 0)
				{
					sourceControlValues.add(sourceValue);
				}
			}
		}
		Integer limitFetch = Integer.parseInt(limit);
		Integer startFetch = Integer.parseInt(start);

		JSONArray jsonArray = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();

		ContainerInterface container = (ContainerInterface) getContainerFromUserSesion(request,
				containerId);
		if (container == null)
		{
			container = EntityCache.getInstance().getContainerById(Long.valueOf(containerId));
		}

		List<NameValueBean> nameValueBeans = null;

		for (ControlInterface control : container.getControlCollection())
		{
			if (Long.parseLong(controlId) == control.getId())
			{
				nameValueBeans = ControlsUtility.populateListOfValues(control, sourceControlValues,
						ControlsUtility.getFormattedDate(keyMap.get(Constants.ENCOUNTER_DATE)));
			}
		}

		Integer total = limitFetch + startFetch;

		List<NameValueBean> querySpecificNVBeans = new ArrayList<NameValueBean>();
		populateQuerySpecificNameValueBeansList(querySpecificNVBeans, nameValueBeans, query);
		mainJsonObject.put("totalCount", querySpecificNVBeans.size());

		for (int i = startFetch; i < total && i < querySpecificNVBeans.size(); i++)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", querySpecificNVBeans.get(i).getValue());
			jsonObject.put("field", querySpecificNVBeans.get(i).getName());
			jsonArray.put(jsonObject);
		}

		mainJsonObject.put("row", jsonArray);
		response.flushBuffer();
		PrintWriter out = response.getWriter();
		out.write(mainJsonObject.toString());

		return null;
	}

	/**
	 * @param controlIdStr
	 * @return
	 */
	private Map<String, String> extractRequestParameter(String controlIdStr)
	{
		Map<String,String> keyMap = new HashMap<String,String>();

		if (controlIdStr != null)
		{
			String[] params = controlIdStr.split("~");
			for (int i = 0; i < params.length; i++)
			{
				String paramKeyValStr = params[i];
				if (paramKeyValStr != null && !"".equals(paramKeyValStr.trim()))
				{
					String[] paramKeyVal = paramKeyValStr.split("=");
					if (i==0)
					{
						keyMap.put("controlId", paramKeyVal[0]);
					}
					else if (paramKeyVal.length == 1)
					{
						keyMap.put(paramKeyVal[0], null);
					}
					else
					{
						keyMap.put(paramKeyVal[0], paramKeyVal[1]);
					}
				}
			}
		}
		return keyMap;
	}

	/**
	 * Retrieves the container from the session
	 * @param request http request
	 * @param containerId containing the requested combobox
	 * @return
	 */
	private Object getContainerFromUserSesion(HttpServletRequest request, String containerId)
	{
		Map<String, ContainerInterface> map = (Map<String, ContainerInterface>) request
				.getSession().getAttribute(WebUIManagerConstants.CONTAINER_MAP);
		ContainerInterface containerInterface = null;
		if (map != null)
		{
			containerInterface = map.get(containerId);
		}
		return containerInterface;
	}

	/**
	 * This method populates name value beans list as per query,
	 * i.e. word typed into the auto-complete drop-down text field.
	 * @param querySpecificNVBeans
	 * @param nameValueBeans
	 * @param query
	 */
	private void populateQuerySpecificNameValueBeansList(List<NameValueBean> querySpecificNVBeans,
			List<NameValueBean> nameValueBeans, String query)
	{
		boolean isShowAll = (query == null || query.length() == 0 || query.equals("*"));
		Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();
		for (NameValueBean nvb : nameValueBeans)
		{
			if (isShowAll || nvb.getName().toLowerCase(locale).contains(query.toLowerCase(locale)))
			{
				nvb.setName(DynamicExtensionsUtility.getUnEscapedStringValue(nvb.getName()));
				querySpecificNVBeans.add(nvb);
			}
		}

	}

}
