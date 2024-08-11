package by.moiseenko.book.service;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.Feedback;
import by.moiseenko.book.domain.User;
import by.moiseenko.book.exception.OperationNotPermittedException;
import by.moiseenko.book.repository.BookRepository;
import by.moiseenko.book.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;

    public Long save(Feedback feedback, User user) {

        Book book = bookRepository.findById(feedback.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with id" + feedback.getBook().getId()));

        if (book.isArchive() || !book.isSharable())
            throw new OperationNotPermittedException("You cannot give a feedback for an archived or not shareable book");

        if (Objects.equals(book.getOwner().getId(), user.getId()))
            throw new OperationNotPermittedException("You cannot give a feedback to your own book");

        return feedbackRepository.save(feedback).getId();
    }

    public Page<Feedback> findAllByBook(Long bookId, int page, int size) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id" + bookId));

        return feedbackRepository.findAllByBook(book, PageRequest.of(page, size));
    }
}
