package com.jumper.jit;

import com.jumper.jit.config.redis.RedisUtil;
import com.jumper.jit.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class IndexPatternTests {

    @Autowired
    private RedisUtil redis;
    @Autowired
    private ArticleService articleService;

    @Test
    void indexTest() {
        String content = articleService.getSimpleWithContentById(101).getContent();
        content = content.replaceAll("<.*?>", "").replaceAll("\\s+", " ");
        System.out.println(content);
    }

}
