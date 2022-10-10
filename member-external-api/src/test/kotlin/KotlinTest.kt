import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KotlinTest {
    @Test
    fun `자바 프로젝트에서 코틀린이 제대로 동작하는지 테스트 합니다`(){
        val string = "kotlin"

        val result = string == "kotlin"

        Assertions.assertTrue(result)
    }

}