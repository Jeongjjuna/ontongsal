package yjh.ontongsal.restapi.adapter.persistence.jpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import yjh.ontongsal.restapi.adapter.persistence.jpa.entity.BoardEntity

interface BoardJpaRepository : JpaRepository<BoardEntity, Long> {

    fun findAllBy(pageable: Pageable): Page<BoardEntity>
}
