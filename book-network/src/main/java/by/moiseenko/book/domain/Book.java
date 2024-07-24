package by.moiseenko.book.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Book extends BaseDomain {

    private String title;

    @Column(name = "author_name")
    private String authorName;

    private String isbn;
    private String synopsis;

    @Column(name = "book_cover")
    private String bookCover;

    private boolean archive;
    private boolean sharable;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }

        double rate = feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        double roundedRate = Math.round(rate * 10.0) / 10.0;

        return roundedRate;
    }
}
