package com.jumper.jit.controller;

import com.jumper.jit.model.Subject;
import com.jumper.jit.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/navigation")
public class NavigationController {
    private SubjectService service;
    @Autowired
    public void setRepository(SubjectService service) {
        this.service = service;
    }
    @GetMapping
    public String navigation(Model model){
        model.addAttribute("navigations",service.findByNavigation(true,null));
        return "navigation";
    }

    @ResponseBody
    @PostMapping("query")
    public List<Subject> query(@RequestParam("keyword") String keyword){
        return service.findByNavigation(false,keyword);
    }

    @ResponseBody
    @GetMapping("moveTo/{id}/{targetId}")
    public void moveTo(@PathVariable("id") Integer id,@PathVariable("targetId") Integer targetId){
        service.moveTo(id,targetId);
    }

    @ResponseBody
    @GetMapping("del/{id}")
    public void delNavigation(@PathVariable("id") Integer id){
        service.deleteNavigation(id);
    }

    @ResponseBody
    @PostMapping("addNavigation")
    public Subject addNavigation(@RequestParam("id") Integer id){
        return service.addNavigation(id);
    }
}
