package com.jumper.jit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SimpleArticleWithoutContentDTO implements Comparable<SimpleArticleWithoutContentDTO> {

    public SimpleArticleWithoutContentDTO(Integer id, String title, Integer pid, Integer sid, Integer orderNum, Integer status, String enName, LocalDateTime createdAt) {
        this.id = id;
        this.pid = pid;
        this.orderNum = orderNum;
        this.sid = sid;
        this.title = title;
        this.status = status;
        this.enName = enName;
        this.createdAt = createdAt;
    }


    private Integer id;
    private Integer pid;
    private Integer sid;
    private Integer orderNum;
    private String title;
    private Integer status;
    private String enName;
    private LocalDateTime createdAt;
    private List<SimpleArticleWithoutContentDTO> children;

    @Override
    public int compareTo(SimpleArticleWithoutContentDTO o) {
        return this.getOrderNum().compareTo(o.getOrderNum());
    }
}
