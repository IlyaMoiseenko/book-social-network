package by.moiseenko.book.specification;

import by.moiseenko.book.domain.BookTransactionHistory;
import org.springframework.data.jpa.domain.Specification;

public class BookTransactionHistorySpecification {

    public static Specification<BookTransactionHistory> withUserId(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
