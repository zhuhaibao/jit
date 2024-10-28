package com.jumper.jit;

import com.jumper.jit.model.Student;
import com.jumper.jit.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({TestConfig.class})
public class TestConfigTester {

    @Autowired
    private Student testBean;

    @Test
    public void test1() {

        ;
    }

}
