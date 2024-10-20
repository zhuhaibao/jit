package com.jumper.jit.controller;

import com.jumper.jit.dto.SimpleArticleWithContentDTO;
import com.jumper.jit.model.Article;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private ArticleService service;
    @Autowired
    public void setArticleService(ArticleService service) {
        this.service = service;
    }

    @ResponseBody
    @PostMapping("addSubArticle")
    public Article saveSubArticle(@Validated(Article.AddSub.class) Article article){
        return service.add(article);
    }
    @ResponseBody
    @PostMapping("addTopArticle")
    public Article saveTopArticle(@Validated(Article.AddTop.class) Article article){
        return service.add(article);
    }

    @ResponseBody
    @DeleteMapping("{id}")
    public void delArticle(@PathVariable("id") Integer id){
        service.delete(id);
    }
    @ResponseBody
    @PostMapping("saveContent")
    public Article saveArticleContent(@Validated(Article.SaveContent.class) Article article){
        return service.updateContent(article);
    }
    @ResponseBody
    @PostMapping("updateTitle")
    public void updateTitle(@RequestParam("id")Integer id,@RequestParam("title")String title){
        service.updateTitle(id,title);
    }

    @ResponseBody
    @PostMapping("moveTo/{id}/{to}")
    public void move(@PathVariable("id")Integer currentId,@PathVariable("to")Integer targetId){
        service.moveTo(currentId,targetId);
    }

    @ResponseBody
    @PostMapping("getArticle/{id}")
    public SimpleArticleWithContentDTO move(@PathVariable("id")Integer id){
        return service.getSimpleWithContentById(id);
    }
}
