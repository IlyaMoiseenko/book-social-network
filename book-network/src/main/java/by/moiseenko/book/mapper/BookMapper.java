package by.moiseenko.book.mapper;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.BookTransactionHistory;
import by.moiseenko.book.dto.request.BookRequest;
import by.moiseenko.book.dto.response.BookResponse;
import by.moiseenko.book.dto.response.BorrowedBookResponse;
import by.moiseenko.book.file.FileUtils;
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
                .cover(FileUtils.readFileFromLocation(from.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory from) {
        return BorrowedBookResponse
                .builder()
                .id(from.getId())
                .title(from.getBook().getTitle())
                .authorName(from.getBook().getAuthorName())
                .isbn(from.getBook().getIsbn())
                .rate(from.getBook().getRate())
                .returned(from.isReturned())
                .returnApproved(from.isReturnedApproved())
                .build();
    }
}
