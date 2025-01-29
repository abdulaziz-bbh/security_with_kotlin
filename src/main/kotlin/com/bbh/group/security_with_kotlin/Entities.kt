package com.bbh.group.security_with_kotlin

import jakarta.persistence.*
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

@MappedSuperclass
abstract class BaseEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) val createdAt: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) val updatedAt: Date? = null,
    @CreatedBy val createdBy: Long? = null,
    @LastModifiedBy val updatedBy: Long? = null,
    var deleted: Boolean = false
)

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false) val firstName: String,
    @Column(nullable = false) val lastName: String,
    @Column(nullable = false) val email: String,
    @Column(nullable = false) val password: String,
    @Column(nullable = false) var enabled: Boolean = false
): BaseEntity()

@Entity
class VerificationToken(
    @Column(nullable = false) val token: String,
    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER) var user: User,
    val expiryDate: Date = Calendar.getInstance().apply {
        add(Calendar.MINUTE, 60 * 24)
    }.time
):BaseEntity()