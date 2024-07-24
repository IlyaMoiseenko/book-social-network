package by.moiseenko.book.controller;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.dto.request.BookRequest;
import by.moiseenko.book.dto.response.BookResponse;
import by.moiseenko.book.mapper.BookMapper;
import by.moiseenko.book.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public ResponseEntity<Long> saveBook(@RequestBody @Valid BookRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.save(request, connectedUser));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable(name = "book-id") Long id) {
        Book book = bookService.findById(id);

        return ResponseEntity.ok(
            bookMapper.toBookResponse(book)
        );
    }
}
