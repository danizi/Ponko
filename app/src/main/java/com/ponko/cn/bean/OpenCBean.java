package com.ponko.cn.bean;

import java.util.List;

public class OpenCBean {

    /**
     * id : bfd7f85c6f0a11e8a93a67b75eb469f2
     * title : 外贸B2B课程
     * url : /product/detail?productId=bfd7f85c6f0a11e8a93a67b75eb469f2
     * payment_color : #FF3542
     * summary_color : #802327
     * summaries : ["· 1368节外贸课程","· 500人微信校友群","· 在线答疑&线下活动","· 入学即送400积分"]
     * bg_url : http://cdn.tradestudy.cn/api/img/product/product_b2b_bg.png
     * logo_url : http://cdn.tradestudy.cn/api/img/product/product_b2b_logo.png
     * expire_in : 1586330167000
     * sort : 1
     */

    private String id;
    private String title;
    private String url;
    private String payment_color;
    private String summary_color;
    private String bg_url;
    private String logo_url;
    private long expire_in;
    private int sort;
    private List<String> summaries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayment_color() {
        return payment_color;
    }

    public void setPayment_color(String payment_color) {
        this.payment_color = payment_color;
    }

    public String getSummary_color() {
        return summary_color;
    }

    public void setSummary_color(String summary_color) {
        this.summary_color = summary_color;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public long getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(long expire_in) {
        this.expire_in = expire_in;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<String> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<String> summaries) {
        this.summaries = summaries;
    }
}
