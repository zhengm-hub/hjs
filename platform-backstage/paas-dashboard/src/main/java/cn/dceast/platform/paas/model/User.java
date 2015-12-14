package cn.dceast.platform.paas.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * ==========user model==============
 * 使用序列可以添加如下的注解:

     //可以用于数字类型,字符串类型(需数据库支持自动转型)的字段
     @SequenceGenerator(name="Any",sequenceName="seq_userid")
     @Id
     private Integer id;
     使用UUID时:

     //可以用于任意字符串类型长度超过32位的字段
     @GeneratedValue(generator = "UUID")
     private String countryname;
     使用主键自增:

     //不限于@Id注解的字段,但是一个实体类中只能存在一个(继承关系中也只能存在一个)
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
 */
public class User {
    //通用Mapper注解 主键自增Id
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    //通用Mapper注解 主键 UUID
    @Id
    @GeneratedValue(generator = "UUID")
    private String userId;

    private String userName;
    private String password;

    public User(){

    }
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

