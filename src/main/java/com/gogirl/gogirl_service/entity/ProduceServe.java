package com.gogirl.gogirl_service.entity;

/**
 * Created by yinyong on 2018/8/20.
 */
public class ProduceServe {

    private Integer id;
    private Integer serveId;
    private Integer produceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServeId() {
        return serveId;
    }

    public void setServeId(Integer serveId) {
        this.serveId = serveId;
    }

    public Integer getProduceId() {
        return produceId;
    }

    public void setProduceId(Integer produceId) {
        this.produceId = produceId;
    }

    @Override
    public String toString() {
        return "ProduceServe{" +
                "id=" + id +
                ", serveId=" + serveId +
                ", produceId=" + produceId +
                '}';
    }
}
