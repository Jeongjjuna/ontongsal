package yjh.ontongsal.api.post.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.ontongsal.api.common.BaseErrorCode
import yjh.ontongsal.api.common.BaseException
import yjh.ontongsal.api.post.application.port.PostRepository
import yjh.ontongsal.api.post.domain.Post
import yjh.ontongsal.api.post.domain.PostCreateCommand
import yjh.ontongsal.api.post.domain.PostUpdateCommand

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {

    @Transactional
    fun create(command: PostCreateCommand): Long {
        val post = Post.create(command)
        return postRepository.save(post).id
    }

    @Transactional
    fun update(id: Long, command: PostUpdateCommand): Long {
        val post = retrieve(id)
        post.update(command)
        return postRepository.save(post).id
    }

    @Transactional
    fun delete(id: Long): Long {
        val post = retrieve(id)
        post.delete()
        return postRepository.save(post).id
    }

    fun retrieve(id: Long): Post {
        return postRepository.findByIdOrNull(id) ?: throw BaseException(BaseErrorCode.NOT_FOUND_POST)
    }

    fun retrieveAll(pageable: Pageable): Page<Post> {
        return postRepository.findAll(pageable)
    }
}
