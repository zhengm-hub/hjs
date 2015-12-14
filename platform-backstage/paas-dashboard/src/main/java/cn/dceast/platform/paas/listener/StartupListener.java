/**
 * Project:platform-paas
 * <p/>
 * File:StartupListener.java
 * <p/>
 * Package:cn.dceast.platform.paas.listener
 * <p/>
 * Date:15/8/6下午4:42
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/6下午4:42		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.listener;

import cn.dceast.platform.paas.config.DockerFileTpls;
import cn.dceast.platform.paas.config.PaasConfig;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 启动监听器
 *
 * @ClassName: StartupListener
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/6 下午4:42
 * @Email shumin_1027@126.com
 */

//@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent evt) {
        if (evt.getApplicationContext().getParent() == null) {

            //初始化 PAAS 配置
            PaasConfig.init();
            //初始化 DockerFile 模板
            DockerFileTpls.init();
            //
        }
    }
}