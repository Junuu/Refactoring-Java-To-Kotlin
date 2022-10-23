package anthill.Anthill.api.controller

import anthill.Anthill.api.dto.common.BasicResponseDTO
import anthill.Anthill.domain.member.service.MemberQueryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DuplicateController(
    val memberQueryService: MemberQueryService,
) {
    @GetMapping("/user-nickname/{nickname}")
    fun checkNicknameDuplicate(@PathVariable nickname: String): ResponseEntity<BasicResponseDTO<*>> {
        val result = memberQueryService.checkNicknameDuplicate(nickname)
        val message = makeMessage(result)
        return ResponseEntity.status(HttpStatus.OK)
            .body(makeBasicResponseDTO(message))
    }

    @GetMapping("/user-id/{userid}")
    fun checkUserIdDuplicate(@PathVariable userid: String): ResponseEntity<BasicResponseDTO<*>> {
        val result = memberQueryService.checkUserIdDuplicate(userid)
        val message = makeMessage(result)
        return ResponseEntity.status(HttpStatus.OK)
            .body(makeBasicResponseDTO(message))
    }

    @GetMapping("/user-phone-number/{phone-number}")
    fun checkPhoneNumberDuplicate(@PathVariable("phone-number") phoneNumber: String): ResponseEntity<BasicResponseDTO<*>> {
        val result = memberQueryService.checkUserIdDuplicate(phoneNumber)
        val message = makeMessage(result)
        return ResponseEntity.status(HttpStatus.OK)
            .body(makeBasicResponseDTO(message))
    }

    private fun makeMessage(result: Boolean): String {
        return if (result) DUPLICATE else NON_DUPLICATE
    }

    private fun makeBasicResponseDTO(message: String): BasicResponseDTO<Any> {
        return BasicResponseDTO(
            message = message,
        )
    }

    companion object {
        const val DUPLICATE = "duplicate"
        const val NON_DUPLICATE = "non-duplicate"
    }
}