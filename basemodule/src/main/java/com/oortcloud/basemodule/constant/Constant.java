package com.oortcloud.basemodule.constant;

import android.os.Environment;

public class Constant {

    //调试的黑历史记录存储sp 名字
    public static String HISTORY_RECORD = "DEBUG_HISTORY";
    //轻应用配置信息存储sp 名字
    public static String LOCAL_APP_CONFIG = "LOCAL_APP_CONFIG";
    //登录信息存储
    public static String LOGIN_RESPONSE = "LOGIN_RESPONSE";
    //应用存储地址
    public static final String BASE_PATH= "file:///android_asset/";
    //本地存储根路径
    public static final String LOCAL_PATH= Environment.getExternalStorageDirectory().getAbsolutePath();

    //获取ip
    public static final String GET_IP = "http://www.myoumuamua.com/";
    //
    public static final String IP_PARAM = "public-api/api/v1/getip?domain=oort.oortcloudsmart.com";
    //动态IP
   // public static String BASE_IP = "http://192.168.88.161";
    //服务网关地址

//    public static final String BASE_URL= "http://192.168.88.119:31610/";

    //任务中心的公众号userid
    public static String PUBLIC_USERID = "10000270";
    //任务中心的消息数量
    public static int PUBLIC_NUM = 3;

    //是否手动配置服务
    public static boolean IF_CONFIG_APP_SERVICE = true;
    //服务网关地址 + 端口


    public static String BASE_IP = "http://oort.oortcloudsmart.com";//"http://lyra.oortcloudsmart.com";//"http://lyra.oortcloudsmart.com";//"http://oort.oortcloudsmart.com";//p://192.168.88.119";//"http://lyra.oortcloudsmart.com";

    public static  String BASE_URL = BASE_IP + ":31610/";//":42610/";//":31610/";//"":42610/";

    public static  String IM_API_BASE = BASE_IP + ":31500/";//":41600/";//:31500/";
    public static  String IM_CONFIG_URL= IM_API_BASE + "config";

    public static  String IM_API_KEY = "VICMdiLyra2020";//"VICMdiLyra2020";// "DemotstTo2020"; xmpp端口
    //XMPPDomain // 虚拟域名
    public static  String IM_XM_PP_DO_MAIN = "im.oortcloud.com";

    //内网没有域名服务时劫持域名指定ip，外网不用
    public static boolean IS_USE_DOMAIN = false;
    //域名劫持时SSL证书使用的域名
    public static String SSL_DOMAIN = "oort.oortcloudsmart.com";
    //域名劫持时ssl域名对应的ip
    public static byte[] DOMAIN_ADDR = {(byte)113,(byte)116,(byte)135,(byte)190};
    //IM配置

    public static  int IM_XM_PP_PORT = 31501; //xmpp端口
    //文件上传配置
    public static String IM_UPLOAD_URL = BASE_IP + ":31505/";
    //文件头像下载服务
    public static  String IM_DOWN_URL = BASE_IP + ":31520/";
    //视频会议服务配置
    public static String IM_JIT_SI_URL = "https://" + SSL_DOMAIN + ":14443/";
    //直播服务上传配置
    public static String IM_LIVE_URL =  BASE_IP.replace("http" , "rtmp") + ":1935/live/";

    
    //一张图视频
//    http://map.oort.oortcloudsmart.com:32610/oort/flv/live/10000083_1615283724.flv
    public static final String VIDEO_URL= "http://map.oort.oortcloudsmart.com:32610/oort/flv/";

    //用户登录
    public static final String LOGIN_SERVICE = "oort/oortcloud-sso/ssoim/v1/login";

    public static final String GET_LOGIN_CODE = "oort/oortcloud-sso/ssoim/v2/getLoginCode";
    public static final String NEW_LOGIN = "oort/oortcloud-sso/ssoim/v2/login";
    //自动登录
    public static final String AUTOLOGIN_SERVICE = "oort/oortcloud-sso/ssoim/v1/autoLogin";
    public static final String AUTOLOGIN_API = "oort/oortcloud-sso/ssoim/v2/autologin";

    //请求类型
    public static final String REQUEST_TYPE = "zuul";

    //警力上报
//    public static final String POLICE_REPORT = "oort/oortcloud-memsql-tools/trace_track/v1/editpolice";
    public static final String POLICE_REPORT = "oort/oortcloud-memsql-tools/trace_track/v1/editvideoterminal";
    //更新轨迹信息
//    public static final String POLICE_LOCATION = "oort/oortcloud-memsql-tools/trace_track/v1/addtrack";
    public static final String POLICE_LOCATION = "oort/oortcloud-memsql-tools/trace_track/v1/reporttrack";
    //文件上传
    public static  final String UPLOAD_FILE = "oort/oortcloud-fastdfsservice/fastdfs/v1/uploadFile";
    //首页地址
    //奥陌陌
//    public static final String HOME_PAGE = "http://oort.oortcloudsmart.com:32610/oort/oortcloud-frontservice/aomomo_home_cetc/index.html#/";
    //华为
//    public static final String HOME_PAGE = "http://oort.oortcloudsmart.com:32610/oort/oortcloud-frontservice/aomomo_home_huawei/index.html#/";
    //中电科
//    public static final String HOME_PAGE = "http://oort.oortcloudsmart.com:32610/oort/oortcloud-frontservice/aomomo_home_huawei_new/index.html#/";
    //筑泰
//    public static final String HOME_PAGE = "http://oort.oortcloudsmart.com:32610/oort/oortcloud-frontservice/aomomo_home_zt/index.html#/";
    //用户登出
    public static final String LOGOUT_SERVICE = "oort/oortcloud-sso/ssoim/v1/logout";
    //修改密码
    public static final String RESET_PASSWORD = "oort/oortcloud-sso/ssoim/v1/resetPassword";
    public static final String RESET_PASSWORD_V2 = "oort/oortcloud-sso/ssoim/v2/resetPassword";
    //获取用户详细信息接口
    public static final String GET_USER_INFO = "oort/oortcloud-sso/sso/v1/getUserInfo";

    //用户登录账号密码 加密公钥
    public static final String PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAwQ9LMVuWA26f+pc4cyiZbZRY+XzJ6B6sC9ZHRU5x3C8g5Cu1MlpZ3v8baD8r+aZOE9t5NnmSLFqcVIlO3DY+bX03188m59zZqWmLhzdKPWJ2ibH4AjCHH0OlJFUIew3qzXOdZw4nk+nBmrRV7XeU7a/K4SYI7bKQg2hn4N9giKdSztvZcjO21ZS2/JiQQfSh7vZDWMsU9RH7MGAkaSkmcOmM4TVA5ponhinnpcf2cJBs94hJgFjC3JagnzqpD8ZPpPG37Ozjz3sG1iOVtC3SSh7Ejxxm75N0wjSpcVmzIitUqOrEiVyo8XoALsGUW24oLBW+LLUGZ/TxwRgHiFSLe5gTaTM+wZNZFK31lyJiZv1HYSRMzmN5SgSp5kh/8pRW42T8mPcSx6NrvZXN3BZKdjkOJ4/eEAY8PlgwKs3vF0DQt5TPrnJIuOo5RIhtbojofe6tFCukr2Fv3k6lPFTbqWRVyK0SVYRAk+V+VLEyj5bouX1gCDvh2evP4+/4/ZGHty04gGHlWWClcjo7iUP9EeWo1IftyuD4fPtFl8sPm/By+/vz3/meavzWEjxL28kOSpTJWIVC2UeVgjMS/0e0s5DllJI3jtAG6AhQTNYrQTtJbtc7SFY6SYptZ+LLZ8kn2pAA1bZUOUCuCnDICLiglEFpPrSPQlWJyzN3WvU4bU0CAwEAAQ==";

    //验证登录
    public static final String LOGIN_CAPTCHA = "oort/oortcloud-sso/sso/v1/login";
    //验证码图片
    public static final String CODE_IMAGE = BASE_URL+"oort/oortcloud-sso/captcha/v1/";

    //检查是否需要验证码接口
    public static final String GET_CAPTCHA = "oort/oortcloud-sso/sso/v1/getCaptcha";
    //发送手机验证码
    public static final String SEND_SMS_CODE = "oort/oortcloud-sso/sso/v1/sendsmscode";
    //注册接口
    public static final String REGISTER = "oort/oortcloud-sso/sso/v1/register";
    //增加sso头像 姓名上传
    public static final String SET_USER_INFO = "oort/oortcloud-sso/sso/v1/setUserInfo";
    public static final String HOME_PAGE = "http://oort.oortcloudsmart.com:32610/oort/oortcloud-frontservice/aomomo_home/index.html#/";

    //会议api
    public static final String MEETING_API = "oort/oortcloud-meeting";
    //会议详情
    public static final String MEETING_DETAIL = MEETING_API +"/v1/detail";
    //会议开启/关闭
    public static final String MEETING_OPEN = MEETING_API +"/v1/open";
    //获取会议列表
    public static final String MEETING_LIST = MEETING_API +"/v1/list";
    //编辑会议
    public static final String MEETING_EDIT = MEETING_API +"/v1/edit";
    //新增会议
    public static final String MEETING_ADD = MEETING_API +"/v1/add";
    //删除会议
    public static final String MEETING_DELETE = MEETING_API +"/v1/delete";
    //会议在线人数
    public static final String MEETING_UPDATE_NUMBAR = MEETING_API +"/v1/updatenumber";

    //app使用上报
    public static final String APP_USE_REPORT = "oort/oortcloud-sso/rank/v1/app.use";

    //获取东软登录部门列表
    public static final String SZJCY_LIST = "oort/oortcloud-sso/sso/v1/szjcyList";
    // 用户列表
    public static final String ORGANIZATION = "oort/oortcloud-sso/sso/v1/getdeptuser";

    //api使用上报
    public static final String API_USE_REPORT = "oort/oortcloud-log-manage/api/v1/reportLog";


    public static  boolean isOffLine = false;
    public static final String FRIENDLAB_LIST = "oort/oortcloud-sso/address/v1/tagList";
    public static final String FRIENDLAB_ADD = "oort/oortcloud-sso/address/v1/tagSet";
    public static final String FRIENDLAB_DELETE = "oort/oortcloud-sso/address/v1/tagDel";
    public static final String FRIENDLAB_UPDATE = "oort/oortcloud-sso/address/v1/tagSet";
    public static final String FRIENDLAB_UPDATEGROUPUSERLIST = "oort/oortcloud-sso/address/v1/tagUserList";
    public static final String FRIENDLAB_UPDATEFRIEND = "oort/oortcloud-sso/friendGroup/updateFriend";



    public static final String TAG_LIST = "oort/oortcloud-sso/tag/v1/tagList";
    public static final String TAG_ADD = "oort/oortcloud-sso/tag/v1/tagSet";
    public static final String TAG_DELETE = "oort/oortcloud-sso/tag/v1/tagDel";
    public static final String TAG_USERADD = "oort/oortcloud-sso/tag/v1/tagUserAdd";
    public static final String TAG_USERDEL = "oort/oortcloud-sso/tag/v1/tagUserDel";
    public static final String TAG_USERLIST = "oort/oortcloud-sso/tag/v1/tagUserList";

    public static final String ADDRESS_TAG_LIST = "oort/oortcloud-sso/address/v1/tagList";
    public static final String ADDRESS_TAG_ADD = "oort/oortcloud-sso/address/v1/tagSet";
    public static final String ADDRESS_TAG_DELETE = "oort/oortcloud-sso/address/v1/tagDel";
    public static final String ADDRESS_TAG_USERADD = "oort/oortcloud-sso/address/v1/tagUserAdd";
    public static final String ADDRESS_TAG_USERDEL = "oort/oortcloud-sso/address/v1/tagUserDel";
    public static final String ADDRESS_TAG_USERLIST = "oort/oortcloud-sso/address/v1/tagUserList";

    public static final String USED_TAG_USERCLEAR = "oort/oortcloud-sso/tag/v1/usedDel";
    public static final String USED_TAG_USERGET = "oort/oortcloud-sso/tag/v1/usedGet";
    public static final String USED_TAG_USERSET = "oort/oortcloud-sso/tag/v1/usedSet";


    public static final String STE_MY_STATUS = BASE_URL+"oort/oortcloud-sso/sso/v1/setMyStatus";

    public static final String GETALLAPP = "oort/oortcloud-admin-platform/client/getallapp";
    public static final String MYCollectSave = "oort/oortcloud-log-manage/api/v1/myCollectSave";
    public static final String MYCollectList = "oort/oortcloud-log-manage/api/v1/myCollectList";
    public static final String MYCollectDel = "oort/oortcloud-log-manage/api/v1/myCollectDel";

    public static String KEY_DEVICE_ID = "KEY_DEVICE_ID";


}
