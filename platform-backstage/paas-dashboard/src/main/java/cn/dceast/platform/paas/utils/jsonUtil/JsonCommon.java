/*
 *	Copyright © 2013 Changsha Shishuo Network Technology Co., Ltd. All rights reserved.
 *	长沙市师说网络科技有限公司 版权所有
 *	http://www.shishuo.com
 */

package cn.dceast.platform.paas.utils.jsonUtil;

import java.util.HashMap;

import com.alibaba.fastjson.annotation.JSONType;

//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
/*
@JSONType：
	orders属性 --orders属性可以设置序列化顺序，
	ignores属性 --ignores可以将不需要序列化的字段排除掉 
@JSONField：
	name属性，设置别名 操作演示代码为： 
=========demo=======
// UserInfo.java文件为： // 设置排序规则 
@JSONType(orders = {"session","image","mCount"}, ignores = {"userName" }) 
public class UserInfo {  
		private String userName; 
		// 设置别名为mCount 
		@JSONField(name = "mCount")     
		private int messageCount;     
		private String image;     
		private Session session;
		....
		}
*/
@JSONType
public class JsonCommon {
    /**
     * 通用报文头
     */
    private JsonVO header;
    /**
     * 返回报文体
     */
    private Object body;

    public JsonCommon(JsonVO jsonVO,Object object) {
        this.header = jsonVO;
        this.body = object;
    }

    public JsonVO getHeader() {
        return header;
    }

    public void setHeader(JsonVO header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
