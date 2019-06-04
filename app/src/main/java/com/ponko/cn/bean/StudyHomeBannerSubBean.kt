package com.ponko.cn.bean

class StudyHomeBannerSubBean {
    var images = ArrayList<String>()

    init {
        val url = "http://img3.imgtn.bdimg.com/it/u=3322681947,235183590&fm=26&gp=0.jpg"
        val url2 = "http://img2.imgtn.bdimg.com/it/u=607145006,1780286760&fm=26&gp=0.jpg"
        val url3 = "http://img3.imgtn.bdimg.com/it/u=1637419018,2173218689&fm=26&gp=0.jpg"
        images.add(url)
        images.add(url2)
        images.add(url3)
    }
}