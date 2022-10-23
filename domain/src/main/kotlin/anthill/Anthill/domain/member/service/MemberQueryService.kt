package anthill.Anthill.domain.member.service

import anthill.Anthill.domain.member.dto.MemberResponseDTO
import anthill.Anthill.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryService(
    val memberRepository: MemberRepository,
) {

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

    fun checkNicknameDuplicate(nickName: String): Boolean {
        return memberRepository.existsByNickName(nickName)
    }

    fun checkUserIdDuplicate(userId: String): Boolean {
        return memberRepository.existsByUserId(userId)
    }

    fun checkPhoneNumberDuplicate(phoneNumber: String): Boolean {
        return memberRepository.existsByPhoneNumber(phoneNumber)
    }
}