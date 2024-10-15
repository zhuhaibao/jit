package com.jumper.jit.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultData<Object> handleArgumentsNotValid(MethodArgumentNotValidException e){
        log.error("字段错误: {}",e.getMessage(),e);
        Map<String,Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fe->{
            errors.put(fe.getField(),fe.getDefaultMessage());
        });
        return ResultData.failWithData(ResultData.Status.RC101,errors);
    }

    @ExceptionHandler(DbException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultData<Object> handleDbException(DbException e){
        log.error("数据操作错误: {},data{}",e.getMessage(),e.getData(),e);
        Object errData = e.getData()==null?e.getMessage():e.getData();
        return ResultData.failWithData(ResultData.Status.RC103,errData);
    }

    /**
     * 统一错误转发页面
     * @param e NoResourceFoundException
     * @return ModelAndView
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView noResourceHandle(NoResourceFoundException e){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("err");
        return modelAndView;
    }
    /**
     * 统一错误处理
     * @param e 一般异常
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultData<String> handle(HttpServletRequest rq,Exception e){
        log.error("统一捕获:{}",e.getMessage(),e);
        return ResultData.fail(ResultData.Status.RC500);
    }
}
