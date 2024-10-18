package com.jumper.jit;

import com.jumper.jit.repository.ArticleRepository;
import com.jumper.jit.repository.ArticleRepositoryPage;
import com.jumper.jit.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ArticleDaoTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleRepositoryPage articleRepositoryPage;

    @Autowired
    ArticleService articleService;
    @Test
    void articleDaoTest(){
//        Article article = articleRepository.findById(6).orElseThrow();
//        System.out.println(article.getTitle());
//
//        List<ArticleDTO> articleList = articleRepositoryPage.findBySid(124);
//        articleList.forEach(e-> System.out.println(e.getTitle()));
//
//        List<SubjectArticleDTO> subjectArticleDTOS = articleRepositoryPage.findAllSubjectsWithArticles();
//        subjectArticleDTOS.forEach(e-> System.out.println(e.getTitle()+e.getSubjectTitle()));
//
//        List<SubjectDTO> subjects = articleService.findAllSubjectArticles();
//        System.out.println(subjects);

//        List<ArticleDTO> dtoList = articleRepositoryPage.findByTitleContainingIgnoreCaseAndSidIsNotNullOrderBySidAscPidAscOrderNumAsc("ava");
//        dtoList.forEach(e->{
//            System.out.println(printArticle(e)+"->(主题)"+e.getSubject().getSubjectTitle());
//        });
        Integer sum = articleRepository.findChildrenCountByPid(1);
//        System.out.println(sum);

//        boolean b1 = articleRepository.existsByPid(4);
//        boolean b2 = articleRepository.existsByPid(5);
//        System.out.printf("%s,%s",b1,b2);

//            SimpleArticleWithContentDTO simpleArticleDTO = articleRepository.getArticleById(5);
//        System.out.println(simpleArticleDTO.getTitle());

//            List<SimpleArticleWithoutContentDTO> sL = articleRepository.findArticlesWithoutContent(124);
//            sL.forEach(e-> System.out.println(e.getTitle()));
    }
//    static String printArticle(ArticleDTO article){
//        String str = "";
//        str+= article.getTitle();
//        if(article.getParent()!=null){
//            str+="->"+printArticle(article.getParent());
//        }
//        return str;
//    }
    @BeforeEach
    public void prepare(){

    }
}
