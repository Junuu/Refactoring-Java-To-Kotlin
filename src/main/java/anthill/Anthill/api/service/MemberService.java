package anthill.Anthill.api.service;

import anthill.Anthill.api.dto.member.MemberLoginRequestDTO;
import anthill.Anthill.api.dto.member.MemberRequestDTO;
import anthill.Anthill.api.dto.member.MemberResponseDTO;


public interface MemberService {
    Long join(MemberRequestDTO memberRequestDTO);

    boolean checkNicknameDuplicate(String nickName);

    boolean checkUserIdDuplicate(String userId);

    boolean checkPhoneNumberDuplicate(String phoneNumber);

    boolean login(MemberLoginRequestDTO memberLoginRequestDTO);

    MemberResponseDTO findByUserID(String userId);
}
