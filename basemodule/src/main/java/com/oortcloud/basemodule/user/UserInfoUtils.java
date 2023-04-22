package com.oortcloud.basemodule.user;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.oortcloud.basemodule.constant.Constant;
import com.oortcloud.basemodule.constant.UserConstantKey;
import com.oortcloud.basemodule.utils.fastsharedpreferences.FastSharedPreferences;

/**
 * @filename: userInfoUtil.java
 * @function： 只适用于登录的接口,该接口Header不能传值accessToken,请注意使用
 * @version：
 * @author: zhangzhijun
 * @date: 2019/11/4 12:01
 */

public class UserInfoUtils {

    private Context mContext;
    private static UserInfoUtils mInstance;

    private UserInfoUtils(Context context) {
        mContext = context.getApplicationContext();
    }

    public static UserInfoUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UserInfoUtils.class) {
                if (mInstance == null) {
                    mInstance = new UserInfoUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取 name
     *
     * @return name 返回空说明没有登录
     */
    public String getUserName() {
        if (getLoginUserInfo() == null) return "";
        String loginUserName = getLoginUserInfo().getOort_name();
        return TextUtils.isEmpty(loginUserName) ? "" : loginUserName;
    }

    /**
     * 获取 userId
     *
     * @return id 返回空说明没有登录
     */
    public String getUserId() {
        if (getLoginResponse() == null) return null;
        UserInfo userInfo = getLoginResponse().getData().getUserInfo();
        return userInfo.getOort_uuid();
    }

    public String getLoginId() {
        if (getLoginUserInfo() == null) return "";
        UserInfo userInfo = getLoginResponse().getData().getUserInfo();
        String loginId = userInfo.getOort_loginid();
        return TextUtils.isEmpty(loginId) ? "" : loginId;
    }


    /**
     * 获取 登录用户信息,这里不包含userCode
     *
     * @return LoginUserInfo 返回空说明没有登录
     */
    public UserInfo getLoginUserInfo() {
        if (getLoginResponse() == null) return null;
        UserInfo userInfo = getLoginResponse().getData().getUserInfo();
        return userInfo;
    }

    /**
     * 获取登录返回的JSON
     *
     * @return LoginResponse
     */
    private LoginResponse getLoginResponse() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(Constant.LOGIN_RESPONSE);
        String response = sharedPreferences.getString(UserConstantKey.SSO_LOGIN_RESPONSE,"");
        return TextUtils.isEmpty(response) ? null : JSON.parseObject(response, LoginResponse.class);
    }

    /**
     * 删除登录信息
     */
    public void deleteUserInfo() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(Constant.LOGIN_RESPONSE);
        sharedPreferences.edit().putString(UserConstantKey.SSO_LOGIN_RESPONSE,setLoginRespinse());
    }


    public String getIdCard() {
        if (getLoginUserInfo() == null) return "";
        String id_card = getLoginUserInfo().getOort_idcard();
        return TextUtils.isEmpty(id_card) ? "" : id_card;
    }


    private String setLoginRespinse(){


        UserInfo mLoginUserInfo = new UserInfo();
        mLoginUserInfo.setOort_uuid("");

        mLoginUserInfo.setOort_idcard("");

        mLoginUserInfo.setOort_name("");
        mLoginUserInfo.setOort_loginid("");

        Data data = new Data();
        data.setUserInfo(mLoginUserInfo);

        LoginResponse mLoginResponse = new LoginResponse();
        mLoginResponse.setResultCode(200);
        mLoginResponse.setResultMsg("退出登录数据");
        mLoginResponse.setData(data);

        return JSON.toJSONString(mLoginResponse);
    }

}
