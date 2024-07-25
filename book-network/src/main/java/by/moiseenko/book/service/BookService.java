package by.moiseenko.book.service;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.User;
import by.moiseenko.book.mapper.BookMapper;
import by.moiseenko.book.repository.BookRepository;
import by.moiseenko.book.specification.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper mapper;
    private final BookRepository bookRepository;

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
}
