package main.model;

public final class SciFi extends Book {
    private static final int LOAN_DURATION = 14;
    private static int count = 0;

    public SciFi(String id, String title, String author, String loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public SciFi(String id, String title, String author) {
        super(id, title, author);
        count++;
    }

    @Override
    public int getLoanDuration() {
        return LOAN_DURATION;
    }

    @Override
    public String getCategory() {
        return "Sci-fi";
    }

    public static int getCount() {
        return count;
    }


}
