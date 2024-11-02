package com.jumper.jit.service.impl;

import com.alibaba.fastjson.JSON;
import com.jumper.jit.aspect.DeployException;
import com.jumper.jit.aspect.DeployTools;
import com.jumper.jit.dto.SimpleArticleWithContentDTO;
import com.jumper.jit.dto.SimpleArticleWithoutContentDTO;
import com.jumper.jit.model.Article;
import com.jumper.jit.model.SiteConfig;
import com.jumper.jit.model.Subject;
import com.jumper.jit.service.ArticleService;
import com.jumper.jit.service.DeployService;
import com.jumper.jit.service.IndexService;
import com.jumper.jit.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class DeployServiceImpl implements DeployService {
    private ArticleService articleService;
    private SubjectService subjectService;
    private IndexService indexService;
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

    @Autowired
    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }


    @Override
    public void deployCurrentSubjectArticle(Integer id) throws IOException {
        Article dbArticle = articleService.findById(id);
        List<SimpleArticleWithoutContentDTO> dbList = articleService.findArticleTree(dbArticle.getSid(), Article.Status.PUBLISHED.getCode());
        if (dbList.isEmpty()) return;
        List<Subject> navigations = subjectService.findByNavigation(true, null);//查询所有导航
        SiteConfig siteConfig = subjectService.findSiteConfig();
        deployCurrentAndSubjectTreeProcess(dbArticle, dbList, navigations, siteConfig);
        //写入索引
        indexService.addOrUpdateIndex(dbArticle);
    }

    private void deployCurrentAndSubjectTreeProcess(Article currentArticle, List<SimpleArticleWithoutContentDTO> dbList, List<Subject> navigations, SiteConfig siteConfig) throws IOException {
        //获取第一条作为主题根目录下的index.html
        Article index = articleService.findById(dbList.getFirst().getId());
        deploySubjectIndexProcess(index, dbList, navigations, siteConfig);
        //发布当前文章
        if (currentArticle == null) return;
        deploySubjectArticleProcess(currentArticle, dbList, navigations, siteConfig);
    }

    /**
     * 发布该主题subject的主页
     *
     * @param article 作为主页的主题文章
     */
    private void deploySubjectIndexProcess(Article article, List<SimpleArticleWithoutContentDTO> subjectArticleTrees, List<Subject> navigations, SiteConfig siteConfig) throws IOException {
        //读取模版
        String templateContent = Files.readString(DeployTools.getSubjectArticleTemplate());
        //替换
        String result = replaceSubjectArticleTemplate(article.getSubject(), article.getPublishedAt(), article.getTitle(), article.getContent(), templateContent, subjectArticleTrees, navigations, siteConfig);
        //存放
        Path path = Paths.get(savePath + savePathForSubject, article.getSubject().getEnName());
        if (!Files.exists(path)) Files.createDirectories(path);
        Files.writeString(Paths.get(path.toString(), "index.html"), result);
    }

    /**
     * 仅发布该主题文章,不修改发布状态
     *
     * @param article 主题文章
     */
    private void deploySubjectArticleProcess(Article article, List<SimpleArticleWithoutContentDTO> subjectArticleTrees, List<Subject> navigations, SiteConfig siteConfig) throws IOException {
        //读取模版
        String templateContent = Files.readString(DeployTools.getSubjectArticleTemplate());
        //替换
        String result = replaceSubjectArticleTemplate(article.getSubject(), article.getPublishedAt(), article.getTitle(), article.getContent(), templateContent, subjectArticleTrees, navigations, siteConfig);
        //存放
        Path path = Paths.get(savePath + savePathForSubject, article.getSubject().getEnName(), article.getEnName());
        if (!Files.exists(path)) Files.createDirectories(path);
        Files.writeString(Paths.get(path.toString(), "index.html"), result);
        //生成对应的json文件
        deploySubjectArticleJson(path, article.getTitle(), article.getSubject().getSubjectTitle(), article.getContent());
    }

    private void deploySubjectArticleJson(Path path, String title, String subjectTitle, String content) throws IOException {
        Map<String, Object> map = new HashMap<>();
        if (subjectTitle != null) {
            map.put("pageTitle", title + " - " + subjectTitle);
        } else {
            map.put("pageTitle", title);
        }
        map.put("title", title);
        map.put("content", content);
        if (!Files.exists(path)) Files.createDirectories(path);
        Files.writeString(Paths.get(path.toString(), "index.json"), JSON.toJSONString(map));
    }

    private String replaceSubjectArticleTemplate(Subject subject, LocalDateTime publishTime, String title, String articleContent, String templateContent, List<SimpleArticleWithoutContentDTO> subjectArticleTrees, List<Subject> navigations, SiteConfig siteConfig) {
        //替换描述
        String result = templateContent.replaceFirst("<meta\\s+name=['\"]description['\"].*?>", "<meta name='description' content='" + siteConfig.getSiteDesc() + "'>");
        //替换关键词
        result = result.replaceFirst("<meta\\s+name=['\"]keywords['\"].*?>", "<meta name='keywords' content='" + siteConfig.getSiteKeywords() + "'>");
        //替换页面标题
        result = result.replaceFirst("<title>.*?</title>", "<title>" + title + " - " + subject.getSubjectTitle() + "</title>");
        //替换主题
        result = result.replaceFirst("<p class='subjectTitle' data-sub-dir></p>",
                "<p class='subjectTitle' data-sub-dir='" + subject.getEnName() + "'>"
                        + subject.getSubjectTitle() + "</p>");
        //替换内容

        result = result.replaceFirst("<span class='articleTitle' id='articleTitle'></span>",
                "<span class='articleTitle' id='articleTitle'>" + title + "</span>");
        result = result.replaceFirst("<span\\s+class=['\"]publishTime['\"]>.*?</span>", "<span class='publishTime'>lastUpdated : " + dateTimeFormatter.format(publishTime == null ? LocalDateTime.now() : publishTime) + "</span>");
        result = result.replaceFirst("<article id='articleContent'></article>",
                "<article id='articleContent'>" + articleContent + "</article>");

        //替换顶部导航
        result = result.replaceFirst("<li id=topNav_placeholders></li>", replaceTopNaveStr(navigations));
        //替换左侧菜单
        String leftNav = convertToSubjectLeftNav(subject, subjectArticleTrees);
        result = result.replaceFirst("<a id=article_placeholders></a>", leftNav);
        return result;
    }

    private String convertToSubjectLeftNav(Subject subject, List<SimpleArticleWithoutContentDTO> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(s -> {
            //a标签替换
            sb.append("<a href='/subject/").append(subject.getEnName()).append("/")
                    .append(s.getEnName()).append("/index.html'>")
                    .append(s.getTitle()).append("</a>").append("\n");
            //下面的div
            if (s.getChildren() != null && !s.getChildren().isEmpty()) {
                sb.append("<div>");
                sb.append(convertToSubjectLeftNav(subject, s.getChildren()));
                sb.append("</div>").append("\n");
            }
        });
        return sb.toString();
    }


    @Override
    public void deployCurrentSingle(Integer id) throws IOException {
        List<SimpleArticleWithoutContentDTO> singleList = articleService.findAllSingleArticleByStatus(Article.Status.PUBLISHED.getCode());
        List<Subject> navigations = subjectService.findByNavigation(true, null);//查询所有导航

        if (singleList.isEmpty() || navigations.isEmpty()) return;
        //第一条发布为index.html
        SimpleArticleWithoutContentDTO first = singleList.getFirst();
        SiteConfig siteConfig = subjectService.findSiteConfig();
        deploySingleIndexProcess(articleService.findById(first.getId()), singleList, navigations, siteConfig);
        //发布当前
        SimpleArticleWithContentDTO article = articleService.getSimpleWithContentById(id);
        deploySingleProcess(article, singleList, navigations, siteConfig);

        //写入索引
        indexService.addOrUpdateIndex(id, null, article.getEnName(), article.getTitle(), article.getContent());

    }

    private void deploySingleIndexProcess(Article article, List<SimpleArticleWithoutContentDTO> singleList, List<Subject> navigations, SiteConfig siteConfig) throws IOException {
        //读取模版
        String result = replaceSingleTemplate(article.getTitle(), article.getContent(), singleList, navigations, siteConfig);
        //存放
        Path dir = Paths.get(savePath + savePathForArticle);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Files.writeString(Paths.get(dir.toString(), "index.html"), result);
    }

    private void deploySingleProcess(SimpleArticleWithContentDTO article, List<SimpleArticleWithoutContentDTO> singleList, List<Subject> navigations, SiteConfig siteConfig) throws IOException {
        //读取模版
        String result = replaceSingleTemplate(article.getTitle(), article.getContent(), singleList, navigations, siteConfig);
        //存放
        Path dir = Paths.get(savePath + savePathForArticle + article.getEnName());
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Files.writeString(Paths.get(dir.toString(), "index.html"), result);
        //生成对应的json文件
        deploySubjectArticleJson(dir, article.getTitle(), null, article.getContent());
    }

    private String replaceSingleTemplate(String title, String content, List<SimpleArticleWithoutContentDTO> leftList, List<Subject> navigations, SiteConfig siteConfig) throws IOException {


        String contentTemplate = Files.readString(DeployTools.getSingleArticleTemplate());
        //替换描述
        String result = contentTemplate.replaceFirst("<meta\\s+name=['\"]description['\"].*?>", "<meta name='description' content='" + siteConfig.getSiteDesc() + "'>");
        //替换关键词
        result = result.replaceFirst("<meta\\s+name=['\"]keywords['\"].*?>", "<meta name='keywords' content='" + siteConfig.getSiteKeywords() + "'>");
        //替换页面标题
        result = result.replaceFirst("<title>.*?</title>", "<title>" + title + "</title>");
        //替换内容标题
        result = result.replaceFirst("<span class='articleTitle' id='articleTitle'></span>",
                "<span class='articleTitle' id='articleTitle'>" + title + "</span>");
        //替换内容
        result = result.replaceFirst("<article id='articleContent'></article>",
                "<article id='articleContent'>" + content + "</article>");

        //替换顶部导航
        result = result.replaceFirst("<li id=topNav_placeholders></li>", replaceTopNaveStr(navigations));
        //替换左侧树
        StringBuilder leftNav = new StringBuilder();
        leftList.forEach(a -> {
            leftNav.append("<span class='publishTime'>").append(dateTimeFormatter.format(a.getCreatedAt())).append("</span><br>").append("\n");
            leftNav.append("<a href='/articles/").append(a.getEnName()).append("/index.html'>").append(a.getTitle()).append("</a>").append("\n");
        });
        result = result.replaceFirst("<a id=article_placeholders></a>", leftNav.toString());
        return result;
    }

    private String replaceTopNaveStr(List<Subject> navigations) {
        StringBuilder navStr = new StringBuilder();
        navigations.forEach(s -> {
            navStr.append("<li><a href='/subject/").append(s.getEnName()).append("/index.html'>").append(s.getSubjectTitle()).append("</a></li>").append("\n");
        });
        return navStr.toString();
    }

    @Override
    public Article deployById(Integer id) throws IOException {
        Article saved = articleService.findById(id);
        SiteConfig siteConfig = subjectService.findSiteConfig();
        List<SimpleArticleWithoutContentDTO> singleList = articleService.findAllSingleArticleByStatus(Article.Status.PUBLISHED.getCode());
        List<Subject> navigations = subjectService.findByNavigation(true, null);//查询所有导航
        if (saved.getSid() == null) {//单体
            deploySingleProcess(articleService.getSimpleWithContentById(id), singleList, navigations, siteConfig);
        } else {//主题文章
            if (saved.getArticle() != null && saved.getArticle().getStatus().compareTo(Article.Status.PUBLISHED.getCode()) < 0) {
                throw new DeployException();
            }
            deploySubjectArticleProcess(saved, singleList, navigations, siteConfig);
        }
        //更新发布状态
        articleService.updateStatus(saved.getId(), Article.Status.PUBLISHED.getCode(), LocalDateTime.now());
        //写入索引
        indexService.addOrUpdateIndex(saved);
        return saved;
    }

    @Override
    public void deployAllSubjectArticle(Integer sid) throws IOException {
        Subject subject = subjectService.findById(sid);
        if (!subject.getNavigation()) return;
        //读取所有可发布的的文章列表
        List<SimpleArticleWithoutContentDTO> leftNavs = articleService.findAllPublishableArticleTree(sid);
        if (leftNavs.isEmpty()) return;
        List<Integer> ids = leftNavs.stream().map(SimpleArticleWithoutContentDTO::getId).toList();
        //顶部导航信息
        List<Subject> navigations = subjectService.findByNavigation(true, null);//查询所有导航
        if (navigations.isEmpty()) return;
        String templateContent = Files.readString(DeployTools.getSubjectArticleTemplate());        //读取模版
        //主题主页
        Article index = articleService.findById(leftNavs.getFirst().getId());
        SiteConfig siteConfig = subjectService.findSiteConfig();
        deploySubjectIndexProcess(index, leftNavs, navigations, siteConfig);
        //循环发布所有可发布的文章内容
        List<SimpleArticleWithContentDTO> articleWithContentDTOS = articleService.findAllPublishableWithContentBySid(subject.getId());
        articleWithContentDTOS.forEach(article -> {
            //替换
            String result = replaceSubjectArticleTemplate(subject, article.getPublishedAt(), article.getTitle(), article.getContent(),
                    templateContent, leftNavs, navigations, siteConfig);
            //存放html
            Path path = Paths.get(savePath + savePathForSubject, subject.getEnName(), article.getEnName());
            try {
                if (!Files.exists(path)) Files.createDirectories(path);
                Files.writeString(Paths.get(path.toString(), "index.html"), result);
                //json
                deploySubjectArticleJson(path, article.getTitle(), subject.getSubjectTitle(), article.getContent());
            } catch (IOException e) {
                throw new RuntimeException();
            }
            //更新索引
            indexService.addOrUpdateIndex(article.getId(), subject.getEnName(), article.getEnName(), article.getTitle(), article.getContent());
        });
        //批量修改状态
        articleService.updateBatchStatus(Article.Status.PUBLISHED.getCode(), LocalDateTime.now(), ids);
        //发布索引
        indexService.deployIndexList();
    }

    @Override
    public void deployIndex() throws IOException {
        List<Subject> navigations = subjectService.findByNavigation(true, null);//查询所有导航
        //读取发布的单体文章信息
        List<SimpleArticleWithoutContentDTO> singleList = articleService.findAllSingleArticleByStatus(Article.Status.PUBLISHED.getCode());
        //模版内容
        String templateContent = Files.readString(DeployTools.getIndexTemplate());

        //替换顶部导航
        String result = templateContent.replaceFirst("<li id=topNav_placeholders></li>", replaceTopNaveStr(navigations));
        //替换主题区域
        StringBuilder subjectContent = new StringBuilder();
        navigations.forEach(s -> {
            String remark = s.getRemark();
            if (remark != null && remark.length() > 70) {
                remark = remark.substring(0, 70);
            }
            subjectContent.append("<div class='subjectHeader'>").append("    <a href='/subject/").append(s.getEnName()).append("/index.html'>")
                    .append("        <img src='/img/subject-logo1.svg'>")
                    .append("        <h3>").append(s.getSubjectTitle()).append("</h3>")
                    .append("        <p>").append(remark).append("...</p>")
                    .append("</a>")
                    .append("</div>");
        });
        result = result.replaceFirst("<a id='subject_replaceHolder'></a>", subjectContent.toString());
        //替换10条文章
        StringBuilder articleContent = new StringBuilder();
        for (int i = 0; i < Math.min(10, singleList.size()); i++) {
            SimpleArticleWithoutContentDTO a = singleList.get(i);
            articleContent
                    .append("<div class='articleHeader'>")
                    .append("    <h4>").append(dateTimeFormatter.format(a.getCreatedAt())).append("</h4>")
                    .append("    <span><a href='/articles/").append(a.getEnName()).append("/index.html'>").append(a.getTitle()).append("</a></span>")
                    .append("</div>")
            ;
        }
        result = result.replaceFirst("<a id='someArticle_replaceHolder'></a>", articleContent.toString());

        Files.writeString(Paths.get(savePath, "index.html"), result);
    }

    @Override
    public void deployAllSingleArticle() throws IOException {
        List<SimpleArticleWithContentDTO> singleContentList = articleService.findAllSingleArticleWithContentByStatus(Article.Status.PUBLISHED.getCode());
        List<SimpleArticleWithoutContentDTO> singleNavList =
                singleContentList.stream()
                        .map(a -> new SimpleArticleWithoutContentDTO(a.getId(), a.getTitle(), null, null, null, null, a.getEnName(), a.getCreatedAt()))
                        .toList();
        List<Subject> navigations = subjectService.findByNavigation(true, null);//查询所有导航
        SiteConfig siteConfig = subjectService.findSiteConfig();
        if (singleNavList.isEmpty() || navigations.isEmpty()) return;

        //第一条发布为index.html
        SimpleArticleWithContentDTO first = singleContentList.getFirst();
        String result = replaceSingleTemplate(first.getTitle(), first.getContent(), singleNavList, navigations, siteConfig);//读取模版
        Path dir = Paths.get(savePath + savePathForArticle);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Files.writeString(Paths.get(dir.toString(), "index.html"), result);

        //循环发布列表
        singleContentList.forEach(a -> {
            try {
                deploySingleProcess(a, singleNavList, navigations, siteConfig);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //更新索引
            indexService.addOrUpdateIndex(a.getId(), null, a.getEnName(), a.getTitle(), a.getContent());
        });
        //发布索引
        indexService.deployIndexList();
    }
}
