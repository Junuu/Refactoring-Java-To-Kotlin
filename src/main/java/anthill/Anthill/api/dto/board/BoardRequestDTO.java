package anthill.Anthill.api.dto.board;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDTO {

    @NotNull(message = "사용자 아이디는 필수값입니다.")
    Long memberId;
    @NotBlank(message = "제목을 입력해주세요")
    String title;
    @NotBlank(message = "본문을 입력해주세요")
    String content;
    String writer;

    @Builder
    public BoardRequestDTO(Long memberId, String title, String content, String writer) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
