package com.oortcloud.basemodule.im;

/**
 * @Company: 奥尔特云（深圳）智慧科技有限公司
 * @Author: lukezhang
 * @Date: 2022/12/6 15:19
 */
public class MessageEventChangeUI {
    private int fragmentId;

    public MessageEventChangeUI(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }
}
