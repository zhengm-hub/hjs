/**
 * Project:DCEAST-ServicePlatform
 * <p/>
 * File:AppController.java
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
import cn.dceast.platform.paas.common.util.ContextUtil;
import cn.dceast.platform.paas.model.App;
import cn.dceast.platform.paas.service.AppService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Map;

/**
 * @ClassName: AppController
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/7/30 上午3:23
 * @Email shumin_1027@126.com
 */
@Controller
@RequestMapping("/app")
public class AppController {
    @Autowired
    private AppService appService;

    //>> 视图 start

    @RequestMapping({"", "/index"})
    public String index(Model model) {
        return "/app/list";
    }

    /**
     * 查看
     *
     * @param model
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable int id, Model model) {
        App app = appService.getAppById(id);
        model.addAttribute("app", app);
        return "/app/view";
    }

    /**
     * 增加
     *
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String add(Model model) {
        return "/app/add";
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
        App app = appService.getAppById(id);
        model.addAttribute("app", app);
        return "/app/edit";
    }

    /**
     * 列表
     *
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model) {
        return "/app/list";
    }

    //<< 视图 end


    //>> 数据 start


    //<< 数据 end

}
