package cn.dceast.platform.paas.model;

import java.util.Date;

public class Domain {
    private Integer id;

    private String domain;

    private Integer teamId;

    private String teamName;

    private Integer appId;

    private String appName;

    private Integer creator;

    private String creatorName;

    private String bindUserId;

    private Date bindAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain == null ? null : domain.trim();
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName == null ? null : teamName.trim();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    public String getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(String bindUserId) {
        this.bindUserId = bindUserId == null ? null : bindUserId.trim();
    }

    public Date getBindAt() {
        return bindAt;
    }

    public void setBindAt(Date bindAt) {
        this.bindAt = bindAt;
    }
}