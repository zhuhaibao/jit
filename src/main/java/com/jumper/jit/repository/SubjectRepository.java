package com.jumper.jit.repository;

import com.jumper.jit.dto.SimpleSubjectDTO;
import com.jumper.jit.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer>, JpaSpecificationExecutor<Subject> {
    @Modifying
    @Query(value = "update Subject set subjectTitle=:subjectTitle where id=:id")
    Integer updateSubjectTitleById(String subjectTitle, Integer id);

    @Modifying
    @Query("update Subject set articleSum=articleSum+:num where id=:id")
    Integer setSubjectArticleSum(Integer id, Integer num);

    //查询所有主题
    List<SimpleSubjectDTO> queryAllByIdNotNullOrderByCreatedAtDescUpdatedAtDesc();

    Subject findByEnNameEqualsIgnoreCase(String eName);

    /***************************下面是导航操作*******************************/

    @Modifying
    @Query("update Subject set orderNum=:orderNum where id=:id")
    void setSubjectOrderNum(Integer id, Integer orderNum);

    @Modifying
    @Query("update Subject set orderNum=:orderNum,navigation=true where id=:id")
    void setSubjectAsNavigation(Integer id, Integer orderNum);

    @Modifying
    @Query("update Subject set orderNum=orderNum+:num where orderNum between :startNum and :endNum and orderNum is not null")
    void setSubjectOrderNumBetween(Integer num, Integer startNum, Integer endNum);

    @Modifying
    @Query("update Subject set orderNum=:orderNum,navigation=:navigation where id=:id")
    void updateSubjectByNavigation(Integer orderNum, Boolean navigation, Integer id);

    @Modifying
    @Query("update Subject set orderNum=orderNum+:num where orderNum>:orderNum and orderNum is not null")
    void setOrderNumAfter(Integer num, Integer orderNum);

    @Query("select count(id) from Subject where orderNum is not null")
    Integer findNavigationCount();

    //根据导航状态查询
    List<Subject> findSubjectByNavigationOrderByOrderNumAsc(boolean navigation);

    //根据导航状态和关键词查询
    List<Subject> findSubjectByNavigationAndSubjectTitleContainingIgnoreCase(boolean navigation, String subjectTitle);

}
