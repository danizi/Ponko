package com.ponko.cn.bean;

import java.util.List;

public class SearchRecordCBean {

    /**
     * Linda 王瑶 Jamal
     */
    private List<String> teachers;
    private List<SectionsBean> sections;

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public List<SectionsBean> getSections() {
        return sections;
    }

    public void setSections(List<SectionsBean> sections) {
        this.sections = sections;
    }

    public static class SectionsBean {
        /**
         * name : 1.13 May的面试小故事分享
         * id : 2fa18c80845511e5a95900163e000c35
         * courseId : e90b1cbc845411e5a95900163e000c35
         */
        private String name;
        private String id;
        private String courseId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
