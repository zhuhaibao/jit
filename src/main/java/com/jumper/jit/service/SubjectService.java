package com.jumper.jit.service;

import com.jumper.jit.dto.SimpleSubjectDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.io.IOException;
import java.util.List;


public interface SubjectService {
    Subject add(Subject subject) throws IOException;
    Subject updateSubject(SubjectDTO subject) throws IOException;
    void delete(Integer id);
    Page<Subject> findSubjectBy(SubjectDTO dto);
    Subject findById(Integer id);

    List<SimpleSubjectDTO> findAllSimpleSubject();

    /**
     * 调整文章数量
     * @param id 专题id
     * @param num 文章数量的增减
     * @return 修改数量
     */
    @Modifying
    @Query("update Subject set articleSum=articleSum+:num where id=:id")

    Integer updateSubjectArticleSum(Integer id,Integer num);

    /**
     * 根据导航状态查询主题
     * @param navigation true 已经是导航 false 非导航
     * @param keyword 主题关键词
     * @return 列表
     */
    List<Subject> findByNavigation(boolean navigation,String keyword);

    /**
     * 把节点插入到目标节点后
     * @param id 当前节点
     * @param targetId 目标节点
     */
    void moveTo(Integer id,Integer targetId);

    /**
     * 删除当前主题作为导航
     * @param id 主题id
     */
    void deleteNavigation(Integer id);

    /**
     * 添加当前主题作为导航
     * @param id 主题id
     */
    Subject addNavigation(Integer id);

}
