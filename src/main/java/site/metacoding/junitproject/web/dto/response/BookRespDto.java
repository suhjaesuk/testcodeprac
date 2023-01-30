package site.metacoding.junitproject.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.junitproject.domain.Book;

@Getter
@Setter
public class BookRespDto {
    private Long id;
    private String title;
    private String author;

    @Builder
    public BookRespDto(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

//    public static BookRespDto toDto(Book book){
//        BookRespDto dto = new BookRespDto();
//        dto.id = book.getId();
//        dto.title = book.getTitle();
//        dto.author = book.getAuthor();
//        return dto;
//    }
}
