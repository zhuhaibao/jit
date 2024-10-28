package com.jumper.jit.dto.deploy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 前端页面顶部导航信息
 */
@Data
@AllArgsConstructor
@Builder
public class TopNavList {
    private String subName;//主题名
    private String remark;//简介
    private String pic;
    private String dir;//文件夹
}
