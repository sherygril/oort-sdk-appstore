package com.oortcloud.basemodule.im;

public class AppUseInfo {

    public static String accessToken = "";   //
    public static String sn = ""; //手机sn号
    //应用属性
    public static String app_code;   //应用编码
    public static String app_name;   //应用名称
    public static String app_version; //应用版本号

    private volatile static AppUseInfo appUseInfo;

    private AppUseInfo(){

    }
    public static AppUseInfo getInstance() {
        if (appUseInfo == null) {
            synchronized (AppUseInfo.class) {
                if (appUseInfo == null) {
                    appUseInfo = new AppUseInfo();
                }
            }
        }
        return appUseInfo;

    }
}
