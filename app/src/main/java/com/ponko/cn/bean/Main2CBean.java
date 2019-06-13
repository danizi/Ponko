package com.ponko.cn.bean;

import java.util.List;

public class Main2CBean {

    /**
     * banner_middle : [{"link_type":"COURSE","id":"f4050853836e11e9b5c00242ac130004","avatar":"http://cdn.tradestudy.cn/upload/product/20190531/408971a058e0090555ac9f7aceb485b8.png","title":"新课上线","link_value":"f5435f7357e14ada8e2f4a6b3d634888"}]
     * products_purchased : [{"link_type":"PAYMENT","id":"19970891c3a511e79ce80242ac130003","avatar":"http://cdn.tradestudy.cn/upload/product/20171107/23d4700c61be941f7ff816895a2acbac.png","link_value":"/product/detail?productId=19970891c3a511e79ce80242ac130003"}]
     * polyv : {"read_token":"LeT0Q9247Q-qhF3zuoD1A-p1F7Q22lzt","secret_key":"ETd98zg5Ka","write_token":"xYWqgx1vdN5ZFBa-K79vZGqgBw83n19Z","user_id":"26de49f8c2"}
     * banner_top : [{"link_type":"STORE","id":"60095d3b86ab11e9b5c00242ac130004","avatar":"http://cdn.tradestudy.cn/upload/product/20190604/7b09baf1b29c3a8b2b01294d9925701a.jpg","link_value":""}]
     * service : {"wechat":"bangke029","phone":"0755-28284353"}
     * tips : {"pay_success":"同学，恭喜你加入帮课大学，记得加辅导员Mango微信：bangke028，邀请进校友群哦。"}
     * banner_mini : [{"link_type":"URL","avatar":"","link_value":"https://www.baidu.com/"}]
     * products_all : [{"link_type":"PAYMENT","id":"bfd7f85c6f0a11e8a93a67b75eb469f2","avatar":"","link_value":"/product/detail?productId=bfd7f85c6f0a11e8a93a67b75eb469f2"},{"link_type":"PAYMENT","id":"c7e34a3d7c4111e9b5c00242ac130004","avatar":"http://cdn.tradestudy.cn/upload/product/20190522/25823f296809df52554a82304acb234b.jpg","link_value":"/product/detail?productId=c7e34a3d7c4111e9b5c00242ac130004"}]
     */

    private PolyvBean polyv;
    private ServiceBean service;
    private TipsBean tips;
    private List<BannerMiddleBean> banner_middle;
    private List<ProductsPurchasedBean> products_purchased;
    private List<BannerTopBean> banner_top;
    private List<BannerMiniBean> banner_mini;
    private List<ProductsAllBean> products_all;
    private int msg_count=0;

    public int getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(int msg_count) {
        this.msg_count = msg_count;
    }

    public PolyvBean getPolyv() {
        return polyv;
    }

    public void setPolyv(PolyvBean polyv) {
        this.polyv = polyv;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public TipsBean getTips() {
        return tips;
    }

    public void setTips(TipsBean tips) {
        this.tips = tips;
    }

    public List<BannerMiddleBean> getBanner_middle() {
        return banner_middle;
    }

    public void setBanner_middle(List<BannerMiddleBean> banner_middle) {
        this.banner_middle = banner_middle;
    }

    public List<ProductsPurchasedBean> getProducts_purchased() {
        return products_purchased;
    }

    public void setProducts_purchased(List<ProductsPurchasedBean> products_purchased) {
        this.products_purchased = products_purchased;
    }

    public List<BannerTopBean> getBanner_top() {
        return banner_top;
    }

    public void setBanner_top(List<BannerTopBean> banner_top) {
        this.banner_top = banner_top;
    }

    public List<BannerMiniBean> getBanner_mini() {
        return banner_mini;
    }

    public void setBanner_mini(List<BannerMiniBean> banner_mini) {
        this.banner_mini = banner_mini;
    }

    public List<ProductsAllBean> getProducts_all() {
        return products_all;
    }

    public void setProducts_all(List<ProductsAllBean> products_all) {
        this.products_all = products_all;
    }

    public static class PolyvBean {
        /**
         * read_token : LeT0Q9247Q-qhF3zuoD1A-p1F7Q22lzt
         * secret_key : ETd98zg5Ka
         * write_token : xYWqgx1vdN5ZFBa-K79vZGqgBw83n19Z
         * user_id : 26de49f8c2
         */

        private String read_token;
        private String secret_key;
        private String write_token;
        private String user_id;

        public String getRead_token() {
            return read_token;
        }

        public void setRead_token(String read_token) {
            this.read_token = read_token;
        }

        public String getSecret_key() {
            return secret_key;
        }

        public void setSecret_key(String secret_key) {
            this.secret_key = secret_key;
        }

        public String getWrite_token() {
            return write_token;
        }

        public void setWrite_token(String write_token) {
            this.write_token = write_token;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    public static class ServiceBean {
        /**
         * wechat : bangke029
         * phone : 0755-28284353
         */

        private String wechat;
        private String phone;

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class TipsBean {
        /**
         * pay_success : 同学，恭喜你加入帮课大学，记得加辅导员Mango微信：bangke028，邀请进校友群哦。
         */

        private String pay_success;

        public String getPay_success() {
            return pay_success;
        }

        public void setPay_success(String pay_success) {
            this.pay_success = pay_success;
        }
    }

    public static class BannerMiddleBean {
        /**
         * link_type : COURSE
         * id : f4050853836e11e9b5c00242ac130004
         * avatar : http://cdn.tradestudy.cn/upload/product/20190531/408971a058e0090555ac9f7aceb485b8.png
         * title : 新课上线
         * link_value : f5435f7357e14ada8e2f4a6b3d634888
         */

        private String link_type;
        private String id;
        private String avatar;
        private String title;
        private String link_value;

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink_value() {
            return link_value;
        }

        public void setLink_value(String link_value) {
            this.link_value = link_value;
        }
    }

    public static class ProductsPurchasedBean {
        /**
         * link_type : PAYMENT
         * id : 19970891c3a511e79ce80242ac130003
         * avatar : http://cdn.tradestudy.cn/upload/product/20171107/23d4700c61be941f7ff816895a2acbac.png
         * link_value : /product/detail?productId=19970891c3a511e79ce80242ac130003
         */

        private String link_type;
        private String id;
        private String avatar;
        private String link_value;

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getLink_value() {
            return link_value;
        }

        public void setLink_value(String link_value) {
            this.link_value = link_value;
        }
    }

    public static class BannerTopBean {
        /**
         * link_type : STORE
         * id : 60095d3b86ab11e9b5c00242ac130004
         * avatar : http://cdn.tradestudy.cn/upload/product/20190604/7b09baf1b29c3a8b2b01294d9925701a.jpg
         * link_value :
         */

        private String link_type;
        private String id;
        private String avatar;
        private String link_value;

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getLink_value() {
            return link_value;
        }

        public void setLink_value(String link_value) {
            this.link_value = link_value;
        }
    }

    public static class BannerMiniBean {
        /**
         * link_type : URL
         * avatar :
         * link_value : https://www.baidu.com/
         */

        private String link_type;
        private String avatar;
        private String link_value;

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getLink_value() {
            return link_value;
        }

        public void setLink_value(String link_value) {
            this.link_value = link_value;
        }
    }

    public static class ProductsAllBean {
        /**
         * link_type : PAYMENT
         * id : bfd7f85c6f0a11e8a93a67b75eb469f2
         * avatar :
         * link_value : /product/detail?productId=bfd7f85c6f0a11e8a93a67b75eb469f2
         */

        private String link_type;
        private String id;
        private String avatar;
        private String link_value;

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getLink_value() {
            return link_value;
        }

        public void setLink_value(String link_value) {
            this.link_value = link_value;
        }
    }
}
