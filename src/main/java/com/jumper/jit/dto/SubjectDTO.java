package com.jumper.jit.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class SubjectDTO {

    @NotNull(groups = {UpdateTitle.class,UpdateRemark.class,UpdatePic.class,UpdateEnName.class})
    private Integer id;

    @Length(min = 1,max = 50,message = "字符数介于5与50之间",groups = UpdateTitle.class)
    private String subjectTitle;
    private Integer articleSum;

    private String remark;

    private Integer pageNo;
    private Integer pageSize;

    private Order orderByCreate;//按照创建日期排序
    private Order orderByUpdate;//按照更新日期排序

    private Boolean navigation=false;
    private Integer orderNum;

    @Length(min=1,max=100,message = "英文名长度在1~100之间",groups = UpdateEnName.class)
    private String enName;//英文名,主要作为目录使用

    private String pic;//图标

    public enum Order{
        DESC,ASC
    }
    @Transient
    private String keyword;

    @NotNull(groups = UpdatePic.class)
    @Transient
    private MultipartFile picFile;



    public interface UpdateTitle{}
    public interface UpdateRemark{}
    public interface UpdatePic{}
    public interface UpdateEnName{}

}
