package anthill.Anthill.domain.member.service

import anthill.Anthill.domain.member.dto.MemberLoginRequestDTO
import anthill.Anthill.domain.member.dto.MemberRequestDTO
import anthill.Anthill.domain.member.dto.MemberResponseDTO
import anthill.Anthill.domain.member.repository.MemberRepository
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
        val user = memberRepository.findByUserId(memberLoginRequestDTO.userId) ?: throw IllegalStateException()
        val userPassword = user.password
        return BCrypt.checkpw(memberLoginRequestDTO.password, userPassword)
    }

    fun findByUserID(userId: String): MemberResponseDTO {
        val member = memberRepository.findByUserId(userId) ?: throw IllegalArgumentException()
        return MemberResponseDTO(
            userId = member.userId,
            nickName = member.nickName,
            name = member.name,
            phoneNumber = member.phoneNumber,
            address = member.address,
        )

    }
}