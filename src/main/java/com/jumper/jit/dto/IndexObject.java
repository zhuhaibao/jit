package com.jumper.jit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndexObject {
    private Integer id;
    private String url;
    private String subject;
    private String title;
    private String content;
}
