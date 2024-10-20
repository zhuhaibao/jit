package com.jumper.jit.repository;

import com.jumper.jit.dto.SimpleArticleWithContentDTO;
import com.jumper.jit.dto.SimpleArticleWithoutContentDTO;
import com.jumper.jit.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Integer>{

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where id=:id")
    void addOrderNumByOne(Integer id,Integer num);

    @Modifying
    @Query("update Article set orderNum = :orderNum where id=:id")
    void setOrderNum(Integer id,Integer orderNum);

    @Modifying
    @Query("update Article set pid = :pid where id=:id")
    void updatePid(Integer id,Integer pid);
    @Modifying
    @Query("update Article set pid = :pid,orderNum=:orderNum where id=:id")
    void updatePidAndOrderNum(Integer id,Integer pid,Integer orderNum);

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where pid=:pid and orderNum between :startNum and :endNum")
    void setAllOrderNumByBetween(Integer pid, Integer startNum, Integer endNum, Integer num);

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where pid=:pid and orderNum > :orderNum")
    void setAllOrderNumByAfter(Integer pid, Integer orderNum, Integer num);

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where pid=:pid and orderNum>:orderNum")
    void setAllOrderNumAfterCurrentNodeByPid(Integer pid,Integer orderNum,Integer num);
    @Modifying
    @Query("update Article set orderNum = orderNum+:num where pid=:pid and orderNum<:orderNum")
    void setAllOrderNumBeforeCurrentNodeByPid(Integer pid,Integer orderNum,Integer num);

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where sid=:sid and pid is null")
    void setAllOrderNumBySid(Integer sid,Integer num);

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where sid=:sid and orderNum>:orderNum and pid is null")
    void setAllOrderNumAfterCurrentNodeBySid(Integer sid,Integer orderNum,Integer num);

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where sid=:sid and orderNum<:orderNum and pid is null")
    void setAllOrderNumBeforeCurrentNodeBySid(Integer sid,Integer orderNum,Integer num);

    @Modifying
    @Query("update Article set orderNum = orderNum+:num where sid=:sid and orderNum between :startNum and :endNum and pid is null")
    void setAllOrderNumBetweenBySid(Integer sid,Integer num,Integer startNum,Integer endNum);

    @Modifying
    @Query("update Article set content = :content where id=:id")
    void updateArticleContent(Integer id,String content);

    @Modifying
    @Query("update Article set title = :title where id=:id")
    void updateArticleTitle(Integer id,String title);

    @Modifying
    @Query("delete from Article where id=:id")
    void delById(Integer id);

    @Query("select count(id) from Article where pid=:id")
    Integer findChildrenCountByPid(Integer id);

    @Query("select max(orderNum) from Article where sid=:sid and pid is null")
    Integer findTopLevelArticleCountBySid(Integer sid);


    List<Article> findByPid(Integer pid);

    Boolean existsByPid(Integer pid);

    SimpleArticleWithContentDTO getArticleById(Integer id);

    @Query("select new com.jumper.jit.dto.SimpleArticleWithoutContentDTO(id,title,pid,sid,orderNum) from article where sid=:sid")
    List<SimpleArticleWithoutContentDTO> findArticlesWithoutContent(Integer sid);

    @Query("select new com.jumper.jit.dto.SimpleArticleWithoutContentDTO(id,title,pid,sid,orderNum) from article where pid=:pid")
    List<SimpleArticleWithoutContentDTO> findArticlesWithoutContentByPid(Integer pid);

    @Query("select new com.jumper.jit.dto.SimpleArticleWithoutContentDTO(id,title,pid,sid,orderNum) from article where id=:id")
    SimpleArticleWithoutContentDTO getArticleWithoutContentById(Integer id);

}
