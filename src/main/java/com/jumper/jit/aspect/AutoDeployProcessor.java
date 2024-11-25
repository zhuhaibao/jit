package com.jumper.jit.aspect;

import com.jumper.jit.config.redis.RedisTopics;
import com.jumper.jit.config.redis.RedisUtil;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AutoDeployProcessor {

    private ArticleService articleService;
    private SubjectService subjectService;
    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @Pointcut("execution(* com.jumper.jit.service.impl.ArticleServiceImpl.saveAndUpdateSubjectArticleStatus(..))||" +
            "execution(* com.jumper.jit.service.impl.ArticleServiceImpl.saveAndUpdateSingleStatus(..))||" +
            "execution(* com.jumper.jit.service.impl.DeployServiceImpl.deployById(..))")
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

    @Pointcut("execution(* com.jumper.jit.service.impl.DeployServiceImpl.deployCurrentSubjectArticle(..))||" +
            "execution(* com.jumper.jit.service.impl.DeployServiceImpl.deployCurrentSingle(..))")
    private void deployAndCauseUpdateIndexData() {
    }

    @After("deployAndCauseUpdateIndexData()")
    public void deployByIdAndDeployIndexList(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        log.info("{} ->auto deployIndexDataList..", signature.getName());
        //发布索引
        redisUtil.sendMessage(RedisTopics.GenIndexData, "");
    }

    @After("saveAndUpdateArticleStatus()")
    public void deployAfterSaveAndUpdateArticleStatus(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Integer id = null;
        if (signature.getMethod().getName().contains("deployById")) {
            id = (Integer) point.getArgs()[0];
        } else {
            Article param = (Article) point.getArgs()[0];
            id = param.getId();
        }
        Article dbArticle = articleService.findById(id);
        log.info("{} ->auto deploy current Article[id={},title={}]... ", signature.getName(), dbArticle.getId(), dbArticle.getTitle());
        if (dbArticle.getSid() != null) {
            redisUtil.sendMessage(RedisTopics.SubjectArticle, String.valueOf(id));
        } else {
            redisUtil.sendMessage(RedisTopics.SingleArticle, String.valueOf(id));
        }
    }

    @After("updateNavigation()")
    public void autoDeployTopNav(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //发布主页index
        log.info("{} -> auto deploy index.html... ", signature.getName());
        redisUtil.sendMessage(RedisTopics.IndexPage, "");
    }

    @After("updateSubject()")
    public void autoDeploySubject(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //发布主页index
        log.info("{} -> auto deploy index.html...  ", signature.getName());
        redisUtil.sendMessage(RedisTopics.IndexPage, "");
        //如果修改了enName,也就是主题存放目录,那么整个主题要重发一遍
        SubjectDTO subject = (SubjectDTO) point.getArgs()[0];
        Subject dbSubject = subjectService.findById(subject.getId());
        if (subject.getEnName() != null) {
            log.info("{}: enName changed[{}] -> auto deploy AllSubject[{}]... ", signature.getName(), subject.getEnName(), dbSubject.getSubjectTitle());
            redisUtil.sendMessage(RedisTopics.AllSubjectArticle, String.valueOf(subject.getId()));
        }
    }

    @Around("updateArticle()")
    public Object autoDeploySubjectCauseOfArticleModified(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取第一个参数id,检查文章状态
        Integer id = (Integer) point.getArgs()[0];
        Article article = articleService.findById(id);
        Object returnValue = point.proceed();
        if (article.getStatus().equals(Article.Status.PUBLISHED.getCode())) {

            //判断是否是删除,删除索引
            if (signature.getMethod().getName().contains("delete")) {
                log.info("{}: ->Article[{}]  auto delete index key... ", id, signature.getName());
                redisUtil.sendMessage(RedisTopics.DelIndex, String.valueOf(id));
            }
            if (article.getSid() != null) {
                log.info("{}: ->Article[{}] auto deploy AllSubject[{}]... ", id, signature.getName(), article.getSubject().getSubjectTitle());
                redisUtil.sendMessage(RedisTopics.AllSubjectArticle, String.valueOf(article.getSubject().getId()));
            } else {
                log.info("{}: ->Article[{}]  auto deploy deployAllSingleArticle... ", id, signature.getName());
                redisUtil.sendMessage(RedisTopics.AllSingleArticle, "");
            }
        }
        return returnValue;
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
            redisUtil.sendMessage(RedisTopics.AllSubjectArticle, String.valueOf(article.getSid()));
        } else {
            redisUtil.sendMessage(RedisTopics.AllSingleArticle, "");
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
            redisUtil.sendMessage(RedisTopics.AllSubjectArticle, String.valueOf(targetSid));
        }
        return object;
    }
}
