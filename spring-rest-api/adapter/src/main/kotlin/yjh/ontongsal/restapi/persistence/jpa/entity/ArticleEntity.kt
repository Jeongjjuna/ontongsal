package yjh.ontongsal.restapi.persistence.jpa.entity

import jakarta.persistence.*

@Entity
@Table(name = "articles")
class ArticleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val authorId: Long,

    @Column(nullable = false)
    var content: String,

) : BaseEntity()
