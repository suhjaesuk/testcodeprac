package site.metacoding.junitproject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.metacoding.junitproject.service.BookService;
import site.metacoding.junitproject.web.dto.response.BookListRespDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;
import site.metacoding.junitproject.web.dto.response.CommonRespDto;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    //책 등록
    @PostMapping("/api/book")
    public ResponseEntity<CommonRespDto<?>> saveBook(@Valid @RequestBody BookSaveReqDto request, BindingResult bindingResult){
        BookRespDto response = bookService.saveBook(request);

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()){
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }

        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 저장 성공").data(response).build(), HttpStatus.CREATED);
    }


    //책 목록
    @GetMapping("/api/book")
    public ResponseEntity<?>  getBookList(){
        BookListRespDto response = bookService.책목록보기();
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 목록보기 성공").data(response).build(),
                HttpStatus.OK);
    }

    //책 한건보기
    @GetMapping("/api/book/{id}")
    public ResponseEntity<?>  getBookOne(@PathVariable Long id){
        BookRespDto response = bookService.책한건보기(id);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 한건보기 성공").data(response).build(),
                HttpStatus.OK);
    }

    //책 삭제
    @DeleteMapping("/api/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookService.책삭제하기(id);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 삭제하기 성공").data(null).build(),
                HttpStatus.OK);
    }

    //책 수정
    @PutMapping("/api/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookSaveReqDto request, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()){
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }

        BookRespDto response = bookService.책수정하기(id, request);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).msg("글 수정하기 성공").data(response).build(),
                HttpStatus.OK);
    }
}
