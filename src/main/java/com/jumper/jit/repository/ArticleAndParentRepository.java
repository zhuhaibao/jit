package com.jumper.jit.repository;

import com.jumper.jit.dto.ArticleAndParentDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleAndParentRepository extends JpaRepository<ArticleAndParentDTO,Integer>{
    List<ArticleAndParentDTO> findByTitleContainingIgnoreCase(String title);
}
