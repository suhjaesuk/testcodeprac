package site.metacoding.junitproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.web.dto.response.BookListRespDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final MailSender mailSender;

    //1. 책 등록
    @Transactional
    public BookRespDto saveBook(BookSaveReqDto request){
        Book book = bookRepository.save(request.toEntity());
        if(!mailSender.send()){
            throw new RuntimeException("메일이 전송되지 않았습니다.");
        }
        return book.toDto();
    }

    //2. 책 목록보기
    @Transactional(readOnly = true)
    public BookListRespDto 책목록보기(){
        List<BookRespDto> dtos = bookRepository.findAll().stream()
                .map(Book::toDto)
                .collect(Collectors.toList());

        return BookListRespDto.builder().items(dtos).build();
    }

    //3. 책 한건보기
    public BookRespDto 책한건보기(Long id){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            return book.toDto();
        }else{
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }
    //4. 책 삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void 책삭제하기(Long id){
        bookRepository.deleteById(id);
    }

    //5. 책 수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto 책수정하기(Long id, BookSaveReqDto dto){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            book.update(dto.getTitle(),dto.getAuthor());
            return book.toDto();
        }else{
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
        //메서드 종료시에 더티체킹(flush)으로 update 된다.
    }
}
