package anthill.Anthill.domain.member.repository

import anthill.Anthill.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUserId(userId: String): Member?
    fun existsByUserId(userId: String): Boolean
    fun existsByNickName(nickName: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}