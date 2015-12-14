package cn.dceast.platform.paas.repository;

import cn.dceast.platform.paas.model.App;
import cn.dceast.platform.paas.model.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends Mapper<User> {
    /*
    ========================================
    集成通用Mapper插件，自带默认数据库操作方法
    如：
    @Override
    int insert(User user);

    @Override
    User selectByPrimaryKey(Object o);
    =========================================
    =====================
    @Param(value="")：MyBatis还提供了一个使用注解来参入多个参数的方式。这种方式需要在接口的参数上添加@Param注解
    如：(@Param(value="userName") String userName,
        @Param(value="password") String password)
    =====================
    */

    /**
     * 以下为业务需要拓展方法
     * @return
     */
    List<User> selectAll();

    List<User> selectBy(Map<String, Object> paras);

    User selectUserByName(String userName) throws Exception;
}
