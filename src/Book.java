public abstract class Book {
    private final String id;
    private final String title;
    private final String author;
    private Member loanedTo;


    public Book(String id, String title, String author, Member loanedTo) {
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

    public Member getLoanedTo() {
        return loanedTo;
    }

    public void setLoanedTo(Member loanedTo) {
        this.loanedTo = loanedTo;
    }

    public abstract void checkBookId(String id) throws IllegalArgumentException;
    public abstract void checkMember(Member member) throws IllegalArgumentException;

}
