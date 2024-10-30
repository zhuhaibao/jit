package com.jumper.jit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class SiteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Length(min = 2, max = 500, message = "字符数介于2与500之间")
    private String siteKeywords;
    @Length(min = 2, max = 500, message = "字符数介于2与500之间")
    private String siteDesc;

    private String siteIco;//图标

    @Transient
    private MultipartFile picFile;

}
