/**
 * Project:platform-paas
 * <p/>
 * File:PaasConfig.java
 * <p/>
 * Package:cn.dceast.platform.paas.config
 * <p/>
 * Date:15/8/3下午12:43
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/3下午12:43		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.config;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @ClassName: PaasConfig
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/3 下午12:43
 * @Email shumin_1027@126.com
 */

public class PaasConfig {

    private static final CompositeConfiguration config = new CompositeConfiguration();

    public static void init() {
        try {
            String rootClassPath = PaasConfig.class.getClassLoader().getResource("").getPath();
            String paasConfigFile = rootClassPath + "paas/PaasConfig.properties";
            config.addConfiguration(new PropertiesConfiguration(paasConfigFile), true);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static String getDomain() {
        return config.getString("paas.domain");
    }

    public static String getRegistry() {
        return config.getString("paas.registry");
    }

    public static String getBuildScript() {
        return config.getString("paas.buildScript");
    }

    public static String getTplDir() {
        return config.getString("paas.tplDir");
    }
}
