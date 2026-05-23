package yjh.ontongsal.testing.infrastructure.jpa.entity

import jakarta.persistence.*
import yjh.ontongsal.testing.common.web.security.crypto.converter.EncryptConverter
import yjh.ontongsal.testing.domain.User
import yjh.ontongsal.testing.domain.UserRole

@Entity
@Table(name = "users")
class UserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 100)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    @Convert(converter = EncryptConverter::class)
    val phone: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val role: UserRole,
) {
    fun toDomain(): User = User(
        id = id,
        email = email,
        password = password,
        phone = phone,
        role = role,
    )

    companion object {
        fun fromDomain(domain: User): UserJpaEntity = UserJpaEntity(
            id = domain.id,
            email = domain.email,
            password = domain.password,
            phone = domain.phone,
            role = domain.role,
        )
    }
}
