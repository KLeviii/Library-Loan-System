/**
 * AI:
 * import java.util.stream.Collectors;
 */

package main.service;

import main.model.*;
import main.exception.*;
import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    private List<Book> books;

    public BookService(List<Book> books) {
        this.books = books;
    }

    public Book findBookById(String id) throws InvalidBookIdException {
        return books.stream()
                .filter(b -> b.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new InvalidBookIdException("Book not found: " + id));
    }

    public boolean bookExists(String bookId) {
        return books.stream()
                .anyMatch(m -> m.getId().equalsIgnoreCase(bookId));
    }

    public List<Book> getBooksByCategory(String category) {
        return books.stream()
                .filter(b -> b.getCategory().equals(category))
                .sorted(Comparator.comparingInt(this::getNumericPart))
                .collect(Collectors.toList());
    }

    private int getNumericPart(Book book) {
        String id = book.getId();
        String numPart = id.substring(id.indexOf('-') + 1);
        return Integer.parseInt(numPart);
    }

    public List<Book> searchByTitle(String titleFragment) {
        if (titleFragment == null || titleFragment.isBlank()) {
            return Collections.emptyList();
        }

        String lowerCaseFragment = titleFragment.toLowerCase();

        return books.stream()
                .filter(b -> b.getTitle() != null &&
                        b.getTitle().toLowerCase().contains(lowerCaseFragment))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String authorFragment) {
        if (authorFragment == null || authorFragment.isBlank()) {
            return Collections.emptyList();
        }

        String lowerCaseFragment = authorFragment.toLowerCase();

        return books.stream()
                .filter(b -> b.getAuthor() != null &&
                        b.getAuthor().toLowerCase().contains(lowerCaseFragment))
                .sorted(Comparator.comparing(Book::getAuthor)
                        .thenComparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }


}
