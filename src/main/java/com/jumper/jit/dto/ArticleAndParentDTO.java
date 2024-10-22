package com.jumper.jit.dto;

import com.jumper.jit.model.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专门定义的dto,内部包含父节点和主题的关联
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "articleAndParent")
@Table(name = "article")
public class ArticleAndParentDTO{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Integer orderNum;//相同pid内的文章的排序

    @ManyToOne
    @JoinColumn(name = "pid")
    private ArticleAndParentDTO parent;
    @ManyToOne
    @JoinColumn(name = "sid")
    private Subject subject;
}
