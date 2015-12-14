/**
 * Project:platform-paas
 * <p/>
 * File:GlobalInterceptor.java
 * <p/>
 * Package:cn.dceast.platform.paas.interceptor
 * <p/>
 * Date:15/8/2下午4:54
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/2下午4:54		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.interceptor;

import cn.dceast.platform.paas.common.DictKeys;
import cn.dceast.platform.paas.common.util.ContextUtil;
import cn.dceast.platform.paas.model.CoreUser;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @ClassName: GlobalInterceptor
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/2 下午4:54
 * @Email shumin_1027@126.com
 */
public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        HttpSession seeeion = httpServletRequest.getSession();
        CoreUser currentUser = new CoreUser();
        try {
            // 获取当前登陆的用户
            Assertion assertion = (Assertion) seeeion.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
            AssertionImpl assertionImpl = (AssertionImpl) seeeion.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
            Map<String, Object> attrs = assertionImpl.getAttributes();
            AttributePrincipal principal = assertion.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            currentUser.setName(attributes.get("userName").toString());
            currentUser.setId(Integer.valueOf(attributes.get("userId").toString()));
        } catch (Exception e) {
            currentUser.setName("debug");
            currentUser.setId(0);
        }
        seeeion.setAttribute(DictKeys.SYSTEM_CURRENTUSER, currentUser);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
