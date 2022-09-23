package anthill.Anthill.api.dto.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginRequestDTO {
    private String userId;
    private String password;

    @Builder
    public MemberLoginRequestDTO(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
