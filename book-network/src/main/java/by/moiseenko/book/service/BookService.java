package by.moiseenko.book.service;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.BookTransactionHistory;
import by.moiseenko.book.domain.User;
import by.moiseenko.book.exception.OperationNotPermittedException;
import by.moiseenko.book.mapper.BookMapper;
import by.moiseenko.book.repository.BookRepository;
import by.moiseenko.book.repository.BookTransactionHistoryRepository;
import by.moiseenko.book.specification.BookSpecification;
import by.moiseenko.book.specification.BookTransactionHistorySpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper mapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Long save(Book book, User user) {
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + id));
    }

    public Page<Book> findAllBooks(int page, int size, User user) {
        return bookRepository.findAllDisplayableBooks(
                PageRequest.of(page, size),
                user.getId()
        );
    }

    public Page<Book> findAllByOwner(int page, int size, User user) {
        return bookRepository.findAll(
                BookSpecification.withOwnerId(user.getId()),
                PageRequest.of(page, size)
        );
    }

    public Page<BookTransactionHistory> findAllBorrowedBooks(int page, int size, User user) {
        return bookTransactionHistoryRepository.findAll(
                BookTransactionHistorySpecification.withUserId(user.getId()),
                PageRequest.of(page, size)
        );
    }

    public Page<BookTransactionHistory> findAllReturnedBooks(int page, int size, User user) {
        Specification<BookTransactionHistory> specification = Specification.where(
                BookTransactionHistorySpecification.withBookOwnerId(user.getId())
                        .and(BookTransactionHistorySpecification.isReturned(true))
        );

        return bookTransactionHistoryRepository.findAll(
                specification,
                PageRequest.of(page, size)
        );
    }

    public Long updateShareableStatus(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));

        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can not update book shareable status");
        }

        book.setSharable(!book.isSharable());

        return bookRepository.save(book).getId();
    }

    public Long updateArchivedStatus(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));

        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can not update book archived status");
        }

        book.setArchive(!book.isArchive());

        return bookRepository.save(book).getId();
    }

    public Long borrowBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));

        // If the book is not available for shareable and it is archived then it cannot be borrowed
        if (!book.isSharable() || book.isArchive()) {
            throw new OperationNotPermittedException("The requested book is not sharable or archived");
        }

        // If the current user is the owner of the book then it cannot be borrowed either
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can not your own book");
        }

        // If the current user borrow the book and did not return it then it cannot be borrowed either
        final boolean idAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (idAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory
                .builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnedApproved(false)
                .build();

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Long returnBorrowBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));

        // If the book is not available for shareable and it is archived then it cannot be borrowed
        if (!book.isSharable() || book.isArchive()) {
            throw new OperationNotPermittedException("The requested book is not sharable or archived");
        }

        // If the current user is the owner of the book then it cannot be returned either
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can not return your own book");
        }

        BookTransactionHistory transaction = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

        transaction.setReturned(true);

        return bookTransactionHistoryRepository.save(transaction).getId();
    }

    public Long approveReturnBorrowBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + bookId));

        // If the book is not available for shareable and it is archived then it cannot be borrowed
        if (!book.isSharable() || book.isArchive()) {
            throw new OperationNotPermittedException("The requested book is not sharable or archived");
        }

        // If the current user is not the owner of the book then he cannot approve return
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot approve the return of a book you do not own");
        }

        BookTransactionHistory transaction = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));

        transaction.setReturnedApproved(true);

        return bookTransactionHistoryRepository.save(transaction).getId();
    }
}
