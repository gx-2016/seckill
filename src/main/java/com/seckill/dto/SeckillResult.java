package com.seckill.dto;

/**
 * Created by Administrator on 2016/9/3.
 * 所有的ajax请求结果
 */
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String messgae;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String messgae) {
        this.success = success;
        this.messgae = messgae;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }
}
