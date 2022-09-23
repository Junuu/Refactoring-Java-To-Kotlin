package anthill.Anthill.repository;

import anthill.Anthill.db.domain.member.Address;
import anthill.Anthill.db.domain.member.Member;
import anthill.Anthill.db.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @DisplayName("CREATE 테스트")
    @Test
    public void insertSuccessTest() {

        //given
        Member member = makeMember("test");

        //when
        memberRepository.save(member);

        //then
        List<Member> members = memberRepository.findAll();
        Assertions.assertThat(members.size())
                  .isEqualTo(1);
    }

    @DisplayName("DELETE 테스트")
    @Test
    public void deleteSuccessTest() {
        //given
        Member member = makeMember("test");
        memberRepository.save(member);

        //when
        memberRepository.deleteById(member.getId());

        //then
        List<Member> members = memberRepository.findAll();
        Assertions.assertThat(members.size())
                  .isEqualTo(0);
    }

    @DisplayName("READ 테스트")
    @Test
    public void selectSuccessTest() {
        //given
        Member member = makeMember("test");
        Member fail = makeMember("fail");
        memberRepository.save(member);

        //when
        Optional<Member> result = memberRepository.findById(member.getId());

        //then
        Assertions.assertThat(member.getUserId())
                  .isEqualTo(result.orElse(fail)
                                   .getUserId());
    }

    @DisplayName("UPDATE 테스트")
    @Test
    public void updateSuccessTest() {
        //given
        Member member = makeMember("test");
        Member fail = makeMember("fail");
        memberRepository.save(member);

        //when
        String updated = "updated";
        member.changeNickName(updated);

        //then
        Optional<Member> result = memberRepository.findById(member.getId());
        Assertions.assertThat(result.orElse(fail)
                                    .getNickName())
                  .isEqualTo(updated);
    }

    @Test
    @DisplayName("회원 아이디로 조회")
    public void selectByUserId() {
        //given
        Member member = makeMember("test");
        memberRepository.save(member);

        //when
        Member result = memberRepository.findByUserId("test")
                                        .get();

        //then
        Assertions.assertThat("test")
                  .isEqualTo(result.getUserId());
    }

    @Test
    @DisplayName("아무것도 없을때 값을 꺼내면 NoSuchElementException 발생")
    public void selectByUserIdNone() {
        //given

        //when
        Optional<Member> result = memberRepository.findByUserId("None");

        //then
        assertThrows(NoSuchElementException.class, () -> {
            result.get();
        });
    }

    @Test
    @DisplayName("아무것도 없을때 삭제시엔 EmptyResultDataAccessException 발생")
    public void deleteByUserIdNone() {
        //given

        //then
        assertThrows(EmptyResultDataAccessException.class, () -> {
            //when
            memberRepository.deleteById(3L);
        });
    }

    @Test
    @DisplayName("Unique값이 겹칠 경우 DataIntegrityViolationException 발생")
    public void insertDuplicateTest() {
        //given
        Member member1 = makeMember("test");
        memberRepository.save(member1);
        Member member2 = makeMember("test");

        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            //when
            memberRepository.save(member2);
        });

    }

    @DisplayName("DB NotNull으로 설정된 값들을 그냥 넣어 버리면 DataIntegrityViolationException 발생")
    @Test
    public void insertFailTest() {
        //given
        Member member = Member.builder()
                              .build();

        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            //when
            memberRepository.save(member);
        });
    }

    @Autowired
    private EntityManager entityManager;

    @DisplayName("영속성 컨텍스트 테스트를 clear시키면 동일한 객체x")
    @Test
    public void EntityContextTest() {
        //given
        Member member1 = makeMember("test");
        memberRepository.save(member1);
        entityManager.clear();

        //when
        Member member2 = memberRepository.findByUserId("test")
                                         .get();

        //then
        Assertions.assertThat(member1)
                  .isNotEqualTo(member2);

    }

    private Member makeMember(String settingValue) {
        Member member = Member.builder()
                              .userId(settingValue)
                              .name(settingValue)
                              .nickName(settingValue)
                              .password(settingValue)
                              .phoneNumber(settingValue)
                              .address(new Address("a1", "a2", "a3"))
                              .build();
        return member;
    }

}