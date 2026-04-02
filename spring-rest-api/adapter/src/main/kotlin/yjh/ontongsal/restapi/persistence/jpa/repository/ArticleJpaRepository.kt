package yjh.ontongsal.restapi.persistence.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import yjh.ontongsal.restapi.persistence.jpa.entity.ArticleEntity

interface ArticleJpaRepository : JpaRepository<ArticleEntity, Long>
