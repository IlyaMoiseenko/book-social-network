package by.moiseenko.book.controller;

import by.moiseenko.book.domain.Feedback;
import by.moiseenko.book.domain.User;
import by.moiseenko.book.dto.request.FeedbackRequest;
import by.moiseenko.book.mapper.FeedbackMapper;
import by.moiseenko.book.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
