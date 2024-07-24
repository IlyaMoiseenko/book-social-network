package by.moiseenko.book.service;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.User;
import by.moiseenko.book.dto.request.BookRequest;
import by.moiseenko.book.mapper.BookMapper;
import by.moiseenko.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper mapper;
    private final BookRepository bookRepository;

    public Long save(BookRequest request, Authentication authentication) {
        User user = ((User) authentication.getPrincipal());
        Book book = mapper.toBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book found with id: " + id));
    }
}
