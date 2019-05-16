package com.ponko.cn.bean;

import java.util.List;

public class VideoInfoCBean {

    /**
     * code : 200
     * status : success
     * message : success
     * data : [{"images_b":["26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_4_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_5_b.jpg"],"images":["26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_4.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_5.jpg"],"imageUrls":["http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_4.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_5.jpg"],"tag":"标签","mp4":"http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.mp4","title":"1.11 致迷茫的外贸销售员 该选择什么样的公司-1.mp4","df":3,"times":"13432","vid":"26de49f8c22abafd8adc1b49246262c6_2","mp4_1":"http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.mp4","mp4_2":"http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.mp4","mp4_3":"http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.mp4","cataid":"1445515794920","swf_link":"http://player.polyv.net/videos/26de49f8c22abafd8adc1b49246262c6_2.swf","status":"60","seed":1,"flv1":"http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.plv","flv2":"http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.plv","flv3":"http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.plv","sourcefile":"","playerwidth":"600","default_video":"http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.plv","duration":"00:12:21","first_image":"http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0.jpg","original_definition":"1920x1080","context":"描述","playerheight":"337","ptime":"2015-11-03 11:55:49","source_filesize":126717500,"filesize":[31280255,54891832,117855820],"md5checksum":"41e9843765e5c9edaa99092460cb85bb","hls":["http://hls.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.m3u8","http://hls.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.m3u8","http://hls.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.m3u8"],"tsfilesize1":"35351696","tsfilesize2":"0","tsfilesize3":"0","previewVid":"u23kc79n5m22lplnk5lkm1p79273232m3_2"}]
     */

    private int code;
    private String status;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VideoInfoCBean{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * images_b : ["26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_4_b.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_5_b.jpg"]
         * images : ["26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_4.jpg","26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_5.jpg"]
         * imageUrls : ["http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_4.jpg","http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_5.jpg"]
         * tag : 标签
         * mp4 : http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.mp4
         * title : 1.11 致迷茫的外贸销售员 该选择什么样的公司-1.mp4
         * df : 3
         * times : 13432
         * vid : 26de49f8c22abafd8adc1b49246262c6_2
         * mp4_1 : http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.mp4
         * mp4_2 : http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.mp4
         * mp4_3 : http://mpv.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.mp4
         * cataid : 1445515794920
         * swf_link : http://player.polyv.net/videos/26de49f8c22abafd8adc1b49246262c6_2.swf
         * status : 60
         * seed : 1
         * flv1 : http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.plv
         * flv2 : http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.plv
         * flv3 : http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.plv
         * sourcefile :
         * playerwidth : 600
         * default_video : http://plvod01.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.plv
         * duration : 00:12:21
         * first_image : http://img.videocc.net/uimage/2/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_0.jpg
         * original_definition : 1920x1080
         * context : 描述
         * playerheight : 337
         * ptime : 2015-11-03 11:55:49
         * source_filesize : 126717500
         * filesize : [31280255,54891832,117855820]
         * md5checksum : 41e9843765e5c9edaa99092460cb85bb
         * hls : ["http://hls.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.m3u8","http://hls.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_2.m3u8","http://hls.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_3.m3u8"]
         * tsfilesize1 : 35351696
         * tsfilesize2 : 0
         * tsfilesize3 : 0
         * previewVid : u23kc79n5m22lplnk5lkm1p79273232m3_2
         */

        private String tag;
        private String mp4;
        private String title;
        private int df;
        private String times;
        private String vid;
        private String mp4_1;
        private String mp4_2;
        private String mp4_3;
        private String cataid;
        private String swf_link;
        private String status;
        private int seed;
        private String flv1;
        private String flv2;
        private String flv3;
        private String sourcefile;
        private String playerwidth;
        private String default_video;
        private String duration;
        private String first_image;
        private String original_definition;
        private String context;
        private String playerheight;
        private String ptime;
        private int source_filesize;
        private String md5checksum;
        private String tsfilesize1;
        private String tsfilesize2;
        private String tsfilesize3;
        private String previewVid;
        private List<String> images_b;
        private List<String> images;
        private List<String> imageUrls;
        private List<Integer> filesize;
        private List<String> hls;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getMp4() {
            return mp4;
        }

        public void setMp4(String mp4) {
            this.mp4 = mp4;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDf() {
            return df;
        }

        public void setDf(int df) {
            this.df = df;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getMp4_1() {
            return mp4_1;
        }

        public void setMp4_1(String mp4_1) {
            this.mp4_1 = mp4_1;
        }

        public String getMp4_2() {
            return mp4_2;
        }

        public void setMp4_2(String mp4_2) {
            this.mp4_2 = mp4_2;
        }

        public String getMp4_3() {
            return mp4_3;
        }

        public void setMp4_3(String mp4_3) {
            this.mp4_3 = mp4_3;
        }

        public String getCataid() {
            return cataid;
        }

        public void setCataid(String cataid) {
            this.cataid = cataid;
        }

        public String getSwf_link() {
            return swf_link;
        }

        public void setSwf_link(String swf_link) {
            this.swf_link = swf_link;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getSeed() {
            return seed;
        }

        public void setSeed(int seed) {
            this.seed = seed;
        }

        public String getFlv1() {
            return flv1;
        }

        public void setFlv1(String flv1) {
            this.flv1 = flv1;
        }

        public String getFlv2() {
            return flv2;
        }

        public void setFlv2(String flv2) {
            this.flv2 = flv2;
        }

        public String getFlv3() {
            return flv3;
        }

        public void setFlv3(String flv3) {
            this.flv3 = flv3;
        }

        public String getSourcefile() {
            return sourcefile;
        }

        public void setSourcefile(String sourcefile) {
            this.sourcefile = sourcefile;
        }

        public String getPlayerwidth() {
            return playerwidth;
        }

        public void setPlayerwidth(String playerwidth) {
            this.playerwidth = playerwidth;
        }

        public String getDefault_video() {
            return default_video;
        }

        public void setDefault_video(String default_video) {
            this.default_video = default_video;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFirst_image() {
            return first_image;
        }

        public void setFirst_image(String first_image) {
            this.first_image = first_image;
        }

        public String getOriginal_definition() {
            return original_definition;
        }

        public void setOriginal_definition(String original_definition) {
            this.original_definition = original_definition;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getPlayerheight() {
            return playerheight;
        }

        public void setPlayerheight(String playerheight) {
            this.playerheight = playerheight;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        public int getSource_filesize() {
            return source_filesize;
        }

        public void setSource_filesize(int source_filesize) {
            this.source_filesize = source_filesize;
        }

        public String getMd5checksum() {
            return md5checksum;
        }

        public void setMd5checksum(String md5checksum) {
            this.md5checksum = md5checksum;
        }

        public String getTsfilesize1() {
            return tsfilesize1;
        }

        public void setTsfilesize1(String tsfilesize1) {
            this.tsfilesize1 = tsfilesize1;
        }

        public String getTsfilesize2() {
            return tsfilesize2;
        }

        public void setTsfilesize2(String tsfilesize2) {
            this.tsfilesize2 = tsfilesize2;
        }

        public String getTsfilesize3() {
            return tsfilesize3;
        }

        public void setTsfilesize3(String tsfilesize3) {
            this.tsfilesize3 = tsfilesize3;
        }

        public String getPreviewVid() {
            return previewVid;
        }

        public void setPreviewVid(String previewVid) {
            this.previewVid = previewVid;
        }

        public List<String> getImages_b() {
            return images_b;
        }

        public void setImages_b(List<String> images_b) {
            this.images_b = images_b;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        public List<Integer> getFilesize() {
            return filesize;
        }

        public void setFilesize(List<Integer> filesize) {
            this.filesize = filesize;
        }

        public List<String> getHls() {
            return hls;
        }

        public void setHls(List<String> hls) {
            this.hls = hls;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "tag='" + tag + '\'' +
                    ", mp4='" + mp4 + '\'' +
                    ", title='" + title + '\'' +
                    ", df=" + df +
                    ", times='" + times + '\'' +
                    ", vid='" + vid + '\'' +
                    ", mp4_1='" + mp4_1 + '\'' +
                    ", mp4_2='" + mp4_2 + '\'' +
                    ", mp4_3='" + mp4_3 + '\'' +
                    ", cataid='" + cataid + '\'' +
                    ", swf_link='" + swf_link + '\'' +
                    ", status='" + status + '\'' +
                    ", seed=" + seed +
                    ", flv1='" + flv1 + '\'' +
                    ", flv2='" + flv2 + '\'' +
                    ", flv3='" + flv3 + '\'' +
                    ", sourcefile='" + sourcefile + '\'' +
                    ", playerwidth='" + playerwidth + '\'' +
                    ", default_video='" + default_video + '\'' +
                    ", duration='" + duration + '\'' +
                    ", first_image='" + first_image + '\'' +
                    ", original_definition='" + original_definition + '\'' +
                    ", context='" + context + '\'' +
                    ", playerheight='" + playerheight + '\'' +
                    ", ptime='" + ptime + '\'' +
                    ", source_filesize=" + source_filesize +
                    ", md5checksum='" + md5checksum + '\'' +
                    ", tsfilesize1='" + tsfilesize1 + '\'' +
                    ", tsfilesize2='" + tsfilesize2 + '\'' +
                    ", tsfilesize3='" + tsfilesize3 + '\'' +
                    ", previewVid='" + previewVid + '\'' +
                    ", images_b=" + images_b +
                    ", images=" + images +
                    ", imageUrls=" + imageUrls +
                    ", filesize=" + filesize +
                    ", hls=" + hls +
                    '}';
        }
    }
}
