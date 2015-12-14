/**
 * Project:platform-paas
 * <p/>
 * File:AppService.java
 * <p/>
 * Package:cn.dceast.platform.paas.service
 * <p/>
 * Date:15/7/31下午2:37
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/7/31下午2:37		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.service;

import cn.dceast.platform.paas.model.App;
import cn.dceast.platform.paas.repository.AppMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AppService
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/7/31 下午2:37
 * @Email shumin_1027@126.com
 */
@Service
public class AppService {

    @Autowired
    private AppMapper appMapper;

    // ///////////////////////////////
    // ///// 增加 ////////
    // ///////////////////////////////

    public App addApp(App app) {
        int a = 1/0;
        appMapper.insert(app);
        return app;
    }

    // ///////////////////////////////
    // ///// 刪除 ////////
    // ///////////////////////////////

    public int deleteApp(int id) {
        return appMapper.deleteByPrimaryKey(id);
    }

    public int deleteApp(App app) {
        return appMapper.deleteByPrimaryKey(app.getId());
    }

    // ///////////////////////////////
    // ///// 修改 ////////
    // ///////////////////////////////
    public App update(App app) {
        appMapper.updateByPrimaryKeySelective(app);
        return app;
    }

    // ///////////////////////////////
    // ///// 查詢 ////////
    // ///////////////////////////////

    public App getAppById(int id) {
        return appMapper.selectByPrimaryKey(id);
    }

    public List<App> getAll() {
        return appMapper.selectAll();
    }

    public PageInfo<App> page(int pageIndex, int pageSize, String orderBy) {
        PageHelper.startPage(pageIndex, pageSize);

        Map paras = Maps.newHashMap();
        paras.put("orderBy", orderBy);
        List<App> apps = appMapper.selectBy(paras);
        PageInfo<App> pageinfo = new PageInfo(apps);
        return pageinfo;
    }
}
