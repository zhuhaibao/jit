package com.jumper.jit.aspect;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Path;

public class DeployTools {

    public static Path getIndexTemplate() throws IOException {
        return ResourceUtils.getFile("classpath:templates/front-template/index.html").toPath();
    }

    public static Path getSubjectArticleTemplate() throws IOException {
        return ResourceUtils.getFile("classpath:templates/front-template/articles.html").toPath();
    }

    public static Path getSingleArticleTemplate() throws IOException {
        return ResourceUtils.getFile("classpath:templates/front-template/article.html").toPath();
    }
}
