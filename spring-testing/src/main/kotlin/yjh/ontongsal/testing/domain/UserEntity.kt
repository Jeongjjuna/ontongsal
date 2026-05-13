package yjh.ontongsal.testing.domain

import jakarta.persistence.*
import yjh.ontongsal.testing.common.persistence.converter.EncryptConverter

@Entity
@Table(name = "users")
class UserEntity(
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
)
