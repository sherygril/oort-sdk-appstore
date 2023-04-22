package com.oortcloud.basemodule.login;

import static com.oortcloud.basemodule.constant.Constant.BASE_URL;
import static com.oortcloud.basemodule.constant.Constant.GET_LOGIN_CODE;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oortcloud.basemodule.constant.Constant;
import com.oortcloud.basemodule.constant.UserConstantKey;
import com.oortcloud.basemodule.login.model.LoginRegisterResult;
import com.oortcloud.basemodule.login.model.LoginSsosign;
import com.oortcloud.basemodule.login.model.PayPrivateKey;
import com.oortcloud.basemodule.login.model.UserInfoData;
import com.oortcloud.basemodule.login.okhttp.HttpUtils;
import com.oortcloud.basemodule.login.okhttp.callback.BaseCallback;
import com.oortcloud.basemodule.login.okhttp.result.ObjectResult;
import com.oortcloud.basemodule.login.okhttp.result.Result;
import com.oortcloud.basemodule.login.secure.AES;
import com.oortcloud.basemodule.login.secure.Base64;
import com.oortcloud.basemodule.login.secure.MAC;
import com.oortcloud.basemodule.login.secure.MD5;
import com.oortcloud.basemodule.login.secure.RSA;
import com.oortcloud.basemodule.utils.DeviceInfoUtil;
import com.oortcloud.basemodule.utils.RSAUtils;
import com.oortcloud.basemodule.utils.fastsharedpreferences.FastSharedPreferences;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

public class LoginTool {


    public static String URL = "http://oort.oortcloudsmart.com:31610/";
    public static String IMURL = "http://oort.oortcloudsmart.com:31500/";
    public static String GETLODINCODEURL = URL + "oort/oortcloud-sso/ssoim/v2/getLoginCode";
    public static String GETLPKey = IMURL + "authkeys/getLoginPrivateKey";//http://oort.oortcloudsmart.com:31500/authkeys/getLoginPrivateKey
    public static String IM_V2_LOGIN = URL + "oort/oortcloud-sso/ssoim/v2/login";
    public static String GETUSERINFO = URL + "oort/oortcloud-sso/sso/v1/getUserInfo";

    private static final String DEVICE_ID = "android";
    public Context ctx;
    public static String TAG = "LoginTool";
    public static String apiKey = "DemotstTo2020";

    public interface LoginRes{
        public void loginRes(int code,String userId,String token,String data,String data1,String data2);
    }

    public static void generateHttpParam(
            Context ctx,
            Map<String, String> params,
            Boolean beforeLogin
    ) {
//        if (params.containsKey("secret")) {
//            UserSp sp = UserSp.getInstance(MyApplication.getContext());
//            String accessToken = sp.getAccessToken();
//            if (!TextUtils.isEmpty(accessToken)) {
//                params.put("access_token", accessToken);
//            }
//            return;
//        }
//        if (beforeLogin) {
//            generateBeforeLoginParam(ctx, params);
//            return;
//        }
//        CoreManager coreManager = CoreManager.getInstance(ctx);
//        if (coreManager.getSelf() == null) {
//            generateBeforeLoginParam(ctx, params);
//            return;
//        }
//        String userId = coreManager.getSelf().getUserId();
//        UserSp sp = UserSp.getInstance(ctx);
//        String accessToken = sp.getAccessToken();
//        if (accessToken == null) {
//            generateBeforeLoginParam(ctx, params);
//            return;
//        }
        String httpKey = null;//sp.getHttpKey();
        if (httpKey == null) {
            generateBeforeLoginParam(ctx, params);
            return;
        }
        String userId = params.get("userId");
        String accessToken= params.get("access_token");
        String salt = params.remove("salt");
        if (salt == null) {
            salt = String.valueOf(System.currentTimeMillis());
        }
        // 旧代码手动添加的accessToken无视，
        params.remove("access_token");
        String macContent = apiKey + userId + accessToken + Parameter.joinValues(params) + salt;
        String mac = MAC.encodeBase64(macContent.getBytes(), Base64.decode(httpKey));
        params.put("access_token", accessToken);
        params.put("salt", salt);
        params.put("secret", mac);
    }

    private static void generateBeforeLoginParam(Context ctx, Map<String, String> params) {
        String salt = params.remove("salt");
        if (salt == null) {
            salt = String.valueOf(System.currentTimeMillis());
        }
        String macContent = apiKey + Parameter.joinValues(params) + salt;
        byte[] key = MD5.encrypt(apiKey);
        String mac = MAC.encodeBase64(macContent.getBytes(), key);
        params.put("salt", salt);
        params.put("secret", mac);
    }

    public static void login(Context ctx,String account,String loginPassword,LoginRes res){


        FastSharedPreferences.init(ctx);
        byte[] key = encode(loginPassword);

        account = "999990" + account;

        Map<String, String> params = new HashMap<>();

        params.put("xmppVersion", "1");
        // 附加信息+
        params.put("model", DeviceInfoUtil.getModel());
        params.put("osVersion", DeviceInfoUtil.getOsVersion());
        params.put("serial", DeviceInfoUtil.getDeviceId(ctx));


        myGetCode(ctx, account, key,loginPassword, new LoginRes() {
            @Override
            public void loginRes(int gccode, String userId, String token, String data, String data1, String data2) {


                String encryptedCode = data;

                String ssodata = data1;
                getRsaPrivateKey(ctx, userId, key, new LoginRes() {
                    @Override
                    public void loginRes(int grcode, String userId, String token, String data, String data1, String data2) {

                        byte[] code;
                        String encryptedPrivateKey = data;
                        byte[] privateKey = AES.decryptFromBase64(encryptedPrivateKey, key);
                        try {
                            code = RSA.decryptFromBase64(encryptedCode, privateKey);
                        } catch (Exception e) {
                            Log.i(TAG, "私钥解密code失败", e);
                            requestPrivateKey(ctx, userId, key, res);
                            return;
                        }
                        String loginPasswordMd5 = md5(key);

                        String salt = createSalt();
                        String mac = MAC.encodeBase64((apiKey + userId + Parameter.joinValues(params) + salt + loginPasswordMd5).getBytes(), code);
                        JSONObject json = new JSONObject();
                        json.putAll(params);
                        json.put("mac", mac);
                        String data01 = AES.encryptBase64(json.toJSONString(), code);
                        Map<String, String> p = new HashMap<>();
                        p.put("data", data01);
                        p.put("userId", userId);
                        p.put("salt", salt);
                        p.put("ssosign",ssodata);
                        String rsacode = "";
                        String mycode = Base64.encode(code);
                        try {
                            String str = RSAUtils.encryptRSA(mycode);
                            rsacode = str.replace("\n", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        p.put("code",rsacode);

                        String url = IM_V2_LOGIN;
                        loginIM(ctx,url,code,p, new LoginRes() {
                            @Override
                            public void loginRes(int code, String userId, String token, String data, String data1, String data2) {

                                FastSharedPreferences.get("USERINFO_SAVE").edit().putString("token",token).apply();
                                getUserInfo(ctx, userId, token, new LoginRes() {
                                    @Override
                                    public void loginRes(int code, String userId, String token, String data, String data1, String data2) {

                                        res.loginRes(1,userId,token,data,"","");
                                    }
                                });


                            }
                        });
                    }
                });
            }
        });

    }




    private static void loginIM(Context ctx, String url, byte[] code, Map<String, String> p, LoginRes res) {
        p.put("deviceId", DEVICE_ID);
        HttpUtils.get().url(url)
                .params(p)
                .build(true, true)
                .executeSync(new BaseCallback<EncryptedData>(EncryptedData.class, false) {
                    @Override
                    public void onResponse(ObjectResult<EncryptedData> result) {
//                        Log.e("test result",result.getData().getData());
                        ObjectResult<LoginRegisterResult> objectResult = new ObjectResult<>();
                        objectResult.setCurrentTime(result.getCurrentTime());
                        objectResult.setResultCode(result.getResultCode());
                        objectResult.setResultMsg(result.getResultMsg());
                        if (Result.checkSuccess(ctx, result, false) && result.getData() != null && result.getData().getData() != null) {
                            String realData = decodeLoginResult(code, result.getData().getData());
                            if (realData != null) {
                                LoginRegisterResult realResult = JSON.parseObject(realData, LoginRegisterResult.class);
                                objectResult.setData(realResult);
                                res.loginRes(1, realResult.getUserId(),realResult.getAccessToken(),"","","");

                            }
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //onError.apply(e);
                    }
                });
    }

    private static void getUserInfo(Context ctx,String userId,String token, LoginRes res) {
        HashMap map = new HashMap();
        map.put("accessToken",token);
        HttpUtils.post().url(GETUSERINFO)
                .params(map)
                .build(true, true)
                .executeSync(new BaseCallback<UserInfoData>(UserInfoData.class, false) {
                    @Override
                    public void onResponse(ObjectResult<UserInfoData> result) {
//
                        if (result.getResultCode() == 0 && result.getData() != null && result.getData().getUserInfo() != null ) {

                            UserInfoData.UserInfo  u = result.getData().getUserInfo();

                            if (u != null) {


                                FastSharedPreferences sharedPreferences = FastSharedPreferences.get(Constant.LOGIN_RESPONSE);
                                sharedPreferences.edit().putString(UserConstantKey.SSO_LOGIN_RESPONSE,JSON.toJSONString(result));
                                res.loginRes(1, userId,token,u.getOort_uuid(),"","");

                            }
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //onError.apply(e);
                    }
                });
    }


    private static void myGetCode(Context ctx, String account, byte[] key,String password,LoginRes res) {
        String salt = createSalt();
        final Map<String, Object> params = new HashMap<>();
        params.put("username", account);
        params.put("password", password);
        params.put("timestamp", getSecondTimestampTwo());
        params.put("captchaID", "");
        params.put("code", "");
        params.put("client", "android");
        params.put("apiKey", apiKey);
        params.put("areaCode", "86");
        params.put("language", Locale.getDefault().getLanguage());

        String json = JSONObject.toJSONString(params); //params.toString();//new Gson().toJson(params);
        //加密后的密文
        String userInfo_key = null;

        try {
            String str = RSAUtils.encryptRSA(json);
            userInfo_key = str.replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //body
        HashMap<String, String> map = new HashMap<>();
        map.put("userInfo", userInfo_key);

        HttpUtils.post().url(GETLODINCODEURL)
                .params(map)
                .build(true, true)
                .executeSync(new BaseCallback<LoginSsosign>(LoginSsosign.class, false) {

                    @Override
                    public void onResponse(ObjectResult<LoginSsosign> result) {
                        if (Result.checkSuccess(ctx, result, false) && result.getData() != null) {
                            String userId = result.getData().getUserId();
                            if (result.getData() == null || TextUtils.isEmpty(result.getData().getCode())) {
                                // 服务器没有公钥，创建一对上传后从新调用getCode,
                                makeRsaKeyPair(ctx,  userId, key, new LoginRes() {
                                    @Override
                                    public void loginRes(int code, String userId, String token, String data,String data1,String data2) {
                                        myGetCode(ctx,account, key,password,res);
                                    }


                                });
                            } else {
                                res.loginRes(1,userId,"",result.getData().getCode(),result.getData().getSsosign(),"");

                                //onSuccess.apply(result.getData().getCode(), userId,result.getData().getSsosign());
                            }
                        } else {
                            if (result.getData() != null && !TextUtils.isEmpty(result.getData().getCaptchaID())){
                                String msg = Result.getErrorMessage(ctx, result);
                                String captchaid = result.getData().getCaptchaID();
                                String res = msg + ":"+ captchaid;
                                //onError.apply(new IllegalStateException(res));
                            }else {
                                //onError.apply(new IllegalStateException(Result.getErrorMessage(ctx, result)));
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //onError.apply(e);
                    }
                });

    }

    @NonNull
    private static String createSalt() {
        return String.valueOf(System.currentTimeMillis());
    }
    private static void getCode(Context ctx, String account, byte[] key, LoginRes res) {
        String loginPasswordMd5 = md5(key);
        String salt = createSalt();
        String mac = MAC.encodeBase64((apiKey + "" + account + salt).getBytes(), loginPasswordMd5);
        final Map<String, String> params = new HashMap<>();
        params.put("areaCode", "");
        params.put("account", account);
        params.put("mac", mac);
        params.put("salt", salt);
        params.put("deviceId", DEVICE_ID);

        HttpUtils.post().url(GETLODINCODEURL)
                .params(params)
                .build(true, true)
                .executeSync(new BaseCallback<LoginCode>(LoginCode.class, false) {

                    @Override
                    public void onResponse(ObjectResult<LoginCode> result) {
                        if (Result.checkSuccess(ctx, result, false) && result.getData() != null) {
                            String userId = result.getData().getUserId();
                            if (result.getData() == null || TextUtils.isEmpty(result.getData().getCode())) {
                                // 服务器没有公钥，创建一对上传后从新调用getCode,
                                makeRsaKeyPair(ctx, userId, key, new LoginRes() {
                                    @Override
                                    public void loginRes(int code, String userId, String token, String data,String data1,String data2) {
                                        getCode(ctx, account, key, res);
                                    }


                                });
                            } else {
                                //onSuccess.apply(result.getData().getCode(), userId);
                                res.loginRes(1,userId,"",result.getData().getCode(),"","");
                            }
                        } else {
                            //onError.apply(new IllegalStateException(Result.getErrorMessage(ctx, result)));
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //onError.apply(e);
                    }
                });

    }

    private static void getRsaPrivateKey(Context ctx, String userId, byte[] key, LoginRes res) {
        // 本地不保存密码登录私钥，每次都通过接口获取，
        requestPrivateKey(ctx, userId, key, res);
    }

    @WorkerThread
    private static void requestPrivateKey(Context ctx, String userId, byte[] key,LoginRes res) {
        String loginPasswordMd5 = md5(key);
        String salt = createSalt();
        String mac = MAC.encodeBase64((apiKey + userId + salt).getBytes(), loginPasswordMd5);
        final Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("mac", mac);
        params.put("salt", salt);

        HttpUtils.post().url(GETLPKey)
                .params(params)
                .build(true, true)
                .executeSync(new BaseCallback<PayPrivateKey>(PayPrivateKey.class, false) {

                    @Override
                    public void onResponse(ObjectResult<PayPrivateKey> result) {
                        if (Result.checkSuccess(ctx, result, false)) {
                            String encryptedPrivateKey;
                            if (result.getData() != null && !TextUtils.isEmpty(encryptedPrivateKey = result.getData().getPrivateKey())) {
                                byte[] privateKey;
                                try {
                                    privateKey = AES.decryptFromBase64(encryptedPrivateKey, key);
                                    res.loginRes(1,userId,"",result.getData().getPrivateKey(),"","");
                                } catch (Exception e) {
                                    return;
                                }
                            } else {
                            }
                        } else {
                            //onError.apply(new IllegalStateException(Result.getErrorMessage(ctx, result)));
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Exception ex = e;
                        //onError.apply(e);
                    }
                });
    }

    @WorkerThread
    private static void makeRsaKeyPair(Context ctx, String userId,
                                       byte[] key,LoginRes res) {
        String salt = createSalt();
        RSA.RsaKeyPair rsaKeyPair = RSA.genKeyPair();
        String encryptedPrivateKeyBase64 = AES.encryptBase64(rsaKeyPair.getPrivateKey(), key);
        String publicKeyBase64 = rsaKeyPair.getPublicKeyBase64();
        String macKey = md5(key);
        String macContent = apiKey + userId + encryptedPrivateKeyBase64 + publicKeyBase64 + salt;
        String mac = MAC.encodeBase64(macContent.getBytes(), macKey);
        final Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("publicKey", publicKeyBase64);
        params.put("privateKey", encryptedPrivateKeyBase64);
        params.put("salt", salt);
        params.put("mac", mac);

        HttpUtils.post().url("")
                .params(params)
                .build(true, true)
                .executeSync(new BaseCallback<Void>(Void.class, false) {

                    @Override
                    public void onResponse(ObjectResult<Void> result) {
                        if (Result.checkSuccess(ctx, result, false)) {
                           // onSuccess.apply(rsaKeyPair.getPrivateKey());
                        } else {
                           // onError.apply(new IllegalStateException(Result.getErrorMessage(ctx, result)));
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                       // onError.apply(e);
                    }
                });
    }

    public interface Function<T> {
        void apply(T t);
    }

    public interface Function2<T, R> {
        void apply(T t, R r);
    }

    public interface Function3<T, R, E> {
        void apply(T t, R r, E e);
    }

    public interface Function4<T, R, E, W> {
        void apply(T t, R r, E e, W w);
    }

    public static class LoginTokenOvertimeException extends IllegalStateException {
        public LoginTokenOvertimeException(String s) {
            super(s);
        }
    }

    //秒级时间戳
    public static int getSecondTimestampTwo(){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        String timestamp = String.valueOf(date.getTime()/1000);
        return Integer.valueOf(timestamp);
    }

    public static byte[] encode(String payPassword) {
        return MD5.encrypt(payPassword);
    }

    public static String md5(byte[] key) {
        return MD5.encryptHex(AES.encrypt(key, key));
    }


    private static String decodeLoginResult(byte[] code, String data) {
        try {
            String ret = AES.decryptStringFromBase64(data, code);
            //LogUtils.log("HTTP", "login data: " + ret);
            return ret;
        } catch (Exception e) {
            //Reporter.post("登录结果解密失败", e);
            return data;
        }
    }




    public class LoginCode {

        private String code;
        private String userId;

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
    }

    public static  class Parameter {
        public static String joinObjectValues(Map<String, ?> map) {
            TreeMap<String, String> treeMap = new TreeMap<>(String::compareTo);
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof Number) {
                    value = ((Number) value).longValue();
                }
                treeMap.put(key, value == null ? null : value.toString());
            }
            return joinSortedMap(treeMap);
        }

        public static String joinValues(Map<String, String> map) {
            TreeMap<String, String> treeMap = new TreeMap<>(String::compareTo);
            treeMap.putAll(map);
            return joinSortedMap(treeMap);
        }

        @NonNull
        public static String joinSortedMap(TreeMap<String, String> treeMap) {
            StringBuilder sb = new StringBuilder();
            for (String value : treeMap.values()) {
                sb.append(value);
            }
            return sb.toString();
        }
    }







}
