package com.oortcloud.basemodule.im;

/**
 * Created by Administrator on 2017/6/26 0026.
 */
public class ApplicationMessageSendChat {
    public final String toUserId;
    public final String content;

    public ApplicationMessageSendChat(String toUserId, String content) {
        this.toUserId = toUserId;
        this.content = content;
    }
}