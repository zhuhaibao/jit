package com.jumper.jit.repository;

import com.jumper.jit.dto.SimpleSubjectDTO;
import com.jumper.jit.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,Integer>, JpaSpecificationExecutor<Subject> {
    @Modifying
    @Query(value = "update Subject set subjectTitle=:subjectTitle where id=:id")
    Integer updateSubjectTitleById(String subjectTitle,Integer id);

    @Modifying
    @Query("update Subject set articleSum=articleSum+:num where id=:id")
    Integer setSubjectArticleSum(Integer id,Integer num);

    //查询所有主题
    List<SimpleSubjectDTO> queryAllByIdNotNullOrderByCreatedAtDescUpdatedAtDesc();
}
