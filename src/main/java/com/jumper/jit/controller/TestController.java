package com.jumper.jit.controller;

import com.jumper.jit.model.TM;
import com.jumper.jit.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class TestController {
    private SubjectRepository repository;
    @Autowired
    public void setRepository(SubjectRepository repository) {
        this.repository = repository;
    }
    //默认主页
    @GetMapping("/")
    public String loginSuccess(){
        return "subject";
    }

    @GetMapping("data1")
    @ResponseBody
    public TM testResultData(){
//        Map<String,Object> m = new HashMap<>();
//        m.put("k1","k1");
//        m.put("k2",12);
//        m.put("k3", LocalDateTime.now());
        TM tm = new TM(10L,"zhagnsan",30,null);
        return tm;
    }
    @PostMapping("data3")
    @ResponseBody
    public TM testResultData3(){
//        Map<String,Object> m = new HashMap<>();
//        m.put("k1","k1");
//        m.put("k2",12);
//        m.put("k3", LocalDateTime.now());
        TM tm = new TM(10L,"zhagnsan",30,null);
        return tm;
    }
    @GetMapping("data2")
    @ResponseBody
    public Object testResultData2(){
        return "nihao 你好";
    }
    @GetMapping("err1")
    @ResponseBody
    public Object testResultDataError() throws Exception{
        throw new Exception("test err1");
    }

    @PostMapping("add")
    @ResponseBody
    public TM testValid(@Valid TM tm){
        return tm;
    }
}
