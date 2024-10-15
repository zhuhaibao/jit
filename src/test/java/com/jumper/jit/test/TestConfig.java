package com.jumper.jit.test;

import com.jumper.jit.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public Student testBean(){
        return new Student(1,"jumper",100);
    }
}
