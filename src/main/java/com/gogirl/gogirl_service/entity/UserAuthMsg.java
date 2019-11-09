package com.gogirl.gogirl_service.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yinyong on 2018/8/20.
 */
public class UserAuthMsg implements Serializable {
    private static final Long serialVersionUID = 1l;
    private String id;
    private String text;
    private String img;
    private String value;
    private String title;
    private String parentnodes;
    private boolean showcheck;
    private boolean isexpand;
    private boolean haschildren;
    private List<UserAuthMsg> childnodes;

    public String getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentnodes() {
        return parentnodes;
    }

    public void setParentnodes(String parentnodes) {
        this.parentnodes = parentnodes;
    }

    public boolean isShowcheck() {
        return showcheck;
    }

    public void setShowcheck(boolean showcheck) {
        this.showcheck = showcheck;
    }

    public boolean isIsexpand() {
        return isexpand;
    }

    public void setIsexpand(boolean isexpand) {
        this.isexpand = isexpand;
    }

    public boolean isHaschildren() {
        return haschildren;
    }

    public void setHaschildren(boolean haschildren) {
        this.haschildren = haschildren;
    }

    public List<UserAuthMsg> getChildnodes() {
        return childnodes;
    }

    public void setChildnodes(List<UserAuthMsg> childnodes) {
        this.childnodes = childnodes;
    }
}
