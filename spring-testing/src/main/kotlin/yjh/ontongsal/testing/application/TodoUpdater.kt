package yjh.ontongsal.testing.application

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.domain.Todo
import yjh.ontongsal.testing.infrastructure.jpa.TodoJpaRepository
import yjh.ontongsal.testing.infrastructure.jpa.entity.TodoJpaEntity

@Component
class TodoUpdater(
    private val todoJpaRepository: TodoJpaRepository,
) {

    fun update(todo: Todo): Todo {
        return todoJpaRepository.save(TodoJpaEntity.fromDomain(todo)).toDomain()
    }
}
