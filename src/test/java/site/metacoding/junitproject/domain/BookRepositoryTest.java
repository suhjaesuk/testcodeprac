package site.metacoding.junitproject.domain;

import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest //DB와 관련된 컴포넌트만 메모리에 로딩
@ActiveProfiles("dev")
public class BookRepositoryTest {

    @Autowired //DI -> Test시에는 Autowired로 의존관계를 설정한다. 편함.
    private BookRepository bookRepository;
    //@BeforeAll 테스트 시작전에 한번만 실행
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

    //1. 책 등록
    @Test
    public void 책등록_준비(){
        //given 데이터 준비
        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        //when 테스트 실행
        Book bookPersistence = bookRepository.save(book);

        //then 검증
        assertEquals(title, bookPersistence.getTitle());
        assertEquals(author, bookPersistence.getAuthor());

    }

    @Test
    //2. 책 목록 보기
    public void 책목록보기_test(){
        //given
        String title = "junit5";
        String author = "겟인데어";

        //when
        List<Book> books = bookRepository.findAll();

        assertEquals(title,books.get(0).getTitle());
        assertEquals(author,books.get(0).getAuthor());
        //hen

    }
    //3. 책 한건 보기
    @Test
    @Sql("classpath:db/tableInit.sql")
    public void 책한건보기_test(){
        //given
        String title = "junit5";
        String author = "겟인데어";

        //when
        Book bookPersistence = bookRepository.findById(1L).get();

        assertEquals(title, bookPersistence.getTitle());
        assertEquals(author, bookPersistence.getAuthor());

    }
    //4. 책 수정
    @Test
    @Sql("classpath:db/tableInit.sql")
    //수정이 된건가..?
    public void 책수정_test(){
        //given
        Long id = 1L;
        String title = "junit5";
        String author = "";
        Book book = new Book(id,title, author);

        //when
        Book bookPersistence = bookRepository.save(book);

//        bookRepository.findAll().stream()
//                .forEach(b -> {
//                    System.out.println(b.getId());
//                    System.out.println(b.getTitle());
//                    System.out.println(b.getAuthor());
//                });

        assertEquals(id, bookPersistence.getId());
        assertEquals(title, bookPersistence.getTitle());
        assertEquals(author, bookPersistence.getAuthor());
    }

    //5. 책 삭제
    @Test
    @Sql("classpath:db/tableInit.sql")
    public void 책삭제_test(){
        //given
        Long id = 1L;

        //when
        bookRepository.deleteById(id);

        //then
        assertFalse(bookRepository.findById(id).isPresent());
    }
}
