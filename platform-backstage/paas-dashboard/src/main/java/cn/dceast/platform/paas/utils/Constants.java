package cn.dceast.platform.paas.utils;

import cn.dceast.platform.paas.utils.jsonUtil.JsonVO;

/**
 * Created by zhming on 2015/11/23.
 */
public class Constants {
    /**
     * 成功code
     */
    public static final String CODE_SUCCESS = "000000";
    /**
     * 成功msg
     */
    public static final String MSG_SUCCESS = "本次请求成功";

    /**
     * 数据库操作异常code
     */
    public static final String CODE_ERROR_DATABASE = "999990";
    /**
     * 数据库操作异常msg
     */
    public static final String MSG_ERROR_DATABASE = "数据库操作异常";

    /**
     * 重名code
     */
    public static final String CODE_SUCCESS_REPEAT_NAME = "000001";
    /**
     * 重名msg
     */
    public static final String MSG_SUCCESS_REPEAT_NAME = "用户名已存在";

    /**
     * 无此信息code
     */
    public static final String CODE_SUCCESS_NULL = "000002";
    /**
     * 无此信息msg
     */
    public static final String MSG_SUCCESS_NULL = "无此信息";
    /**
     * 通用操作
     */
    public static final JsonVO JV_SUCCESS = new JsonVO(CODE_SUCCESS,MSG_SUCCESS);

    public static final JsonVO JV_ERROR_DATABASE = new JsonVO(CODE_ERROR_DATABASE,MSG_ERROR_DATABASE);

    public static final JsonVO JV_SUCCESS_REPEAT_NAME = new JsonVO(CODE_SUCCESS_REPEAT_NAME,MSG_SUCCESS_REPEAT_NAME);

    public static final JsonVO JV_SUCCESS_NULL = new JsonVO(CODE_SUCCESS_NULL,MSG_SUCCESS_NULL);
}
