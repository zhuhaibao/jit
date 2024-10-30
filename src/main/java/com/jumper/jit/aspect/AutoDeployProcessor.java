package com.jumper.jit.aspect;

import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.DeployService;
import com.jumper.jit.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
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
    private ArticleService articleService;
    private SubjectService subjectService;

    @Autowired
    public void setDeployService(DeployService deployService) {
        this.deployService = deployService;
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Pointcut("execution(* com.jumper.jit.service.impl.DeployServiceImpl.saveAndUpdateSubjectArticleStatus(..))||" +
            "execution(* com.jumper.jit.service.impl.DeployServiceImpl.saveAndUpdateSingleStatus(..))")
    private void saveAndUpdateArticleStatus() {
    }

    @Pointcut("execution(* com.jumper.jit.service.impl.SubjectServiceImpl.moveTo(..))||" +
            "execution(* com.jumper.jit.service.impl.SubjectServiceImpl.deleteNavigation(..))||" +
            "execution(* com.jumper.jit.service.impl.SubjectServiceImpl.addNavigation(..))")
    private void updateNavigation() {
    }

    @Pointcut("execution(* com.jumper.jit.service.impl.SubjectServiceImpl.updateSubject(..))")
    private void updateSubject() {
    }

    @Pointcut("execution(* com.jumper.jit.service.impl.ArticleServiceImpl.delete(..))||" +
            "execution(* com.jumper.jit.service.impl.ArticleServiceImpl.updateTitle(..))||" +
            "execution(* com.jumper.jit.service.impl.ArticleServiceImpl.updateEnName(..))||" +
            "execution(* com.jumper.jit.service.impl.ArticleServiceImpl.moveTo(..))")
    private void updateArticle() {
    }

    @Pointcut("execution(* com.jumper.jit.service.impl.ArticleServiceImpl.insertNodeAsChild(..))")
    private void insertNodeAsChild() {
    }


    @After("saveAndUpdateArticleStatus()")
    public void deployAfterSaveAndUpdateArticleStatus(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Article param = (Article) point.getArgs()[0];
        Article dbArticle = articleService.findById(param.getId());
        log.info("{} ->auto deploy current Article[id={},title={}]... ", signature.getName(), param.getId(), param.getTitle());
        if (dbArticle.getSid() != null) {
            deployService.deployCurrentSubjectArticle(param.getId());
        } else {
            deployService.deployCurrentSingle(param.getId());
        }
    }


    @After("updateNavigation()")
    public void autoDeployTopNav(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //发布主页index
        log.info("{} -> auto deploy index.html... ", signature.getName());
        deployService.deployIndex();
    }


    @After("updateSubject()")
    public void autoDeploySubject(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //发布主页index
        log.info("{} -> auto deploy index.html...  ", signature.getName());
        deployService.deployIndex();
        //如果修改了enName,也就是主题存放目录,那么整个主题要重发一遍
        SubjectDTO subject = (SubjectDTO) point.getArgs()[0];
        Subject dbSubject = subjectService.findById(subject.getId());
        if (subject.getEnName() != null) {
            log.info("{}: enName changed[{}] -> auto deploy AllSubject[{}]... ", signature.getName(), subject.getEnName(), dbSubject.getSubjectTitle());
            deployService.deployAllSubjectArticle(subject.getId());
        }
    }


    @After("updateArticle()")
    public void autoDeploySubjectCauseOfArticleModified(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取第一个参数id,检查文章状态
        Integer id = (Integer) point.getArgs()[0];
        Article article = articleService.findById(id);
        if (article.getStatus().equals(Article.Status.PUBLISHED.getCode())) {
            if (article.getSid() != null) {
                log.info("{}: ->Article[{}] auto deploy AllSubject[{}]... ", id, signature.getName(), article.getSubject().getSubjectTitle());
                deployService.deployAllSubjectArticle(article.getSubject().getId());
            } else {
                log.info("{}: ->Article[{}]  auto deploy deployAllSingleArticle... ", id, signature.getName());
                deployService.deployAllSingleArticle();
            }
        }
    }


    @Around("insertNodeAsChild()")
    public Object autoDeployAfterInsertNode(ProceedingJoinPoint point) throws Throwable {
        Integer articleId = (Integer) point.getArgs()[0];
        Integer targetId = (Integer) point.getArgs()[1];
        boolean isSubject = (boolean) point.getArgs()[2];
        Article article = articleService.findById(articleId);//执行前获取参数
        Object object = point.proceed(point.getArgs());//执行
        //非发布状态直接返回
        if (article.getStatus().compareTo(Article.Status.PUBLISHED.getCode()) < 0) return object;
        //有所属主题则重新发布该主题,否则重新发布单体文章列表
        if (article.getSid() != null) {
            deployService.deployAllSubjectArticle(article.getSid());
        } else {
            deployService.deployAllSingleArticle();
        }
        //获取移动目标的主题
        Integer targetSid;
        if (isSubject) {
            targetSid = targetId;
        } else {
            Article target = articleService.findById(targetId);
            targetSid = target.getSid();
        }
        //如果主题不同,则重发布目标主题
        if (!targetSid.equals(article.getSid())) {
            deployService.deployAllSubjectArticle(targetSid);
        }
        return object;
    }
}
