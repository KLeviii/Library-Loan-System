package main.model;

public final class Technical extends Book{
    private static final int LOAN_DURATION = 7;
    private static int count = 0;

    public Technical(String id, String title, String author, String loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public Technical(String id, String title, String author) {
        super(id, title, author);
        count++;
    }

    @Override
    public int getLoanDuration() {
        return LOAN_DURATION;
    }

    @Override
    public String getCategory() {
        return "Technical";
    }

    public static int getCount() {
        return count;
    }
}
