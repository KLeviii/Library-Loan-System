public abstract class Book {
    private String id;
    private String title;
    private String author;
    private int loanDuration;
    private static int count;

    public Book(String id, String title, String author, int loanDuration) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.loanDuration = loanDuration;
    }

    public void checkId(String id) throws IllegalArgumentException {


    }

}
