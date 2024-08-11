package by.moiseenko.book.mapper;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.Feedback;
import by.moiseenko.book.dto.request.FeedbackRequest;
import by.moiseenko.book.dto.response.FeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

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

    public FeedbackResponse toResponse(Feedback from, Long userId) {
        return FeedbackResponse
                .builder()
                .note(from.getNote())
                .comment(from.getComment())
                .ownFeedback(Objects.equals(from.getCreatedBy(), userId))
                .build();
    }
}
