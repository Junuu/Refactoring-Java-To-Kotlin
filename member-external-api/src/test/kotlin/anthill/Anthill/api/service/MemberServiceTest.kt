package anthill.Anthill.api.service

import TestFixture
import anthill.Anthill.domain.member.repository.MemberRepository
import anthill.Anthill.domain.member.service.MemberService
import anthill.Anthill.util.JwtUtil
import anthill.Anthill.util.PasswordEncodingUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class MemberServiceTest @Autowired constructor(
    val memberRepository: MemberRepository,
) {
    lateinit var memberService: MemberService

    val jwtUtil = JwtUtil()

    @BeforeEach
    fun setup() {
        memberService = MemberService(
            memberRepository = memberRepository,
            passwordEncodingUtil = PasswordEncodingUtil(),
            jwtUtil = jwtUtil,
        )
    }

    @Test
    fun `회원 가입시 비밀번호 해싱이 정상적으로 이루어진다`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        val originalPassword = memberRequestDTO.password

        val savedMemberId = memberService.join(memberRequestDTO)

        val result = memberRepository.findById(savedMemberId).orElseThrow()
        Assertions.assertEquals(result.id, savedMemberId)
        Assertions.assertNotEquals(result.password, originalPassword)
    }

    @Test
    fun `회원 가입시 중복이 발생한다`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        memberService.join(memberRequestDTO)

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            memberService.join(memberRequestDTO)
        }
    }

    @Test
    fun `nickName, userId, phoneNumber이 중복되면 true를 반환한다`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        memberService.join(memberRequestDTO)

        val userIdDuplicateResult = memberService.checkUserIdDuplicate("junwooKim")
        val nickNameDuplicateResult = memberService.checkNicknameDuplicate("junuuu")
        val phoneNumberDuplicateResult = memberService.checkPhoneNumberDuplicate("01012345678")

        Assertions.assertTrue(userIdDuplicateResult)
        Assertions.assertTrue(nickNameDuplicateResult)
        Assertions.assertTrue(phoneNumberDuplicateResult)
    }

    @Test
    fun `id, password가 올바르면 로그인시 토큰을 반환한다`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        memberService.join(memberRequestDTO)
        val memberLoginRequestDTO = TestFixture.memberLoginRequestDTO(
            password = "123456789"
        )

        val result = memberService.login(memberLoginRequestDTO)

        Assertions.assertTrue(jwtUtil.isUsable(result))
    }

    @Test
    fun `userId가 존재하지 않으면 IllegalStateException이 발생한다`() {
        val memberLoginRequestDTO = TestFixture.memberLoginRequestDTO(
            password = "123456789"
        )

        Assertions.assertThrows(IllegalStateException::class.java) {
            memberService.login(memberLoginRequestDTO)
        }
    }

    @Test
    fun `id, password가 올바르지 않으면 로그인에 false를 반환한다`() {
        val memberRequestDTO = TestFixture.memberRequestDTO()
        memberService.join(memberRequestDTO)
        val memberLoginRequestDTO = TestFixture.memberLoginRequestDTO(
            password = "wrongPassword"
        )

        Assertions.assertThrows(IllegalStateException::class.java) {
            memberService.login(memberLoginRequestDTO)
        }
    }

    @Test
    fun `userId가 존재하지 않으면 IllegalArgumentException이 발생한다`() {
        val NOT_EXIST_USERID = "NOT_EXIST_USERID"

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            memberService.findByUserID(NOT_EXIST_USERID)
        }
    }

    @Test
    fun `userId가 존재하면 MemberResponseDTO가 반환된다`() {
        memberRepository.save(TestFixture.makeMember())

        val result = memberService.findByUserID("userId")

        Assertions.assertEquals(result.userId, "userId")
        Assertions.assertEquals(result.name, "김준우")
    }
}