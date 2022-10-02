import anthill.Anthill.api.dto.board.*
import anthill.Anthill.api.dto.member.MemberLoginRequestDTO
import anthill.Anthill.api.dto.member.MemberRequestDTO
import anthill.Anthill.api.dto.member.MemberResponseDTO
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

    fun boardResponseDTO(): BoardResponseDTO {
        return BoardResponseDTO.builder()
            .id(1L)
            .title("제목")
            .content("본문")
            .writer("작성자")
            .hits(1L)
            .build()
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
        return Member.builder()
            .id(1L)
            .userId("userId")
            .password("123456789")
            .nickName("junwoo")
            .name("김준우")
            .phoneNumber("01012345678")
            .build()
    }

    fun memberLoginRequestDTO(
        userId: String = "junwooKim",
        password: String,
    ): MemberLoginRequestDTO {
        return MemberLoginRequestDTO
            .builder()
            .userId(userId)
            .password(password)
            .build()
    }

    fun memberRequestDTO(): MemberRequestDTO {
        val myAddress = Address.builder()
            .streetNameAddress("경기도 시흥시")
            .detailAddress("XX아파트 XX호")
            .zipCode("429-010")
            .build()
        return MemberRequestDTO.builder()
            .userId("junwooKim")
            .name("KIM")
            .nickName("junuuu")
            .password("123456789")
            .phoneNumber("01012345678")
            .address(myAddress)
            .build()
    }

    fun memberResponseDTO(): MemberResponseDTO {
        val myAddress = Address.builder()
            .streetNameAddress("경기도 시흥시")
            .detailAddress("XX아파트 XX호")
            .zipCode("429-010")
            .build()
        return MemberResponseDTO.builder()
            .userId("test")
            .name("test")
            .nickName("test")
            .phoneNumber("01012345678")
            .address(myAddress)
            .build()
    }

}
