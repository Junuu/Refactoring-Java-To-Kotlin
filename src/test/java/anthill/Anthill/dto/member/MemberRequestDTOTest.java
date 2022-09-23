package anthill.Anthill.dto.member;

import anthill.Anthill.api.dto.member.MemberRequestDTO;
import anthill.Anthill.db.domain.member.Address;
import anthill.Anthill.db.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberRequestDTOTest {

    @Test
    @DisplayName("회원 비밀번호 해싱화")
    public void memberPasswordHashingTest() {
        //given
        String originPassword = "Test";
        MemberRequestDTO memberRequestDTO = getMemberRequestDTO();
        //when
        memberRequestDTO.hashingPassword();

        //then
        Assertions.assertThat(originPassword)
                  .isNotEqualTo(memberRequestDTO.getPassword());
    }

    @Test
    @DisplayName("DTO를 Entity로 변경하는 테스트")
    public void memberRequestDtoToEntityTest() {
        //given
        MemberRequestDTO memberRequestDTO = getMemberRequestDTO();
        //when
        Member member = memberRequestDTO.toEntity();

        //then
        Assertions.assertThat(member)
                  .isInstanceOf(Member.class);
    }

    private MemberRequestDTO getMemberRequestDTO() {
        MemberRequestDTO memberRequestDTO = MemberRequestDTO.builder()
                                                            .userId("Test")
                                                            .name("Test")
                                                            .nickName("Test")
                                                            .password("Test")
                                                            .phoneNumber("Test")
                                                            .address(new Address("a1", "a2", "a3"))
                                                            .build();
        return memberRequestDTO;
    }

}