package anthill.Anthill.db.repository

import anthill.Anthill.db.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUserId(userId: String): Member?
    fun existsByUserId(userId: String): Boolean
    fun existsByNickName(nickName: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}