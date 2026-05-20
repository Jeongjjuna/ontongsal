package yjh.ontongsal.testing.infrastructure.jpa

import org.springframework.stereotype.Component
import yjh.ontongsal.testing.application.port.TodoRepository
import yjh.ontongsal.testing.domain.Todo
import yjh.ontongsal.testing.infrastructure.jpa.entity.TodoJpaEntity

@Component
class TodoRepositoryAdapter(
    private val todoJpaRepository: TodoJpaRepository,
) : TodoRepository {

    override fun findById(id: Long): Todo? {
        return todoJpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAllByUserId(userId: Long): List<Todo> {
        return todoJpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }

    override fun save(todo: Todo): Todo {
        return todoJpaRepository.save(TodoJpaEntity.fromDomain(todo)).toDomain()
    }

    override fun deleteById(id: Long) {
        todoJpaRepository.deleteById(id)
    }
}
