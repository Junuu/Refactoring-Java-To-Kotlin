package anthill.Anthill.api.dto.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDeleteDTO {
    Long id;
    Long memberId;

    @Builder
    public BoardDeleteDTO(Long id, Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }
}

