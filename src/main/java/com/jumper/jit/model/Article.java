package com.jumper.jit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Article {
    @NotNull(groups = {SaveContent.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Length(min = 2, max = 100, message = "title长度在2和100之间", groups = {AddSub.class, AddTop.class, AddSingleArticle.class})
    private String title;

    @Length(min = 20, message = "不能少于20个字符", groups = {AddSingleArticle.class, SaveContent.class})
    private String content;
    @Length(min = 1, message = "英文名长度在1和100之间", groups = {AddSingleArticle.class, SaveContent.class})
    private String enName;
    private String articleKeyword;

    private Integer status = Status.NO_CONTENT.code;//文章状态 0 无内容,1 已保存,2 已发布,3修改未发布

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;


    @NotNull(groups = {AddSub.class})
    @Column(insertable = false, updatable = false)
    private Integer pid;//上级文章

    @NotNull(groups = {AddSub.class, AddTop.class})
    @Column(insertable = false, updatable = false)
    private Integer sid;//所属主题
    private Integer orderNum = 1;//相同pid内的文章的排序

    @ManyToOne
    @JoinColumn(name = "sid")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Article article;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    //针对专题下顶级节点
    public interface AddTop {
    }

    //针对子节点
    public interface AddSub {
    }

    //针对单体文章
    public interface AddSingleArticle {
    }

    ;

    //针对保存内容
    public interface SaveContent {
    }

    ;

    public static enum Status {
        NO_CONTENT(0), SAVE_CONTENT(1), PUBLISHED(2), MODIFIED_UNPUBLISHED(3);
        private final int code;

        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }
}
