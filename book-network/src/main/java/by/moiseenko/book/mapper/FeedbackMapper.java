package by.moiseenko.book.mapper;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.Feedback;
import by.moiseenko.book.dto.request.FeedbackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackMapper {

    public Feedback toDomain(FeedbackRequest from) {
        return Feedback
                .builder()
                .note(from.getNote())
                .comment(from.getComment())
                .book(Book
                        .builder()
                        .id(from.getBookId())
                        .build()
                )
                .build();
    }
}
