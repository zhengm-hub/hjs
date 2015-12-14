/**
 * Project:platform-paas
 * <p/>
 * File:DockerFileTpls.java
 * <p/>
 * Package:cn.dceast.platform.paas.common
 * <p/>
 * Date:15/8/3上午10:54
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/3上午10:54		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.config;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DockerFileTpls
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/3 上午10:54
 * @Email shumin_1027@126.com
 */
@Component
public class DockerFileTpls {
    private static final Map<String, String> tpls = Maps.newHashMap();


    public static String getTpl(String key) {
        return tpls.get(key);
    }

    public static void init() {
        loadDockerTplFiles();
    }

    /**
     * 加载DockerFileTpl文件
     */
    private static void loadDockerTplFiles() {
        List<File> tplFilses = new ArrayList<File>();
        String rootClassPath = DockerFileTpls.class.getClassLoader().getResource("").getPath();
        File tplDir = new File(rootClassPath + PaasConfig.getTplDir());
        listDockerTplFiles(tplDir, tplFilses);
        int size = tplFilses.size();
        for (int i = 0; i < size; i++) {
            File tpl = tplFilses.get(i);
            try {
                String content = FileUtils.readFileToString(tpl, "UTF-8");
                tpls.put(tpl.getName().toLowerCase(), content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<File> listDockerTplFiles(File baseFile, List<File> tplFilse) {

        if (!baseFile.isDirectory()) {
            if (baseFile.getName().endsWith(".tpl")) {
                tplFilse.add(baseFile);
            }
        } else {
            File[] fileList = baseFile.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    listDockerTplFiles(file, tplFilse);
                } else {
                    if (file.getName().endsWith(".tpl")) {
                        tplFilse.add(file);
                    }
                }
            }
        }
        return tplFilse;
    }
}