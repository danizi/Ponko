package com.ponko.cn.bean;

import java.util.ArrayList;
import java.util.List;

public class AnalysisCBean {

    /**
     * types : [{"id":"2eadbe8d5f4c11e9b5c00242ac130004","name":"头脑风暴","sort":0},{"id":"37df76030bec11e88cc60242ac130003","name":"市场开发","sort":0},{"id":"3cf32c2d0bec11e88cc60242ac130003","name":"跟进谈判","sort":0},{"id":"15d20d27193611e88cc60242ac130003","name":"异议处理","sort":0},{"id":"41c3a61d0bec11e88cc60242ac130003","name":"验厂验货","sort":0},{"id":"45d3b4590bec11e88cc60242ac130003","name":"货运报关","sort":0},{"id":"4a05e5b70bec11e88cc60242ac130003","name":"收款付款","sort":0},{"id":"4e6e0e240bec11e88cc60242ac130003","name":"团队管理","sort":0},{"id":"516300d00bec11e88cc60242ac130003","name":"外贸职场","sort":0}]
     * activities : [{"brief":"请教大家下怎么把客人定制的库存推荐给别的客人？我们是工厂，客人定制的产品没有专利，也没有定制客户的logo，小产品，我们以后也不生产了，就是一点库存，想着推荐给另一个客户便宜点卖出去，客户是一直买同类产品，就是形状不一样，不知道邮件该怎么写？ 我担心写不好了，让客户误会我把卖不出的货卖给他。","createTime":1556614533816,"targetType":"DETAIL","typeId":"3cf32c2d0bec11e88cc60242ac130003","id":"b6e8b4b36b2511e9b5c00242ac130004","type":"跟进谈判","title":"定制产品转推荐给其他客户，怎么推？","url":""},{"brief":"如果我们想给业务员划分区域，业务员主动开发和公司来的询盘一定要限制在自己的区域吗？区域有交叉的话会不会有问题？","createTime":1556510197355,"targetType":"DETAIL","typeId":"4e6e0e240bec11e88cc60242ac130003","id":"c988277d6a3211e9b5c00242ac130004","type":"团队管理","title":"新公司建议做市场区域分配么？","url":""}]
     * url : http://my.tradestudy.cn/analysis_share/
     */

    private String url;
    private List<TypesBean> types;
    private ArrayList<ActivitiesBean> activities;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }

    public ArrayList<ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivitiesBean> activities) {
        this.activities = activities;
    }

    public static class TypesBean {
        /**
         * id : 2eadbe8d5f4c11e9b5c00242ac130004
         * name : 头脑风暴
         * sort : 0
         */

        private String id;
        private String name;
        private int sort;

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

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }

    public static class ActivitiesBean {
        /**
         * brief : 请教大家下怎么把客人定制的库存推荐给别的客人？我们是工厂，客人定制的产品没有专利，也没有定制客户的logo，小产品，我们以后也不生产了，就是一点库存，想着推荐给另一个客户便宜点卖出去，客户是一直买同类产品，就是形状不一样，不知道邮件该怎么写？ 我担心写不好了，让客户误会我把卖不出的货卖给他。
         * createTime : 1556614533816
         * targetType : DETAIL
         * typeId : 3cf32c2d0bec11e88cc60242ac130003
         * id : b6e8b4b36b2511e9b5c00242ac130004
         * type : 跟进谈判
         * title : 定制产品转推荐给其他客户，怎么推？
         * url :
         */

        private String brief;
        private long createTime;
        private String targetType;
        private String typeId;
        private String id;
        private String type;
        private String title;
        private String url;

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getTargetType() {
            return targetType;
        }

        public void setTargetType(String targetType) {
            this.targetType = targetType;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
