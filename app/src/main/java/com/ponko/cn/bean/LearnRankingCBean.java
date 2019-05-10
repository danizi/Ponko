package com.ponko.cn.bean;

import java.util.List;

public class LearnRankingCBean {

    /**
     * ranking : [{"headPicture":"http://cdn.tradestudy.cn/upload/mobile/20180925/62facb5c8755a9498ada1fbb6f5c6482.png","duration":103155,"nickname":"Ivy","id":"99607afc85d711e5873e272649fb1264"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/p7BTmns87u65ic7NRBWQ82QD6btGuxlbicpwv4p0Qy36mzN2jwfMlAgFucn1OvO5iaGlwQ34gLZV2TiamECibU8UMlw/132","duration":50061,"nickname":"heidi","id":"17292f091a4311e6997100163e000c35"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epGGsA5LOjsNkBhzmsDTNXKsw90VPoyiad7yAoOMdKg8o3ib3niadMYia9Dz3jtzwuQfOUBQtD3FOA4vw/132","duration":49769,"nickname":"Jack","id":"2095391634b611e88b440242ac130003"},{"headPicture":"http://cdn.tradestudy.cn/upload/default-avatar.png","duration":45522,"nickname":"糖糖TY2008","id":"ff689e761d0611e88cc60242ac130003"},{"headPicture":"http://cdn.tradestudy.cn/upload/default-avatar.png","duration":34835,"nickname":"Amy and Jerry","id":"9a8baf1985d711e5873e272649fb1264"},{"headPicture":"http://cdn.tradestudy.cn/upload/default-avatar.png","duration":31248,"nickname":"tcbest","id":"c52cdf2cf17311e58dfa00163e000c35"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJYQ2t5vVmVJ5VVPK1HLibEAOer1FKQgPyakjuYps3BIE3ewQGnibkicfpicianZ5ic6cm6txjtgiaEIo0pQ/132","duration":30531,"nickname":"笑三少","id":"d3e99996878411e58bee00163e000c35"},{"headPicture":"http://wx.qlogo.cn/mmopen/mk3HhjZspKZ5QImypia8PrX0jOBrQkyQ9yGtCbewYLTJNuhgeqMpm6pVicWPl6ysKel4GjYAnu1tBPBluJXtDhTslSboap8Brn/0","duration":29205,"nickname":"Rove","id":"4e7bad2f110c11e7ad2b00163e000c35"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK0kt9KiaztfWqYQKQaiafpYseQfcvicFlBh5ibQe0fNlvagHPibG5zD5QmHnbicCibEBtpEibwXLIQr2U7tg/132","duration":27604,"nickname":"燕萍 一棵树","id":"4285e613964111e88b440242ac130003"},{"headPicture":"http://cdn.tradestudy.cn/home/images/avatar.png","duration":26351,"nickname":"Wendy","id":"5cbf8f18211611e9b5c00242ac130004"},{"headPicture":"http://wx.qlogo.cn/mmopen/0xyIbfYxlw4OwR5ia3MDxXjrDcV9111jVIcb5DaibiaaHzynZ1oTQ2lPlPvDlx75VDJWdSyuyYUibuNfH6ViaDmKpHWZjftnBfBn7/0","duration":26320,"nickname":"醉在赏月夜","id":"fe4b0d2aee9611e6a79d00163e000c35"},{"headPicture":null,"duration":25630,"nickname":"Cheryl","id":"4b2ec24a67fe11e88b440242ac130003"},{"headPicture":"http://cdn.tradestudy.cn/upload/default-avatar.png","duration":24780,"nickname":"Sunny.Zhou","id":"061a0ee7091711e7810000163e000c35"},{"headPicture":"http://cdn.tradestudy.cn/upload/mobile/20180531/706935cace3641dbbf17aab7b69999dd.","duration":24090,"nickname":"杰哥","id":"b160904b37d111e7985e00163e04f103"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIrS59fpvXfuVKBFdo4SXSUCkMuv90JGLWLiaD0daWJFwzpQU3uUs3nDWpvx2ic4j9nXLjFStvT5gIw/132","duration":23480,"nickname":"Lily","id":"d462e7c7378e11e7985e00163e04f103"},{"headPicture":"http://cdn.tradestudy.cn/upload/default-avatar.png","duration":22540,"nickname":"carol","id":"f87a8ca824ab11e7b02000163e000c35"},{"headPicture":null,"duration":22207,"nickname":"Nina","id":"221a7f9e2f3511e88cc60242ac130003"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/yrwTmJiaLbkmfxiciaOeaMNsT0cSCx3eZVprUPlicg2JNLnjnVDomr9wIvh0fptHG4cDBpA7AtmFA8UFOHO0KBjhgw/132","duration":21282,"nickname":"Sara","id":"9a20817585d711e5873e272649fb1264"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/dglMUGgevnibhWHlMTamM8YdD4Zy3gyCPlA2t49wsUpKYT4qHCag7UFMoWQI5GsodqpNTyA6pjMZTHqGQANicuOg/132","duration":20999,"nickname":"May","id":"99dc181785d711e5873e272649fb1264"},{"headPicture":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJtdmS4icBfWtCJzpnlOBianxMialenibIEMAGn3Zs2cfZSq5jUtOHo2aLVM0gDF0S7wq5JPwEyOkibdCw/132","duration":20342,"nickname":"Angelina","id":"aff02d1b883911e58bee00163e000c35"}]
     * oneself : {}
     */

    private OneselfBean oneself;
    private List<RankingBean> ranking;

    public OneselfBean getOneself() {
        return oneself;
    }

    public void setOneself(OneselfBean oneself) {
        this.oneself = oneself;
    }

    public List<RankingBean> getRanking() {
        return ranking;
    }

    public void setRanking(List<RankingBean> ranking) {
        this.ranking = ranking;
    }

    public static class OneselfBean {
        private String headPicture;
        private int duration;
        private String nickname;
        private String id;

        public String getHeadPicture() {
            return headPicture;
        }

        public void setHeadPicture(String headPicture) {
            this.headPicture = headPicture;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
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

    public static class RankingBean {
        /**
         * headPicture : http://cdn.tradestudy.cn/upload/mobile/20180925/62facb5c8755a9498ada1fbb6f5c6482.png
         * duration : 103155
         * nickname : Ivy
         * id : 99607afc85d711e5873e272649fb1264
         */

        private String headPicture;
        private int duration;
        private String nickname;
        private String id;

        public String getHeadPicture() {
            return headPicture;
        }

        public void setHeadPicture(String headPicture) {
            this.headPicture = headPicture;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
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
