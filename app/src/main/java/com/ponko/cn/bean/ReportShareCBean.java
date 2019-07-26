package com.ponko.cn.bean;

import java.util.List;

public class ReportShareCBean {

    /**
     * qr : {"qr":"http://www.baidu.com","title":"《Google精准开发客户》","qr_desc":"长按识别二维码","subtitle":"送你一门课程","desc":"快来和我一起学习吧～"}
     * data : {"nickname":"会飞的鱼","count":[{"unit":"分钟","value":100,"key":"今日学习"},{"unit":"天","value":30,"key":"连续学习"},{"unit":"小时","value":15.2,"key":"累计学习"}],"avatar":"http://cdn.tradestudy.cn/upload/mobile/20181010/7461de0a46dc548a2667d5bbc9dc731e.png","footer":"今日学习力打败了<red>86%<\/red>的同学！","subtitle":"在帮课大学的学习报告"}
     * bg : http://cdn.tradestudy.cn/upload/product/20190724/5c268e6fc26716bc1286384368c1b652.jpg
     * count : {"subtitle2":"共获得0积分","subtitle1":"朋友成功领取0人","avatar":"http://cdn.tradestudy.cn/upload/product/20190724/2d2849c35420991bc0224e405e385082.png","title":"我的打卡记录","desc":"朋友领取并学习了你的课程，你也能获得5积分哦～"}
     * logo : http://cdn.tradestudy.cn/upload/product/20190724/14f4de8467b9f2708bc618c449feb995.png
     * title : 不积跬步，无以至千里
     * btn : 分享至朋友圈打卡
     */

    private QrBean qr;
    private DataBean data;
    private String bg;
    private CountBeanX count;
    private String logo;
    private String title;
    private String btn;

    public QrBean getQr() {
        return qr;
    }

    public void setQr(QrBean qr) {
        this.qr = qr;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public CountBeanX getCount() {
        return count;
    }

    public void setCount(CountBeanX count) {
        this.count = count;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }

    public static class QrBean {
        /**
         * qr : http://www.baidu.com
         * title : 《Google精准开发客户》
         * qr_desc : 长按识别二维码
         * subtitle : 送你一门课程
         * desc : 快来和我一起学习吧～
         */

        private String qr;
        private String title;
        private String qr_desc;
        private String subtitle;
        private String desc;

        public String getQr() {
            return qr;
        }

        public void setQr(String qr) {
            this.qr = qr;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getQr_desc() {
            return qr_desc;
        }

        public void setQr_desc(String qr_desc) {
            this.qr_desc = qr_desc;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class DataBean {
        /**
         * nickname : 会飞的鱼
         * count : [{"unit":"分钟","value":100,"key":"今日学习"},{"unit":"天","value":30,"key":"连续学习"},{"unit":"小时","value":15.2,"key":"累计学习"}]
         * avatar : http://cdn.tradestudy.cn/upload/mobile/20181010/7461de0a46dc548a2667d5bbc9dc731e.png
         * footer : 今日学习力打败了<red>86%</red>的同学！
         * subtitle : 在帮课大学的学习报告
         */

        private String nickname;
        private String avatar;
        private String footer;
        private String subtitle;
        private List<CountBean> count;

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

        public String getFooter() {
            return footer;
        }

        public void setFooter(String footer) {
            this.footer = footer;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<CountBean> getCount() {
            return count;
        }

        public void setCount(List<CountBean> count) {
            this.count = count;
        }

        public static class CountBean {
            /**
             * unit : 分钟
             * value : 100
             * key : 今日学习
             */

            private String unit;
            private double value;
            private String key;

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public double getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }

    public static class CountBeanX {
        /**
         * subtitle2 : 共获得0积分
         * subtitle1 : 朋友成功领取0人
         * avatar : http://cdn.tradestudy.cn/upload/product/20190724/2d2849c35420991bc0224e405e385082.png
         * title : 我的打卡记录
         * desc : 朋友领取并学习了你的课程，你也能获得5积分哦～
         */

        private String subtitle2;
        private String subtitle1;
        private String avatar;
        private String title;
        private String desc;

        public String getSubtitle2() {
            return subtitle2;
        }

        public void setSubtitle2(String subtitle2) {
            this.subtitle2 = subtitle2;
        }

        public String getSubtitle1() {
            return subtitle1;
        }

        public void setSubtitle1(String subtitle1) {
            this.subtitle1 = subtitle1;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
