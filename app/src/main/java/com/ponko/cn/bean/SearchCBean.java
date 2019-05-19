package com.ponko.cn.bean;

import java.util.List;

public class SearchCBean {


    private List<CoursesBean> courses;
    private List<SectionsBean> sections;

    public List<CoursesBean> getCourses() {
        return courses;
    }

    public void setCourses(List<CoursesBean> courses) {
        this.courses = courses;
    }

    public List<SectionsBean> getSections() {
        return sections;
    }

    public void setSections(List<SectionsBean> sections) {
        this.sections = sections;
    }

    public static class CoursesBean {
        /**
         * duration : 27245
         * image : http://cdn.tradestudy.cn/upload/product/20170511/6091a152a1e8d46b4df5cc3d8ac49fd8.jpg
         * teachers : ["Jamal"]
         * num : 38
         * id : 45d9b8fa35f511e7985e00163e04f103
         * title : LinkedIn Marketing
         */

        private int duration;
        private String image;
        private int num;
        private String id;
        private String title;
        private List<String> teachers;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

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

        public List<String> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<String> teachers) {
            this.teachers = teachers;
        }
    }

    public static class SectionsBean {
        /**
         * sectionName : 1. Do Not Ignore Ai，you Should Embrace It
         * id : 00343860ff5111e8b5c00242ac130004
         * courseId : 36ab3552ff5011e8b5c00242ac130004
         */

        private String sectionName;
        private String id;
        private String courseId;

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

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }
    }
}
