package by.moiseenko.book.mapper;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.dto.request.BookRequest;
import by.moiseenko.book.dto.response.BookResponse;
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

    public BookResponse toBookResponse(Book from) {
        return BookResponse
                .builder()
                .id(from.getId())
                .title(from.getTitle())
                .authorName(from.getAuthorName())
                .isbn(from.getIsbn())
                .synopsis(from.getSynopsis())
                .owner(from.getOwner().getFullName())
                .rate(from.getRate())
                .archived(from.isArchive())
                .shareable(from.isSharable())
                .build();
    }
}
