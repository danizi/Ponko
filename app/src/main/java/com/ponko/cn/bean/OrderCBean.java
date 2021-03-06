package com.ponko.cn.bean;

import com.google.gson.annotations.SerializedName;

public class OrderCBean {

    /**
     * wechat : {"package":"Sign=WXPay","appid":"wxd37fb8ce51a02360","sign":"A1C433367FC6FA1B279DF25579235CC1","partnerid":"1507283191","prepayid":"wx091153404274087ebf63570c2172257868","noncestr":"61acc180b95a42eb8d7822032fc354a9","timestamp":"1557374020"}
     * key : 1905091153400045988
     */

    private WechatBean wechat;
    private String key;

    /**
     * alipay : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018010901719722&biz_content=%7B%22body%22%3A%22%E5%B8%AE%E8%AF%BE%E5%A4%A7%E5%AD%A6vip%E6%9C%8D%E5%8A%A11%E5%B9%B4%22%2C%22out_trade_no%22%3A%221905091156300918859%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%B8%AE%E8%AF%BE%E5%A4%A7%E5%AD%A6vip%E6%9C%8D%E5%8A%A11%E5%B9%B4%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.1%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.tradestudy.cn%2Fv3%2Fnotify%2Fpay%2Falipay&sign=QBFeCXgw7Z1nSC9%2FZrqfrAQx5dm6J%2Bo7gGulrpNwOSe%2B7O2cjzb%2FH2AaLzckO3ZUckbrLMWyEIxc4RMvPjeUygIeknRa%2BshPjckckh%2FFD7UoypvTVS5X9U%2FfJEy1FP%2FK1vV9v89iJ6xaPhYuwLSImJRhlNYv6nEODO6Ypa1W2oDr9M2UV%2FmenvhHyEdqovRmUAVmJzYeSAmx7V%2FDy91ac%2FYT6NisExhsw6cowJgnnYizZsvkJERW1F5%2FvsUa6M23L8Az0RIuqRNgQBEU6EQb%2Fvv174Y%2FK0StCBzbWDXBNpsb1cJNGyI8YQEIJHSX6GZ4TRf83Tf9ZTrIa64EKatgIA%3D%3D&sign_type=RSA2&timestamp=2019-05-09+11%3A56%3A30&version=1.0
     */

    private String alipay;

    public WechatBean getWechat() {
        return wechat;
    }

    public void setWechat(WechatBean wechat) {
        this.wechat = wechat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public static class WechatBean {
        /**
         * package : Sign=WXPay
         * appid : wxd37fb8ce51a02360
         * sign : A1C433367FC6FA1B279DF25579235CC1
         * partnerid : 1507283191
         * prepayid : wx091153404274087ebf63570c2172257868
         * noncestr : 61acc180b95a42eb8d7822032fc354a9
         * timestamp : 1557374020
         */

        @SerializedName("package")
        private String packageX;
        private String appid;
        private String sign;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "WechatBean{" +
                    "packageX='" + packageX + '\'' +
                    ", appid='" + appid + '\'' +
                    ", sign='" + sign + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", noncestr='" + noncestr + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderCBean{" +
                "wechat=" + wechat +
                ", key='" + key + '\'' +
                ", alipay='" + alipay + '\'' +
                '}';
    }
}
