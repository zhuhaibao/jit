package com.jumper.jit.service.impl;

import com.alibaba.fastjson.JSON;
import com.jumper.jit.aspect.DeployException;
import com.jumper.jit.aspect.DeployTools;
import com.jumper.jit.dto.SimpleArticleWithContentDTO;
import com.jumper.jit.dto.SimpleArticleWithoutContentDTO;
import com.jumper.jit.dto.deploy.SubjectArticleTree;
import com.jumper.jit.dto.deploy.TopNavList;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.DeployService;
import com.jumper.jit.service.SubjectService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeployServiceImpl implements DeployService {
    private ArticleService articleService;
    private SubjectService subjectService;
    @Value("${deploy.save-path}")
    private String savePath;
    @Value("${deploy.subject}")
    private String savePathForSubject;
    @Value("${deploy.article}")
    private String savePathForArticle;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Autowired
    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void saveAndDeploySubjectArticle(Article article) throws IOException {
        Article saved = articleService.updateContent(article);
        //发布
        deploySubjectArticleProcess(saved);
        //更新发布状态
        articleService.updateStatus(article.getId(), Article.Status.PUBLISHED.getCode());
    }


    @Override
    public void deploySubjectTree(Integer sid) throws IOException {
        Subject subject = subjectService.findById(sid);
        List<SimpleArticleWithoutContentDTO> dbList = articleService.findArticleTree(sid, Article.Status.PUBLISHED.getCode());
        if (dbList.isEmpty()) return;
        deploySubjectTreeProcess(subject, dbList);
    }

    private void deploySubjectTreeProcess(Subject subject, List<SimpleArticleWithoutContentDTO> dbList) throws IOException {
        //获取第一条作为主题根目录下的index.html
        Article index = articleService.findById(dbList.getFirst().getId());
        deploySubjectIndexProcess(index);
        //发布导航tree
        List<SubjectArticleTree> subjectArticleTrees = convertTo(subject, dbList);
        Path dir = Paths.get(savePath + savePathForSubject + subject.getEnName());
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Files.writeString(Paths.get(dir.toString(), "index.json"), JSON.toJSONString(subjectArticleTrees));
    }


    private List<SubjectArticleTree> convertTo(Subject subject, List<SimpleArticleWithoutContentDTO> list) {
        List<SubjectArticleTree> result = new ArrayList<>();
        if (list == null) return result;
        list.forEach(s -> {
            SubjectArticleTree subjectArticleTree = SubjectArticleTree.builder()
                    .articleUrl(savePathForSubject + subject.getEnName() + "/" + s.getEnName() + ".html")
                    .title(s.getTitle())
                    .createdAt(dateTimeFormatter.format(s.getCreatedAt()))
                    .build();
            result.add(subjectArticleTree);
            subjectArticleTree.setChildren(convertTo(subject, s.getChildren()));
        });
        return result;
    }

    /**
     * 发布该主题subject的主页
     *
     * @param article 作为主页的主题文章
     * @throws IOException
     */
    private void deploySubjectIndexProcess(Article article) throws IOException {
        //读取模版
        String templateContent = Files.readString(DeployTools.getSubjectArticleTemplate());
        //替换
        String result = replaceSubjectArticleTemplate(article.getSubject(), article.getTitle(), article.getContent(), templateContent);
        //存放
        Path path = Paths.get(savePath + savePathForSubject, article.getSubject().getEnName());
        if (!Files.exists(path)) Files.createDirectories(path);
        Files.writeString(Paths.get(path.toString(), "index.html"), result);
    }

    /**
     * 仅发布该主题文章,不修改发布状态
     *
     * @param article 主题文章
     * @throws IOException
     */
    private void deploySubjectArticleProcess(Article article) throws IOException {
        //读取模版
        String templateContent = Files.readString(DeployTools.getSubjectArticleTemplate());
        //替换
        String result = replaceSubjectArticleTemplate(article.getSubject(), article.getTitle(), article.getContent(), templateContent);
        //存放
        Path path = Paths.get(savePath + savePathForSubject, article.getSubject().getEnName(), article.getEnName());
        if (!Files.exists(path)) Files.createDirectories(path);
        Files.writeString(Paths.get(path.toString(), "index.html"), result);
    }

    private static String replaceSubjectArticleTemplate(Subject subject, String title, String articleContent, String templateContent) {
        String result = templateContent.replaceFirst("<p class='subjectTitle' data-sub-dir></p>",
                "<p class='subjectTitle' data-sub-dir='" + subject.getEnName() + "'>"
                        + subject.getSubjectTitle() + "</p>");//替换主题
        result = result.replaceFirst("<span class='articleTitle'></span>",
                "<span class='articleTitle'>" + title + "</span>");
        result = result.replaceFirst("<article id='articleContent'></article>",
                "<article id='articleContent'>" + articleContent + "</article>");//替换内容
        return result;
    }


    @Override
    public void deployAndSaveSingle(Article article) throws IOException {
        Article saved;
        if (article.getId() != null) {
            saved = articleService.updateSingle(article);
        } else {
            saved = articleService.add(article);
        }
        deploySingleProcess(saved);
        //更新发布状态
        articleService.updateStatus(saved.getId(), Article.Status.PUBLISHED.getCode());
    }


    @Override
    public void deployArticleTree() throws IOException {
        List<SimpleArticleWithoutContentDTO> singleList = articleService.findAllSingleArticleByStatus(Article.Status.PUBLISHED.getCode());
        if (singleList.isEmpty()) return;
        //第一条发布为index.html
        SimpleArticleWithoutContentDTO first = singleList.getFirst();
        deploySingleIndexProcess(articleService.findById(first.getId()));
        //发布列表
        List<Map<String, Object>> maps = singleList.stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("articleUrl", savePathForArticle + e.getEnName() + ".html");
                    map.put("title", e.getTitle());
                    map.put("createdAt", dateTimeFormatter.format(e.getCreatedAt()));
                    return map;
                })
                .toList();
        Files.writeString(Paths.get(savePath + savePathForArticle + "index.json"), JSON.toJSONString(maps));
    }

    private void deploySingleIndexProcess(Article article) throws IOException {
        //读取模版
        String content = Files.readString(DeployTools.getSingleArticleTemplate());
        String result = content.replaceFirst("<span class='articleTitle'></span>",
                "<span class='articleTitle'>'" + article.getTitle() + "</span>");//标题
        result = result.replaceFirst("<article id='articleContent'></article>",
                "<article id='articleContent'>" + article.getContent() + "</article>");
        //存放
        Files.writeString(Paths.get(savePath + savePathForArticle + "index.html"), result);
    }

    private void deploySingleProcess(Article article) throws IOException {
        //读取模版
        String content = Files.readString(DeployTools.getSingleArticleTemplate());
        String result = content.replaceFirst("<span class='articleTitle'></span>",
                "<span class='articleTitle'>'" + article.getTitle() + "</span>");//标题
        result = result.replaceFirst("<article id='articleContent'></article>",
                "<article id='articleContent'>" + article.getContent() + "</article>");
        //存放
        Path dir = Paths.get(savePath + savePathForArticle + article.getEnName());
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Files.writeString(Paths.get(dir.toString(), "index.html"), result);
    }

    @Override
    public Article deployById(Integer id) throws IOException {
        Article saved = articleService.findById(id);
        if (saved.getSid() == null) {//单体
            deploySingleProcess(saved);
        } else {//主题文章
            if (saved.getArticle() != null && saved.getArticle().getStatus().compareTo(Article.Status.PUBLISHED.getCode()) < 0) {
                throw new DeployException();
            }
            deploySubjectArticleProcess(saved);
        }
        //更新发布状态
        articleService.updateStatus(saved.getId(), Article.Status.PUBLISHED.getCode());
        return saved;
    }


    @Override
    public void deployNavList() throws IOException {
        List<Subject> subjects = subjectService.findByNavigation(true, null);//查询所有导航
        List<TopNavList> navLists = subjects.stream()
                .map(s -> TopNavList.builder().pic(s.getPic()).subName(s.getSubjectTitle()).remark(s.getRemark()).dir(s.getEnName()).build())
                .toList();
        Files.writeString(Paths.get(savePath + "nav.json"), JSON.toJSONString(navLists));
    }

    @Override
    public void deployAllSubjectArticle(Integer sid) throws IOException {
        Subject subject = subjectService.findById(sid);
        if (!subject.getNavigation()) return;
        List<SimpleArticleWithoutContentDTO> dbList = articleService.findArticleTree(sid, Article.Status.PUBLISHED.getCode());
        if (dbList.isEmpty()) return;
        //发布左侧列表
        this.deploySubjectTreeProcess(subject, dbList);
        //循环发布文章内容
        String templateContent = Files.readString(DeployTools.getSubjectArticleTemplate());        //读取模版
        //获取所有文章列表附带内容
        List<SimpleArticleWithContentDTO> articleWithContentDTOS = articleService.findAllWithContentBySidAndStatus(subject.getId(), Article.Status.PUBLISHED.getCode());
        articleWithContentDTOS.forEach(article -> {
            //替换
            String result = replaceSubjectArticleTemplate(subject, article.getTitle(), article.getContent(), templateContent);
            //存放
            Path path = Paths.get(savePath + savePathForSubject, subject.getEnName(), article.getEnName());
            try {
                if (!Files.exists(path)) Files.createDirectories(path);
                Files.writeString(Paths.get(path.toString(), "index.html"), result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**************************下面的接口暂未使用*************************/

    @Override
    public void deploySubjectArticle(Integer id) throws IOException {
        Article article = articleService.findById(id);
        deploySubjectArticleProcess(article);
    }

    @Override
    public void deploySingle(Integer id) throws IOException {
        Article article = articleService.findById(id);
        deploySingleProcess(article);
    }


    /**
     * 如果当前article是为发布状态,则更新为发布状态;同时文章的导航列表也会变更
     *
     * @param article 当前文章
     * @throws IOException
     */
    private void updateDeployStatus(Article article) throws IOException {
        //更新发布状态
        if (!article.getStatus().equals(Article.Status.PUBLISHED.getCode())) {
            articleService.updateStatus(article.getId(), article.getStatus());
            //新文章需要更新导航列表
            this.deploySubjectTree(article.getSubject().getId());
        }
    }

}
