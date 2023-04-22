package com.oortcloud.basemodule.im;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.oortcloud.basemodule.constant.Constant;
import com.oortcloud.basemodule.constant.UserConstantKey;
import com.oortcloud.basemodule.utils.fastsharedpreferences.FastSharedPreferences;

/**
 * @filename:
 * @function：获取IM用户信息工具类
 * @version： v1.0
 * @author: zhangzhijun/@date: 2020/6/11 18:24
 */
public class IMUserInfoUtil {
    private static IMUserInfoUtil mOmmUserInfoUtil;

    private IMUserInfo mIMUserInfo;
    private IMUserInfoUtil(){

    }
    public static IMUserInfoUtil getInstance(){
        if (mOmmUserInfoUtil == null){
            synchronized (IMUserInfoUtil.class){
                if (mOmmUserInfoUtil == null){
                    mOmmUserInfoUtil  =  new IMUserInfoUtil();
                }
                return mOmmUserInfoUtil;
            }
        }
        return mOmmUserInfoUtil;
    }
    private IMUserInfo getOmmUserInfo(){
        String ommUserInfoJson = FastSharedPreferences.get(Constant.LOGIN_RESPONSE).getString(UserConstantKey.IM_USER_INFO_SAVE, "");
        if (!TextUtils.isEmpty(ommUserInfoJson)){
            mIMUserInfo = JSON.parseObject(ommUserInfoJson, IMUserInfo.class);
        }
        return mIMUserInfo;

    }
    public String getToken(){

        return getOmmUserInfo() !=null ? mIMUserInfo.getAccessToken() : "";
    }

    public String getHttpKey(){
        return getOmmUserInfo() !=null ? mIMUserInfo.getHttpKey() : "";
    }
    public String getUserId(){
        return getOmmUserInfo() !=null ? mIMUserInfo.getUserId() : "";
    }

}
