package com.jumper.jit.controller;

import com.jumper.jit.model.Article;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.DeployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("deploy")
public class DeployController {
    private DeployService service;
    private ArticleService articleService;

    @Autowired
    public void setService(DeployService service) {
        this.service = service;
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @ResponseBody
    @PostMapping("saveAndUpdateStatus")
    public void saveAndUpdateStatus(@Validated(Article.SaveContent.class) Article article) throws IOException {
        articleService.saveAndUpdateSubjectArticleStatus(article);
    }

    @ResponseBody
    @PostMapping("addAndUpdateSingleStatus")//添加单体文章
    public void addAndUpdateSingleStatus(@Validated(Article.AddSingleArticle.class) Article article) throws IOException {
        articleService.saveAndUpdateSingleStatus(article);
    }

    @ResponseBody
    @PostMapping("deployCurrentSubjectArticle/{id}")
    public void deployCurrentSubjectArticle(@PathVariable Integer id) throws IOException {
        service.deployCurrentSubjectArticle(id);
    }

    @ResponseBody
    @PostMapping("deployCurrentSingle/{id}")
    public void deployCurrentSingle(@PathVariable Integer id) throws IOException {
        service.deployCurrentSingle(id);
    }

    @ResponseBody
    @PostMapping("{articleId}")
    public Article deploy(@PathVariable Integer articleId) throws IOException {
        return service.deployById(articleId);
    }

    @ResponseBody
    @PostMapping("deployIndex")
    public void deployIndex() throws IOException {
        service.deployIndex();
    }

    @ResponseBody
    @PostMapping("reDeploySubject/{sid}")
    public void reDeploySubject(@PathVariable("sid") Integer sid) throws IOException {
        service.deployAllSubjectArticle(sid);
    }
}
