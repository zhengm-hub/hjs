package cn.dceast.platform.paas.repository;

import cn.dceast.platform.paas.model.Build;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface BuildMapper extends Mapper<Build> {
    int deleteByPrimaryKey(Integer id);

    int insert(Build record);

    int insertSelective(Build record);

    Build selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Build record);

    int updateByPrimaryKey(Build record);

    List<Build> selectAll();

    List<Build> selectBy(Map paras);
}