package anthill.Anthill.api.service

import anthill.Anthill.api.dto.member.MemberLoginRequestDTO
import anthill.Anthill.api.dto.member.MemberRequestDTO
import anthill.Anthill.api.dto.member.MemberResponseDTO
import anthill.Anthill.db.repository.MemberRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberService(
    val memberRepository: MemberRepository,
) {
    fun join(memberRequestDTO: MemberRequestDTO): Long {
        validateIsDuplicate(memberRequestDTO)
        memberRequestDTO.hashingPassword()
        return memberRepository.save(memberRequestDTO.toEntity()).id
    }

    private fun validateIsDuplicate(member: MemberRequestDTO) {
        require(!checkPhoneNumberDuplicate(member.phoneNumber))
        require(!checkNicknameDuplicate(member.nickName))
        require(!checkUserIdDuplicate(member.userId))
    }

    fun checkNicknameDuplicate(nickName: String): Boolean {
        return memberRepository.existsByNickName(nickName)
    }

    fun checkUserIdDuplicate(userId: String): Boolean {
        return memberRepository.existsByUserId(userId)
    }

    fun checkPhoneNumberDuplicate(phoneNumber: String): Boolean {
        return memberRepository.existsByPhoneNumber(phoneNumber)
    }


    fun login(memberLoginRequestDTO: MemberLoginRequestDTO): Boolean {
        val user = memberRepository.findByUserId(memberLoginRequestDTO.userId)
        val userPassword = user.orElseThrow { IllegalStateException() }
            .password
        return BCrypt.checkpw(memberLoginRequestDTO.password, userPassword)
    }

    fun findByUserID(userId: String): MemberResponseDTO {
        return memberRepository.findByUserId(userId)
            .orElseThrow { IllegalArgumentException() }
            .toMemberResponseDTO()
    }
}