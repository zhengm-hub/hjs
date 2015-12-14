/**
 * Project:platform-paas
 * <p/>
 * File:BeetlInterceptor.java
 * <p/>
 * Package:cn.dceast.platform.paas.beetl
 * <p/>
 * Date:15/8/3下午5:52
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/3下午5:52		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.common.beetl.param;

import cn.dceast.platform.paas.common.DictKeys;
import cn.dceast.platform.paas.common.util.ContextUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置全局参数
 *
 * @ClassName: BeetlInterceptor
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/3 下午5:52
 * @Email shumin_1027@126.com
 */
public class GlobalVriablesInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 系统配置参数
        String basePath = ContextUtil.getWebRootUrl(httpServletRequest);
        // 网站根路径
        httpServletRequest.setAttribute(DictKeys.SYSTEM_ROOTPATH, basePath);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}