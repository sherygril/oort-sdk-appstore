package com.oortcloud.basemodule.im;

public class TaskMsgInfoBean {
    public String title ;
    public String sub ;
    public String img ;
    public String url ;
    public String appid ;
    public String param ;
    public long time;
    public String name ;

    public TaskMsgInfoBean(String title, String sub, String img, String url, String appid, String param, long time, String name) {
        this.title = title;
        this.sub = sub;
        this.img = img;
        this.url = url;
        this.appid = appid;
        this.param = param;
        this.time = time;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }


}
