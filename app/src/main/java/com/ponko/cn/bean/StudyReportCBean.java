package com.ponko.cn.bean;

import java.util.ArrayList;
import java.util.List;

public class StudyReportCBean {


    /**
     * color : #41434F
     * title : 我的学习报告
     * list : [{"report":{"subtitle":"单位: 分钟","title":"最近7天学习情况","list":[{"x":"06.30","y":10},{"x":"06.01","y":10},{"x":"06.02","y":10},{"x":"06.03","y":10},{"x":"06.04","y":10},{"x":"06.05","y":10},{"x":"06.06","y":10},{"x":"今天","y":10}]},"title":"近期","list":[{"unit":"分钟","value":100,"key":"今日学习"},{"unit":"积分","value":30,"key":"今日奖励"},{"unit":"天","value":15,"key":"连续学习"}],"footer":"今日学习力打败了<red>86%<\/red>的同学！"},{"title":"累计","list":[{"unit":"分钟","value":100,"key":"累计时长"},{"unit":"积分","value":30,"key":"累计奖励"},{"unit":"天","value":15,"key":"累计学习"}]},{"title":"最近学习","list":[{"sectionName":"3.3 邮件营销的注意事项-3","id":"6e5c5c6e59d511e998aa755c6d1006ed","position":27,"completed":false,"sectionId":"03396bd350da11e7942a00163e04f103","avatar":"http://img.videocc.net/uimage/2/26de49f8c2/2/26de49f8c2b097009daf632de4c0e012_3_b.jpg","courseId":"c49c610950d811e7942a00163e04f103","durationForSecond":891},{"sectionName":"1.1 什么是图片营销","id":"3a922cd659d211e998aa755c6d1006ed","position":149,"completed":false,"sectionId":"d1bdbfca50d911e7942a00163e04f103","avatar":"http://img.videocc.net/uimage/2/26de49f8c2/e/26de49f8c22f083f0478c05188f9cb2e_3_b.jpg","courseId":"c49c610950d811e7942a00163e04f103","durationForSecond":477},{"sectionName":"1.促单方法","id":"51428cd6fe0911e78cc60242ac130003","position":123,"completed":false,"sectionId":"dfced872cb6511e7b5a10242ac130003","avatar":"http://img.videocc.net/uimage/2/26de49f8c2/3/26de49f8c2fa54f5385e6352e3aef513_3_b.jpg","courseId":"0aa87ab0329c11e6be9200163e000c35","durationForSecond":969},{"sectionName":"2.Wish平台最新规则解读","id":"f40c39127f4f11e8b289353775a5da58","position":558,"completed":false,"sectionId":"27ba7bbf2b1a11e7843200163e04f103","avatar":"http://img.videocc.net/uimage/2/26de49f8c2/9/26de49f8c2004456062af58f398c7f89_3_b.jpg","courseId":"f6fb753f2b0e11e7843200163e04f103","durationForSecond":2184},{"sectionName":"2. 时间倒推法&他人刺激法&利润率法","id":"a4562f4a6ed811e8aeb1b9c3a8fdfdfd","position":4,"completed":false,"sectionId":"e536dfa6cb6511e7b5a10242ac130003","avatar":"http://img.videocc.net/uimage/2/26de49f8c2/f/26de49f8c2b60e04c55c4c0826c9ceaf_3_b.jpg","courseId":"0aa87ab0329c11e6be9200163e000c35","durationForSecond":560}],"subtitle":"更多 >"}]
     * subtitle : - 2019.07.24 -
     */

    private String rule;
    private String color;
    private String title;
    private String subtitle;
    private ArrayList<ListBeanXX> list;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public ArrayList<ListBeanXX> getList() {
        return list;
    }

    public void setList(ArrayList<ListBeanXX> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "StudyReportCBean{" +
                "color='" + color + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", list=" + list +
                '}';
    }

    public static class ListBeanXX {
        /**
         * todo 根据情况选择对应的数据字段
         * <p>
         * report : {"subtitle":"单位: 分钟","title":"最近7天学习情况","list":[{"x":"06.30","y":10},{"x":"06.01","y":10},{"x":"06.02","y":10},{"x":"06.03","y":10},{"x":"06.04","y":10},{"x":"06.05","y":10},{"x":"06.06","y":10},{"x":"今天","y":10}]}
         * title : 近期
         * list : [{"unit":"分钟","value":100,"key":"今日学习"},{"unit":"积分","value":30,"key":"今日奖励"},{"unit":"天","value":15,"key":"连续学习"}]
         * footer : 今日学习力打败了<red>86%</red>的同学！
         * subtitle : 更多 >
         */

        private ReportBean report;
        private String title;
        private String footer;
        private String subtitle;
        private List<BasicInfo> list;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ReportBean getReport() {
            return report;
        }

        public void setReport(ReportBean report) {
            this.report = report;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public List<BasicInfo> getList() {
            return list;
        }

        public void setList(List<BasicInfo> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "ListBeanXX{" +
                    "report=" + report +
                    ", title='" + title + '\'' +
                    ", footer='" + footer + '\'' +
                    ", subtitle='" + subtitle + '\'' +
                    ", list=" + list +
                    '}';
        }

        public static class ReportBean {
            /**
             * subtitle : 单位: 分钟
             * title : 最近7天学习情况
             * list : [{"x":"06.30","y":10},{"x":"06.01","y":10},{"x":"06.02","y":10},{"x":"06.03","y":10},{"x":"06.04","y":10},{"x":"06.05","y":10},{"x":"06.06","y":10},{"x":"今天","y":10}]
             */

            private String subtitle;
            private String title;
            private List<PointBean> list;

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<PointBean> getList() {
                return list;
            }

            public void setList(List<PointBean> list) {
                this.list = list;
            }

            public static class PointBean {
                /**
                 * x : 06.30
                 * y : 10
                 */

                private String x;
                private int y;

                public String getX() {
                    return x;
                }

                public void setX(String x) {
                    this.x = x;
                }

                public int getY() {
                    return y;
                }

                public void setY(int y) {
                    this.y = y;
                }
            }
        }

        public static class BasicInfo {
            /**
             * unit : 分钟
             * value : 100
             * key : 今日学习
             */

            private String unit;
            private int value;
            private String key;
            /**
             * sectionName : 3.3 邮件营销的注意事项-3
             * id : 6e5c5c6e59d511e998aa755c6d1006ed
             * position : 27
             * completed : false
             * sectionId : 03396bd350da11e7942a00163e04f103
             * avatar : http://img.videocc.net/uimage/2/26de49f8c2/2/26de49f8c2b097009daf632de4c0e012_3_b.jpg
             * courseId : c49c610950d811e7942a00163e04f103
             * durationForSecond : 891
             */
            private String sectionName;
            private String id;
            private int position;
            private boolean completed;
            private String sectionId;
            private String avatar;
            private String courseId;
            private int durationForSecond;


            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public int getValue() {
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

            public String getSectionName() {
                return sectionName;
            }

            public void setSectionName(String sectionName) {
                this.sectionName = sectionName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public boolean isCompleted() {
                return completed;
            }

            public void setCompleted(boolean completed) {
                this.completed = completed;
            }

            public String getSectionId() {
                return sectionId;
            }

            public void setSectionId(String sectionId) {
                this.sectionId = sectionId;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCourseId() {
                return courseId;
            }

            public void setCourseId(String courseId) {
                this.courseId = courseId;
            }

            public int getDurationForSecond() {
                return durationForSecond;
            }

            public void setDurationForSecond(int durationForSecond) {
                this.durationForSecond = durationForSecond;
            }
        }
    }
}
