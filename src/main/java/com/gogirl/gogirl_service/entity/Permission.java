package com.gogirl.gogirl_service.entity;

/**
 * Created by yinyong on 2019/5/7.
 */
public class Permission {

    private Integer id;
    private String keyword;
    private String url;
    private Integer pid;
    private Integer status;
    private String title;
    private String imgSrc;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", url='" + url + '\'' +
                ", pid=" + pid +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
