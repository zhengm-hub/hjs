package cn.dceast.platform.paas.utils.jsonUtil;

/**
 * Created by zhming on 2015/11/23.
 */

import cn.dceast.platform.paas.utils.Constants;

/**
 * 报文头部实体类
 */
public class JsonVO {

    private String code;
    private String msg;

    public JsonVO(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
