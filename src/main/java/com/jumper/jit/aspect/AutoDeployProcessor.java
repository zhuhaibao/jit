package com.jumper.jit.aspect;

import com.jumper.jit.service.DeployService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
@Slf4j
public class AutoDeployProcessor {

    private DeployService deployService;

    @Autowired
    public void setDeployService(DeployService deployService) {
        this.deployService = deployService;
    }

    @Pointcut("@annotation(com.jumper.jit.aspect.AutoDeploy)")
    private void pointCut() {
    }

    @Pointcut("execution(* com.jumper.jit.service.impl.SubjectServiceImpl.moveTo(..))||" +
            "execution(* com.jumper.jit.service.impl.SubjectServiceImpl.deleteNavigation(..))||" +
            "execution(* com.jumper.jit.service.impl.SubjectServiceImpl.addNavigation(..))")
    private void updateTopNav() {
    }

    @After("updateTopNav()")
    public void autoDeployTopNav(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        log.info("{} -> auto deploy NavList...", signature.getName());
        deployService.deployNavList();
        //同事发布主页index
        log.info("{} -> auto deploy index.html...", signature.getName());
        
    }

    @After("pointCut()")
    public void autoDeploy(JoinPoint joinPoint) throws Throwable {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        AutoDeploy autoDeploy = methodSignature.getMethod().getAnnotation(AutoDeploy.class);
//        System.out.println(autoDeploy.desc());
//
//        Article article = (Article) joinPoint.getArgs()[0];
//        System.out.println(article);
    }
}
