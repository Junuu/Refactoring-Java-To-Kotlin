package anthill.Anthill.exception.advice;


import anthill.Anthill.api.dto.common.BasicResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ApiExceptionAdvice {

    private final String FAIL = "failure";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BasicResponseDTO> dataInvalidateExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(makeBasicResponseDTO(FAIL, "입력 데이터가 유효하지 않음"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BasicResponseDTO> dataBaseExceptionHandler(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(makeBasicResponseDTO(FAIL, "DB 제약조건 오류"));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BasicResponseDTO> LoginFailException(IllegalStateException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(makeBasicResponseDTO(FAIL, "로그인 실패"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BasicResponseDTO> DataNotFoundException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(makeBasicResponseDTO(FAIL, "값이 존재하지 않음"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BasicResponseDTO> AuthenticationException(AuthenticationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(makeBasicResponseDTO(FAIL, "토큰이 유효하지 않음"));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicResponseDTO> internalServerExceptionHandler(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(makeBasicResponseDTO(FAIL, e.getMessage()));
    }

    private BasicResponseDTO makeBasicResponseDTO(String message, String errorMessage) {
        return BasicResponseDTO.builder()
                               .message(message)
                               .errorMessage(errorMessage)
                               .build();
    }


}
