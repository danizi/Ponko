package com.ponko.cn.bean;

import java.util.List;

public class StudyCourseBean {

    /**
     * id : ee9df6117f9211e5a95900163e000c35
     * parentId : 6cf657646f0a11e8a93a67b75eb469f2
     * title : 外贸快速入门
     * subtitle : 从0到优秀
     * subtitle2 : 从0到优秀
     * picture : http://cdn.tradestudy.cn/upload/product/20181008/4ed462e4dc28daca78146340ff6781f1.jpg
     * online : true
     * sequence : 1
     * height : 188.0
     * width : 334.0
     * url : /courses/bluebird/
     * newer : false
     * subTypes : []
     * courses : []
     */

    private String id;
    private String parentId;
    private String title;
    private String subtitle;
    private String subtitle2;
    private String picture;
    private boolean online;
    private int sequence;
    private double height;
    private double width;
    private String url;
    private boolean newer;
    @Deprecated
    private List<?> subTypes;
    @Deprecated
    private List<?> courses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle2() {
        return subtitle2;
    }

    public void setSubtitle2(String subtitle2) {
        this.subtitle2 = subtitle2;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isNewer() {
        return newer;
    }

    public void setNewer(boolean newer) {
        this.newer = newer;
    }

    public List<?> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<?> subTypes) {
        this.subTypes = subTypes;
    }

    public List<?> getCourses() {
        return courses;
    }

    public void setCourses(List<?> courses) {
        this.courses = courses;
    }
}
