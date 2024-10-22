package com.jumper.jit.service;

import com.jumper.jit.dto.SimpleSubjectDTO;
import com.jumper.jit.dto.SubjectDTO;
import com.jumper.jit.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SubjectService {
    Subject save(Subject subject);
    Subject updateSubject(SubjectDTO subject);
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
}
