package site.metacoding.junitproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.web.dto.response.BookListRespDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MailSender mailSender;

    //문제점 : 서비스만 테스트 하고 싶은데, 레포지토리가 의존된다.
    //Mockito를 써서 해결 -> 가짜 객체 보관 환경
    @Test
    public void 책등록하기_테스트(){
        //given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit강의");
        dto.setAuthor("메타코딩");

        //stub 행동을 가설로 정의
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        //when
        BookRespDto bookRespDto = bookService.saveBook(dto);
        //then
        assertEquals(dto.getTitle(), bookRespDto.getTitle());
        assertEquals(dto.getAuthor(), bookRespDto.getAuthor());

        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }

    @Test
    public void 책목록보기_테스트(){
        //stub
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit강의", "메타코딩"));
        books.add(new Book(2L, "spring강의", "최주호"));
        when(bookRepository.findAll()).thenReturn(books);

        //when
        BookListRespDto response = bookService.책목록보기();

        //then
        assertThat(response.getItems().get(0).getTitle()).isEqualTo("junit강의");
        assertThat(response.getItems().get(0).getAuthor()).isEqualTo("메타코딩");
        assertThat(response.getItems().get(1).getTitle()).isEqualTo("spring강의");
        assertThat(response.getItems().get(1).getAuthor()).isEqualTo("최주호");
    }

    @Test
    public void 책한건보기_테스트(){
        //given
        Long id = 1L;
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOptional = Optional.of(book);

        //stub
        when(bookRepository.findById(id)).thenReturn(bookOptional);

        //when
        BookRespDto response = bookService.책한건보기(id);

        //then
        assertThat(response.getTitle()).isEqualTo(book.getTitle());
        assertThat(response.getAuthor()).isEqualTo(book.getAuthor());
    }

    //삭제만 한다면 테스트는 필요없음 (레포지토리가 할 일)


    @Test
    public void 책수정하기_테스트(){
        //given
        Long id = 1L;
        BookSaveReqDto request = new BookSaveReqDto();
        request.setTitle("spring강의");
        request.setAuthor("겟인데어");

        //stub
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOptional);

        //when
        BookRespDto response = bookService.책수정하기(id, request);

        //then
        assertThat(request.getTitle()).isEqualTo(response.getTitle());
        assertThat(request.getAuthor()).isEqualTo(response.getAuthor());


     }


}
