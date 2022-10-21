package anthill.Anthill.domain.member.service

import anthill.Anthill.domain.member.dto.MemberLoginRequestDTO
import anthill.Anthill.domain.member.dto.MemberRequestDTO
import anthill.Anthill.domain.member.dto.MemberResponseDTO
import anthill.Anthill.domain.member.repository.MemberRepository
import anthill.Anthill.util.JwtUtil
import anthill.Anthill.util.PasswordEncodingUtil
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException.Unauthorized

@Service
@Transactional
class MemberService(
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