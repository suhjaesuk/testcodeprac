package site.metacoding.junitproject.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//통합 테스트(C,S,R)
@ActiveProfiles("dev") // 일정 환경에서만 작동하게끔 설정
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BookRepository bookRepository;
    private static ObjectMapper objectMapper;
    private static HttpHeaders httpHeaders;


    @BeforeAll
    public static void init(){
        objectMapper = new ObjectMapper();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach //각 테스트 시작전에 한번씩 실행
    public void 데이터준비(){
        String title = "junit5";
        String author = "겟인데어";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.save(book);
    }

    @Test
    public void saveBook_test() throws Exception {
        //given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("스프링1강");
        bookSaveReqDto.setAuthor("겟인데어");

        String body = objectMapper.writeValueAsString(bookSaveReqDto);

        //when
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/api/book", HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());

        //then
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Object title = documentContext.read("$.data.title");
        String author = documentContext.read("$.data.author");

        assertThat(title).isEqualTo("스프링1강");
        assertThat(author).isEqualTo("겟인데어");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookList_test(){
        //given

        //when
        HttpEntity<String> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/api/book", HttpMethod.GET, request, String.class);

        //then
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Integer code = documentContext.read("$.code");
        String title = documentContext.read("$.data.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit5");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookOne_test(){
        //given
        Long id = 1L;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/api/book/"+id, HttpMethod.GET, request, String.class);

        //then
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Integer code = documentContext.read("$.code");
        String title = documentContext.read("$.data.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit5");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteBook_test(){
        //given
        Long id = 1L;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/api/book/"+id, HttpMethod.DELETE, request, String.class);

        //then
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Integer code = documentContext.read("$.code");
        HttpStatus statusCode = response.getStatusCode();
        assertThat(code).isEqualTo(1);
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void updateBook_test() throws Exception{
        //given
        Long id = 1L;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("spring");
        bookSaveReqDto.setAuthor("메타코딩");

        String body = objectMapper.writeValueAsString(bookSaveReqDto);
        //when
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/api/book/"+id, HttpMethod.PUT, request, String.class);

        //then
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String title = documentContext.read("$.data.title");
        String author = documentContext.read("$.data.author");

        assertThat(title).isEqualTo("spring");
        assertThat(author).isEqualTo("메타코딩");
    }
}
