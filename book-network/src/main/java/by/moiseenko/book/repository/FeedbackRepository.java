package by.moiseenko.book.repository;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findAllByBook(Book book, Pageable pageable);
}
