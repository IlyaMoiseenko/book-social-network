package by.moiseenko.book.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
}
