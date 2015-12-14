package cn.dceast.platform.paas.service.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zhming on 2015/11/25.
 */
//@Service
public class ExceptionLogger {

    public static final Log log = LogFactory.getLog(ExceptionLogger.class);
//    public Logger logger = LoggerFactory.getLogger(ExceptionLogger.class);
    public void loggerExcetpion(Exception ex){
        //TODO 将ex异常信息写入文件中
        StringBuffer sb = new StringBuffer();
        sb.append("========================\n");
        StackTraceElement[] element = ex.getStackTrace();
        for(StackTraceElement e:element){
            sb.append(e+"\n");
        }
        log.error(sb.toString());
        System.out.println("================切面异常为：=========\n"+sb.toString());
    }
}
