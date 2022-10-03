package anthill.Anthill.db.domain.board

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `게시글은 id,제목,내용,작성자,조회수,작성자아이디를 가진다`() {
        val board = TestFixture.makeBoard()

        Assertions.assertEquals(board.content, "myContent")
        Assertions.assertEquals(board.hits, 0)
        Assertions.assertEquals(board.title, "myTitle")
        Assertions.assertEquals(board.writer, "writer")
    }

    @Test
    fun `게시글의 제목와 내용을 변경할 수 있다`() {
        val board = TestFixture.makeBoard()
        val changedTitle = "changedTitle"
        val changedContent = "changedContent"

        board.changeInfo(
            changedTitle,
            changedContent
        )

        Assertions.assertEquals(board.title, changedTitle)
        Assertions.assertEquals(board.content, changedContent)
    }


}