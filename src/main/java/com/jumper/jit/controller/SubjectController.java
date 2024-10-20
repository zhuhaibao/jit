package com.jumper.jit.controller;

import com.jumper.jit.dto.PageDTO;
import com.jumper.jit.dto.SimpleArticleWithoutContentDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Subject saveSubject(@Valid Subject subject){
        return service.save(subject);
    }
    @PostMapping("find")
    @ResponseBody
    public PageDTO findAllBy(SubjectDTO subject){
        return PageDTO.toPageDTO(service.findSubjectBy(subject));
    }

    @PostMapping("updateSubjectTitle")
    @ResponseBody
    public Subject updateSubjectTitle(@Validated(SubjectDTO.UpdateTitle.class) SubjectDTO subject){
        return service.updateSubject(subject);
    }
    @PostMapping("updateRemark")
    @ResponseBody
    public Subject updateRemark(@Validated(SubjectDTO.UpdateRemark.class) SubjectDTO subject){
        return service.updateSubject(subject);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public void deleteSubject(@PathVariable("id") Integer id){
        service.delete(id);
    }

    @GetMapping("{sid}")
    public String subjectArticleBySid(@PathVariable("sid") Integer sid, Model model){
        model.addAttribute("subject",service.findById(sid));
        List<SimpleArticleWithoutContentDTO> list = articleService.findArticleTree(sid);
        model.addAttribute("treeL",list);
        if(!list.isEmpty())
            model.addAttribute("article",articleService.getSimpleWithContentById(list.getFirst().getId()));
        return "subject-article";
    }
}
