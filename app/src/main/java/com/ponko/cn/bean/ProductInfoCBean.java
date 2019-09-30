package com.ponko.cn.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductInfoCBean {

    /**
     * header : {"nickname":"帮课测试","avatar":"http://cdn.tradestudy.cn/upload/mobile/20190909/5d19650fbb11c8947340fd45a6ee5c19.","phone":"15074770708"}
     * list : [{"title":"入学课程","type":"type","list":[{"avatar":"http://cdn.tradestudy.cn/upload/product/20190702/b4d18109b22262dcf7b60d671c536ee2.jpg","title":"外贸团队管理一年学籍","price":"需支付￥2000.0元"}]},{"title":"入学权益","type":"summary","list":[{"text":"1. 学籍有效期至2020年09月18日\r\n\n2. 有效期内学习外贸团队管理课程全部内容包含更新内容\r\n\n3. 赠送400积分，用于积分商城兑换\r\n\n4. 加入帮课大学微信校友群答疑解惑参加活动\r\n\n课程为特殊商品，不支持无理由退货\r\n\n帮课大学拥有课程安排最终解释权"}]},{"title":"支付方式","type":"pay","list":[{"name":"微信支付","default":true,"type":1},{"name":"支付宝支付","default":false,"type":2}]}]
     * footer : {"url":"/web/protocol","text":"同意并接受服务协议"}
     */

    private HeaderBean header;
    private FooterBean footer;
    private List<ListBeanX> list;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public FooterBean getFooter() {
        return footer;
    }

    public void setFooter(FooterBean footer) {
        this.footer = footer;
    }

    public List<ListBeanX> getList() {
        return list;
    }

    public void setList(List<ListBeanX> list) {
        this.list = list;
    }


    public static class HeaderBean {
        /**
         * nickname : 帮课测试
         * avatar : http://cdn.tradestudy.cn/upload/mobile/20190909/5d19650fbb11c8947340fd45a6ee5c19.
         * phone : 15074770708
         */

        private String nickname;
        private String avatar;
        private String phone;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class FooterBean {
        /**
         * url : /web/protocol
         * text : 同意并接受服务协议
         */

        private String url;
        private String text;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class ListBeanX {
        /**
         * title : 入学课程
         * type : type
         * list : [{"avatar":"http://cdn.tradestudy.cn/upload/product/20190702/b4d18109b22262dcf7b60d671c536ee2.jpg","title":"外贸团队管理一年学籍","price":"需支付￥2000.0元"}]
         */

        private String title;
        private String type;
        private List<ListBean> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * avatar : http://cdn.tradestudy.cn/upload/product/20190702/b4d18109b22262dcf7b60d671c536ee2.jpg
             * title : 外贸团队管理一年学籍
             * price : 需支付￥2000.0元
             */

            private String avatar;
            private String title;
            private String price;

            private String text;
            private String name;
            @SerializedName("default")
            private Boolean defaultX;
            private int type;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Boolean getDefaultX() {
                return defaultX;
            }

            public void setDefaultX(Boolean defaultX) {
                this.defaultX = defaultX;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }

}



