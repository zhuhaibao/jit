package com.jumper.jit;

import com.jumper.jit.dto.SimpleSubjectDTO;
import com.jumper.jit.repository.SubjectRepository;
import com.jumper.jit.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SubjectDaoTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    SubjectService subjectService;
    @Test
    void subjectDaoTest(){
//        Integer num1 = subjectRepository.setSubjectArticleSum(7,1);
//        Integer num2 = subjectRepository.setSubjectArticleSum(9,-1);
//        System.out.printf("%d,%d",num1,num2);

        List<SimpleSubjectDTO> simpleSubjectDTOS = subjectRepository.queryAllByIdNotNullOrderByCreatedAtDescUpdatedAtDesc();
        simpleSubjectDTOS.forEach(s->{
            System.out.printf("%d->%s",s.getId(),s.getSubjectTitle());
        });

    }
    @BeforeEach
    public void prepare(){

    }
}
