package com.jumper.jit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Length(min = 2,max = 100,message = "title长度在2和100之间",groups = {Insert.class, InsertWithSid.class,InsertNoPidAndSid.class})
    private String title;
    private String content;
    private Integer status=0;//文章状态 0 无内容,1 已保存,2 已发布,3修改未发布
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(groups = {Insert.class})
    @Column(insertable=false, updatable=false)
    private Integer pid;//上级文章

    @NotNull(groups = {Insert.class, InsertWithSid.class})
    @Column(insertable=false, updatable=false)
    private Integer sid;//所属主题
    private Integer orderNum=1;//相同pid内的文章的排序

    @ManyToOne
    @JoinColumn(name = "sid")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Article article;

    @PrePersist
    void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public interface InsertWithSid {}
    public interface Insert{}
    public interface InsertNoPidAndSid{};
}
