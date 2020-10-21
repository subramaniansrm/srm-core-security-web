package com.srm.core.security.web.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.srm.coreframework.exception.ErrorMessageCode;
import com.srm.coreframework.i18n.CoreMessageSource;
import com.srm.coreframework.response.CustomUserDetails;
import com.srm.coreframework.security.OAuth2AuthenticationUser;
import com.srm.coreframework.util.CommonConstant;


@Component
/**
 * Interceptor
 * 
 *
 */
public class UserServiceCheckInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceCheckInterceptor.class);

	@Autowired
	private CoreMessageSource coreMessageSource;
	/*@Autowired
	private RoleService roleService;*/

	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		CustomUserDetails customUserDetails = null;
		boolean access = true;
		List<String> authorityList = null;
		String roleAccessControlSet = null;

		String url = request.getRequestURI();
		try {
			if ((!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken))) {
				OAuth2AuthenticationUser authentication = (OAuth2AuthenticationUser) SecurityContextHolder.getContext()
						.getAuthentication();
				if (authentication != null) {
					customUserDetails = authentication.getCustomUserDetails();
					authorityList = new ArrayList<String>();
					StringTokenizer st = new StringTokenizer(customUserDetails.getAuthorities().toString(), ",=[]{}");
					while (st != null && st.hasMoreTokens()) {
						String s2 = st.nextToken();
						if (s2 != null && !CommonConstant.AUTHORITY.equalsIgnoreCase(s2)
								&& !" ".equalsIgnoreCase(s2)) {
							authorityList.add(s2.trim());
						}
					}
				
					LOG.info(" Screen Access List " + roleAccessControlSet);
				}
			} else {
				access = true;
			}
		} catch (Exception exception) {
			LOG.debug(exception.getMessage());
		}
		return access;
	}

	/**
	 * 
	 * @param authorityList
	 * @param roleAccessControlSet
	 * @param roleAccessControlList
	 * @param screenIdStr
	 * @return
	 */
	private String validateAuthority(List<String> authorityList, String roleAccessControlSet,
			Set<String> roleAccessControlList, String screenIdStr) {
		for (String roleId : authorityList) {
			
			/*Set<RoleItemActivity> set1= role.getRoleItemActivities();
			for(RoleItemActivity Roleitemactivity : set1) {
				
				Long erpItemActivityId = Roleitemactivity.getItemactivity().getItemactivityId();
				if(screenIdStr!=null && screenIdStr.trim().equalsIgnoreCase(String.valueOf(erpItemActivityId).trim())
						&& Roleitemactivity.getIsEnabled()) {
					LOG.info(" Screen ID matched=========>");
					roleAccessControlList.add("Value");
					break;
				}
			}*/
			
			/*for (ERPModuleRoleAccess mra : role.getModuleRoleAccesses()) {
				List<ERPScreenAccess> screenList = mra.getScreenAccesses();
				if (screenList != null) {
					for (ERPScreenAccess erpScreenAccess : screenList) {
						if (erpScreenAccess.getScreen() != null) {
							Long dbScreenId = erpScreenAccess.getScreen().getScreenId();

							if (dbScreenId != null
									&& screenIdStr.trim().equalsIgnoreCase(String.valueOf(dbScreenId).trim())) {
								LOG.info(" Screen ID matched=========>");
								roleAccessControlSet = erpScreenAccess.getAccessControlSet();
								roleAccessControlList.add(roleAccessControlSet);
							}
						}
					}

				}
			}*/
		}
		return roleAccessControlSet;
	}
}
