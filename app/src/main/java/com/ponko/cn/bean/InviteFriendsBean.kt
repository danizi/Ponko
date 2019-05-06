package com.ponko.cn.bean

class InviteFriendsBean {

    //课程图标
    var couresResId: Int = 0
    //课程标题
    var titleDes: String? = null
    //成功邀请
    var inviteSuccessNum: Int = 0
    //获取积分数
    var integralNum: Int = 0

    /**
     * products : [{"id":"bfd7f85c6f0a11e8a93a67b75eb469f2","title":"外贸B2B课程","logo_url":"http://cdn.tradestudy.cn/api/img/invite/product_b2b_logo.png   ","success":0,"score":0,"sort":1},{"id":"c279381e6f0a11e8a93a67b75eb469f2","title":"跨境电商B2C课程","logo_url":"http://cdn.tradestudy.cn/api/img/invite/product_b2c_logo.png   ","success":0,"score":0,"sort":2}]
     * rule_title : 成为推广大使你将获得什么?
     * rule_content : 1.好友通过您的邀请二维码成功入学《外贸B2B课程》后，您将获得106积分；
     *
     * 2.好友通过您的邀请二维码成功入学《外贸B2B课程》后，您将获得106积分；
     *
     * 好友通过您的邀请二维码成功入学《外贸B2B课程》后，您将获得106积分；
     */

    private var rule_title: String? = null
    private var rule_content: String? = null
    private var products: List<ProductsBean>? = null

    fun getRule_title(): String? {
        return rule_title
    }

    fun setRule_title(rule_title: String) {
        this.rule_title = rule_title
    }

    fun getRule_content(): String? {
        return rule_content
    }

    fun setRule_content(rule_content: String) {
        this.rule_content = rule_content
    }

    fun getProducts(): List<ProductsBean>? {
        return products
    }

    fun setProducts(products: List<ProductsBean>) {
        this.products = products
    }

    class ProductsBean {
        /**
         * id : bfd7f85c6f0a11e8a93a67b75eb469f2
         * title : 外贸B2B课程
         * logo_url : http://cdn.tradestudy.cn/api/img/invite/product_b2b_logo.png
         * success : 0
         * score : 0
         * sort : 1
         */

        var id: String? = null
        var title: String? = null
        var logo_url: String? = null
        var success: Int = 0
        var score: Int = 0
        var sort: Int = 0
    }
}