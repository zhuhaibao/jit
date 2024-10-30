package com.jumper.jit.service;

import com.jumper.jit.model.Article;

import java.io.IOException;

public interface DeployService {

    //保存单体文章只修改发布状态
    void saveAndUpdateSingleStatus(Article article) throws IOException;

    //保存专题文章,只修改发布状态
    void saveAndUpdateSubjectArticleStatus(Article article) throws IOException;

    //导航添加专题,需要发布专题
    void deployCurrentSubjectArticle(Integer id) throws IOException;

    //发布所有文章
    void deployCurrentSingle(Integer id) throws IOException;

    //根据id自动判断来发布
    Article deployById(Integer id) throws IOException;

    //发布某一个专题所有文章
    void deployAllSubjectArticle(Integer sid) throws IOException;

    //发布所有单体文章列表
    void deployAllSingleArticle() throws IOException;

    //发布主页
    void deployIndex() throws IOException;
}
