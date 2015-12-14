/**
 * Project:platform-paas
 * <p/>
 * File:UploadController.java
 * <p/>
 * Package:cn.dceast.platform.paas.controller
 * <p/>
 * Date:15/8/5上午12:03
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/5上午12:03		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.controller;

import cn.dceast.platform.paas.common.util.ContextUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName: UploadController
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/5 上午12:03
 * @Email shumin_1027@126.com
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
    private static final Logger log = LoggerFactory.getLogger(BuildController.class);

    @RequestMapping({"", "/index"})
    public String index(Model model) {
        return "/upload/index";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Map upload(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
        Map result = Maps.newHashMap();
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    // 创建临时目录
                    log.info("创建临时目录……");
                    String webRoot = request.getServletContext().getRealPath("/");
                    File tmpFile = new File(webRoot + "/tmp/" + System.currentTimeMillis());
                    if (!tmpFile.exists()) {
                        tmpFile.mkdirs();
                    }
                    log.info("创建临时目录成功：" + tmpFile.getPath());
                    //取得当前上传文件的文件名称
                    String filename = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (filename.trim() != "") {
                        File localFile = new File(tmpFile + "/" + filename);
                        file.transferTo(localFile);
                        result.put("file", localFile);
                    }
                }
            }
        }
        return result;
    }

    @RequestMapping("/upload1")
    @ResponseBody
    public Map upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException {
        Map result = Maps.newHashMap();
        if (file != null && !file.isEmpty()) {
            // 创建临时目录
            log.info("创建临时目录……");
            String webRootPath = ContextUtil.getWebRootPath(request);
            File tmpFile = new File(webRootPath + "/tmp/" + System.currentTimeMillis());
            if (!tmpFile.exists()) {
                tmpFile.mkdirs();
            }
            log.info("创建临时目录成功：" + tmpFile.getPath());
            //取得当前上传文件的文件名称
            String filename = file.getOriginalFilename();
            //如果名称不为“”,说明该文件存在，否则说明该文件不存在
            if (filename.trim() != "") {
                File localFile = new File(tmpFile + "/" + filename);
                file.transferTo(localFile);
                result.put("file", localFile);
            }

        }
        return result;
    }
}
