package cn.dceast.platform.paas.common;

import cn.dceast.platform.paas.service.AppService;
import cn.dceast.platform.paas.service.UserService;
import cn.dceast.platform.paas.service.aop.ExceptionLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CommandExecutorTest {

    /*@Autowired
    ExceptionLogger logger;*/

    @Test
    public void testExce() throws Exception{
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
       /* AppService s = ac.getBean(AppService.class);
        s.addApp(null);*/
        UserService us = ac.getBean(UserService.class);
//        us.getUserByName("zhming3");
        System.out.println(ac.getBean("cacheManager"));
    }
}