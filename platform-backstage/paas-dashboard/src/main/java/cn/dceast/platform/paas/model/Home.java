package cn.dceast.platform.paas.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * ==========home model==============
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
public class Home {
    //通用Mapper注解 主键自增Id
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    //通用Mapper注解 主键 UUID
//    bannerImgUrl（大图）、quickAnswerImgUrl（快速解答）、myAnswerImgUrl（我的回答）、findEngineerImgUrl（找工程师）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer homeId;

    private String bannerImgUrl;
    private Integer quickAnswerImgUrl;
    private Integer myAnswerImgUrl;
    private Integer findEngineerImgUrl;

    public Integer getFindEngineerImgUrl() {
        return findEngineerImgUrl;
    }

    public void setFindEngineerImgUrl(Integer findEngineerImgUrl) {
        this.findEngineerImgUrl = findEngineerImgUrl;
    }

    public Integer getHomeId() {
        return homeId;
    }

    public void setHomeId(Integer homeId) {
        this.homeId = homeId;
    }

    public String getBannerImgUrl() {
        return bannerImgUrl;
    }

    public void setBannerImgUrl(String bannerImgUrl) {
        this.bannerImgUrl = bannerImgUrl;
    }

    public Integer getQuickAnswerImgUrl() {
        return quickAnswerImgUrl;
    }

    public void setQuickAnswerImgUrl(Integer quickAnswerImgUrl) {
        this.quickAnswerImgUrl = quickAnswerImgUrl;
    }

    public Integer getMyAnswerImgUrl() {
        return myAnswerImgUrl;
    }

    public void setMyAnswerImgUrl(Integer myAnswerImgUrl) {
        this.myAnswerImgUrl = myAnswerImgUrl;
    }
}

