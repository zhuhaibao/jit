package com.jumper.jit.repository;

import com.jumper.jit.dto.ArticleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArticleRepositoryPage extends JpaRepository<ArticleDTO,Integer>, JpaSpecificationExecutor<ArticleDTO> {
}
