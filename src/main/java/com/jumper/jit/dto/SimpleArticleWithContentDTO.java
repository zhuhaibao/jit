package com.jumper.jit.dto;

import java.time.LocalDateTime;

public interface SimpleArticleWithContentDTO {
    Integer getId();

    String getTitle();

    String getEnName();

    String getArticleKeyword();

    String getContent();

    LocalDateTime getCreatedAt();

    LocalDateTime getPublishedAt();
}
