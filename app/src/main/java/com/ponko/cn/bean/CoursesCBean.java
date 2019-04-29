package com.ponko.cn.bean;

import java.util.List;

public class CoursesCBean {


    private List<Exchanged> wechat;
    private List<Exchanged> exchanged;
    private List<TrialBean> trial;

    public List<Exchanged> getWechat() {
        return wechat;
    }

    public void setWechat(List<Exchanged> wechat) {
        this.wechat = wechat;
    }

    public List<Exchanged> getExchanged() {
        return exchanged;
    }

    public void setExchanged(List<Exchanged> exchanged) {
        this.exchanged = exchanged;
    }

    public List<TrialBean> getTrial() {
        return trial;
    }

    public void setTrial(List<TrialBean> trial) {
        this.trial = trial;
    }

    public static class Exchanged {
        public String duration;
        public String image;
        public Double num;
        public String id;
        public String title;
        public List<String> teachers;
    }

    public static class TrialBean {
        /**
         * id : bfd7f85c6f0a11e8a93a67b75eb469f2
         * title : 外贸B2B课程
         * summary : 9大系列 丨320门课程丨1650小节丨458.6小时
         * summary2 : 此为免费试听课程精选，不定期更换。
         * sort : 1
         * list : [{"id":"0347e9dda34c46fdb7bfff13af7a20cc","vid":"26de49f8c2a6a16b2b8f41f88045f7b4_2","title":"客户分析七大途径","summary":"从客户背景、产品、上下游、采购行为、信用分析等五个维度全方位了解客户，配合多种实用工具及实战技巧，快速找准用户痛点！","avatar":"http://cdn.tradestudy.cn/upload/product/20180930/3f04f73d7d13f29d82573ac93a30311b.png","sort":1},{"id":"1657fa85951b40fa81f397e7477e5a87","vid":"26de49f8c24d5853b97554e27430fc0c_2","title":"员工培训及考核转正","summary":"从客户背景、产品、上下游、采购行为、信用分析等五个维度全方位了解客户，配合多种实用工具及实战技巧，快速找准用户痛点！","avatar":"http://cdn.tradestudy.cn/upload/product/20180930/ded26cee5f359a3a77760363482ddbf1.png","sort":1}]
         */

        private String id;
        private String title;
        private String summary;
        private String summary2;
        private int sort;
        private List<ListBean> list;

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

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getSummary2() {
            return summary2;
        }

        public void setSummary2(String summary2) {
            this.summary2 = summary2;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 0347e9dda34c46fdb7bfff13af7a20cc
             * vid : 26de49f8c2a6a16b2b8f41f88045f7b4_2
             * title : 客户分析七大途径
             * summary : 从客户背景、产品、上下游、采购行为、信用分析等五个维度全方位了解客户，配合多种实用工具及实战技巧，快速找准用户痛点！
             * avatar : http://cdn.tradestudy.cn/upload/product/20180930/3f04f73d7d13f29d82573ac93a30311b.png
             * sort : 1
             */

            private String id;
            private String vid;
            private String title;
            private String summary;
            private String avatar;
            private int sort;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }
    }
}
