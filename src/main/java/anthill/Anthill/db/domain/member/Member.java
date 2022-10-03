package anthill.Anthill.db.domain.member;

import anthill.Anthill.api.dto.member.MemberResponseDTO;
import anthill.Anthill.db.domain.board.Board;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "member"
        , indexes = {
        @Index(name = "unique_idx_user_id", columnList = "user_id", unique = true),
        @Index(name = "unique_idx_nickname", columnList = "nickname", unique = true),
        @Index(name = "unique_idx_phone_number", columnList = "phone_number", unique = true)
}
)


@Entity
@DynamicUpdate
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySQL의 AUTO_INCREMENT를 사용
    @Column(name = "member_id")
    private Long id;

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickName;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 40)
    private String phoneNumber;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public Member(Long id, String userId, String password, String nickName, String name, String phoneNumber, Address address) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.nickName = nickName;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public MemberResponseDTO toMemberResponseDTO() {
        return MemberResponseDTO.builder()
                                .userId(this.getUserId())
                                .nickName(this.getNickName())
                                .name(this.getName())
                                .phoneNumber(this.getPhoneNumber())
                                .address(this.getAddress())
                                .build();
    }

}
