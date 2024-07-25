package by.moiseenko.book.specification;

import by.moiseenko.book.domain.BookTransactionHistory;
import org.springframework.data.jpa.domain.Specification;

public class BookTransactionHistorySpecification {

    public static Specification<BookTransactionHistory> withUserId(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<BookTransactionHistory> withBookOwnerId(Long bookOwnerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("book").get("owner").get("id"), bookOwnerId);
    }

    public static Specification<BookTransactionHistory> isReturned(boolean isReturned) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("returned"), isReturned);
    }
}
