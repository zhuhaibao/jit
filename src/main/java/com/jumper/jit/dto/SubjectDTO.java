package com.jumper.jit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
public class SubjectDTO {

    @NotNull(groups = {UpdateTitle.class,UpdateRemark.class})
    private Integer id;

    @Length(min = 5,max = 50,message = "字符数介于5与50之间",groups = UpdateTitle.class)
    private String subjectTitle;
    private Integer articleSum;

    private String remark;

    private Integer pageNo;
    private Integer pageSize;

    private Order orderByCreate;//按照创建日期排序
    private Order orderByUpdate;//按照更新日期排序

    public enum Order{
        DESC,ASC
    }
    private List<SubjectArticleDTO> articles;
    public interface UpdateTitle{}
    public interface UpdateRemark{}

}
