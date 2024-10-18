package com.jumper.jit.controller;

import com.jumper.jit.dto.SimpleArticleWithoutContentDTO;
import com.jumper.jit.model.Article;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private ArticleService service;
    private SubjectService subjectService;
    @Autowired
    public void setArticleService(ArticleService service) {
        this.service = service;
    }
    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("{id}")
    public String subjectArticle(@PathVariable("id") Integer id, Model model){
        model.addAttribute("subject",subjectService.findById(id));
        List<SimpleArticleWithoutContentDTO> list = service.findArticleTree(id);
        model.addAttribute("treeL",list);
        if(!list.isEmpty())
            model.addAttribute("article",service.getSimpleWithContentById(list.getFirst().getId()));
        return "subject-article";
    }
    @ResponseBody
    @PostMapping("saveSubArticle")
    public Article saveSubArticle(@Validated(Article.Insert.class) Article article){
        return service.save(article);
    }
    @ResponseBody
    @PostMapping("saveTopArticle")
    public Article saveTopArticle(@Validated(Article.InsertWithSid.class) Article article){
        return service.save(article);
    }

    @ResponseBody
    @DeleteMapping("{id}")
    public void delArticle(@PathVariable("id") Integer id){
        service.delete(id);
    }
    @ResponseBody
    @PostMapping("save")
    public Article saveArticle(@Validated(Article.InsertNoPidAndSid.class) Article article){
        return service.save(article);
    }
}
