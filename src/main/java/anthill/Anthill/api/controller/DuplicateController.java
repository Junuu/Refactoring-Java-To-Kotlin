package anthill.Anthill.api.controller;

import anthill.Anthill.api.dto.common.BasicResponseDTO;
import anthill.Anthill.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DuplicateController {

    private final MemberService memberService;

    private final static String DUPLICATE = "duplicate";
    private final static String NON_DUPLICATE = "non-duplicate";

    @GetMapping("/user-nickname/{nickname}")
    public ResponseEntity<BasicResponseDTO> checkNicknameDuplicate(@PathVariable String nickname) {
        boolean result = memberService.checkNicknameDuplicate(nickname);
        String message = makeMessage(result);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeBasicResponseDTO(message));
    }

    @GetMapping("/user-id/{userid}")
    public ResponseEntity<BasicResponseDTO> checkUserIdDuplicate(@PathVariable String userid) {
        boolean result = memberService.checkUserIdDuplicate(userid);
        String message = makeMessage(result);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeBasicResponseDTO(message));


    }

    @GetMapping("/user-phone-number/{phone-number}")
    public ResponseEntity<BasicResponseDTO> checkPhoneNumberDuplicate(@PathVariable("phone-number") String phoneNumber) {
        boolean result = memberService.checkUserIdDuplicate(phoneNumber);
        String message = makeMessage(result);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeBasicResponseDTO(message));

    }

    private String makeMessage(boolean result) {
        return result == true ? DUPLICATE : NON_DUPLICATE;
    }

    private BasicResponseDTO makeBasicResponseDTO(String message) {
        return BasicResponseDTO.builder()
                               .message(message)
                               .build();
    }
}
