/**
 * Project:platform-paas
 * <p/>
 * File:BuildResource.java
 * <p/>
 * Package:cn.dceast.platform.paas.api.resource
 * <p/>
 * Date:15/7/31下午5:16
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/7/31下午5:16		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.api.resource;

import cn.dceast.platform.paas.api.MediaTypes;
import cn.dceast.platform.paas.common.DictKeys;
import cn.dceast.platform.paas.config.DockerFileTpls;
import cn.dceast.platform.paas.config.PaasConfig;
import cn.dceast.platform.paas.model.Build;
import cn.dceast.platform.paas.model.CoreUser;
import cn.dceast.platform.paas.service.BuildService;
import cn.dceast.platform.paas.task.ImgBuilder;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.beetl.core.BeetlKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName: BuildResource
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/7/31 下午5:16
 * @Email shumin_1027@126.com
 */

@RestController
@RequestMapping(value = "/api/builds")
public class BuildResource {

    private static final Logger log = LoggerFactory.getLogger(BuildResource.class);

    @Autowired
    private BuildService buildService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON)
    public Object list(
            @RequestParam(value = "isPage", defaultValue = "false", required = false) Boolean isPage,
            @RequestParam(value = "pageIndex", defaultValue = "1", required = false) int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "9999", required = false) int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "", required = false) String orderBy
    ) {
        if (isPage) {
            Map map = Maps.newHashMap();
            PageInfo<Build> page = buildService.page(pageIndex, pageSize, orderBy);
            map.put("pageSize", page.getPageSize());    //每页记录数
            map.put("pageNum", page.getPageNum());      //当前页码
            map.put("recordCount", page.getTotal());    //总记录数
            map.put("pageCount", page.getPages());      //总页数
            map.put("dataList", page.getList());        //数据列表
            return map;
        } else {
            return buildService.getAll();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON)
    public Build get(@PathVariable("id") int id) {
        Build task = buildService.getBuildById(id);
        if (task == null) {
            String message = "App不存在(id:" + id + ")";
        }
        return task;
    }


    @RequestMapping(method = RequestMethod.POST, produces = MediaTypes.JSON)
    public ResponseEntity<?> create(@RequestBody Build build, UriComponentsBuilder uriBuilder) {

        // 从Session中获取当前登录的用户
        CoreUser me = (CoreUser) request.getSession().getAttribute(DictKeys.SYSTEM_CURRENTUSER);
        build.setUserId(me.getId());
        build.setUserName(me.getName());
        build.setCreateAt(new Date());
        // app包
        File appTarball = new File(build.getTarball());
        // 工作目录
        File workDir = appTarball.getParentFile();

        // 生成Dockerfile
        log.info("生成Dockerfile……");
        String content = DockerFileTpls.getTpl(build.getBase().toLowerCase() + ".tpl");
        Map<String, Object> para = Maps.newHashMap();
        para.put("Registry", PaasConfig.getRegistry());
        para.put("Tarball", appTarball.getName());
        para.put("AppDir", "/opt/app");
        content = BeetlKit.render(content, para);
        File dockFile = new File(workDir.getPath() + File.separator + "Dockerfile");
        try {
            FileUtils.writeStringToFile(dockFile, content, "UTF-8");
        } catch (IOException e) {
            //TODO
        }
        log.info("生成Dockerfile成功：" + dockFile.getPath());

        build.setRepo(workDir.getPath());
        // 构建镜像

        if (buildImg(workDir.getPath(), build.getUserName(), build.getApp(), build.getVersion(),
                PaasConfig.getRegistry(), "log.out")) {
            build.setStatus("build succeed!");
            String imge = PaasConfig.getRegistry() + "/" + build.getUserName() + "/" + build.getApp() + ":" + build.getVersion();
            build.setImage(imge);
        } else {
            build.setStatus("build failed!");
        }

        // 保存任务
        buildService.addBuild(build);

        // 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
        Integer id = build.getId();
        // 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
        URI uri = uriBuilder.path("/build/progress/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        Map responseContent = Maps.newHashMap();
        responseContent.put("status", 0);
        responseContent.put("msg", "OK");
        responseContent.put("url", uri);
        return new ResponseEntity(responseContent, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Build Build) {
        // 保存任务
        buildService.update(Build);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        buildService.deleteBuild(id);
    }


    /**
     * 构建app镜像
     *
     * @param workDir        工作目录
     * @param userName       用户名
     * @param appName        app名称
     * @param version        版本
     * @param dockerRegistry docker仓库
     * @param logfile        日志输出文件
     * @return 是否成功
     */
    private boolean buildImg(String workDir, String userName, String appName, String version, String dockerRegistry,
                             String logfile) {
        log.info("构建镜像……");

        ImgBuilder builder = new ImgBuilder(workDir, userName, appName, version, dockerRegistry, logfile);
        try {
            new Thread(builder).start();
        } catch (Exception e) {
            log.error("构建镜像出错：");
            e.printStackTrace();
            return false;
        } finally {
            // log.info("删除临时文件：");
        }
        return true;
    }
}
