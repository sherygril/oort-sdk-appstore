package com.oortcloud.basemodule.login.model;

/**
 * 支付加固的临时密码，
 */
public class LoginSsosign {

    private String code;
    private String userId;
    private String ssosign;
    private String CaptchaID;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSsosign() {
        return ssosign;
    }

    public void setSsosign(String ssosign) {
        this.ssosign = ssosign;
    }

    public String getCaptchaID() {
        return CaptchaID;
    }

    public void setCaptchaID(String captchaID) {
        CaptchaID = captchaID;
    }
}
