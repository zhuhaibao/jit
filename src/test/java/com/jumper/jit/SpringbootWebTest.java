package com.jumper.jit;

import com.jumper.jit.dto.ArticleDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import com.jumper.jit.repository.ArticleRepository;
import com.jumper.jit.repository.SubjectRepository;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.SubjectService;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SpringbootWebTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SubjectRepository repository;
    @Autowired
    private SubjectService service;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Test
    void test() throws Exception{
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/test/data1");
        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        StatusResultMatchers status = status();
        ResultMatcher matcher = status.isOk();
        resultActions.andExpect(matcher);
    }
    @Test
    void testController()throws Exception{
        mockMvc.perform(
                    get("/test/data1")
                            .accept(MediaType.ALL)
                            .cookie(new Cookie("JSESSIONID","DB58C842C41ADEAE8E9298D2F0FB1989"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.is(100)))
                .andExpect(jsonPath("$.statusText",Matchers.is("ok")))
                .andExpect(jsonPath("$.data.name",Matchers.is("zhagnsan")))
        ;
    }
    @Test
    void testDao1(){
        Subject subject = new Subject();
        subject.setSubjectTitle("java12345");
        subject.setRemark("java12345");
        Subject result = repository.save(subject);

        assertEquals("java12345", subject.getSubjectTitle());
        assertEquals("java12345", subject.getRemark());
    }
    @Test
    void testDao(){
        String[] techs = ("Java,Spring,SpringBoot,Javascript,Html,Css,Mysql,Mariadb,Hibernate," +
                "Mybatis,Tomcat,Apache,Redis,RabbitMq,Kafka").split(",");
        String[] suffix = "简介,基础知识,简单应用,深化理解,实战,典型案例,常见问题汇总".split(",");
        Random rd = new Random(47);
        List<Subject> l = new ArrayList<>();
        IntStream.range(1,100).forEach(i->{
            String subjectTitle = techs[rd.nextInt(techs.length)]+i;
            String remark = subjectTitle+suffix[rd.nextInt(suffix.length)]+i;
            Subject subject = new Subject();
            subject.setSubjectTitle(subjectTitle);
            subject.setRemark(remark);
            l.add(subject);
        });
        repository.saveAll(l);
    }
    @Test
    void pageTest(){
        SubjectDTO dto = SubjectDTO.builder().subjectTitle("Java").build();
        Page<Subject> page = service.findSubjectBy(dto);
        System.out.println(JSONObject.wrap(page));

        assertTrue(page.getTotalElements()>0);
    }
    @Test
    void articleDaoTest(){
        Page<ArticleDTO> list = articleService.findArticles(new ArticleDTO());
        list.forEach(e-> System.out.println(e.getTitle()));
    }
    @BeforeEach
    public void prepare(){

    }
}
