package com.baekseju.howmuch.pos.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "`order`")
@EntityListeners(value = [AuditingEntityListener::class])
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    var menus: String,
    var price: Int,
    var payWith: String,
    @CreatedDate
    var createdAt: Instant? = null,
    @LastModifiedDate
    var updatedAt: Instant? = null,
    deletedAt: Instant? = null
) : SoftDeleteEntity(deletedAt)
