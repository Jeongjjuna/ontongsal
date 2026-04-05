package yjh.ontongsal.restapi.adpater.persistence.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import yjh.ontongsal.restapi.adpater.persistence.jpa.entity.ArticleEntity

interface ArticleJpaRepository : JpaRepository<ArticleEntity, Long>
