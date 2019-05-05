package com.ponko.cn.bean;

import java.util.List;

/**
 * {
 * "all": [
 * {
 * "headPicture": "http://cdn.tradestudy.cn/upload/mobile/20180126/10f07100ce48e0df14b6a3ca793862ec.png",
 * "product_level_name": "",
 * "scores": 6453,
 * "nickname": "廖启深",
 * "id": "c54c625286ca11e5b0ab00163e000c35"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/upload/default-avatar.png",
 * "product_level_name": "",
 * "scores": 2700,
 * "nickname": "helen",
 * "id": "95fcdb4624fa11e7b02000163e000c35"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/upload/default-avatar.png",
 * "product_level_name": "",
 * "scores": 2620,
 * "nickname": "kalan",
 * "id": "d2bbf6a2dcc111e78cc60242ac130003"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/upload/default-avatar.png",
 * "product_level_name": "",
 * "scores": 2380,
 * "nickname": "林康carry",
 * "id": "88b527fc737511e7baff00163e04f103"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/upload/mobile/20181025/0a30a7f5575d35751498ded81ea08a6f.",
 * "product_level_name": "",
 * "scores": 2101,
 * "nickname": "秋水",
 * "id": "65783b15d47711e88b440242ac130003"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/home/images/avatar/default_avatar.jpg",
 * "product_level_name": "",
 * "scores": 2100,
 * "nickname": "rayray",
 * "id": "f96ac24d0f9c11e68dfa00163e000c35"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/home/images/avatar/default_avatar.jpg",
 * "product_level_name": "",
 * "scores": 2080,
 * "nickname": "Echo",
 * "id": "0f3fc21f314f11e6be9200163e000c35"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/upload/default-avatar.png",
 * "product_level_name": "",
 * "scores": 2000,
 * "nickname": "徐敏捷",
 * "id": "3be784ab825911e7baff00163e04f103"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/home/images/avatar/default_avatar.jpg",
 * "product_level_name": "",
 * "scores": 1998,
 * "nickname": "jack",
 * "id": "22049aedbe8111e58dfa00163e000c35"
 * },
 * {
 * "headPicture": "http://cdn.tradestudy.cn/upload/default-avatar.png",
 * "product_level_name": "",
 * "scores": 1900,
 * "nickname": "LILY",
 * "id": "652ee570c9ec11e79ce80242ac130003"
 * }
 * ],
 * "mine": {
 * "scores": 2101,
 * "nickname": "秋水",
 * "ranking": 5,
 * "id": "65783b15d47711e88b440242ac130003",
 * "head_picture": "http://cdn.tradestudy.cn/upload/mobile/20181025/0a30a7f5575d35751498ded81ea08a6f."
 * }
 * }
 */
public class RankingV2 {
    private MineBean mine;
    private List<AllBean> all;

    public MineBean getMine() {
        return mine;
    }

    public void setMine(MineBean mine) {
        this.mine = mine;
    }

    public List<AllBean> getAll() {
        return all;
    }

    public void setAll(List<AllBean> all) {
        this.all = all;
    }

    public static class MineBean {
        /**
         * scores : 2101
         * nickname : 秋水
         * ranking : 5
         * id : 65783b15d47711e88b440242ac130003
         * head_picture : http://cdn.tradestudy.cn/upload/mobile/20181025/0a30a7f5575d35751498ded81ea08a6f.
         */

        private int scores;
        private String nickname;
        private int ranking;
        private String id;
        private String head_picture;
        private String product_name;

        public String getProductName() {
            return product_name;
        }

        public void setProductName(String productName) {
            this.product_name = productName;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHead_picture() {
            return head_picture;
        }

        public void setHead_picture(String head_picture) {
            this.head_picture = head_picture;
        }
    }

    public static class AllBean {
        /**
         * headPicture : http://cdn.tradestudy.cn/upload/mobile/20180126/10f07100ce48e0df14b6a3ca793862ec.png
         * product_level_name :
         * scores : 6453
         * nickname : 廖启深
         * id : c54c625286ca11e5b0ab00163e000c35
         */

        private String headPicture;
        private String product_level_name;
        private int scores;
        private String nickname;
        private String id;
        private int ranking;
        private String productName;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public String getHeadPicture() {
            return headPicture;
        }

        public void setHeadPicture(String headPicture) {
            this.headPicture = headPicture;
        }

        public String getProduct_level_name() {
            return product_level_name;
        }

        public void setProduct_level_name(String product_level_name) {
            this.product_level_name = product_level_name;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
