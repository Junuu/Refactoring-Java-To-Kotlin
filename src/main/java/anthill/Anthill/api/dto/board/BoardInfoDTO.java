package anthill.Anthill.api.dto.board;


import anthill.Anthill.db.domain.board.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardInfoDTO {
    Long id;
    String title;
    String content;
    String writer;
    Long hits;

    @Builder
    public BoardInfoDTO(Long id, String title, String content, String writer, Long hits) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.hits = hits;
    }

    public static BoardInfoDTO toBoardPagingDTO(Board board) {
        return BoardInfoDTO.builder()
                           .id(board.getId())
                           .title(board.getTitle())
                           .content(board.getContent())
                           .writer(board.getWriter())
                           .hits(board.getHits())
                           .build();
    }
}
