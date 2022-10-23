package anthill.Anthill.api.controller

import anthill.Anthill.api.dto.common.BasicResponseDTO
import anthill.Anthill.domain.member.dto.MemberLoginRequestDTO
import anthill.Anthill.domain.member.dto.MemberRequestDTO
import anthill.Anthill.domain.member.service.MemberCommandService
import anthill.Anthill.domain.member.service.MemberQueryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/members")
class MemberController(
    val memberQueryService: MemberQueryService,
    val memberCommandService: MemberCommandService,
) {
    @GetMapping("/{userid}")
    fun findByUserId(@PathVariable(value = "userid") userId: String): ResponseEntity<*> {
        val memberResponseDTO = memberQueryService.findByUserID(userId)
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                makeBasicResponseDTO(
                    message = SUCCESS,
                    responseData = memberResponseDTO
                )
            )
    }

    @PostMapping
    fun registerMember(@RequestBody @Valid memberRequestDTO: MemberRequestDTO): ResponseEntity<BasicResponseDTO<*>> {
        memberCommandService.join(memberRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                makeBasicResponseDTO<Any>(
                    message = FAIL,
                )
            )
    }

    @PostMapping("/login")
    fun loginMember(@RequestBody memberLoginRequestDTO: MemberLoginRequestDTO): ResponseEntity<BasicResponseDTO<*>> {
        val token = memberCommandService.login(memberLoginRequestDTO)
        return ResponseEntity.status(HttpStatus.OK)
            .header("access-token", token)
            .body(
                makeBasicResponseDTO<Any>(
                    message = FAIL,
                )
            )
    }

    private fun <T> makeBasicResponseDTO(message: String, responseData: T? = null): BasicResponseDTO<*> {
        return BasicResponseDTO(
            message = message,
            responseData = responseData,
        )
    }


    companion object {
        const val FAIL = "failure"
        const val SUCCESS = "success"
    }
}