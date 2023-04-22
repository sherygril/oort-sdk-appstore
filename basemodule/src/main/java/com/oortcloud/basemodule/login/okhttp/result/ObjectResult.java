package com.oortcloud.basemodule.login.okhttp.result;


public class ObjectResult<T> extends Result {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
