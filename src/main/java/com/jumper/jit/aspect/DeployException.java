package com.jumper.jit.aspect;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发布操作异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeployException extends RuntimeException {
    private Object data;

    public DeployException() {
    }

    public DeployException(String s) {
    }

    public DeployException(Object data) {
        this.data = data;
    }
}
