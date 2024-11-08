package com.jumper.jit.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumper.jit.config.redis.RedisUtil;
import com.jumper.jit.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class RedisTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void delKey() {

    }

    @Test
    void testSetObject() {

        Article parent = Article.builder().id(1).title("parent_article" + 1).content("parent_content" + 1).build();
        Article child = Article.builder().id(13).title("child_article" + 1).content("child_content" + 1).article(parent).build();
        redisUtil.setObject(child.getId().toString(), child);
        Article result = redisUtil.getT(child.getId().toString(), Article.class);
        System.out.println(result);
    }

    @Test
    void testSetHashObject() {

        Article child = Article.builder().id(13).title("child_article" + 13).content("child_content" + 13).build();
        redisUtil.setObjectForHash(child.getId().toString(), "hashForArticle", child);
        Article result = redisUtil.getHashT(child.getId().toString(), "hashForArticle", Article.class);
        System.out.println(result);
    }

    @Test
    void testSetTList() {
        List<Article> articles = new ArrayList<>();
        for (int i = 2; i < 10; i++) {
            Article x = Article.builder().id(i).title("parent_article" + i).content("parent_content" + i).build();
            articles.add(x);
        }
        redisUtil.setObject("articleList", articles);
        List<Article> articleList = redisUtil.getTList("articleList", Article.class);
        System.out.println(articleList);

    }

    @Test
    void testObjectMapper() throws Exception {
        Article parent = Article.builder().id(1).title("parent_article").build();
        Article child = Article.builder().id(2).title("child_article").build();
        child.setArticle(parent);
        String objStr = objectMapper.writeValueAsString(child);
        System.out.println(objStr);
        Article objFromStr = objectMapper.readValue(objStr, Article.class);
        System.out.println(objFromStr);
    }
}
