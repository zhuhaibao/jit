package com.jumper.jit.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PageDTO {
    private Long total;
    private Integer totalPage;
    private Integer pageSize;
    private Integer pageNo;
    private List<?> content;

    public static PageDTO toPageDTO(Page<?> page){
        return PageDTO.builder()
                .total(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .pageSize(page.getSize())
                .pageNo(page.getNumber())
                .content(page.getContent())
                .build();
    }
}
