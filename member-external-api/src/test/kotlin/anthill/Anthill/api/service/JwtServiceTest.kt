package anthill.Anthill.api.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class JwtServiceTest {
    private val jwtService: JwtService = JwtService()

    @Test
    fun `userID를 기반해서 header 토큰이 올바르게 생성`() {
        val userId = "testUserId"
        val exceptedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"

        val token = jwtService.create(
            "userId",
            userId,
            "access-token"
        )
        val st = StringTokenizer(token, ".")
        val headerToken = st.nextToken()

        assertEquals(headerToken, exceptedToken)
    }

    @Test
    fun `토큰이 유효성 검사를 통과한다`() {
        val userId = "testUserId"
        val token = jwtService.create(
            "userId",
            userId,
            "access-token"
        )

        val result = jwtService.isUsable(token)

        assertTrue(result)
    }

    @Test
    fun `토큰이 유효성 검사를 통과하지 않는다`() {
        val invalidToken = "is.invalid.token"

        val result = jwtService.isUsable(invalidToken)

        assertFalse(result)
    }
}