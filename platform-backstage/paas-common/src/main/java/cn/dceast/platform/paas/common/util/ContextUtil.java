/**
 * Project:platform-paas
 * <p/>
 * File:ContextUtil.java
 * <p/>
 * Package:cn.dceast.platform.paas.common.util
 * <p/>
 * Date:15/8/2下午4:48
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/2下午4:48		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * WEB上下文工具类
 *
 * @ClassName: ContextUtil
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/2 下午4:48
 * @Email shumin_1027@126.com
 */
public class ContextUtil {

    private static String webRootPath;

    /**
     * 获取上下文URL全路径
     *
     * @param request
     * @return
     */
    public static String getWebRootUrl(HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80) {
            sb.append(":").append(request.getServerPort());
            sb.append(request.getContextPath());
        } else {
            sb.append(request.getContextPath());
        }
        String path = sb.toString();
        sb = null;
        return path;
    }

    public static String getWebRootPath(HttpServletRequest request) {
        if (webRootPath != null && !webRootPath.isEmpty()) {
            return webRootPath;
        }
        webRootPath = request.getServletContext().getRealPath("/");
        return webRootPath;
    }
}
