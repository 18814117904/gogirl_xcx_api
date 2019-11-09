package com.gogirl.gogirl_user.entity;

public class TimesCardServeTypeRelevance {
    private Integer id;

    private Integer timesCardId;

    private Integer serveTypeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimesCardId() {
        return timesCardId;
    }

    public void setTimesCardId(Integer timesCardId) {
        this.timesCardId = timesCardId;
    }

    public Integer getServeTypeId() {
        return serveTypeId;
    }

    public void setServeTypeId(Integer serveTypeId) {
        this.serveTypeId = serveTypeId;
    }
}