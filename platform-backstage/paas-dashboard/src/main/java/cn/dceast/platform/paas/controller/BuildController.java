/**
 * Project:DCEAST-ServicePlatform
 * <p/>
 * File:BuildController.java
 * <p/>
 * Package:cn.dceast.serviceplatform.paas.web.controller
 * <p/>
 * Date:15/7/30上午3:23
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/7/30上午3:23		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.controller;

import cn.dceast.platform.paas.api.MediaTypes;
import cn.dceast.platform.paas.model.App;
import cn.dceast.platform.paas.model.Build;
import cn.dceast.platform.paas.service.AppService;
import cn.dceast.platform.paas.service.BuildService;
import cn.dceast.platform.paas.task.ImgBuilder;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName: BuildController
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/7/30 上午3:23
 * @Email shumin_1027@126.com
 */
@Controller
@RequestMapping("/build")
public class BuildController {

    private static final Logger log = LoggerFactory.getLogger(BuildController.class);

    @Autowired
    private BuildService buildService;

    //>> 视图 start

    @RequestMapping({"", "/index"})
    public String index(Model model) {
        model.addAttribute("name", "shumin");
        return "/build/index";
    }

    /**
     * 查看
     *
     * @param model
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable int id, Model model) {

        return "/build/view";
    }

    /**
     * 增加
     *
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("name", "shumin");
        return "/build/add";
    }

    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("name", "shumin");
        return "/build/edit";
    }

    /**
     * 列表
     *
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("name", "shumin");
        return "/build/edit";
    }

    /**
     * 查看进度
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/progress/{id}")
    public String progress(@PathVariable int id, Model model) {
        Build build = buildService.getBuildById(id);
        String logFile = build.getRepo() + File.separator + "log.out";
        String log = "";
        try {
            log = FileUtils.readFileToString(new File(logFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("logFile", logFile);
        model.addAttribute("log", log);
        return "/build/progress";
    }
    //<< 视图 end


    //>> 数据 start


    // 读取日志
    @RequestMapping("/readLog")
    @ResponseBody
    public Map readLog(@RequestParam String logfile) {
        Map result = Maps.newHashMap();
        File logFile = new File(logfile);
        String logContent = null;
        try {
            logContent = FileUtils.readFileToString(logFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("logContent", logContent);
        return result;
    }
    //<< 数据 end

}
