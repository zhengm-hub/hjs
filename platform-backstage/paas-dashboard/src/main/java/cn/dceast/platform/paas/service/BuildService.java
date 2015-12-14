/**
 * Project:platform-paas
 * <p/>
 * File:BuildService.java
 * <p/>
 * Package:cn.dceast.platform.paas.service
 * <p/>
 * Date:15/7/31下午4:52
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/7/31下午4:52		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.service;

import cn.dceast.platform.paas.model.Build;
import cn.dceast.platform.paas.repository.BuildMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BuildService
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/7/31 下午4:52
 * @Email shumin_1027@126.com
 */
@Service
public class BuildService {
    @Autowired
    private BuildMapper buildMapper;

    // ///////////////////////////////
    // ///// 增加 ////////
    // ///////////////////////////////

    public Build addBuild(Build build) {
        buildMapper.insert(build);
        return build;
    }

    // ///////////////////////////////
    // ///// 刪除 ////////
    // ///////////////////////////////

    public int deleteBuild(int id) {
        return buildMapper.deleteByPrimaryKey(id);
    }

    public int deleteBuild(Build build) {
        return buildMapper.deleteByPrimaryKey(build.getId());
    }

    // ///////////////////////////////
    // ///// 修改 ////////
    // ///////////////////////////////
    public Build update(Build build) {
        buildMapper.updateByPrimaryKeySelective(build);
        return build;
    }

    // ///////////////////////////////
    // ///// 查詢 ////////
    // ///////////////////////////////

    public Build getBuildById(int id) {
        return buildMapper.selectByPrimaryKey(id);
    }

    public List<Build> getAll() {
        return buildMapper.selectAll();
    }

    public PageInfo<Build> page(int pageIndex, int pageSize, String orderBy) {
        PageHelper.startPage(pageIndex, pageSize);

        Map paras = Maps.newHashMap();
        paras.put("orderBy", orderBy);
        List<Build> apps = buildMapper.selectBy(paras);
        PageInfo<Build> pageinfo = new PageInfo(apps);
        return pageinfo;
    }
}
