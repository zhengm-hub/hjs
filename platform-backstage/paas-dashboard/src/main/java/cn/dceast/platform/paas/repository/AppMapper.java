package cn.dceast.platform.paas.repository;

import cn.dceast.platform.paas.model.App;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface AppMapper extends Mapper<App> {
    int deleteByPrimaryKey(Integer id);

    int insert(App record);

    int insertSelective(App record);

    App selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);

    List<App> selectAll();

    List<App> selectBy(Map<String, Object> paras);
}
