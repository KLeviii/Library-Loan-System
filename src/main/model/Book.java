package main.model;

import main.exception.InvalidBookIdException;
import main.exception.InvalidMemberException;
import main.exception.InvalidReadingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public abstract class Book {
    private final String id;
    private final String title;
    private final String author;
    private String loanedTo;

    public Book(String id, String title, String author, String loanedTo) {
        this.id = id;
        this.title = title;
        this.author = author;
        setLoanedTo(loanedTo);
    }

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        setLoanedTo(null);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return loanedTo == null;
    }

    public String getLoanedTo() {
        return loanedTo;
    }

    public void setLoanedTo(String loanedTo) {
        this.loanedTo = loanedTo;
    }

    public abstract int getLoanDuration();

    public abstract String getCategory();


    public void checkBookId(String bookid) throws InvalidReadingException, InvalidBookIdException {
        boolean foundBook = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");
                if (st.hasMoreTokens()) {
                    String currentBookId = st.nextToken();
                    if (currentBookId.equals(bookid)) {
                        foundBook = true;
                        break;
                    }
                }
            }
        } catch (IOException ioe) {
            throw new InvalidReadingException("Error reading the books.");
        }
        if (!foundBook) {
            throw new InvalidBookIdException("Couldn't find this book!");
        }
    }

    public void checkMember(int memberId) throws InvalidReadingException, InvalidMemberException {
        boolean foundMember = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");
                if (st.hasMoreTokens()) {
                    String currentMemberId = st.nextToken();
                    if (Integer.parseInt(currentMemberId) == memberId) {
                        foundMember = true;
                        break;
                    }
                }

            }
        } catch (IOException ioe) {
            throw new InvalidReadingException("Error reading the members.");
        }
        if (!foundMember) {
            throw new InvalidMemberException("Couldn't find this member!");
        }
    }
}
