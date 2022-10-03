import anthill.Anthill.api.dto.board.*
import anthill.Anthill.api.dto.member.MemberLoginRequestDTO
import anthill.Anthill.api.dto.member.MemberRequestDTO
import anthill.Anthill.api.dto.member.MemberResponseDTO
import anthill.Anthill.db.domain.board.Board
import anthill.Anthill.db.domain.member.Address
import anthill.Anthill.db.domain.member.Member

object TestFixture {
    fun makeBoard(): Board {
        return Board(
            title = "myTitle",
            content = "myContent",
            hits = 0L,
            writer = "writer",
            member = makeMember(),
        )
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

    fun boardResponseDTO(): BoardResponseDTO {
        return BoardResponseDTO(
            id = 1L,
            title = "제목",
            content = "본문",
            writer = "작성자",
            hits = 1L,
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

    fun boardPageResponseDTO(): BoardPageResponseDTO {
        val data: MutableList<BoardInfoDTO> = ArrayList()
        for (i in 1L..2L) {
            data.add(
                BoardInfoDTO.builder()
                    .id(i)
                    .title("본문")
                    .content("제목")
                    .writer("작성자")
                    .hits(i)
                    .build()
            )
        }
        return BoardPageResponseDTO
            .builder()
            .contents(data)
            .totalPage(1)
            .totalElements(2)
            .curPage(1)
            .size(10)
            .build()
    }

    fun makeMember(): Member {
        return Member(
            userId = "userId",
            password = "123456789",
            nickName = "junwoo",
            name = "김준우",
            phoneNumber = "01012345678",
            address = Address(
                streetNameAddress = "경기도 시흥시",
                detailAddress = "XX아파트 XX호",
                zipCode = "429-010",
            )
        )
    }

    fun memberLoginRequestDTO(
        userId: String = "junwooKim",
        password: String,
    ): MemberLoginRequestDTO {
        return MemberLoginRequestDTO(
            userId = userId,
            password = password,
        )
    }

    fun memberRequestDTO(
        streetNameAddress: String = "경기도 시흥시",
        detailAddress: String = "XX아파트 XX호",
        zipCode: String = "429-010",
        userId: String = "junwooKim",
        name: String = "KIM",
        nickName: String = "junuuu",
        password: String = "123456789",
        phoneNumber: String = "01012345678",
    ): MemberRequestDTO {
        val myAddress = Address(
            streetNameAddress = streetNameAddress,
            detailAddress = detailAddress,
            zipCode = zipCode,
        )
        return MemberRequestDTO(
            userId = userId,
            name = name,
            nickName = nickName,
            password = password,
            phoneNumber = phoneNumber,
            address = myAddress,
        )
    }

    fun memberResponseDTO(): MemberResponseDTO {
        val myAddress = Address(
            streetNameAddress = "경기도 시흥시",
            detailAddress = "XX아파트 XX호",
            zipCode = "429-010",
        )
        return MemberResponseDTO(
            userId = "test",
            name = "test",
            nickName = "test",
            phoneNumber = "01012345678",
            address = myAddress,
        )
    }

}
