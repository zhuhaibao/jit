package com.jumper.jit.controller;

import com.jumper.jit.dto.PageDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
    private SubjectService subjectService;
    @Autowired
    public void setRepository(SubjectService service) {
        this.subjectService = service;
    }
    //默认主页
    @RequestMapping(value = "/",method = {RequestMethod.GET,RequestMethod.POST})
    public String find(SubjectDTO subject, Model model){
        Page<Subject> page = subjectService.findSubjectBy(subject);
        model.addAttribute("data", PageDTO.toPageDTO(page));
        model.addAttribute("subject", subject);
        return "subject";
    }
}
