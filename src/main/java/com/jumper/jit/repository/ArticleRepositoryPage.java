package com.jumper.jit.repository;

import com.jumper.jit.dto.ArticleDTO;
import com.jumper.jit.dto.SubjectArticleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepositoryPage extends JpaRepository<ArticleDTO,Integer>, JpaSpecificationExecutor<ArticleDTO> {

    @Query("select new com.jumper.jit.dto.SubjectArticleDTO(a.title,a.id,a.pid,a.orderNum,b.subjectTitle,a.sid) " +
            "from article a ,Subject b where a.sid=b.id" +
            " and a.sid is not null")
    List<SubjectArticleDTO> findAllSubjectsWithArticles();

    List<ArticleDTO> findByTitleContainingIgnoreCaseAndSidIsNotNullOrderBySidAscPidAscOrderNumAsc(String title);
}
