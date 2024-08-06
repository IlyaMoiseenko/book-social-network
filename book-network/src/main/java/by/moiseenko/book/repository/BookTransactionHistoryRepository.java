package by.moiseenko.book.repository;

import by.moiseenko.book.domain.BookTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long>, JpaSpecificationExecutor<BookTransactionHistory> {

    /*
    If there is at least one entry
    it means that the user borrow the book
    and did not return it
    */
    @Query("""
            SELECT
            (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.id = :userId
            AND bookTransactionHistory.book.id = :bookId
            AND bookTransactionHistory.returnedApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Long bookId, Long userId);

    /*
    If nothing is returned, it means
    that the user has not borrowed the book.
    */
    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.user.id = :userId
            AND transaction.book.id = :bookId
            AND transaction.returned = false
            AND transaction.returnedApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);
}
