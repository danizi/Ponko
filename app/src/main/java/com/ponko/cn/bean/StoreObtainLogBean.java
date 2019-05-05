package com.ponko.cn.bean;

import java.util.List;

public class StoreObtainLogBean {

    /**
     * soon_expired_time : 1574738748379
     * soon_expired_scores : 2011
     * list : [{"id":"f7d728faf12a11e8b5c00242ac130004","taskId":"80073844efd311e78cc60242ac130003","scores":2000,"remaining":2011,"expiredTime":1574738748379,"userId":"65783b15d47711e88b440242ac130003","type":"OTHER","status":true,"createTime":1543202748379,"remarks":null,"orderNumber":null,"nickname":null}]
     */

    private long soon_expired_time;
    private int soon_expired_scores;
    private List<ListBean> list;

    public long getSoon_expired_time() {
        return soon_expired_time;
    }

    public void setSoon_expired_time(long soon_expired_time) {
        this.soon_expired_time = soon_expired_time;
    }

    public int getSoon_expired_scores() {
        return soon_expired_scores;
    }

    public void setSoon_expired_scores(int soon_expired_scores) {
        this.soon_expired_scores = soon_expired_scores;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : f7d728faf12a11e8b5c00242ac130004
         * taskId : 80073844efd311e78cc60242ac130003
         * scores : 2000
         * remaining : 2011
         * expiredTime : 1574738748379
         * userId : 65783b15d47711e88b440242ac130003
         * type : OTHER
         * status : true
         * createTime : 1543202748379
         * remarks : null
         * orderNumber : null
         * nickname : null
         */
        private String title;
        private String id;
        private String taskId;
        private int scores;
        private int remaining;
        private long expiredTime;
        private String userId;
        private String type;
        private boolean status;
        private long createTime;
        private Object remarks;
        private Object orderNumber;
        private Object nickname;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public int getRemaining() {
            return remaining;
        }

        public void setRemaining(int remaining) {
            this.remaining = remaining;
        }

        public long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(Object orderNumber) {
            this.orderNumber = orderNumber;
        }

        public Object getNickname() {
            return nickname;
        }

        public void setNickname(Object nickname) {
            this.nickname = nickname;
        }
    }
}
