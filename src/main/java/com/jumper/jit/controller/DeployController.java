package com.jumper.jit.controller;

import com.jumper.jit.model.Article;
import com.jumper.jit.service.DeployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("deploy")
public class DeployController {
    private DeployService service;

    @Autowired
    public void setService(DeployService service) {
        this.service = service;
    }

    @ResponseBody
    @PostMapping("saveAndDeploy")
    public void saveArticleContent(@Validated(Article.SaveContent.class) Article article) throws IOException {
        service.saveAndDeploySubjectArticle(article);
    }

    @ResponseBody
    @PostMapping("addAndDeploySingle")//添加单体文章
    public void addAndDeploySingle(@Validated(Article.AddSingleArticle.class) Article article) throws IOException {
        service.deployAndSaveSingle(article);
    }

    @ResponseBody
    @PostMapping("deploySubjectTree/{sid}")
    public void deploySubjectTree(@PathVariable Integer sid) throws IOException {
        service.deploySubjectTree(sid);
    }

    @ResponseBody
    @PostMapping("deployArticleTree")
    public void deployArticleTree() throws IOException {
        service.deployArticleTree();
    }

    @ResponseBody
    @PostMapping("{articleId}")
    public Article deploy(@PathVariable Integer articleId) throws IOException {
        return service.deployById(articleId);
    }

    @ResponseBody
    @PostMapping("topNavList")
    public void deployTopNav() throws IOException {
        service.deployNavList();
    }

    @ResponseBody
    @PostMapping("reDeploySubject/{sid}")
    public void reDeploySubject(@PathVariable("sid") Integer sid) throws IOException {
        service.deployAllSubjectArticle(sid);
    }
}
