package com.jumper.jit.service;

import com.jumper.jit.model.Article;

import java.io.IOException;

public interface DeployService {

    //发布并保存单体文章
    void deployAndSaveSingle(Article article) throws IOException;

    //发布并保存专题文章
    void saveAndDeploySubjectArticle(Article article) throws IOException;

    //导航添加专题,需要发布专题json
    void deploySubjectTree(Integer id) throws IOException;

    //发布所有文章json
    void deployArticleTree() throws IOException;

    //根据id自动判断来发布
    Article deployById(Integer id) throws IOException;

    //发布单体文章
    void deploySingle(Integer id) throws IOException;

    //发布专题文章
    void deploySubjectArticle(Integer id) throws IOException;

    //发布某一个专题所有文章
    void deployAllSubjectArticle(Integer sid) throws IOException;


    //发布页面顶部导航json
    void deployNavList() throws IOException;
}
