/**
 * Project:platform-paas
 * <p/>
 * File:ImgBuilder.java
 * <p/>
 * Package:cn.dceast.platform.paas.task
 * <p/>
 * Date:15/8/5上午9:46
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/5上午9:46		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.task;

import cn.dceast.platform.paas.common.CommandExecutor;
import cn.dceast.platform.paas.config.PaasConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 镜像构建器
 *
 * @ClassName: ImgBuilder
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/5 上午9:46
 * @Email shumin_1027@126.com
 */
public class ImgBuilder implements Runnable {
    private static Logger log = LoggerFactory.getLogger(ImgBuilder.class);

    private static String rootClassPath = ImgBuilder.class.getClassLoader().getResource("").getPath();
    private static String buildScript = rootClassPath + PaasConfig.getBuildScript();


    //    private static String[] cmds = {"/bin/sh", buildScript, null};
    private String[] cmds;
    private CommandExecutor.CommandResult result;

    public CommandExecutor.CommandResult getResult() {
        return result;
    }

    public ImgBuilder(String workDir, String userName, String appName, String version, String dockerRegistry, String logfile) {
        String para = workDir + " " + userName + " " + appName + " " + version + " " + dockerRegistry + " " + logfile;
        log.info("参数：" + para);
        cmds = new String[]{"/bin/sh", buildScript, workDir, userName, appName, version, dockerRegistry, logfile};
    }

    @Override
    public void run() {
        try {
            result = CommandExecutor.exec(cmds);
            log.info("Error:" + result.getError());
            log.info("Output:" + result.getOutput());
        } catch (IOException e) {
            log.info("构建镜像出错：");
            log.info("OutPut:" + result.getOutput());
            log.info("Error" + result.getError());
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.info("构建镜像出错：");
            log.info("OutPut:" + result.getOutput());
            log.info("Error" + result.getError());
            e.printStackTrace();
        }
    }


}

