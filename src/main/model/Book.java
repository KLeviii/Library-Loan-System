package main.model;

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

    public void setLoanedTo(String loanedTo) {
        this.loanedTo = loanedTo;
    }

    public abstract int getLoanDuration();

    public abstract String getCategory();
}
