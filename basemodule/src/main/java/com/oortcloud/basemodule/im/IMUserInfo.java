package com.oortcloud.basemodule.im;

/**
 * @filename:
 * @function：获取IM用户信息
 * @version： v1.0
 * @author: zhangzhijun/@date: 2020/6/11 18:34
 */
public class IMUserInfo {


    private String userId;
    private String accessToken;
    private String httpKey;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getHttpKey() {
        return httpKey;
    }

    public void setHttpKey(String httpKey) {
        this.httpKey = httpKey;
    }

}
