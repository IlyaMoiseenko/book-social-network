package by.moiseenko.book.repository;

import by.moiseenko.book.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query(
    """
            SELECT book
            FROM Book book
            WHERE book.archive = false
            AND book.sharable = true
            AND book.owner.id != :id
    """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Long id);
}
