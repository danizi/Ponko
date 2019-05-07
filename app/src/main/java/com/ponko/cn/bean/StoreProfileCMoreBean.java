package com.ponko.cn.bean;

import java.util.List;

public class StoreProfileCMoreBean {


    /**
     * id : 2
     * name : 课程
     * layout : bar
     * sort : 2
     * stores : [{"id":"47975b0c6f4e11e9b5c00242ac130004","name":"帮课大学特训营第7期","type":"COMMODITY","duration":0,"scores":400,"picture":"http://cdn.tradestudy.cn/upload/product/20190506/a78e5a61aec6a4beca0c30387a759b2c.jpg","width":0,"height":0,"detail":null,"online":true,"message":null,"total":12,"totalMax":0,"totalExchanged":17,"expend":17,"sort":0,"createTime":0,"images":null,"url":"/store/exchange_detail?id=47975b0c6f4e11e9b5c00242ac130004","categoryId":0},{"id":"705f96ce696611e9b5c00242ac130004","name":"帮课大学特训营第6期","type":"COMMODITY","duration":0,"scores":400,"picture":"http://cdn.tradestudy.cn/upload/product/20190428/4984de1027a573ebeb15b2781776a71f.jpg","width":0,"height":0,"detail":null,"online":true,"message":null,"total":22,"totalMax":0,"totalExchanged":24,"expend":24,"sort":0,"createTime":0,"images":null,"url":"/store/exchange_detail?id=705f96ce696611e9b5c00242ac130004","categoryId":0},{"id":"1f972b18fddc11e8b5c00242ac130004","name":"Linda外贸英语口语","type":"OTHER","duration":0,"scores":299,"picture":"http://cdn.tradestudy.cn/upload/product/20190129/4882bf5930c0bd609bb1db5e47f00c71.jpg","width":300,"height":300,"detail":null,"online":true,"message":null,"total":3678,"totalMax":0,"totalExchanged":6321,"expend":6321,"sort":0,"createTime":0,"images":null,"url":"/store/exchange_detail?id=1f972b18fddc11e8b5c00242ac130004","categoryId":0},{"id":"53790584e72711e8b5c00242ac130004","name":"100个外贸牛人的干货分享","type":"OTHER","duration":0,"scores":299,"picture":"http://cdn.tradestudy.cn/upload/product/20190129/c262404f6686ba9b357de37c1f1e450b.jpg","width":300,"height":300,"detail":null,"online":true,"message":null,"total":8396,"totalMax":0,"totalExchanged":1603,"expend":1603,"sort":0,"createTime":0,"images":null,"url":"/store/exchange_detail?id=53790584e72711e8b5c00242ac130004","categoryId":0},{"id":"86bca6d4c08b11e88b440242ac130003","name":"外贸邮件专项突破","type":"OTHER","duration":0,"scores":299,"picture":"http://cdn.tradestudy.cn/upload/product/20190129/212887575c6d48a8e96510933736dfe5.jpg","width":300,"height":300,"detail":null,"online":true,"message":null,"total":7102,"totalMax":0,"totalExchanged":2897,"expend":2897,"sort":0,"createTime":0,"images":null,"url":"/store/exchange_detail?id=86bca6d4c08b11e88b440242ac130003","categoryId":0}]
     */

    private int id;
    private String name;
    private String layout;
    private int sort;
    private List<StoresBean> stores;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<StoresBean> getStores() {
        return stores;
    }

    public void setStores(List<StoresBean> stores) {
        this.stores = stores;
    }

    public static class StoresBean {
        /**
         * id : 47975b0c6f4e11e9b5c00242ac130004
         * name : 帮课大学特训营第7期
         * type : COMMODITY
         * duration : 0
         * scores : 400
         * picture : http://cdn.tradestudy.cn/upload/product/20190506/a78e5a61aec6a4beca0c30387a759b2c.jpg
         * width : 0
         * height : 0
         * detail : null
         * online : true
         * message : null
         * total : 12
         * totalMax : 0
         * totalExchanged : 17
         * expend : 17
         * sort : 0
         * createTime : 0
         * images : null
         * url : /store/exchange_detail?id=47975b0c6f4e11e9b5c00242ac130004
         * categoryId : 0
         */

        private String id;
        private String name;
        private String type;
        private int duration;
        private int scores;
        private String picture;
        private int width;
        private int height;
        private Object detail;
        private boolean online;
        private Object message;
        private int total;
        private int totalMax;
        private int totalExchanged;
        private int expend;
        private int sort;
        private int createTime;
        private Object images;
        private String url;
        private int categoryId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Object getDetail() {
            return detail;
        }

        public void setDetail(Object detail) {
            this.detail = detail;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalMax() {
            return totalMax;
        }

        public void setTotalMax(int totalMax) {
            this.totalMax = totalMax;
        }

        public int getTotalExchanged() {
            return totalExchanged;
        }

        public void setTotalExchanged(int totalExchanged) {
            this.totalExchanged = totalExchanged;
        }

        public int getExpend() {
            return expend;
        }

        public void setExpend(int expend) {
            this.expend = expend;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public Object getImages() {
            return images;
        }

        public void setImages(Object images) {
            this.images = images;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }
}
