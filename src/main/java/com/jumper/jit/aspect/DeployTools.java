package com.jumper.jit.aspect;

import com.jumper.jit.JitApplication;

import java.io.IOException;
import java.util.Objects;

public class DeployTools {

    public static String getIndexTemplate() throws IOException {
        return new String(Objects.requireNonNull(JitApplication.class.getClassLoader().getResourceAsStream("templates/front-template/index.html")).readAllBytes());
//        return ResourceUtils.getFile("classpath:templates/front-template/index.html").toPath();
    }

    public static String getSubjectArticleTemplate() throws IOException {
        return new String(Objects.requireNonNull(JitApplication.class.getClassLoader().getResourceAsStream("templates/front-template/articles.html")).readAllBytes());
//        return ResourceUtils.getFile("classpath:templates/front-template/articles.html").toPath();
    }

    public static String getSingleArticleTemplate() throws IOException {
        return new String(Objects.requireNonNull(JitApplication.class.getClassLoader().getResourceAsStream("templates/front-template/article.html")).readAllBytes());

//        return ResourceUtils.getFile("classpath:templates/front-template/article.html").toPath();
    }
}
