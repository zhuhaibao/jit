package com.jumper.jit.controller;

import com.jumper.jit.dto.PageDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subject")
public class SubjectController {
    private SubjectService service;
    @Autowired
    public void setRepository(SubjectService service) {
        this.service = service;
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

    @GetMapping("subjectArticle")
    public String subjectArticle(Model model){

        return "subject-article";
    }
}
