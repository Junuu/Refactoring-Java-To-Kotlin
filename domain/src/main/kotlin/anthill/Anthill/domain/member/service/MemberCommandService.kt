package anthill.Anthill.domain.member.service

import anthill.Anthill.domain.member.dto.MemberLoginRequestDTO
import anthill.Anthill.domain.member.dto.MemberRequestDTO
import anthill.Anthill.domain.member.repository.MemberRepository
import anthill.Anthill.util.JwtUtil
import anthill.Anthill.util.PasswordEncodingUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class MemberCommandService(
    val memberRepository: MemberRepository,
    val passwordEncodingUtil: PasswordEncodingUtil,
    val jwtUtil: JwtUtil,
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

    fun login(memberLoginRequestDTO: MemberLoginRequestDTO): String {
        val savedUser = memberRepository.findByUserId(memberLoginRequestDTO.userId) ?: throw IllegalStateException()
        val savedPassword = savedUser.password
        val result = passwordEncodingUtil.checkPassword(memberLoginRequestDTO.password, savedPassword)
        if(result == false){
            throw IllegalStateException()
        }
        val token = jwtUtil.create("userId", memberLoginRequestDTO.userId, "access-token")
        return token
    }
}