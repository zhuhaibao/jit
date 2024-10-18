package com.jumper.jit.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一返回格式
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultData <T>{
    private int status;
    private String statusText;
    private String msg;
    private T data;
    private long timestamp;

    public ResultData() {
        this.timestamp = System.currentTimeMillis();
    }
    public static <T> ResultData<T> success(T data){
        ResultData<T> r = new ResultData<>();
        r.setStatus(Status.RC100.code);
        r.setStatusText(Status.RC100.codeText);
        r.setMsg(Status.RC100.msg);
        r.setData(data);
        return r;
    }
    public static <T> ResultData<T> failWithData(Status status,T data){
        ResultData<T> r = new ResultData<>();
        r.setStatus(status.code);
        r.setStatusText(status.codeText);
        r.setMsg(status.msg);
        r.setData(data);
        return r;
    }
    public static <T> ResultData<T> fail(Status status){
        ResultData<T> r = new ResultData<>();
        r.setStatus(status.code);
        r.setStatusText(status.codeText);
        r.setMsg(status.msg);
        return r;
    }
    @AllArgsConstructor
    public enum Status{
        RC100(100,"ok","操作成功"),
        RC101(101,"err","参数异常"),
        RC102(102,"err","无此数据"),
        RC103(103,"err","数据操作失败"),
        RC104(104,"err","不支持的请求"),
        RC999(999,"err","操作失败"),
        RC500(500,"err","服务异常")
        ;
        private final int code;
        private final String codeText;
        private final String msg;
    }
}
