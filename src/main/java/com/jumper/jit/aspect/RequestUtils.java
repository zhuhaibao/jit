package com.jumper.jit.aspect;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {
    //获取上下文
    public static String contextPath(HttpServletRequest r){
        return r.getScheme()+"://"+r.getServerName()+":"+r.getServerPort()+"/"+r.getServletPath();
    }
}
