package com.ponko.cn.bean;

public class OauthBean {

    //todo 手机未绑定请求绑定接口回传参数
    private String status="";   //微信未綁定 404 綁定為空""
    private Info info;

    // todo 手机绑定
    private String id;//用户编号
    private String token;//token

    @Override
    public String toString() {
        return "OauthBean{" +
                "status='" + status + '\'' +
                ", info=" + info +
                ", id='" + id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Info {
        @Override
        public String toString() {
            return "Info{" +
                    "id='" + id + '\'' +
                    ", createAt='" + createAt + '\'' +
                    ", updateAt='" + updateAt + '\'' +
                    ", subscribe='" + subscribe + '\'' +
                    ", subscribeTime='" + subscribeTime + '\'' +
                    ", subscribeScene='" + subscribeScene + '\'' +
                    ", remark='" + remark + '\'' +
                    ", openId='" + openId + '\'' +
                    ", sex='" + sex + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", unionId='" + unionId + '\'' +
                    '}';
        }

        private String id;
        //private Account account;
        private String createAt;
        private String updateAt;
        private String subscribe;
        private String subscribeTime;
        private String subscribeScene;
        private String remark;
        private String openId;
        private String sex;
        private String province;
        private String city;
        private String country;
        /**
         * 微信名称
         */
        private String nickname;
        //
        /**
         * 头像地址 todo 没有图片后缀名
         */
        private String avatar;
        /**
         * unionId
         */
        private String unionId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public String getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(String subscribe) {
            this.subscribe = subscribe;
        }

        public String getSubscribeTime() {
            return subscribeTime;
        }

        public void setSubscribeTime(String subscribeTime) {
            this.subscribeTime = subscribeTime;
        }

        public String getSubscribeScene() {
            return subscribeScene;
        }

        public void setSubscribeScene(String subscribeScene) {
            this.subscribeScene = subscribeScene;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

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

        public String getUnionId() {
            return unionId;
        }

        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
