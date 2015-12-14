package cn.dceast.platform.paas.repository;

import cn.dceast.platform.paas.model.Domain;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Domain record);

    int insertSelective(Domain record);

    Domain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Domain record);

    int updateByPrimaryKey(Domain record);
}