package co.bound.exercise.thirdparties.boogle;

import java.util.Set;

public record Book(
        int bookId,
        String author,
        String title,
        String isbn,
        Set<Genre> genres,
        String publisher,
        Format format
) { }
