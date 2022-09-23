package anthill.Anthill.api.controller;

import anthill.Anthill.api.dto.board.*;
import anthill.Anthill.api.dto.common.BasicResponseDTO;
import anthill.Anthill.api.service.BoardService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;
    private final static String SUCCESS = "success";

    @GetMapping("/{board-id}")
    public ResponseEntity<BasicResponseDTO> select(@PathVariable("board-id") Long boardId) {

        BoardResponseDTO boardResponseDTO = boardService.select(boardId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeSelectResponseDTO(SUCCESS, boardResponseDTO));

    }

    @GetMapping({"/page/{paging-id}"})
    public ResponseEntity<BasicResponseDTO> paging(@PathVariable("paging-id") Integer pagingId) {

        BoardPageResponseDTO resultPage = boardService.paging(pagingId - 1);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeSelectResponseDTO(SUCCESS, resultPage));
    }

    @PostMapping
    public ResponseEntity<BasicResponseDTO> posting(@Valid @RequestBody BoardRequestDTO boardRequestDTO) {

        boardService.posting(boardRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(makeBasicResponseDTO(SUCCESS));

    }

    @PutMapping
    public ResponseEntity<BasicResponseDTO> update(@RequestBody BoardUpdateDTO boardUpdateDTO) throws Exception {
        boardService.changeInfo(boardUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeBasicResponseDTO(SUCCESS));
    }


    @DeleteMapping
    public ResponseEntity<BasicResponseDTO> delete(@RequestBody BoardDeleteDTO boardDeleteDTO) throws Exception {
        boardService.delete(boardDeleteDTO);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(makeBasicResponseDTO(SUCCESS));
    }


    private BasicResponseDTO makeBasicResponseDTO(String message) {
        return BasicResponseDTO.builder()
                               .message(message)
                               .build();
    }

    private <T> BasicResponseDTO makeSelectResponseDTO(String message, T responseData) {
        return BasicResponseDTO.builder()
                               .message(message)
                               .responseData(responseData)
                               .build();
    }

}
