package com.jumper.jit.controller;

import com.jumper.jit.dto.PageDTO;
import com.jumper.jit.dto.SimpleArticleWithoutContentDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/subject")
public class SubjectController {
    private SubjectService service;
    private ArticleService articleService;

    @Autowired
    public void setRepository(SubjectService service) {
        this.service = service;
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseBody
    public Subject saveSubject(@Validated Subject subject) throws IOException {
        return service.add(subject);
    }

    @PostMapping("find")
    @ResponseBody
    public PageDTO findAllBy(SubjectDTO subject) {
        return PageDTO.toPageDTO(service.findSubjectBy(subject));
    }

    @GetMapping("find")
    public String find(SubjectDTO subject, Model model) {
        Page<Subject> page = service.findSubjectBy(subject);
        model.addAttribute("data", PageDTO.toPageDTO(page));
        model.addAttribute("subject", subject);
        return "subject";
    }

    @PostMapping("updateSubjectTitle")
    @ResponseBody
    public Subject updateSubjectTitle(@Validated(SubjectDTO.UpdateTitle.class) SubjectDTO subject) throws IOException {
        return service.updateSubject(subject);
    }

    @PostMapping("updateRemark")
    @ResponseBody
    public Subject updateRemark(@Validated(SubjectDTO.UpdateRemark.class) SubjectDTO subject) throws IOException {
        return service.updateSubject(subject);
    }

    @PostMapping("updateEnName")
    @ResponseBody
    public Subject updateEnName(@Validated(SubjectDTO.UpdateEnName.class) SubjectDTO subject) throws IOException {
        return service.updateSubject(subject);
    }

    @PostMapping("updatePic")
    @ResponseBody
    public Subject updatePic(@Validated(SubjectDTO.UpdatePic.class) SubjectDTO subject) throws IOException {
        return service.updateSubject(subject);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public void deleteSubject(@PathVariable("id") Integer id) {
        service.delete(id);
    }

    @GetMapping("{sid}")
    public String subjectArticleBySid(@PathVariable("sid") Integer sid, @RequestParam(value = "id", required = false) Integer id, Model model) {
        model.addAttribute("subject", service.findById(sid));
        List<SimpleArticleWithoutContentDTO> list = articleService.findArticleTree(sid);
        model.addAttribute("treeL", list);
        if (id == null && !list.isEmpty()) {
            id = list.getFirst().getId();
        }
        if (id == null) {
            model.addAttribute("article", new Article());
        } else {
            model.addAttribute("article", articleService.getSimpleWithContentById(id));
        }
        return "subject-article";
    }

    @PostMapping("findSimpleArticlesById")
    @ResponseBody
    public List<SimpleArticleWithoutContentDTO> findAllSimpleWithoutContent(@RequestParam("id") Integer id) {
        return articleService.findArticleTree(id);
    }


}
