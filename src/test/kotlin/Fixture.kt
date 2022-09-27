import anthill.Anthill.db.domain.board.Board
import anthill.Anthill.db.domain.member.Member

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

fun makeMember(): Member {
    return Member.builder()
        .id(1L)
        .userId("userId")
        .password("123456789")
        .nickName("junwoo")
        .name("김준우")
        .build()

}