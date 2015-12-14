/**
 * Project:platform-paas
 * <p/>
 * File:AppResource.java
 * <p/>
 * Package:cn.dceast.platform.paas.api.resource
 * <p/>
 * Date:15/7/31下午2:48
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/7/31下午2:48		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.api.resource;

import cn.dceast.platform.paas.api.MediaTypes;
import cn.dceast.platform.paas.common.DictKeys;
import cn.dceast.platform.paas.model.App;
import cn.dceast.platform.paas.model.CoreUser;
import cn.dceast.platform.paas.service.AppService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AppResource
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/7/31 下午2:48
 * @Email shumin_1027@126.com
 */

@RestController
@RequestMapping(value = "/api/apps")
public class AppResource {
    @Autowired
    private AppService appService;

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
            PageInfo<App> page = appService.page(pageIndex, pageSize, orderBy);
            map.put("pageSize", page.getPageSize());    //每页记录数
            map.put("pageNum", page.getPageNum());      //当前页码
            map.put("recordCount", page.getTotal());    //总记录数
            map.put("pageCount", page.getPages());      //总页数
            map.put("dataList", page.getList());        //数据列表
            return map;
        } else {
            return appService.getAll();
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON)
    public App get(@PathVariable("id") int id) {
        App task = appService.getAppById(id);
        if (task == null) {
            String message = "App不存在(id:" + id + ")";
        }
        return task;
    }


    @RequestMapping(method = RequestMethod.POST, produces = MediaTypes.JSON)
    public ResponseEntity<?> create(@RequestBody App app, UriComponentsBuilder uriBuilder) {

        // 从Session中获取当前登录的用户
        CoreUser me = (CoreUser) request.getSession().getAttribute(DictKeys.SYSTEM_CURRENTUSER);
        app.setCreator(me.getId());
        app.setTeamId(0);
        app.setTeamName("dev");

        app.setStatus((byte) 0);
        // 保存任务
        appService.addApp(app);

        // 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
        Integer id = app.getId();
        URI uri = uriBuilder.path("/api/Apps/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        Map content = Maps.newHashMap();
        content.put("status", 0);
        content.put("msg", "OK");
        return new ResponseEntity(content, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody App App) {
        // 保存任务
        appService.update(App);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        appService.deleteApp(id);
    }
}