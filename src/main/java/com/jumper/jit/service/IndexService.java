package com.jumper.jit.service;

import com.jumper.jit.config.redis.RedisUtil;
import com.jumper.jit.dto.IndexObject;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Service
public class IndexService {
    public static final String INDEX_PREFIX = "Index";
    private RedisUtil redis;

    @Value("${deploy.save-path}")
    private String savePath;
    @Value("${deploy.subject}")
    private String savePathForSubject;
    @Value("${deploy.article}")
    private String savePathForArticle;
    @Value("${deploy.indexFile}")
    private String indexFile;

    @Autowired
    public void setRedis(RedisUtil redis) {
        this.redis = redis;
    }

    /**
     * 缓存文章索引
     *
     * @param articleId     id
     * @param subjectEnName 主题的前端存储dir
     * @param articleEnName 文章的前端存储dir
     * @param title         文章标题
     * @param content       文章内容
     * @return true 成功 false 失败
     */
    public boolean addOrUpdateIndex(Integer articleId, String subjectEnName, String articleEnName, String title, String content) {
        String url = "/" + savePathForSubject + subjectEnName + "/" + articleEnName + "/index.html";
        if (subjectEnName == null) {//没有主题说明是单体文章
            url = "/" + savePathForArticle + articleEnName + "/index.html";
        }
        content = extractIndexContent(content);
        IndexObject index = IndexObject.builder().url(url).title(title).content(content).build();
        return redis.setObject(INDEX_PREFIX + articleId, index);
    }

    public boolean delIndex(Integer articleId) {
        return redis.delByKey(INDEX_PREFIX + articleId);
    }

    public boolean addOrUpdateIndex(Article article) {
        return addOrUpdateIndex(article.getId(), article.getSubject().getEnName(), article.getEnName(), article.getTitle(), article.getContent());
    }

    public boolean addOrUpdateIndex(Subject subject, Article article) {
        return addOrUpdateIndex(article.getId(), subject.getEnName(), article.getEnName(), article.getTitle(), article.getContent());
    }

    public void deployIndexList() throws IOException {
        List<String> jsonList = redis.getJsonListByKeyStartWith(INDEX_PREFIX);
        if (jsonList != null && !jsonList.isEmpty()) {
            Files.writeString(Path.of(savePath, indexFile, "index-data.js"), "export default" + jsonList);
        }
    }

    /**
     * 过滤掉无用的html符号,多个空白换成一个空白
     *
     * @param content html内容
     * @return 可以索引的内容
     */
    private String extractIndexContent(String content) {
        return content.replaceAll("<.*?>", "").replaceAll("\\s+", " ");
    }

}
