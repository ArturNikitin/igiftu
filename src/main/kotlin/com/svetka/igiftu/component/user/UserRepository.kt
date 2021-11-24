package com.svetka.igiftu.component.user

import com.svetka.igiftu.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.data.jpa.repository.EntityGraph

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByEmail(email: String): Optional<User>
    fun existsByIdOrEmail(id: Long, email: String): Boolean
    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = [
            "boards",
            "boards.wishes"
        ]
    )
    fun findUserById(id: Long): Optional<User>
}