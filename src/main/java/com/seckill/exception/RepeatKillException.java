package com.seckill.exception;

/**
 * Created by Administrator on 2016/9/2.
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {
        super(message);
    }
}
