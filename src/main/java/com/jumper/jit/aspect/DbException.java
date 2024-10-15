package com.jumper.jit.aspect;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据操作异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DbException extends RuntimeException{
    private Object data;

    public DbException() {
    }

    public DbException(String s) {
    }

    public DbException(Object data) {
        this.data = data;
    }
}
