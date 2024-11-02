package com.jumper.jit.config.redis;


import lombok.Getter;

@Getter
public enum RedisTopics {
    SubjectArticle,
    AllSubjectArticle,
    SingleArticle,
    AllSingleArticle,
    GenIndexData,
    DelIndex,
    IndexPage,
    ;
}
