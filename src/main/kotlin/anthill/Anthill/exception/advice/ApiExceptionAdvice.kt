package anthill.Anthill.exception.advice

import anthill.Anthill.api.dto.common.BasicResponseDTO
import org.apache.tomcat.websocket.AuthenticationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(annotations = [RestController::class])
class ApiExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun dataInvalidateExceptionHandler(e: MethodArgumentNotValidException): ResponseEntity<BasicResponseDTO<*>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(makeBasicResponseDTO(FAIL, "입력 데이터가 유효하지 않음"))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataBaseExceptionHandler(e: DataIntegrityViolationException): ResponseEntity<BasicResponseDTO<*>> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(makeBasicResponseDTO(FAIL, "DB 제약조건 오류"))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun loginFailException(e: IllegalStateException): ResponseEntity<BasicResponseDTO<*>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(makeBasicResponseDTO(FAIL, "로그인 실패"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun dataNotFoundException(e: IllegalArgumentException): ResponseEntity<BasicResponseDTO<*>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(makeBasicResponseDTO(FAIL, "값이 존재하지 않음"))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun authenticationException(e: AuthenticationException): ResponseEntity<BasicResponseDTO<*>>{
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(makeBasicResponseDTO(FAIL, "토큰이 유효하지 않음"))
    }


    @ExceptionHandler(Exception::class)
    fun internalServerExceptionHandler(e: Exception): ResponseEntity<BasicResponseDTO<*>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(makeBasicResponseDTO(FAIL, e.message))
    }

    private fun makeBasicResponseDTO(message: String, errorMessage: String?): BasicResponseDTO<Any> {
        return BasicResponseDTO(
            message = message,
            errorMessage = errorMessage,
        )
    }

    companion object {
        const val FAIL = "failure"
    }
}