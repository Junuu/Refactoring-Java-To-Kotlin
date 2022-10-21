package anthill.Anthill.domain.member.service

import anthill.Anthill.domain.member.dto.MemberLoginRequestDTO
import anthill.Anthill.domain.member.dto.MemberRequestDTO
import anthill.Anthill.domain.member.dto.MemberResponseDTO
import anthill.Anthill.domain.member.repository.MemberRepository
import anthill.Anthill.util.PasswordEncodingUtil
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberService(
    val memberRepository: MemberRepository,
    val passwordEncodingUtil: PasswordEncodingUtil,
) {
    fun join(memberRequestDTO: MemberRequestDTO): Long {
        validateIsDuplicate(memberRequestDTO)
        val hashedPassword = passwordEncodingUtil.hashingPassword(memberRequestDTO.password)
        memberRequestDTO.setEncodedPassword(hashedPassword)
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
        val savedUser = memberRepository.findByUserId(memberLoginRequestDTO.userId) ?: throw IllegalStateException()
        val savedPassword = savedUser.password
        return passwordEncodingUtil.checkPassword(memberLoginRequestDTO.password, savedPassword)
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