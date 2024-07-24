package by.moiseenko.book.mapper;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.dto.request.BookRequest;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toBook(BookRequest from) {
        return Book
                .builder()
                .id(from.getId())
                .title(from.getTitle())
                .authorName(from.getAuthorName())
                .synopsis(from.getSynopsis())
                .archive(false)
                .sharable(from.isShareable())
                .build();
    }
}
