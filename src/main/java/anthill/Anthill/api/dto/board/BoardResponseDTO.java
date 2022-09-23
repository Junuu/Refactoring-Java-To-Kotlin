package anthill.Anthill.api.dto.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDTO {
    Long id;
    String title;
    String content;
    String writer;
    Long hits;

    @Builder
    public BoardResponseDTO(Long id, String title, String content, String writer, Long hits) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.hits = hits;
    }
}
