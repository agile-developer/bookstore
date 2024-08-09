package co.bound.exercise.thirdparties.valdivia;

import java.util.UUID;

/**
 * A very simple client class for interacting with the Valdivia bookstore. All the data (what little there is) is
 * hard-coded, but for the sake of the exercise you should pretend that this is making API calls across the internet
 * to Valdivia.
 */
public class ValdiviaClient {
    /**
     * Get a quote (price) for the book with the given ISBN.
     * @param isbn the ISBN of the book
     * @return a quote object with the price and a quote ID
     */
    public BookQuote getBookQuote(String isbn) {
        return new BookQuote(
                10.99,
                UUID.randomUUID().toString()
        );
    }

    /**
     * Accept the quote identified by the ID in order to purchase the book.
     * @param quoteId the identity of the quote
     */
    public void purchaseBook(String quoteId) {
        // Let's pretend there's a remote call in here
    }
}
