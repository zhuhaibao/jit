package com.jumper.jit.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * 拦截所有ajax返回结果,自动包装为ResultData
 */
@Slf4j
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private ObjectMapper objectMapper;
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("returnType {},{},{}",returnType.getGenericParameterType(),returnType.equals(Map.class),returnType.getContainingClass());
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("converterType {}",selectedConverterType);
        if(body instanceof String){
            return objectMapper.writeValueAsString(ResultData.success(body));
        }
        if(body instanceof ResultData<?>){
            return body;
        }
        return ResultData.success(body);
    }
}
