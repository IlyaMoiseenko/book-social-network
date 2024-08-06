package by.moiseenko.book.controller;

import by.moiseenko.book.domain.Book;
import by.moiseenko.book.domain.BookTransactionHistory;
import by.moiseenko.book.domain.User;
import by.moiseenko.book.dto.request.BookRequest;
import by.moiseenko.book.dto.response.BookResponse;
import by.moiseenko.book.dto.response.BorrowedBookResponse;
import by.moiseenko.book.dto.response.PageResponse;
import by.moiseenko.book.mapper.BookMapper;
import by.moiseenko.book.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public ResponseEntity<Long> saveBook(@RequestBody @Valid BookRequest request, Authentication authentication) {
        Book book = bookMapper.toBook(request);
        User connectedUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(bookService.save(book, connectedUser));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable(name = "book-id") Long id) {
        Book book = bookService.findById(id);

        return ResponseEntity.ok(
            bookMapper.toBookResponse(book)
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getAllDisplayableBooks(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Page<Book> books = bookService.findAllBooks(page, size, user);
        List<BookResponse> listOfBookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return ResponseEntity.ok(
                new PageResponse<>(
                        listOfBookResponse,
                        books.getNumber(),
                        books.getSize(),
                        books.getTotalElements(),
                        books.getTotalPages(),
                        books.isFirst(),
                        books.isLast()
                )
        );
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> getAllBooksByOwner(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Page<Book> books = bookService.findAllByOwner(page, size, user);
        List<BookResponse> listOfBookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return ResponseEntity.ok(
                new PageResponse<>(
                        listOfBookResponse,
                        books.getNumber(),
                        books.getSize(),
                        books.getTotalElements(),
                        books.getTotalPages(),
                        books.isFirst(),
                        books.isLast()
                )
        );
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllBorrowedBooks(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Page<BookTransactionHistory> books = bookService.findAllBorrowedBooks(page, size, user);
        List<BorrowedBookResponse> listOfBookResponse = books.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return ResponseEntity.ok(
                new PageResponse<>(
                        listOfBookResponse,
                        books.getNumber(),
                        books.getSize(),
                        books.getTotalElements(),
                        books.getTotalPages(),
                        books.isFirst(),
                        books.isLast()
                )
        );
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllReturnedBooks(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Page<BookTransactionHistory> books = bookService.findAllReturnedBooks(page, size, user);
        List<BorrowedBookResponse> listOfBookResponse = books.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return ResponseEntity.ok(
                new PageResponse<>(
                        listOfBookResponse,
                        books.getNumber(),
                        books.getSize(),
                        books.getTotalElements(),
                        books.getTotalPages(),
                        books.isFirst(),
                        books.isLast()
                )
        );
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Long> updateShareableStatus(
            @PathVariable(name = "book-id") Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(bookService.updateShareableStatus(id, user));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Long> updateArchivedStatus(
            @PathVariable(name = "book-id") Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(bookService.updateArchivedStatus(id, user));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Long> borrowBook(
            @PathVariable(name = "book-id") Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(bookService.borrowBook(id, user));
    }

    @PostMapping("/borrow/return/{book-id}")
    public ResponseEntity<Long> returnBook(
            @PathVariable(name = "book-id") Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(bookService.returnBorrowBook(id, user));
    }
}
