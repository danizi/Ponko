package com.ponko.cn.bean;

public class ShareCBean {

    /**
     * qr : http://cdn.tradestudy.cn/home/v3/v2/img/revision/code3.png
     * title1 : 会飞的鱼
     * title2 : 扫码免费领取
     * params : {"summary":"测试课程描述","path":"pages/detail/detail?id=cfd55eb0d37d11e99753010514587b60&rid=922c34b47ee211e5a95900163e000c35","websiteUrl":"http://www.tradestudy.cn","avatar":"http://cdn.tradestudy.cn/upload/product/20190416/cb7fb75516f33df96a19d96d35c6cbf7.jpg","type":0,"title":"测试课程","username":"gh_1cdd0ec91a7e"}
     * bg : http://cdn.tradestudy.cn/upload/product/20190416/cb7fb75516f33df96a19d96d35c6cbf7.jpg
     */

    private String qr;
    private String title1;
    private String title2;
    private String title3;
    private ParamsBean params;
    private String bg;

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public static class ParamsBean {
        /**
         * path : pages/detail/detail?id=cfd55eb0d37d11e99753010514587b60&rid=922c34b47ee211e5a95900163e000c35
         * websiteUrl : http://www.tradestudy.cn
         * avatar : http://cdn.tradestudy.cn/upload/product/20190416/cb7fb75516f33df96a19d96d35c6cbf7.jpg
         * type : 0
         * title : 测试课程
         * username : gh_1cdd0ec91a7e
         */

        private String summary;
        private String path;
        private String websiteUrl;
        private String avatar;
        private int type;
        private String title;
        private String username;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
