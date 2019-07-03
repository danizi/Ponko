package com.ponko.cn.bean;

import java.util.List;

public class CourseAllCBeanV2 {

    /**
     * id : ccaff182ea2e11e7936c305a3a522e0b
     * parentId : ee9df6117f9211e5a95900163e000c35
     * title : 职业规划
     * subtitle : 职业规划
     * subtitle2 : 职业规划
     * picture : null
     * online : true
     * sequence : 1
     * height : 0
     * width : 0
     * url : null
     * newer : false
     * subTypes : []
     * courses : [{"id":"e90b1cbc845411e5a95900163e000c35","title":"外贸职场笔记","image":"http://cdn.tradestudy.cn/upload/product/20170204/ac5ddedba3cad3c2659489dbfefe2487.jpg","features":null,"introduction":null,"createTime":0,"online":true,"sequence":1,"ppt":false,"files":null,"teachers":["May"],"chapters":[],"accounts":[],"coursePPTs":[],"ppts":[],"num":28,"duration":24399,"newer":false,"possess":false,"sectionCount":0,"teacher":null,"watch":0,"newTemplate":false},{"id":"ed9cc02065d211e6a8ea00163e000c35","title":"外贸人成长宝典","image":"http://cdn.tradestudy.cn/upload/product/20170204/93cbc763693a9c407c053902ee4c668a.jpg","features":null,"introduction":null,"createTime":0,"online":true,"sequence":1,"ppt":false,"files":null,"teachers":["Betty"],"chapters":[],"accounts":[],"coursePPTs":[],"ppts":[],"num":10,"duration":8340,"newer":false,"possess":false,"sectionCount":0,"teacher":null,"watch":0,"newTemplate":false}]
     */

    private String id;
    private String parentId;
    private String title;
    private String subtitle;
    private String subtitle2;
    private Object picture;
    private boolean online;
    private int sequence;
    private int height;
    private int width;
    private Object url;
    private boolean newer;
    private List<?> subTypes;
    private List<CoursesBean> courses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getSubtitle2() {
        return subtitle2;
    }

    public void setSubtitle2(String subtitle2) {
        this.subtitle2 = subtitle2;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public boolean isNewer() {
        return newer;
    }

    public void setNewer(boolean newer) {
        this.newer = newer;
    }

    public List<?> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<?> subTypes) {
        this.subTypes = subTypes;
    }

    public List<CoursesBean> getCourses() {
        return courses;
    }

    public void setCourses(List<CoursesBean> courses) {
        this.courses = courses;
    }

    public static class CoursesBean {
        /**
         * id : e90b1cbc845411e5a95900163e000c35
         * title : 外贸职场笔记
         * image : http://cdn.tradestudy.cn/upload/product/20170204/ac5ddedba3cad3c2659489dbfefe2487.jpg
         * features : null
         * introduction : null
         * createTime : 0
         * online : true
         * sequence : 1
         * ppt : false
         * files : null
         * teachers : ["May"]
         * chapters : []
         * accounts : []
         * coursePPTs : []
         * ppts : []
         * num : 28
         * duration : 24399
         * newer : false
         * possess : false
         * sectionCount : 0
         * teacher : null
         * watch : 0
         * newTemplate : false
         */

        private String id;
        private String title;
        private String image;
        private Object features;
        private Object introduction;
        private int createTime;
        private boolean online;
        private int sequence;
        private boolean ppt;
        private Object files;
        private int num;
        private int duration;
        private boolean newer;
        private boolean possess;
        private int sectionCount;
        private Object teacher;
        private int watch;
        private boolean newTemplate;
        private List<String> teachers;
        private List<?> chapters;
        private List<?> accounts;
        private List<?> coursePPTs;
        private List<?> ppts;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Object getFeatures() {
            return features;
        }

        public void setFeatures(Object features) {
            this.features = features;
        }

        public Object getIntroduction() {
            return introduction;
        }

        public void setIntroduction(Object introduction) {
            this.introduction = introduction;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public boolean isPpt() {
            return ppt;
        }

        public void setPpt(boolean ppt) {
            this.ppt = ppt;
        }

        public Object getFiles() {
            return files;
        }

        public void setFiles(Object files) {
            this.files = files;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public boolean isNewer() {
            return newer;
        }

        public void setNewer(boolean newer) {
            this.newer = newer;
        }

        public boolean isPossess() {
            return possess;
        }

        public void setPossess(boolean possess) {
            this.possess = possess;
        }

        public int getSectionCount() {
            return sectionCount;
        }

        public void setSectionCount(int sectionCount) {
            this.sectionCount = sectionCount;
        }

        public Object getTeacher() {
            return teacher;
        }

        public void setTeacher(Object teacher) {
            this.teacher = teacher;
        }

        public int getWatch() {
            return watch;
        }

        public void setWatch(int watch) {
            this.watch = watch;
        }

        public boolean isNewTemplate() {
            return newTemplate;
        }

        public void setNewTemplate(boolean newTemplate) {
            this.newTemplate = newTemplate;
        }

        public List<String> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<String> teachers) {
            this.teachers = teachers;
        }

        public List<?> getChapters() {
            return chapters;
        }

        public void setChapters(List<?> chapters) {
            this.chapters = chapters;
        }

        public List<?> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<?> accounts) {
            this.accounts = accounts;
        }

        public List<?> getCoursePPTs() {
            return coursePPTs;
        }

        public void setCoursePPTs(List<?> coursePPTs) {
            this.coursePPTs = coursePPTs;
        }

        public List<?> getPpts() {
            return ppts;
        }

        public void setPpts(List<?> ppts) {
            this.ppts = ppts;
        }
    }
}
