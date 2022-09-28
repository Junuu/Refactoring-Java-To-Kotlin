package anthill.Anthill.service.domain.member

import TestFixture
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MemberTest {
    @Test
    fun `회원은 id,userId,password,nickName,name을 가진다`(){
        val member = TestFixture.makeMember()

        Assertions.assertEquals(member.id, 1L)
        Assertions.assertEquals(member.userId, "userId")
        Assertions.assertEquals(member.password, "123456789")
        Assertions.assertEquals(member.nickName, "junwoo")
        Assertions.assertEquals(member.name, "김준우")
    }

    @Test
    fun `회원은 nickName을 변경할 수 있다`(){
        val member = TestFixture.makeMember()
        val changedNickName = "changedNickName"

        member.changeNickName(changedNickName)

        Assertions.assertEquals(member.nickName, changedNickName)
    }
}