package com.ponko.cn.bean;

import java.util.List;

public class ProfileCBean {
    /**
     * task : [{"id":"03e3aa0b0f1511e9b5c00242ac130004","name":"入学B2C","summary":"入学或续费，奖励100分","scores":100,"completed":false,"icon":"score_icon_invite","type":2,"linkType":"payment","linkValue":"/product/detail?productId=c279381e6f0a11e8a93a67b75eb469f2"},{"id":"ec37a551d68a44138b9b0a3ba04afd86","name":"入学B2B","summary":"入学或续费，奖励400分","scores":400,"completed":false,"icon":"score_icon_invite","type":2,"linkType":"payment","linkValue":"/product/detail?productId=bfd7f85c6f0a11e8a93a67b75eb469f2"},{"id":"3729b0530c6b11e9b5c00242ac130004","name":"邀请好友入学B2C","summary":"成功入学，奖励300积分","scores":300,"completed":false,"icon":"score_icon_invite","type":2,"linkType":"invite","linkValue":"c279381e6f0a11e8a93a67b75eb469f2"},{"id":"9b5df5f40c6a11e9b5c00242ac130004","name":"邀请好友入学B2B","summary":"成功入学，奖励400积分","scores":400,"completed":false,"icon":"score_icon_invite","type":2,"linkType":"invite","linkValue":"bfd7f85c6f0a11e8a93a67b75eb469f2"},{"id":"a177fe086c034241ba70fbf57cf51cf6","name":"完善个人资料","summary":"完善个人真实信息","scores":1,"completed":true,"icon":"score_icon_edit","type":1,"linkType":"profile_edit","linkValue":""}]
     * exchange_rule_url : /help/detail?id=be348e54f45211e7936c305a3a522e0b
     * polyv : {"read_token":"LeT0Q9247Q-qhF3zuoD1A-p1F7Q22lzt","secret_key":"ETd98zg5Ka","write_token":"xYWqgx1vdN5ZFBa-K79vZGqgBw83n19Z","user_id":"26de49f8c2"}
     * inviteImageAd : http://cdn.tradestudy.cn/upload/product/20171220/banner2.png
     * score_rule_url : /help/detail?id=be348d40f45211e7936c305a3a522e0b
     * account : {"qq":"619348292","city":"即可","heartbeat":7,"companyName":"帮课大学","industry":"计算机","avatar":"http://cdn.tradestudy.cn/upload/mobile/20190415/3d65bfde386055559c4df8daec239c25.","study_count":3,"study_duration":16750,"expiredTime":1586330167000,"intention":"没有用途","realName":"帮课测试","weiXin":"w空ee","is_bind_wechat":true,"phone":"15074770708","nickname":"帮课测试","integration":2636,"email":"15074770708@163.com","status":2}
     * msg_count : 24
     * joinImageAd : http://cdn.tradestudy.cn/upload/product/20171219/banner.png
     * products : [{"id":"bfd7f85c6f0a11e8a93a67b75eb469f2","title":"外贸B2B课程","url":"/product/detail?productId=bfd7f85c6f0a11e8a93a67b75eb469f2"},{"id":"c279381e6f0a11e8a93a67b75eb469f2","title":"跨境电商B2C课程","url":"/product/detail?productId=c279381e6f0a11e8a93a67b75eb469f2"},{"id":"19970891c3a511e79ce80242ac130003","title":"帮课全站课程","url":"/product/detail?productId=19970891c3a511e79ce80242ac130003"}]
     */

    private String exchange_rule_url;
    private PolyvBean polyv;
    private String inviteImageAd;
    private String score_rule_url;
    private AccountBean account;
    private int msg_count;
    private String joinImageAd;
    private List<TaskBean> task;
    private List<ProductsBean> products;
    private boolean sign_completed;

    public boolean isSign_completed() {
        return sign_completed;
    }

    public void setSign_completed(boolean sign_completed) {
        this.sign_completed = sign_completed;
    }

    public String getExchange_rule_url() {
        return exchange_rule_url;
    }

    public void setExchange_rule_url(String exchange_rule_url) {
        this.exchange_rule_url = exchange_rule_url;
    }

    public PolyvBean getPolyv() {
        return polyv;
    }

    public void setPolyv(PolyvBean polyv) {
        this.polyv = polyv;
    }

    public String getInviteImageAd() {
        return inviteImageAd;
    }

    public void setInviteImageAd(String inviteImageAd) {
        this.inviteImageAd = inviteImageAd;
    }

    public String getScore_rule_url() {
        return score_rule_url;
    }

    public void setScore_rule_url(String score_rule_url) {
        this.score_rule_url = score_rule_url;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public int getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(int msg_count) {
        this.msg_count = msg_count;
    }

    public String getJoinImageAd() {
        return joinImageAd;
    }

    public void setJoinImageAd(String joinImageAd) {
        this.joinImageAd = joinImageAd;
    }

    public List<TaskBean> getTask() {
        return task;
    }

    public void setTask(List<TaskBean> task) {
        this.task = task;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
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

    public static class AccountBean {
        /**
         * qq : 619348292
         * city : 即可
         * heartbeat : 7
         * companyName : 帮课大学
         * industry : 计算机
         * avatar : http://cdn.tradestudy.cn/upload/mobile/20190415/3d65bfde386055559c4df8daec239c25.
         * study_count : 3
         * study_duration : 16750
         * expiredTime : 1586330167000
         * intention : 没有用途
         * realName : 帮课测试
         * weiXin : w空ee
         * is_bind_wechat : true
         * phone : 15074770708
         * nickname : 帮课测试
         * integration : 2636
         * email : 15074770708@163.com
         * status : 2
         */

        private String qq;
        private String city;
        private int heartbeat;
        private String companyName;
        private String industry;
        private String avatar;
        private int study_count;
        private int study_duration;
        private long expiredTime;
        private String intention;
        private String realName;
        private String weiXin;
        private boolean is_bind_wechat;
        private String phone;
        private String nickname;
        private int integration;
        private String email;
        private int status;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getHeartbeat() {
            return heartbeat;
        }

        public void setHeartbeat(int heartbeat) {
            this.heartbeat = heartbeat;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getStudy_count() {
            return study_count;
        }

        public void setStudy_count(int study_count) {
            this.study_count = study_count;
        }

        public int getStudy_duration() {
            return study_duration;
        }

        public void setStudy_duration(int study_duration) {
            this.study_duration = study_duration;
        }

        public long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getIntention() {
            return intention;
        }

        public void setIntention(String intention) {
            this.intention = intention;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getWeiXin() {
            return weiXin;
        }

        public void setWeiXin(String weiXin) {
            this.weiXin = weiXin;
        }

        public boolean isIs_bind_wechat() {
            return is_bind_wechat;
        }

        public void setIs_bind_wechat(boolean is_bind_wechat) {
            this.is_bind_wechat = is_bind_wechat;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIntegration() {
            return integration;
        }

        public void setIntegration(int integration) {
            this.integration = integration;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "AccountBean{" +
                    "qq='" + qq + '\'' +
                    ", city='" + city + '\'' +
                    ", heartbeat=" + heartbeat +
                    ", companyName='" + companyName + '\'' +
                    ", industry='" + industry + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", study_count=" + study_count +
                    ", study_duration=" + study_duration +
                    ", expiredTime=" + expiredTime +
                    ", intention='" + intention + '\'' +
                    ", realName='" + realName + '\'' +
                    ", weiXin='" + weiXin + '\'' +
                    ", is_bind_wechat=" + is_bind_wechat +
                    ", phone='" + phone + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", integration=" + integration +
                    ", email='" + email + '\'' +
                    ", status=" + status +
                    '}';
        }
    }

    public static class TaskBean {
        /**
         * id : 03e3aa0b0f1511e9b5c00242ac130004
         * name : 入学B2C
         * summary : 入学或续费，奖励100分
         * scores : 100
         * completed : false
         * icon : score_icon_invite
         * type : 2
         * linkType : payment
         * linkValue : /product/detail?productId=c279381e6f0a11e8a93a67b75eb469f2
         */

        private String id;
        private String name;
        private String summary;
        private int scores;
        private boolean completed;
        private String icon;
        private int type;
        private String linkType;
        private String linkValue;

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

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

        public String getLinkValue() {
            return linkValue;
        }

        public void setLinkValue(String linkValue) {
            this.linkValue = linkValue;
        }
    }

    public static class ProductsBean {
        /**
         * id : bfd7f85c6f0a11e8a93a67b75eb469f2
         * title : 外贸B2B课程
         * url : /product/detail?productId=bfd7f85c6f0a11e8a93a67b75eb469f2
         */

        private String id;
        private String title;
        private String url;

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
    }
}
