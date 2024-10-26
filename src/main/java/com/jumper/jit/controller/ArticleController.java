package com.jumper.jit.controller;

import com.jumper.jit.dto.*;
import com.jumper.jit.model.Article;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping("find")
    @ResponseBody
    public PageDTO findPost(ArticleDTO dto) {
        return PageDTO.toPageDTO(service.findArticles(dto));
    }

    @GetMapping("find")
    public String findGet(ArticleDTO dto, Model model) {
        model.addAttribute("data", PageDTO.toPageDTO(service.findArticles(dto)));
        model.addAttribute("article", dto);
        model.addAttribute("subjectTree", subjectService.findAllSimpleSubject());
        return "articles";
    }

    @PostMapping("addSubArticle")
    @ResponseBody
    public Article saveSubArticle(@Validated(Article.AddSub.class) Article article) {
        return service.add(article);
    }

    @ResponseBody
    @PostMapping("addTopArticle")
    public Article saveTopArticle(@Validated(Article.AddTop.class) Article article) {
        return service.add(article);
    }

    @GetMapping("add")
    public String add(Model model) {
        model.addAttribute("article", new Article());
        return "article";
    }

    @ResponseBody
    @PostMapping("add")
    public Article postAdd(@Validated(Article.AddSingleArticle.class) Article article) {
        if (article.getId() != null) {
            return service.updateSingle(article);
        }
        return service.add(article);
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("article", service.findById(id));
        return "article";
    }

    @ResponseBody
    @DeleteMapping("{id}")
    public void delArticle(@PathVariable("id") Integer id) {
        service.delete(id);
    }

    @ResponseBody
    @PostMapping("saveContent")
    public Article saveArticleContent(@Validated(Article.SaveContent.class) Article article) {
        return service.updateContent(article);
    }

    @ResponseBody
    @PostMapping("updateTitle")
    public void updateTitle(@RequestParam("id") Integer id, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "enName", required = false) String enName) {
        if (title != null && !title.isEmpty()) {
            service.updateTitle(id, title);
        } else if (enName != null && !enName.isEmpty()) {
            service.updateEnName(id, enName);
        }
    }

    @ResponseBody
    @PostMapping("moveTo/{id}/{to}")
    public void move(@PathVariable("id") Integer currentId, @PathVariable("to") Integer targetId) {
        service.moveTo(currentId, targetId);
    }

    @ResponseBody
    @PostMapping("insertAsChildToSubject/{id}/{to}")
    public void insertAsChildToSubject(@PathVariable("id") Integer currentId, @PathVariable("to") Integer targetId) {
        service.insertNodeAsChild(currentId, targetId, true);
    }

    @ResponseBody
    @PostMapping("insertAsChildToNode/{id}/{to}")
    public void insertAsChildToNode(@PathVariable("id") Integer currentId, @PathVariable("to") Integer targetId) {
        service.insertNodeAsChild(currentId, targetId, false);
    }

    @ResponseBody
    @PostMapping("getArticle/{id}")
    public SimpleArticleWithContentDTO move(@PathVariable("id") Integer id) {
        return service.getSimpleWithContentById(id);
    }

    @ResponseBody
    @PostMapping("findArticlesWithParentAndSubjectByKeyword")
    public List<ArticleAndParentDTO> findArticlesWithParentAndSubject(@RequestParam("keyword") String keyword) {
        return service.findArticlesWithParentAndSubject(keyword);
    }

    @ResponseBody
    @PostMapping("uploadFile")
    public FileDTO findArticlesWithParentAndSubject(FileDTO fileDTO) throws IOException {
        return service.saveFile(fileDTO);
    }


}
