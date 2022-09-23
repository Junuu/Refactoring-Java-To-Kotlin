package anthill.Anthill.db.repository;

import anthill.Anthill.db.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByNickName(String nickName);

    boolean existsByPhoneNumber(String phoneNumber);
}
