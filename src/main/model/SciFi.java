package main.model;

public final class SciFi extends Book {
    private static final int loanDuration = 14;
    private static int count = 0;

    public SciFi(String id, String title, String author, int loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public SciFi(String id, String title, String author) {
        super(id, title, author);
        count++;
    }

    public static String getLoanDuration() {
        return loanDuration + " days";
    }

    public static int getCount() {
        return count;
    }
}
