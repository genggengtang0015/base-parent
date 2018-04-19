package org.zxs.base.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.zxs.base.model.SessionUser;

/**
 * session过期拦截器
 */
public class SessionTimeoutInterceptor implements HandlerInterceptor{
	
	private List<String> allowUrls;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String requestUrl = request.getRequestURI();
		
		if(null != allowUrls){ // 不拦截地址
			for(String url : allowUrls) {
				if(requestUrl.endsWith(url)) {
					return true;
				}
			}
		}
		
		
		SessionUser session = (SessionUser) WebUtils.getSessionAttribute(request, "user");
		if(session != null) {
			return true;
		}else{
			String ajaxLabel = request.getHeader("x-requested-with");
			if (ajaxLabel != null && ajaxLabel.equalsIgnoreCase("XMLHttpRequest")){ //如果是ajax请求响应头会有，x-requested-with  
	            response.setHeader("sessionstatus", "timeout");//在响应头设置session状态  
	            return false;
	        }

			throw new SessionTimeoutException();
		}
	}


}
