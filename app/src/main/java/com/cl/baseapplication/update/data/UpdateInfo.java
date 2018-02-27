package com.cl.baseapplication.update.data;

import java.io.Serializable;

/**
 * com.blueooo.miao.module.update.data in blueooo
 * Created by zhangdonghai on 2017/8/15
 */

public class UpdateInfo implements Serializable {


    /**
     * version : 1.0.0
     * url : http://211.159.149.181/app.apk
     * force : 1
     * content : 这里是软件说明
     */

    private String version;
    private String url;
    private String force;
    private String content;
    private String title;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
