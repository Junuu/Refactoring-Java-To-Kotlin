package anthill.Anthill.api.controller

import anthill.Anthill.api.dto.board.BoardDeleteDTO
import anthill.Anthill.api.dto.board.BoardRequestDTO
import anthill.Anthill.api.dto.board.BoardUpdateDTO
import anthill.Anthill.api.dto.common.BasicResponseDTO
import anthill.Anthill.api.service.BoardService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/boards")
class BoardController(
    val boardService: BoardService,
) {

    @GetMapping("/{board-id}")
    fun select(@PathVariable("board-id") boardId: Long): ResponseEntity<BasicResponseDTO<*>> {
        val boardResponseDTO = boardService.select(boardId)
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                makeSelectResponseDTO(
                    message = SUCCESS,
                    responseData = boardResponseDTO,
                )
            )
    }

    @GetMapping("/page/{paging-id}")
    fun paging(@PathVariable("paging-id") pagingId: Int): ResponseEntity<BasicResponseDTO<*>> {
        val resultPage = boardService.paging(pagingId - 1)
        return ResponseEntity.status(HttpStatus.OK)
            .body(makeSelectResponseDTO(SUCCESS, resultPage))
    }

    @PostMapping
    fun posting(@RequestBody boardRequestDTO: @Valid BoardRequestDTO): ResponseEntity<BasicResponseDTO<*>> {
        boardService.posting(boardRequestDTO!!)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(makeBasicResponseDTO(SUCCESS))
    }

    @PutMapping
    @Throws(Exception::class)
    fun update(@RequestBody boardUpdateDTO: BoardUpdateDTO): ResponseEntity<BasicResponseDTO<*>> {
        boardService.changeInfo(boardUpdateDTO)
        return ResponseEntity.status(HttpStatus.OK)
            .body(makeBasicResponseDTO(SUCCESS))
    }


    @DeleteMapping
    @Throws(Exception::class)
    fun delete(@RequestBody boardDeleteDTO: BoardDeleteDTO?): ResponseEntity<BasicResponseDTO<*>> {
        boardService.delete(boardDeleteDTO!!)
        return ResponseEntity.status(HttpStatus.OK)
            .body(makeBasicResponseDTO(SUCCESS))
    }

    private fun makeBasicResponseDTO(message: String): BasicResponseDTO<Any> {
        return BasicResponseDTO(
            message = message,
        )
    }

    private fun <T> makeSelectResponseDTO(message: String, responseData: T): BasicResponseDTO<*> {
        return BasicResponseDTO(
            message = message,
            responseData = responseData,
        )
    }

    companion object {
        const val SUCCESS = "success"
    }
}