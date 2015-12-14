/**
 * Project:platform-paas
 * <p/>
 * File:CoreUser.java
 * <p/>
 * Package:cn.dceast.platform.paas.model
 * <p/>
 * Date:15/8/7上午10:42
 * <p/>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p/>
 * <p/>
 * Modification History:
 * <p/>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/7上午10:42		   shumin		1.0			1.0 Version
 */
package cn.dceast.platform.paas.model;

/**
 * @ClassName: CoreUser
 * @Description: ${todo}
 * @Author shumin
 * @Date 15/8/7 上午10:42
 * @Email shumin_1027@126.com
 */
public class CoreUser {
    private String name;

    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
