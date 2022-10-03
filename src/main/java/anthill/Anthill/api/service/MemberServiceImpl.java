package anthill.Anthill.api.service;

import anthill.Anthill.api.dto.member.MemberLoginRequestDTO;
import anthill.Anthill.api.dto.member.MemberRequestDTO;
import anthill.Anthill.api.dto.member.MemberResponseDTO;
import anthill.Anthill.db.domain.member.Member;
import anthill.Anthill.db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Long join(MemberRequestDTO memberRequestDTO) {
        validateIsDuplicate(memberRequestDTO);
        memberRequestDTO.hashingPassword();
        return memberRepository.save(memberRequestDTO.toEntity())
                               .getId();
    }

    private void validateIsDuplicate(MemberRequestDTO member) {
        if (checkPhoneNumberDuplicate(member.getPhoneNumber())) {
            throw new IllegalArgumentException();
        }
        if (checkNicknameDuplicate(member.getNickName())) {
            throw new IllegalArgumentException();
        }
        if (checkUserIdDuplicate(member.getUserId())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean checkNicknameDuplicate(String nickName) {
        return memberRepository.existsByNickName(nickName);
    }

    @Override
    public boolean checkUserIdDuplicate(String userId) {
        return memberRepository.existsByUserId(userId);
    }

    @Override
    public boolean checkPhoneNumberDuplicate(String phoneNumber) {
        return memberRepository.existsByPhoneNumber(phoneNumber);
    }


    @Override
    public boolean login(MemberLoginRequestDTO memberLoginRequestDTO) {
        Optional<Member> user = memberRepository.findByUserId(memberLoginRequestDTO.getUserId());
        String userPassword = user.orElseThrow(() -> new IllegalStateException())
                                  .getPassword();

        return BCrypt.checkpw(memberLoginRequestDTO.getPassword(), userPassword);
    }

    @Override
    public MemberResponseDTO findByUserID(String userId) {
        return memberRepository.findByUserId(userId)
                               .orElseThrow(() -> new IllegalArgumentException())
                               .toMemberResponseDTO();
    }

}
