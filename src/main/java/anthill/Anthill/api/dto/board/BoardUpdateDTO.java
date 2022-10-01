package anthill.Anthill.api.dto.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUpdateDTO {
    Long id;
    String title;
    String content;
    String writer;
    Long memberId;

    @Builder
    public BoardUpdateDTO(Long id, String title, String content, String writer, Long memberId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.memberId = memberId;
    }
}
