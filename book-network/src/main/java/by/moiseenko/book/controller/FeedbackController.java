package by.moiseenko.book.controller;

import by.moiseenko.book.domain.Feedback;
import by.moiseenko.book.domain.User;
import by.moiseenko.book.dto.request.FeedbackRequest;
import by.moiseenko.book.dto.response.FeedbackResponse;
import by.moiseenko.book.dto.response.PageResponse;
import by.moiseenko.book.mapper.FeedbackMapper;
import by.moiseenko.book.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;

    @PostMapping
    public ResponseEntity<Long> saveFeedback(
            @RequestBody @Valid FeedbackRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Feedback feedback = feedbackMapper.toDomain(request);

        return ResponseEntity.ok(feedbackService.save(feedback, user));
    }

    @GetMapping("/book/{book-id}")
    public PageResponse<FeedbackResponse> findAllByBook(
            @PathVariable(name = "book-id") Long bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Page<Feedback> allFeedbacksByBook = feedbackService.findAllByBook(bookId, page, size);
        List<FeedbackResponse> responses = allFeedbacksByBook.stream()
                .map(feedback -> feedbackMapper.toResponse(feedback, user.getId()))
                .toList();

        return new PageResponse<>(
                responses,
                allFeedbacksByBook.getNumber(),
                allFeedbacksByBook.getSize(),
                allFeedbacksByBook.getTotalElements(),
                allFeedbacksByBook.getTotalPages(),
                allFeedbacksByBook.isFirst(),
                allFeedbacksByBook.isLast()
        );
    }
}
