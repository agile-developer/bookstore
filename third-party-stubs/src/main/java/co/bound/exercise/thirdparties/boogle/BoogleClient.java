package co.bound.exercise.thirdparties.boogle;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static co.bound.exercise.thirdparties.boogle.Genre.*;

/**
 * Client class for interacting with Boogle. All the data (what little there is) is hard-coded, but for the sake of the
 * exercise you should pretend that this is making API calls across the internet to Boogle.
 */
public class BoogleClient {
    /**
     * Retrieve a list of books by author.
     *
     * @param author the author
     * @return a list of books
     */
    public List<BookSummary> searchBooksByAuthor(String author) {
        return getAllBooks().stream()
                .filter(b -> b.author().equals(author))
                .toList();
    }

    /**
     * Retrieve a book by ISBN.
     *
     * @param isbn the isbn
     * @return an Optional<BookSummary> with the given ISBN
     */
    public Optional<BookSummary> findBookByIsbn(String isbn) {
        return getAllBooks().stream()
                .filter(book -> book.isbn().equals(isbn))
                .findFirst();
    }

    /**
     * Get a list of all books.
     *
     * @return all the books
     */
    public List<BookSummary> getAllBooks() {
        return Stream.of(getBookDetails(1), getBookDetails(2), getBookDetails(3), getBookDetails(4))
                .map(b -> new BookSummary(b.isbn(), b.title(), b.author()))
                .toList();
    }

    /**
     * Get the full details for the book with the given ID.
     *
     * @param bookId the book ID to look up
     * @return the book that matches the ID
     */
    public Book getBookDetails(int bookId) {
        return switch (bookId) {
            case 1 -> new Book(bookId, "Gabriel Garcia Marquez", "One Hundred Years of Solitude", "978-0141184999",
                    EnumSet.of(FICTION, FANTASY, CONTEMPORARY), "Penguin", Format.PAPERBACK);
            case 2 -> new Book(bookId, "Bruce Chatwin", "In Patagonia", "978-0099769514",
                    EnumSet.of(NON_FICTION, TRAVEL), "Vintage Classics", Format.PAPERBACK);
            case 3 ->
                    new Book(bookId, "Katie Mack", "The End of Everything: (Astrophysically Speaking)", "978-0141989587",
                            EnumSet.of(NON_FICTION, SCIENCE), "Penguin", Format.PAPERBACK);
            case 4 -> new Book(bookId, "Gabriel Garcia Marquez", "Chronicle of a Death Foretold", "978-0241968628",
                    EnumSet.of(FICTION, FANTASY, CONTEMPORARY), "Penguin", Format.PAPERBACK);
            default -> throw new IllegalArgumentException("Unknown book");
        };
    }
}
