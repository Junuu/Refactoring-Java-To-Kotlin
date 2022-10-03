package anthill.Anthill.db.repository

import anthill.Anthill.db.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUserId(userId: String): Optional<Member>
    fun existsByUserId(userId: String): Boolean
    fun existsByNickName(nickName: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}