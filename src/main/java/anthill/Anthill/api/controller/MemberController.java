package anthill.Anthill.api.controller;

import anthill.Anthill.api.dto.common.BasicResponseDTO;
import anthill.Anthill.api.dto.member.MemberLoginRequestDTO;
import anthill.Anthill.api.dto.member.MemberRequestDTO;
import anthill.Anthill.api.dto.member.MemberResponseDTO;
import anthill.Anthill.api.service.JwtService;
import anthill.Anthill.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")

public class MemberController {

    private final MemberService memberService;

    private final JwtService jwtService;

    private final static String FAIL = "failure";
    private final static String SUCCESS = "success";

    @GetMapping
    public String helloMessage() {
        return "ok";
    }

    @GetMapping("/{userid}")
    public ResponseEntity<BasicResponseDTO> findByUserID(@PathVariable(value = "userid") final String userId) {
        MemberResponseDTO memberResponseDTO = memberService.findByUserID(userId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeBasicResponseDTO(SUCCESS, memberResponseDTO));
    }

    @PostMapping
    public ResponseEntity<BasicResponseDTO> registerMember(@Valid @RequestBody MemberRequestDTO memberRequestDTO) {

        if (memberService.validateIsDuplicate(memberRequestDTO)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(makeBasicResponseDTO(FAIL, null));
        }

        memberService.join(memberRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(makeBasicResponseDTO(SUCCESS, null));
    }

    @PostMapping("/login")
    public ResponseEntity<BasicResponseDTO> loginMember(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {

        if (memberService.login(memberLoginRequestDTO)) {
            String token = jwtService.create("userId", memberLoginRequestDTO.getUserId(), "access-token");
            return ResponseEntity.status(HttpStatus.OK)
                                 .header("access-token", token)
                                 .body(makeBasicResponseDTO(SUCCESS, null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(makeBasicResponseDTO(FAIL, null));

    }

    private <T> BasicResponseDTO makeBasicResponseDTO(String message, T responseData) {
        return BasicResponseDTO.builder()
                               .message(message)
                               .responseData(responseData)
                               .build();
    }


}
