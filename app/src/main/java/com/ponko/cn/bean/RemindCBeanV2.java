package com.ponko.cn.bean;

import java.util.List;

public class RemindCBeanV2 {

    /**
     * count : 0
     * messages : [{"createTime":1561906396202,"unread":false,"description":"距离B2B课程拆分仅剩最后1小时，现在入学仅需2000元，参团团满还可得1000积分，你还要继续错过吗？点我把握最后机会！","id":"caaf7b299b4611e9b5c00242ac130004","title":"外贸B2B课程拆分倒计时最后1小时","type":"URL","url":"/wx/activity-team-app?tid=916608f0901c11e9b5c00242ac130004"},{"createTime":1561889496118,"unread":false,"description":"B2B课程拆分倒计时最后6小时啦，有需要入学或者续费的抓紧时间上车，立省7000元，点我不要错过！","id":"71735ba19b1f11e9b5c00242ac130004","title":"外贸B2B课程拆分倒计时最后6小时","type":"URL","url":"/wx/activity-team-app?tid=916608f0901c11e9b5c00242ac130004"},{"createTime":1561867018358,"unread":false,"description":"B2B课程拆分倒计时最后12小时，现在入学只需2000元/年，快抓住立省7000元的投资自己的机会吧！点我了解详情","id":"1ba96eda9aeb11e9b5c00242ac130004","title":"外贸B2B课程拆分倒计时最后12小时","type":"URL","url":"/wx/activity-team-app?tid=916608f0901c11e9b5c00242ac130004"},{"createTime":1561780828796,"unread":false,"description":"外贸B2B视频课程，月底前入学2000元即可，团满后，团长和团员均有1000积分，团长团数大于等于2团有机会赢取柬埔寨免费考察之旅，快点我上车！","id":"6eadeef39a2211e9b5c00242ac130004","title":"外贸B2B课程拆分倒计时2天","type":"URL","url":"/wx/activity-team-app?tid=916608f0901c11e9b5c00242ac130004"},{"createTime":1561718562241,"unread":false,"description":"B2B课程即将拆分成7门课程（拆后总价9000元/年），6月30日晚12点前入学只需2000元/年，再送1000积分，还有丰富奖品等你来拿，点我了解详情！","id":"74eb9ee1999111e9b5c00242ac130004","title":"外贸B2B课程拆分倒计时3天","type":"URL","url":"/wx/activity-team-app?tid=916608f0901c11e9b5c00242ac130004"},{"createTime":1561714740532,"unread":false,"description":"【课程上线】\r\n《开拓澳洲市场（新》分享开发澳洲客户的有效途径，找到更多客户。\r\n《开拓中东市场（新）》包含阿联酋、土耳其、伊朗和沙特等市场详细攻略，开拓市场更有策略。","id":"8f011d8b998811e9b5c00242ac130004","title":"上线通知","type":"SIMPLE","url":""},{"createTime":1561113030187,"unread":false,"description":"【课程上线】\r\n提升了《邮件技能储备》，才能《储备有价值的客户》，正确的《外贸样品寄送与管理（新）》让成交更近一步！","id":"97b0c66d940f11e9b5c00242ac130004","title":"上线通知","type":"SIMPLE","url":""}]
     */

    private int count;
    private List<MessagesBean> messages;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public static class MessagesBean {
        /**
         * createTime : 1561906396202
         * unread : false
         * description : 距离B2B课程拆分仅剩最后1小时，现在入学仅需2000元，参团团满还可得1000积分，你还要继续错过吗？点我把握最后机会！
         * id : caaf7b299b4611e9b5c00242ac130004
         * title : 外贸B2B课程拆分倒计时最后1小时
         * type : URL
         * url : /wx/activity-team-app?tid=916608f0901c11e9b5c00242ac130004
         */

        private long createTime;
        private boolean unread;
        private String description;
        private String id;
        private String title;
        private String type;
        private String url;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public boolean isUnread() {
            return unread;
        }

        public void setUnread(boolean unread) {
            this.unread = unread;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
