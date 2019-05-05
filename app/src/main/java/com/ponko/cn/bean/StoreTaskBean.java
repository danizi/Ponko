package com.ponko.cn.bean;

import java.util.List;

public class StoreTaskBean {

    /**
     * days : 0
     * completed : false
     * scores : 2011
     * tasks : [{"id":"a177fe086c034241ba70fbf57cf51cf6","name":"完善个人资料","summary":"完善个人真实信息","scores":1,"completed":false,"icon":"score_icon_edit","type":1,"linkType":"profile_edit","linkValue":""},{"id":"ec37a551d68a44138b9b0a3ba04afd86","name":"推荐好友入学","summary":"","scores":300,"completed":false,"icon":"score_icon_invite","type":2,"linkType":"invite","linkValue":""},{"id":"9b5df5f40c6a11e9b5c00242ac130004","name":"邀请好友入学B2B","summary":"成功入学，奖励300积分","scores":300,"completed":false,"icon":null,"type":0,"linkType":"invite","linkValue":"bfd7f85c6f0a11e8a93a67b75eb469f2"},{"id":"3729b0530c6b11e9b5c00242ac130004","name":"邀请好友入学B2C","summary":"成功入学，奖励200积分","scores":200,"completed":false,"icon":null,"type":0,"linkType":"invite","linkValue":"c279381e6f0a11e8a93a67b75eb469f2"}]
     */

    private int days;
    private boolean completed;
    private int scores;
    private List<TasksBean> tasks;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public List<TasksBean> getTasks() {
        return tasks;
    }

    public void setTasks(List<TasksBean> tasks) {
        this.tasks = tasks;
    }

    public static class TasksBean {
        /**
         * id : a177fe086c034241ba70fbf57cf51cf6
         * name : 完善个人资料
         * summary : 完善个人真实信息
         * scores : 1
         * completed : false
         * icon : score_icon_edit
         * type : 1
         * linkType : profile_edit
         * linkValue :
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
}
