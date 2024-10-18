package com.jumper.jit.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubjectArticleDTO implements Comparable<SubjectArticleDTO>{
    public SubjectArticleDTO(String title, Integer id,Integer pid,Integer orderNum, String subjectTitle, Integer sid) {
        this.title = title;
        this.id = id;
        this.pid = pid;
        this.orderNum = orderNum;
        this.subjectTitle = subjectTitle;
        this.sid = sid;
    }

    private Integer sid;
    private String subjectTitle;
    private Integer id;
    private Integer pid;
    private Integer orderNum;
    private String title;

    private List<SubjectArticleDTO> children;

    @Override
    public int compareTo(SubjectArticleDTO o) {
        return this.getOrderNum().compareTo(o.getOrderNum());
    }
}
