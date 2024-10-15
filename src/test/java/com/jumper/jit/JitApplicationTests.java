package com.jumper.jit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {"test.p1=v1","test.p2=v2"}
)
class JitApplicationTests {

    @Value("${test.p1}")
    String p1;
    @Value("${test.p2}")
    String p2;

    @Test
    void test1(){
        System.out.println(p1);
        System.out.println(p2);
    }
}
