package com.jumper.jit.dto;

import com.jumper.jit.model.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 为了分页查询返回部分字段,故意把dto定义成Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "article")
@Table(name = "article")
public class ArticleDTO implements Comparable<ArticleDTO>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Length(min = 2,max = 100,message = "title长度在2和100之间",groups = UpdateTitle.class)
    private String title;
    private Integer status;//文章状态 0 无内容,1 已保存,2 已发布,3修改未发布
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer pid;//上级文章

    @Column(insertable=false, updatable=false)
    private Integer sid;//所属主题
    private Integer orderNum;//相同pid内的文章的排序

    @ManyToOne
    @JoinColumn(name = "sid")
    private Subject subject;

    @PrePersist
    void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    @Transient
    private String keyword;
    @Transient
    private String subjectTitle;
    @Transient
    private List<ArticleDTO> children;
    @Transient
    private Integer pageNo;
    @Transient
    private Integer pageSize;

    public interface UpdateTitle{}

    @Override
    public int compareTo(ArticleDTO o) {
        return this.getOrderNum().compareTo(o.getOrderNum());
    }
}
