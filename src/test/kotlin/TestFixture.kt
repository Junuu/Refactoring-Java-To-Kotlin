import anthill.Anthill.api.dto.board.BoardDeleteDTO
import anthill.Anthill.api.dto.board.BoardRequestDTO
import anthill.Anthill.api.dto.board.BoardUpdateDTO
import anthill.Anthill.api.dto.member.MemberLoginRequestDTO
import anthill.Anthill.api.dto.member.MemberRequestDTO
import anthill.Anthill.db.domain.board.Board
import anthill.Anthill.db.domain.member.Address
import anthill.Anthill.db.domain.member.Member

object TestFixture {
    fun makeBoard(): Board {
        return Board.builder()
            .id(1L)
            .title("myTitle")
            .content("myContent")
            .hits(0)
            .writer("writer")
            .member(makeMember())
            .build()
    }

    fun boardRequestDTO(
        savedMemberId: Long
    ): BoardRequestDTO {
        val userInputTitle = "userInputTitle"
        val userInputContent = "userInputContent"
        val userInputWriter = "userInputWriter"
        return BoardRequestDTO(
            savedMemberId,
            userInputTitle,
            userInputContent,
            userInputWriter,
        )
    }

    fun boardUpdateDTO(
        savedBoardId: Long,
        savedMemberId: Long,
    ): BoardUpdateDTO {
        val userInputUpdateTitle = "userInputUpdateTitle"
        val userInputUpdateContent = "userInputUpdateContent"
        val savedWriter = "userInputWriter"
        return BoardUpdateDTO(
            savedBoardId,
            userInputUpdateTitle,
            userInputUpdateContent,
            savedWriter,
            savedMemberId,
        )
    }

    fun boardDeleteDTO(
        savedBoardId: Long,
        savedMemberId: Long,
    ): BoardDeleteDTO {
        return BoardDeleteDTO(
            savedBoardId,
            savedMemberId,
        )
    }

    fun makeMember(): Member {
        return Member.builder()
            .id(1L)
            .userId("userId")
            .password("123456789")
            .nickName("junwoo")
            .name("김준우")
            .phoneNumber("01012345678")
            .build()
    }

    fun memberRequestDTO(): MemberRequestDTO {
        return MemberRequestDTO.builder()
            .userId("userId")
            .password("correctPassword")
            .nickName("junwoo")
            .name("김준우")
            .phoneNumber("01012345678")
            .address(
                Address(
                    "zipCode",
                    "streetNameAddress",
                    "detailAddress",
                )
            )
            .build()
    }

    fun memberLoginRequestDTO(
        password : String,
    ): MemberLoginRequestDTO {
        return MemberLoginRequestDTO
            .builder()
            .userId("userId")
            .password(password)
            .build()
    }
}