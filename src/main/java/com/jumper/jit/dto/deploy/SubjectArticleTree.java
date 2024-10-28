package com.jumper.jit.dto.deploy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SubjectArticleTree {
    private String articleUrl;//html名称,对应 Article enName
    private String title;
    private String createdAt;
    private List<SubjectArticleTree> children;//构成树状子节点
}
