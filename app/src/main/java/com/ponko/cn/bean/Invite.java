package com.ponko.cn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Invite {


    /**
     * saying : [{"id":"0f48ce177fa344f6b847eecf4b153e43","saying":"生活不止眼前的苟且，还有永远读不懂的诗和到不了的远方"},{"id":"61851181d4574b47b0dd026f25b08830","saying":"明明可以靠脸吃饭，你却要靠才华，这就是你跟明明的差距"},{"id":"b91e3100cc1d430296479ab4d20b9db8","saying":"失败并不可怕，可怕的是你还相信这句话"},{"id":"2ceb1ceff45445738d9cf6a64c542ca2","saying":"善良没用，你得漂亮。"},{"id":"535603e2426940d98342d3435b5c5c5f","saying":"有些人不是赢在了起跑线上，而是直接生在了终点。"}]
     * materials : [{"image":"http://cdn.tradestudy.cn/upload/product/Background/1.jpg","id":"793af46f7d10497d9c509a8e5daaab2c"},{"image":"http://cdn.tradestudy.cn/upload/product/Background/2.jpg","id":"921a474f4eb34d0d8cb83882a22bb9b5"},{"image":"http://cdn.tradestudy.cn/upload/product/Background/3.jpg","id":"759cab3cadaa462c9c306a219c564fed"},{"image":"http://cdn.tradestudy.cn/upload/product/Background/4.jpg","id":"375ab9989cbf43fe91d242530b7b7c62"},{"image":"http://cdn.tradestudy.cn/upload/product/Background/5.jpg","id":"759e0e53f3654113a16fd786fb2ba7dc"},{"image":"http://cdn.tradestudy.cn/upload/product/Background/6.jpg","id":"20db922ae1324d03873d2f46cd8acfb0"}]
     * qrcode : http://cdn.tradestudy.cn/upload/mobile/20180124/2ff79795929d175b2fc0160b48a8aaa1.jpg
     * templates : [{"image":"http://cdn.tradestudy.cn/api/img/poster/poster1.jpg","custom":false,"id":"692d87b05a424db39dcf9a44ff554fd4","saying":null,"url":null},{"image":"http://cdn.tradestudy.cn/api/img/poster/poster2.jpg","custom":false,"id":"b3b1edb2985c4440a84ee198e05ab3d9","saying":null,"url":null},{"image":"http://cdn.tradestudy.cn/api/img/poster/poster3.jpg","custom":false,"id":"83baf50fe49e45e3b58782c1fcc75533","saying":null,"url":null},{"image":"http://cdn.tradestudy.cn/api/img/poster/poster4.jpg","custom":false,"id":"83baf50fe49e45e3b58782c1fcc75534","saying":null,"url":null}]
     */

    private String qrcode;
    private ArrayList<Saying> saying;
    private ArrayList<Material> materials;

    private ArrayList<Template> templates;
    private String course_name;
    private String nickname;
    private String qr_url;
    private String share_title;
    private String share_description;
    private String share_link;
    private String productId;

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public void setSaying(ArrayList<Saying> saying) {
        this.saying = saying;
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<Material> materials) {
        this.materials = materials;
    }

    public ArrayList<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(ArrayList<Template> templates) {
        this.templates = templates;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_description() {
        return share_description;
    }

    public void setShare_description(String share_description) {
        this.share_description = share_description;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQrcode() {
        return qrcode;
    }


    public ArrayList<Saying> getSaying() {
        return saying;
    }


    public ArrayList<Material> getMaterial() {
        return materials;
    }

    public ArrayList<Template> getTemplate() {
        return templates;
    }


    public static class Saying implements Parcelable {
        /**
         * id : 0f48ce177fa344f6b847eecf4b153e43
         * saying : 生活不止眼前的苟且，还有永远读不懂的诗和到不了的远方
         */

        private String id;
        private String saying;
        private boolean isChecked;


        protected Saying(Parcel in) {
            id = in.readString();
            saying = in.readString();
            isChecked = in.readByte() != 0;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public static final Creator<Saying> CREATOR = new Creator<Saying>() {
            @Override
            public Saying createFromParcel(Parcel in) {
                return new Saying(in);
            }

            @Override
            public Saying[] newArray(int size) {
                return new Saying[size];
            }
        };

        public String getId() {
            return id;
        }

        public String getSaying() {
            return saying;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(saying);
            dest.writeByte((byte) (isChecked ? 1 : 0));

        }
    }

    public static class Material implements Parcelable {
        /**
         * image : http://cdn.tradestudy.cn/upload/product/Background/1.jpg
         * id : 793af46f7d10497d9c509a8e5daaab2c
         */

        private String image;
        private String id;
        private boolean isSelector;

        protected Material(Parcel in) {
            image = in.readString();
            id = in.readString();
        }

        public static final Creator<Material> CREATOR = new Creator<Material>() {
            @Override
            public Material createFromParcel(Parcel in) {
                return new Material(in);
            }

            @Override
            public Material[] newArray(int size) {
                return new Material[size];
            }
        };

        public void setSelector(boolean selector) {
            isSelector = selector;
        }

        public boolean isSelector() {
            return isSelector;
        }

        public String getImage() {
            return image;
        }

        public String getId() {
            return id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(image);
            dest.writeString(id);
        }
    }

    public static class Template extends Material implements Parcelable {
        /**
         * image : http://cdn.tradestudy.cn/api/img/poster/poster1.jpg
         * custom : false
         * id : 692d87b05a424db39dcf9a44ff554fd4
         * saying : null
         * url : null
         */
        private String full_url;
        private String bg_url;
        private String course_name;
        private String nickname;
        private String qr_url;

        private boolean custom;
        private String saying;
        private String url;

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getQr_url() {
            return qr_url;
        }

        public void setQr_url(String qr_url) {
            this.qr_url = qr_url;
        }

        protected Template(Parcel in) {
            super(in);
            saying = in.readString();
            url = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(saying);
            dest.writeString(url);
        }

        public boolean isCustom() {
            return custom;
        }

        public void setCustom(boolean custom) {
            this.custom = custom;
        }


        public Object getSaying() {
            return saying;
        }


        public String getUrl() {
            return url;
        }

        public static final Creator<Template> CREATOR = new Creator<Template>() {
            @Override
            public Template createFromParcel(Parcel in) {
                return new Template(in);
            }

            @Override
            public Template[] newArray(int size) {
                return new Template[size];
            }
        };

        public String getFull_url() {
            return full_url;
        }

        public void setFull_url(String full_url) {
            this.full_url = full_url;
        }

        public String getBg_url() {
            return bg_url;
        }

        public void setBg_url(String bg_url) {
            this.bg_url = bg_url;
        }

        public void setSaying(String saying) {
            this.saying = saying;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
