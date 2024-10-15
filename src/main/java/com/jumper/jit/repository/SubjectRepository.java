package com.jumper.jit.repository;

import com.jumper.jit.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubjectRepository extends JpaRepository<Subject,Integer>, JpaSpecificationExecutor<Subject> {
    @Modifying
    @Query(value = "update Subject set subjectTitle=:subjectTitle where id=:id")
    Integer updateSubjectTitleById(String subjectTitle,Integer id);
}
