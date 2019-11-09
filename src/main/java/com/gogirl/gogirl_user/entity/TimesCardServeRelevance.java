package com.gogirl.gogirl_user.entity;

public class TimesCardServeRelevance {
    private Integer id;

    private Integer timesCardTypeId;

    private Integer serveId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimesCardTypeId() {
        return timesCardTypeId;
    }

    public void setTimesCardTypeId(Integer timesCardTypeId) {
        this.timesCardTypeId = timesCardTypeId;
    }

    public Integer getServeId() {
        return serveId;
    }

    public void setServeId(Integer serveId) {
        this.serveId = serveId;
    }
}