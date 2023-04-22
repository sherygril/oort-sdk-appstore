package com.oortcloud.basemodule.user;

/**
 * @filename: LoginResponse.java
 * @function： 登录返回的信息
 * @version：
 * @author: zhangzhijun
 * @date: 2019/11/4 14:40
 */

/**
*
* "code": 200,
  "msg": "成功",
  "data": {
    "userInfo": {
      "ID": "c64c64a9-ab34-43b4-94e2-5689aeeb51e8",
      "userloginID": "admin",
      "name": "admin",
      "idcard": "442001201911111234",
      "logintime": "2019-11-11 15:30:52",
      "accessToken": "d04376e535d87e2ecda301575b4efd1d",
      "client": "pcweb",
      "expiresIn": 3600
    }
  }
* *
* */
public class LoginResponse {

    private int resultCode;
    private String resultMsg;
    private Data data;
    private long currentTime;


    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
