package com.seckill.exception;

/**
 * Created by Administrator on 2016/9/2.
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillCloseException(String message) {
        super(message);
    }
}
