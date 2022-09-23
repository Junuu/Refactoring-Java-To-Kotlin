package anthill.Anthill.api.dto.member;

import anthill.Anthill.db.domain.member.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDTO {
    private String userId;
    private String nickName;
    private String name;
    private String phoneNumber;
    Address address;

    @Builder
    public MemberResponseDTO(String userId, String nickName, String name, String phoneNumber, Address address) {
        this.userId = userId;
        this.nickName = nickName;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
