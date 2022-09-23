package anthill.Anthill.api.service;

import anthill.Anthill.db.domain.member.Member;
import anthill.Anthill.api.dto.member.MemberLoginRequestDTO;
import anthill.Anthill.api.dto.member.MemberRequestDTO;
import anthill.Anthill.api.dto.member.MemberResponseDTO;
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
    public void join(MemberRequestDTO memberRequestDTO) {
        memberRequestDTO.hashingPassword();
        memberRepository.save(memberRequestDTO.toEntity());
    }

    @Override
    public boolean validateIsDuplicate(MemberRequestDTO member) {
        if (checkPhoneNumberDuplicate(member.getPhoneNumber())) {
            return true;
        }
        if (checkNicknameDuplicate(member.getNickName())) {
            return true;
        }
        if (checkUserIdDuplicate(member.getUserId())) {
            return true;
        }
        return false;
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
