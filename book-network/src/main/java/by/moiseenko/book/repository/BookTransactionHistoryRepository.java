package by.moiseenko.book.repository;

import by.moiseenko.book.domain.BookTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long>, JpaSpecificationExecutor<BookTransactionHistory> {
}
