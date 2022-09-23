package anthill.Anthill.api.dto.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDeleteDTO {
    Long id;
    String writer;

    @Builder
    public BoardDeleteDTO(Long id, String writer) {
        this.id = id;
        this.writer = writer;
    }
}

